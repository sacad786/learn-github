package com.taskuser.service;

import com.taskuser.dto.TaskUpdateDto;
import com.taskuser.model.Task;
import com.taskuser.model.User;
import com.taskuser.repository.TaskRepository;
import com.taskuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskService taskService;

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> findTaskByUserId(Long userId) {
        return taskRepository.getTaskByUserId(userId);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findOne(Long taskId) {
        return taskRepository.findOne(taskId);
    }

    public void delete(Task task) {
        taskRepository.delete(task);
    }

    public ResponseEntity<?> create(Long userId, Task task, BindingResult result) {
        User user = userRepository.findOne(userId);

        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
        }

        if (user == null) {
            return new ResponseEntity<>("User with id " + userId + " doesn't exist", HttpStatus.NOT_FOUND);
        }
        task.setUser(user);
        return ResponseEntity.ok().body(taskRepository.save(task));

    }

    public ResponseEntity<?> update(Long userId, Long taskId, TaskUpdateDto taskDetails) {

        User user = userRepository.findOne(userId);

        if (user == null) {
            return new ResponseEntity<>("User with id " + userId + " doesn't exist", HttpStatus.BAD_REQUEST);

        }

        Task taskInTheDb = taskRepository.findOne(taskId);
        if (taskInTheDb == null) {
            return new ResponseEntity<>("Task with id " + taskId + " doesn't exist", HttpStatus.NOT_FOUND);

        }
        if (taskDetails.getName() != null) {
            taskInTheDb.setName(taskDetails.getName());
        }
        if (taskDetails.getDescriptionOfTask() != null) {
            taskInTheDb.setDescriptionOfTask(taskDetails.getDescriptionOfTask());
        }
        Task savedTask = taskRepository.save(taskInTheDb);
        return ResponseEntity.ok().body(savedTask);
    }

    public ResponseEntity<?> delete(Long taskId) {
        Task task = taskRepository.findOne(taskId);
        if (task == null) {
            return new ResponseEntity<>("Task with id " + taskId + " doesn't exist", HttpStatus.NOT_FOUND);
        }
        taskRepository.delete(task);
        return ResponseEntity.ok("Task with id " + taskId + " deleted");
    }

    public ResponseEntity<?> getTaskById(Long userId, Long taskId) {

        Task task = taskRepository.findOne(taskId);
        User user = userRepository.findOne(userId);

        if (user == null) {
            return new ResponseEntity<>("User with id " + userId + " doesn't exist", HttpStatus.NOT_FOUND);
        }

        if (task == null) {
            return new ResponseEntity<>("Task with id " + taskId + " doesn't exist", HttpStatus.NOT_FOUND);

        }
        return ResponseEntity.ok().body(task);

    }

    public ResponseEntity<?> getAllTasksByUserId(Long userId) {
        User user = userRepository.findOne(userId);

        if (user == null) {
            return new ResponseEntity<>("User with id " + userId + " doesn't exist", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok().body(taskService.findTaskByUserId(userId));
    }
}