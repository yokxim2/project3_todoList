package com.sparta.project3_todolist.controller;

import com.sparta.project3_todolist.dto.TodoRequestDto;
import com.sparta.project3_todolist.dto.TodoResponseDto;
import com.sparta.project3_todolist.entity.Todo;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

    @GetMapping("/todos")
    public List<TodoResponseDto> getTodos() {
        // Map to List
        List<TodoResponseDto> responseList = todoList.values().stream().map(TodoResponseDto::new).toList();
        return responseList;
    }

    @PutMapping("/todos/{id}")
    public Long updateTodo(@PathVariable Long id, @RequestBody TodoRequestDto requestDto) {
        // 해당 todo가 DB에 존재하는지 확인
        if (todoList.containsKey(id)) {
            // 해당 todo 가져오기
            Todo todo = todoList.get(id);

            // todo 수정
            todo.update(requestDto);
            return todo.getId();
        } else {
            throw new IllegalArgumentException("선택한 일정은 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/todos/{id}")
    public Long deleteTodo(@PathVariable Long id) {
        // 해당 todo가 DB에 존재하는지 확인
        if (todoList.containsKey(id)) {
            // 해당 todo 삭제
            todoList.remove(id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 일정은 존재하지 않습니다.");
        }
    }
}
