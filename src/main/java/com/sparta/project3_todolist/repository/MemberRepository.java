package com.sparta.project3_todolist.repository;

import com.sparta.project3_todolist.dto.TodoRequestDto;
import com.sparta.project3_todolist.entity.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member save(Member member) {
        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO member (username, password, registered_at, modified_at) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, member.getUsername());
                    preparedStatement.setString(2, member.getPassword());
                    preparedStatement.setString(3, member.getFormattedRegisteredAt());
                    preparedStatement.setString(4, member.getFormattedModifiedAt());
                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        member.setId(id);

        return member;
    }

    public Member findById(Long memberId) {
        // DB 조회
        String sql = "SELECT * FROM member WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                Member member = new Member();
                member.setId(resultSet.getLong("id"));
                member.setUsername(resultSet.getString("username"));
                member.setPassword(resultSet.getString("password"));
                member.setRegisteredAt(resultSet.getTimestamp("registered_at").toLocalDateTime());
                member.setModifiedAt(resultSet.getTimestamp("modified_at").toLocalDateTime());
                return member;
            } else {
                throw new IllegalArgumentException("일치하는 회원이 없습니다.");
            }
        }, memberId);
    }

    public void update(Long memberId, TodoRequestDto requestDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String sql = "UPDATE member SET username = ?, modified_at = ? WHERE id = ?";
        jdbcTemplate.update(sql, requestDto.getUsername(), LocalDateTime.now().format(formatter), memberId);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
