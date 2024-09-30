package com.sparta.project3_todolist.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class TodoRequestDto {
    private String title;
    private String content;
    private String username;
    private String password;
}
