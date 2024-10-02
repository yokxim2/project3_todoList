package com.sparta.project3_todolist.repository;

import com.sparta.project3_todolist.dto.TodoRequestDto;
import com.sparta.project3_todolist.dto.TodoResponseDto;
import com.sparta.project3_todolist.entity.Todo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TodoRepository {

    private final JdbcTemplate jdbcTemplate;

    public TodoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Todo save(Todo todo, Long memberId) {
        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder();         // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO todo (member_id, title, content, created_at, modified_at) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setLong(1, memberId);
                    preparedStatement.setString(2, todo.getTitle());
                    preparedStatement.setString(3, todo.getContent());
                    preparedStatement.setString(4, todo.getFormattedCreatedAt());
                    preparedStatement.setString(5, todo.getFormattedModifiedAt());
                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        todo.setId(id);

        return todo;
    }

    public List<TodoResponseDto> findAll(Long pageNum, Long pageSize, @Nullable String username, @Nullable String modifiedAt) {
        // 페이징 인덱스 계산
        Long start = (pageNum - 1) * pageSize;
        Long end = pageSize;

        // SQL 쿼리 작성
        StringBuilder query = new StringBuilder("SELECT t.*");

        // 조건 1: 작성자명 조건 추가 (member 테이블과 join)
        if (username != null && !username.isEmpty()) {
            query.append(", m.username FROM todo t JOIN member m ON t.member_id = m.id WHERE m.username = ?");
        } else {
            query.append(", m.username FROM todo t JOIN member m ON t.member_id = m.id WHERE 1 = 1");
        }

        // 조건 2: 수정일 조건 추가
        if (modifiedAt != null && !modifiedAt.isEmpty()) {
            query.append(" AND DATE(t.modified_at) = ?");
        }

        // 수정일 내림차순 정렬 + 페이징 인덱스 LIMIT 사용
        query.append(" ORDER BY t.modified_at DESC LIMIT ").append(start).append(", ").append(end);

        // 쿼리 파라미터 설정
        List<Object> queryParams = new ArrayList<>();
        if (username != null && !username.isEmpty()) {
            queryParams.add(username);
        }
        if (modifiedAt != null && !modifiedAt.isEmpty()) {
            queryParams.add(modifiedAt);
        }

        // 데이터베이스 조회
        List<TodoResponseDto> responseList = jdbcTemplate.query(
                query.toString(),
                queryParams.toArray(),
                (rs, rowNum) ->
                        new TodoResponseDto(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getTimestamp("created_at").toString(),
                        rs.getTimestamp("modified_at").toString()
                )
        );

        return responseList;
    }

    public void update(Long id, TodoRequestDto requestDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String sql = "UPDATE todo SET content = ?, modified_at = ? WHERE id = ?";
        jdbcTemplate.update(sql, requestDto.getContent(), LocalDateTime.now().format(formatter), id);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM todo WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Todo findById(Long todoId) {
        // DB 조회
        String sql = "SELECT * FROM todo WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                Todo todo = new Todo();
                todo.setId(resultSet.getLong("id"));
                todo.setMemberId(resultSet.getLong("member_id"));
                todo.setTitle(resultSet.getString("title"));
                todo.setContent(resultSet.getString("content"));
                todo.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                todo.setModifiedAt(resultSet.getTimestamp("modified_at").toLocalDateTime());
                return todo;
            } else {
                throw new IllegalArgumentException("일치하는 일정이 없습니다.");
            }
        }, todoId);
    }
}
