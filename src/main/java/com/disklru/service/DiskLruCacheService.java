package com.disklru.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.disklru.bean.Cache;
import com.disklru.exception.DiskLruException;
import com.disklru.interfaces.CacheService;
import com.disklru.repository.CacheRepository;

@Service
public class DiskLruCacheService implements CacheService {
	
	private static final Logger logger = LoggerFactory.getLogger(DiskLruCacheService.class);
	
	private static final String DISK_CACHE_FILE = "disk_cache";
	
	private volatile Map<Long, Map<String, String>> cacheMap = new HashMap<>();
	
	//private ExecutorService executorService = Executors.newFixedThreadPool(5);
	
	@Autowired
	private CacheRepository cacheRepository;

	@Override
	public long createCache(String filePath, long maxSize) {
		
		if (maxSize <= 0) {
			throw new IllegalArgumentException("Cache max size should be greater than 0");
		}
		
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
			File childFile = new File(file, DISK_CACHE_FILE);
			try {
				childFile.createNewFile();
			} catch (IOException e) {
				logger.error("Caught IOException while creating the cache file {}", e);
				throw new DiskLruException(e);
			}
			logger.info("Directory got created in location {}", filePath);
			Cache cache = cacheRepository.save(createCacheObject(filePath, maxSize));
			cacheMap.put(cache.getId(), new LinkedHashMap<>(0, 0.75f, true));
			return cache.getId();
		}
		logger.info("Directory already exists in the given location");
		Cache cache = cacheRepository.findByFilePath(filePath);
		return cache.getId();
	}

	@Override
	public void deleteCache(String filePath) {
		Cache cache = cacheRepository.findByFilePath(filePath);
		if (cache == null) {
			logger.info("Cache not found in the given path");
			return;
		}
		Map<String, String> map = cacheMap.get(cache.getId());
		synchronized (map) {
			File file = new File(filePath);
			if (!file.exists()) {
				throw new NotFoundException("Cache not found in given path");
			}
			if (!file.isDirectory()) {
				throw new IllegalArgumentException("Not a cache file path");
			}
			for (File childFile : file.listFiles()) {
				logger.info("Deleting {} file", childFile.getAbsolutePath());
				childFile.delete();
			}
			file.delete();
			cacheMap.remove(cache.getId());
			cacheRepository.delete(cache);
		}
		logger.info("Deleted the cache from {}", filePath);
	}

	@Override
	public String getFromCache(long cacheId, String key) {
		Map<String, String> map = cacheMap.get(cacheId);
		if (map == null) {
			logger.info("Cache does not exist with Id {}", cacheId);
			return null;
		}
		return map.get(key);
	}
	
	@Override
	public boolean putIntoCache(long cacheId, String key, String value) {
		Map<String, String> map = cacheMap.get(cacheId);
		if (map == null) {
			logger.info("Cache does not exist with Id {}", cacheId);
			return false;
		}
		FileOutputStream f;
		ObjectOutputStream s;
		try {
			Cache cache = cacheRepository.findById(cacheId);
			synchronized (map) {
				map.put(key, value);
				updateCaheFile(map, cache.getFilePath());
				if (map.size() > cache.getMaxSize()) {
					trimCache(map, cache.getMaxSize(), cache.getFilePath());
					//Set<Callable<Void>> callables = new HashSet<>();
					//callables.add(() -> trimCache(map, cache.getMaxSize(), cache.getFilePath()));
					//executorService.invokeAll(callables);
				}
			}
			//executorService.shutdown();
		} catch (FileNotFoundException e) {
			logger.error("File not found in given path. Cache is corrupted {}", e);
			throw new DiskLruException(e);
		} catch (IOException e) {
			logger.error("IOException occured while updating the file {}", e);
			throw new DiskLruException(e);
		}
		return true;
	}

	@Override
	public boolean removeFromCache(long cacheId, String key) {
		Map<String, String> map = cacheMap.get(cacheId);
		if (map == null) {
			logger.info("Cache does not exist with Id {}", cacheId);
			return false;
		}
		FileOutputStream f;
		ObjectOutputStream s;
		try {
			Cache cache = cacheRepository.findById(cacheId);
			synchronized (map) {
				map.remove(key);
				updateCaheFile(map, cache.getFilePath());
			}
		} catch (FileNotFoundException e) {
			logger.error("File not found in given path. Cache is corrupted {}", e);
			throw new DiskLruException(e);
		} catch (IOException e) {
			logger.error("IOException occured while updating the file", e);
			throw new DiskLruException(e);
		}
		return true;
	}
	
	private void updateCaheFile(Map<String, String> map, String filePath) throws IOException, FileNotFoundException {
		FileOutputStream f;
		ObjectOutputStream s;
		synchronized (map) {
			File cacheFile = new File(filePath + "/DISK_CACHE_FILE");
			f = new FileOutputStream(cacheFile);
			f.write("".getBytes());
			if (map.size() > 0) {
				s = new ObjectOutputStream(f);
				s.writeObject(map);
				s.close();
			}
			f.close();
		}
	}
	
	private Void trimCache(Map<String, String> map, long maxSize, String filePath) throws IOException, FileNotFoundException {
		synchronized(map) {
			int size = map.size();
			while(size > maxSize) {
				Iterator it = map.entrySet().iterator();
				it.next();
				it.remove();
				size--;
			}
			updateCaheFile(map, filePath);
		}
		return null;
	}
	
	private Cache createCacheObject(String filePath, long maxSize) {
		Cache cache = new Cache();
		cache.setFilePath(filePath);
		cache.setMaxSize(maxSize);
		return cache;
	}
}
