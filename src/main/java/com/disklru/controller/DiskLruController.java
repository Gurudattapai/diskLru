package com.disklru.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.disklru.dto.CreateCacheDto;
import com.disklru.dto.PutIntoCacheDto;
import com.disklru.interfaces.CacheService;

@RestController
@RequestMapping("/diskLru")
public class DiskLruController {
	
	@Autowired
	private CacheService diskLruCacheService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/createCache", produces = {"application/json"},
			consumes = {"application/json"})
	public ResponseEntity<Long> createCache(@RequestBody CreateCacheDto createCacheDto) {
		long cacheId = diskLruCacheService.createCache(createCacheDto.getFileLocation(), createCacheDto.getMaxSize());
		return new ResponseEntity<>(cacheId, HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/deleteCache")
	public ResponseEntity<Void> deleteCache(@RequestParam(value = "filePath", required = true) String filePath) {
		diskLruCacheService.deleteCache(filePath);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/putIntoCache")
	public ResponseEntity<Boolean> putIntoCache(@RequestParam(value = "cacheId", required = true) long cacheId,
											 @RequestBody PutIntoCacheDto putIntoCacheDto) {
		boolean cacheUpdated = diskLruCacheService.putIntoCache(cacheId, putIntoCacheDto.getKey(), putIntoCacheDto.getValue());
		return new ResponseEntity<>(cacheUpdated, HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getFromCache")
	public ResponseEntity<Object> getFromCache(@RequestParam(value = "cacheId", required = true) long cacheId,
			                                 @RequestParam(value = "key", required = true) String key) {
		String obj = diskLruCacheService.getFromCache(cacheId, key);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/deleteFromCache")
	public ResponseEntity<Object> deleteFromCache(@RequestParam(value = "cacheId", required = true) long cacheId,
			                                 @RequestParam(value = "key", required = true) String key) {
		Object obj = diskLruCacheService.removeFromCache(cacheId, key);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

}
