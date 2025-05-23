package com.example.todolist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.todolist.model.Task;
import com.example.todolist.model.User;
import com.example.todolist.security.services.UserDetailsImpl;
import com.example.todolist.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(Authentication authentication) {
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        List<Task> tasks = taskService.findAllByUser(user);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, Authentication authentication) {
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        Task createdTask = taskService.createTask(task, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id, Authentication authentication) {
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        Task task = taskService.findByIdAndUser(id, user);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails, 
                                         Authentication authentication) {
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        Task updatedTask = taskService.updateTask(id, taskDetails, user);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Authentication authentication) {
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        taskService.deleteTask(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Task>> filterTasksByCompletion(
            @RequestParam boolean completed, 
            Authentication authentication) {
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        List<Task> tasks = taskService.findByUserAndCompleted(user, completed);
        return ResponseEntity.ok(tasks);
    }
}