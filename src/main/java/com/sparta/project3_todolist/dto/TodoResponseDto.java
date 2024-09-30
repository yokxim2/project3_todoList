package com.sparta.project3_todolist.dto;

import com.sparta.project3_todolist.entity.Todo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class TodoResponseDto {
    private Long id;
    private String title;
    private String content;
    private String createdAt;
    private String modifiedAt;

    public TodoResponseDto(Todo todo) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.content = todo.getContent();
        this.createdAt = todo.getFormattedCreatedAt();
        this.modifiedAt = todo.getFormattedModifiedAt();
    }
//
//    public TodoResponseDto(Long id, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
//        this.id = id;
//        this.title = title;
//        this.content = content;
//        this.createdAt = createdAt;
//        this.modifiedAt = modifiedAt;
//    }
}
