package com.sparta.project3_todolist.controller;

import com.sparta.project3_todolist.dto.TodoRequestDto;
import com.sparta.project3_todolist.dto.TodoResponseDto;
import com.sparta.project3_todolist.service.TodoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/todos")
    public TodoResponseDto createTodo(@RequestBody TodoRequestDto requestDto) {
        return todoService.createTodo(requestDto);
    }

    @GetMapping("/todos")
    public List<TodoResponseDto> getTodos(@RequestParam(required = false) String username, @RequestParam(required = false) String modifiedAt) {
        return todoService.getTodos(username, modifiedAt);
    }

    @PutMapping("/todos/{id}")
    public Long updateTodo(@PathVariable Long id, @RequestBody TodoRequestDto requestDto) {
        return todoService.updateTodo(id, requestDto);
    }

    @DeleteMapping("/todos/{id}")
    public Long deleteTodo(@PathVariable Long id, @RequestBody TodoRequestDto requestDto) {
        return todoService.deleteTodo(id, requestDto);
    }
}