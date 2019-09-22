package com.disklru.repository;

import org.springframework.data.repository.CrudRepository;

import com.disklru.bean.Cache;

public interface CacheRepository extends CrudRepository<Cache, Long> {
	
	Cache findById(long id);
	
	Cache findByFilePath(String filePath);
}
