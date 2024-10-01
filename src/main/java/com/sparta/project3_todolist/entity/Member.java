package com.sparta.project3_todolist.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Member {
    private Long id;
    private String username;
    private String password;
    private LocalDateTime registeredAt;
    private LocalDateTime modifiedAt;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
