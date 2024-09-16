package com.juandaabril.springboot_data_mongodb.service;


import com.juandaabril.springboot_data_mongodb.model.Todo;
import com.juandaabril.springboot_data_mongodb.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository repository;
    private final RestTemplate restTemplate; // Para llamar a App2

    public List<Todo> getAllTodos() {
        return repository.findAll();
    }

    public Optional<Todo> getTodoById(String id) {
        return repository.findById(id);
    }

    public Todo createTodo(Todo todo) {
        // Llamar a App2 para obtener un string aleatorio
        String randomString = restTemplate.getForObject("http://localhost:8081/api/random-string", String.class);
        todo.setDescription(todo.getDescription() + " " + randomString);
        todo.setDate(LocalDate.now()); // Establecer la fecha actual
        return repository.save(todo);
    }

    public Todo updateTodo(Todo todo) {
        return repository.save(todo);
    }

    public void deleteTodo(String id) {
        repository.deleteById(id);
    }
}
