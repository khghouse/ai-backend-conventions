package com.practice.cursor.domain.todo.repository;

import com.practice.cursor.domain.todo.entity.Todo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findAllByDeletedFalseOrderByIdAsc();
}
