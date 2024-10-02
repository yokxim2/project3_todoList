package com.sparta.project3_todolist.service;

import com.sparta.project3_todolist.dto.TodoRequestDto;
import com.sparta.project3_todolist.dto.TodoResponseDto;
import com.sparta.project3_todolist.entity.Member;
import com.sparta.project3_todolist.entity.Todo;
import com.sparta.project3_todolist.repository.MemberRepository;
import com.sparta.project3_todolist.repository.TodoRepository;
import com.sparta.project3_todolist.utility.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

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
        Member saveMember;
        try {
            saveMember = memberRepository.save(member); // 이메일과 멤버 정보 검증 및 저장
        } catch (IllegalArgumentException e) {
            // 예외가 발생한 경우에는 일정 저장 중단
            throw new IllegalArgumentException("일정 저장에 실패했습니다. " + e.getMessage());
        }

        // 멤버가 정상적으로 저장된 경우에만 Todo 저장
        Todo saveTodo = todoRepository.save(todo, saveMember.getId());

        // Entity -> ResponseDto
        TodoResponseDto todoResponseDto = new TodoResponseDto(saveTodo, saveMember);

        return todoResponseDto;
    }

    public List<TodoResponseDto> getTodos(Page page, @Nullable String username, @Nullable String modifiedAt) {
        return todoRepository.findAll(page.getOffset(), page.getPageSize(), username, modifiedAt);
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
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
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
            // 해당 member가 작성했던 글이 하나도 남지 않은 경우 member 삭제
            this.deleteMemberIfNoPosts(member.getId());

            return todoId;
        } else {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }
    }

    private void deleteMemberIfNoPosts(Long id) {
        // memberId로 해당 멤버가 작성한 모든 Todo를 찾음
        List<Todo> todoList = memberRepository.findTodosByMemberId(id);

        // 작성한 Todo가 없다면 해당 멤버 삭제
        if (todoList.isEmpty()) {
            memberRepository.delete(id);
        }
    }
}
