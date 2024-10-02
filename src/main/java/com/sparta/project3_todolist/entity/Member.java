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
public class Member {
    private Long id;
    private String username;
    private String email;
    private String password;
    private LocalDateTime registeredAt;
    private LocalDateTime modifiedAt;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Member(TodoRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.email = requestDto.getEmail();
        this.password = requestDto.getPassword();
        this.registeredAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    public Member(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getFormattedRegisteredAt() {
        return registeredAt.format(formatter);
    }

    public String getFormattedModifiedAt() {
        return modifiedAt.format(formatter);
    }
}
