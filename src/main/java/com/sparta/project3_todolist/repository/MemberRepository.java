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
import java.util.List;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member save(Member member) {
        // 1. 이메일로 기존 멤버 조회
        String findMemberSql = "SELECT id, username, password FROM member WHERE email = ?";
        List<Member> existingMembers = jdbcTemplate.query(
                findMemberSql,
                new Object[]{member.getEmail()},
                (rs, rowNum) -> new Member(
                    rs.getLong("id"),
                    rs.getString("username"),
                    rs.getString("password")
                )
        );

        if (!existingMembers.isEmpty()) {
            // 2. 이미 사용된 이메일의 경우: 이름과 비밀번호 일치 여부 확인
            Member foundMember = existingMembers.get(0);
            if (!foundMember.getUsername().equals(member.getUsername()) || !foundMember.getPassword().equals(member.getPassword())) {
                // 이름 또는 비밀번호가 일치하지 않으면 예외 발생
                throw new IllegalArgumentException("이미 등록되어 있는 이메일입니다. 알맞은 사용자명 혹은 비밀번호를 입력하십시오.");
            }
            // 일치하는 경우에는 기존 멤버 반환
            return foundMember;
        }

        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO member (username, email, password, registered_at, modified_at) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, member.getUsername());
                    preparedStatement.setString(2, member.getEmail());
                    preparedStatement.setString(3, member.getPassword());
                    preparedStatement.setString(4, member.getFormattedRegisteredAt());
                    preparedStatement.setString(5, member.getFormattedModifiedAt());
                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        member.setId(id);

        return member;
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
}
