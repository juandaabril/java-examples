package com.juandaabril.consumer.repository;

import com.juandaabril.consumer.model.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface BookRepository extends CrudRepository<Book, UUID> {
}
