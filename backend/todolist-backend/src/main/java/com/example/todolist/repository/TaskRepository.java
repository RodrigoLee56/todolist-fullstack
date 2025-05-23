package com.example.todolist.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todolist.model.Task;
import com.example.todolist.model.User;

public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findByUser(User user);

	Optional<Task> findByIdAndUser(Long id, User user);

	List<Task> findByUserAndCompleted(User user, boolean completed);
}