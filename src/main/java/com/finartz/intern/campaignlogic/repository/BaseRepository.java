package com.finartz.intern.campaignlogic.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T, String> extends CrudRepository<T, String>{
  Optional<T> findByName(String s);

  Optional<T> findByEmail(String s);

  boolean existsByEmail(String s);

  Optional<List<T>> findBySellerId(String s);

}
