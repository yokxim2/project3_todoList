package com.sparta.project3_todolist.dto;

import com.sparta.project3_todolist.entity.Member;
import com.sparta.project3_todolist.entity.Todo;
import lombok.Getter;

@Getter
public class TodoResponseDto {
    private Long id;
    private String username;
    private String title;
    private String content;
    private String createdAt;
    private String modifiedAt;

    public TodoResponseDto(Todo todo, Member member) {
        this.id = todo.getId();
        this.username = member.getUsername();
        this.title = todo.getTitle();
        this.content = todo.getContent();
        this.createdAt = todo.getFormattedCreatedAt();
        this.modifiedAt = todo.getFormattedModifiedAt();
    }

    public TodoResponseDto(Long id, String username, String title, String content, String createdAt, String modifiedAt) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
