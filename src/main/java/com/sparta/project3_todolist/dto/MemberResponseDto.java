package com.sparta.project3_todolist.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberResponseDto {
    private Long id;
    private String email;
    private LocalDateTime registeredAt;
    private LocalDateTime modifiedAt;
}
