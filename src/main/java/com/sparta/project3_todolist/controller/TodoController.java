package com.sparta.project3_todolist.controller;

import com.sparta.project3_todolist.dto.TodoRequestDto;
import com.sparta.project3_todolist.dto.TodoResponseDto;
import com.sparta.project3_todolist.entity.Todo;
import com.sparta.project3_todolist.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TodoController {

    private final JdbcTemplate jdbcTemplate;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TodoController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/todos")
    public TodoResponseDto createTodo(@RequestBody TodoRequestDto requestDto) {
        TodoService todoService = new TodoService(jdbcTemplate);
        return todoService.createTodo(requestDto);
    }

    @GetMapping("/todos")
    public List<TodoResponseDto> getTodos(@RequestParam(required = false) String username, @RequestParam(required = false) String modifiedAt) {
        TodoService todoService = new TodoService(jdbcTemplate);
        return todoService.getTodos(username, modifiedAt);
    }

    @PutMapping("/todos/{id}")
    public Long updateTodo(@PathVariable Long id, @RequestBody TodoRequestDto requestDto) {
        TodoService todoService = new TodoService(jdbcTemplate);
        return todoService.updateTodo(id, requestDto);
    }

    @DeleteMapping("/todos/{id}")
    public Long deleteTodo(@PathVariable Long id, @RequestBody TodoRequestDto requestDto) {
        TodoService todoService = new TodoService(jdbcTemplate);
        return todoService.deleteTodo(id, requestDto);
    }
}