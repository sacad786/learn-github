package com.taskuser.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskuser.model.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> getTaskByUserId(long userId);
}
