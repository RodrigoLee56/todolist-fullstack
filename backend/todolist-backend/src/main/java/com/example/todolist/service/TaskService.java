package com.example.todolist.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.todolist.exception.TaskNotFoundException;
import com.example.todolist.model.Task;
import com.example.todolist.model.User;
import com.example.todolist.repository.TaskRepository;

@Service
public class TaskService {

	private final TaskRepository taskRepository;

	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public Task createTask(Task task, User user) {
		task.setUser(user);
		return taskRepository.save(task);
	}

	public List<Task> findAllByUser(User user) {
		return taskRepository.findByUser(user);
	}

	public Task findByIdAndUser(Long id, User user) {
		return taskRepository.findByIdAndUser(id, user).orElseThrow(() -> new TaskNotFoundException("Task not found"));
	}

	public Task updateTask(Long id, Task taskDetails, User user) {
		Task task = findByIdAndUser(id, user);

		task.setTitle(taskDetails.getTitle());
		task.setDescription(taskDetails.getDescription());
		task.setCompleted(taskDetails.isCompleted());

		return taskRepository.save(task);
	}

	public void deleteTask(Long id, User user) {
		Task task = findByIdAndUser(id, user);
		taskRepository.delete(task);
	}

	public List<Task> findByUserAndCompleted(User user, boolean completed) {
		return taskRepository.findByUserAndCompleted(user, completed);
	}
}