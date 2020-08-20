package com.taskuser.controller;


import com.taskuser.dto.TaskUpdateDto;
import com.taskuser.model.Task;

import com.taskuser.service.TaskService;
import com.taskuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class TaskController {
    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;

    @PostMapping("{userId}/task")
    public ResponseEntity<?> createTask(@Valid @PathVariable Long userId, @RequestBody Task task, BindingResult result) {
        return taskService.create(userId, task, result);
    }

    @GetMapping("{userId}/task")
    public ResponseEntity<?> getAllTasksByUserId(@PathVariable Long userId) {
       return taskService.getAllTasksByUserId(userId);
    }

    @GetMapping("/{userId}/task/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable Long userId, @PathVariable Long taskId) {
        return taskService.getTaskById(userId, taskId);
    }

    @GetMapping("/task/all")
    public List<Task> getAllTasks() {
        return taskService.findAll();
    }

    @PutMapping("{userId}/task/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Long userId, @PathVariable Long taskId, @Valid @RequestBody TaskUpdateDto taskDetails) {
        return taskService.update(userId, taskId, taskDetails);
    }

    @DeleteMapping("/{userId}/task/{taskId}")
    public ResponseEntity deleteTask(@PathVariable Long taskId) {
        return taskService.delete(taskId);
    }

}
