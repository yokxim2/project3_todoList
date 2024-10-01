package com.sparta.project3_todolist.service;

import com.sparta.project3_todolist.dto.TodoRequestDto;
import com.sparta.project3_todolist.dto.TodoResponseDto;
import com.sparta.project3_todolist.entity.Todo;
import com.sparta.project3_todolist.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoResponseDto createTodo(TodoRequestDto requestDto) {
        // RequestDto -> Entity(Todo)
        Todo todo = new Todo(requestDto);

        // DB 저장
        Todo saveTodo = todoRepository.save(todo);

        // Entity -> ResponseDto
        TodoResponseDto todoResponseDto = new TodoResponseDto(saveTodo);

        return todoResponseDto;
    }

    public List<TodoResponseDto> getTodos(@RequestParam(required = false) String username, @RequestParam(required = false) String modifiedAt) {
        return todoRepository.findAll(username, modifiedAt);
    }

    public Long updateTodo(Long id, TodoRequestDto requestDto) {
        // 해당 todo가 DB에 존재하는지 확인
        Todo todo = todoRepository.findById(id);

        if (todo != null) {
            // 비밀번호가 일치하는지 확인
            if (todo.getPassword().equals(requestDto.getPassword())) {
                // todo 내용 수정
                todoRepository.update(id, requestDto);
                return id;
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("선택한 일정은 존재하지 않습니다.");
        }
    }

    public Long deleteTodo(Long id, TodoRequestDto requestDto) {
        // 해당 todo가 DB에 존재하는지 확인
        Todo todo = todoRepository.findById(id);
        if (todo != null) {
            // 비밀번호가 일치하는지 확인
            if (todo.getPassword().equals(requestDto.getPassword())) {
                // todo 삭제
                todoRepository.delete(id);
                return id;
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("선택한 일정은 존재하지 않습니다.");
        }
    }
}
