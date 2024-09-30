package com.sparta.project3_todolist.controller;

import com.sparta.project3_todolist.dto.TodoRequestDto;
import com.sparta.project3_todolist.dto.TodoResponseDto;
import com.sparta.project3_todolist.entity.Todo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<TodoResponseDto> getTodos(@RequestParam(required = false) String username, @RequestParam(required = false) String modifiedAt) {
        // Map to List
        List<TodoResponseDto> responseList = todoList.values().stream()
                // 조건 1: 작성자명 일치 여부 확인
                .filter(todo -> username == null || todo.getUsername().equals(username))
                // 조건 2: 수정일 일치 여부 확인 (입력받은 날짜 형식: YYYY-MM-DD)
                .filter(todo -> {
                    if (modifiedAt == null) return true;
                    LocalDate modifiedDate = LocalDate.parse(modifiedAt);
                    return todo.getModifiedAt().toLocalDate().equals(modifiedDate);
                })
                // 수정일 기준 내림차순 정렬
                .sorted((todo1, todo2) -> todo2.getModifiedAt().compareTo(todo1.getModifiedAt()))
                // TodoResponseDto로 변환
                .map(TodoResponseDto::new).collect(Collectors.toList());
        return responseList;
    }

    @PutMapping("/todos/{id}")
    public Long updateTodo(@PathVariable Long id, @RequestBody TodoRequestDto requestDto) {
        // 해당 todo가 DB에 존재하는지 확인
        if (todoList.containsKey(id)) {
            // 해당 todo 가져오기
            Todo todo = todoList.get(id);

            // 비밀번호가 일치하는지 확인
            if (todo.getPassword().equals(requestDto.getPassword())) {
                // todo 수정
                todo.update(requestDto);
                return todo.getId();
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 맞지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("선택한 일정은 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/todos/{id}")
    public Long deleteTodo(@PathVariable Long id, @RequestBody TodoRequestDto requestDto) {
        // 해당 todo가 DB에 존재하는지 확인
        if (todoList.containsKey(id)) {
            // 해당 todo 가져오기
            Todo todo = todoList.get(id);

            // 비밀번호가 일치하는지 확인
            if (todo.getPassword().equals(requestDto.getPassword())) {
                // 해당 todo 삭제
                todoList.remove(id);
                return id;
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 맞지 않습니다.");
            }

        } else {
            throw new IllegalArgumentException("선택한 일정은 존재하지 않습니다.");
        }
    }
}
