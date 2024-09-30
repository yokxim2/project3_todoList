package com.sparta.project3_todolist.controller;

import com.sparta.project3_todolist.dto.TodoRequestDto;
import com.sparta.project3_todolist.dto.TodoResponseDto;
import com.sparta.project3_todolist.entity.Todo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TodoController {

    private final Map<Long, Todo> todoList = new HashMap<>();

    @PostMapping("/todos")
    public TodoResponseDto createTodo(@RequestBody TodoRequestDto requestDto) {
        // RequestDto -> Entity(Todo)
        Todo todo = new Todo(requestDto);

        // Todo Max ID Check
        Long maxId = !todoList.isEmpty() ? Collections.max(todoList.keySet()) + 1 : 1;
        todo.setId(maxId);

        // DB 저장
        todoList.put(todo.getId(), todo);

        // Entity -> ResponseDto
        TodoResponseDto todoResponseDto = new TodoResponseDto(todo);

        return todoResponseDto;
    }
}
