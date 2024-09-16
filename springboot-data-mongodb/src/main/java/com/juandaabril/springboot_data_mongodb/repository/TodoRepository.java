package com.juandaabril.springboot_data_mongodb.repository;

import com.juandaabril.springboot_data_mongodb.model.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TodoRepository extends MongoRepository<Todo, String> {}
