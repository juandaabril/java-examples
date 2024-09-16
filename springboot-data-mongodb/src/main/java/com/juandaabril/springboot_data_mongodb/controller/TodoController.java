package com.juandaabril.springboot_data_mongodb.controller;


import com.juandaabril.springboot_data_mongodb.model.Todo;
import com.juandaabril.springboot_data_mongodb.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService service;

    @GetMapping
    public List<Todo> getAllTodos() {
        return service.getAllTodos();
    }

    @GetMapping("/{id}")
    public Todo getTodoById(@PathVariable String id) {
        return service.getTodoById(id).orElse(null);
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return service.createTodo(todo);
    }

    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable String id, @RequestBody Todo updatedTodo) {
        updatedTodo.setId(id);
        return service.updateTodo(updatedTodo);
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable String id) {
        service.deleteTodo(id);
    }

    @PutMapping("/check/{id}/{itemIndex}")
    public Todo toggleCheck(@PathVariable String id, @PathVariable int itemIndex) {
        Todo todo = service.getTodoById(id).orElseThrow();
        todo.getItems().get(itemIndex).setCompleted(!todo.getItems().get(itemIndex).isCompleted());
        return service.updateTodo(todo);
    }
}
