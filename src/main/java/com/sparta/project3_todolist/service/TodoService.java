package com.sparta.project3_todolist.service;

import com.sparta.project3_todolist.dto.TodoRequestDto;
import com.sparta.project3_todolist.dto.TodoResponseDto;
import com.sparta.project3_todolist.entity.Member;
import com.sparta.project3_todolist.entity.Todo;
import com.sparta.project3_todolist.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    public TodoResponseDto createTodo(TodoRequestDto requestDto) {
        // RequestDto -> Entity(Todo), Entity(Member)
        Todo todo = new Todo(requestDto);
        Member member = new Member(requestDto);

        // DB 저장
        Member saveMember = memberRepository.save(member);
        Todo saveTodo = todoRepository.save(todo, saveMember.getId());

        // Entity -> ResponseDto
        TodoResponseDto todoResponseDto = new TodoResponseDto(saveTodo, saveMember);

        return todoResponseDto;
    }

    public List<TodoResponseDto> getTodos(@RequestParam(required = false) String username, @RequestParam(required = false) String modifiedAt) {
        return todoRepository.findAll(username, modifiedAt);
    }

    public Long updateTodo(Long todoId, TodoRequestDto requestDto) {
        // 해당 todo가 DB에 존재하는지 확인
        Todo todo = todoRepository.findById(todoId);

        // todo의 memberId와 일치하는 member를 DB에서 조회
        Member member = memberRepository.findById(todo.getMemberId());

        // 비밀번호가 일치하는지 확인
        if (member.getPassword().equals(requestDto.getPassword())) {
            // todo 내용 수정 [content]
            todoRepository.update(todoId, requestDto);
            // member 내용 수정 [username]
            memberRepository.update(member.getId(), requestDto);
            return todoId;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다.");
        }
    }

    public Long deleteTodo(Long todoId, TodoRequestDto requestDto) {
        // 해당 todo가 DB에 존재하는지 확인
        Todo todo = todoRepository.findById(todoId);

        // 해당 member가 DB에 존재하는지 확인
        Member member = memberRepository.findById(todo.getMemberId());

        // 비밀번호가 일치하는지 확인
        if (member.getPassword().equals(requestDto.getPassword())) {
            // todo 삭제
            todoRepository.delete(todoId);
            // member 삭제
            memberRepository.delete(member.getId());
            return todoId;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다.");
        }
    }
}
