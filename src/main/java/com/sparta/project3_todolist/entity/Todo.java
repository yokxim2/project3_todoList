package com.sparta.project3_todolist.entity;

import com.sparta.project3_todolist.dto.TodoRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class Todo {
    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Todo(TodoRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    public String getFormattedCreatedAt() {
        return createdAt.format(formatter);
    }

    public String getFormattedModifiedAt() {
        return modifiedAt.format(formatter);
    }
}
