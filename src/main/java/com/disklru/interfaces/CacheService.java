package com.disklru.interfaces;

public interface CacheService {
	
	long createCache(String filePath, long maxSize);
	
	void deleteCache(String FilePath);
	
	String getFromCache(long cacheId, String key);
	
	boolean putIntoCache(long cacheId, String key, String value);
	
	boolean removeFromCache(long cacheId, String key);

}
