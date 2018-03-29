package com.todos.dal;

import com.todos.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Performs CRUD operations of {@link Todo}.
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT s from Todo s where s.dueDate >= :startDate and s.dueDate <= :endDate")
    List<Todo> getTodos(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
