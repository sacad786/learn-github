package com.taskuser.controller;

import com.taskuser.dto.UserUpdateDto;
import com.taskuser.model.User;
import com.taskuser.repository.UserRepository;
import com.taskuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/user", produces = "application/json")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> createUser(@Valid @RequestBody User userInfoFromRequest) {
        return userService.create(userInfoFromRequest);
    }

    @GetMapping("")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<?> getUserById(@PathVariable(value = "id") Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/username/{userName:[a-zA-Z]+}")
    public ResponseEntity<?> getUserByUsername(@PathVariable(value = "userName") String userName) {
        return userService.getUserByUsername(userName);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable(value = "id") Long userId, @Valid @RequestBody UserUpdateDto userDetails) {
        return userService.update(userId, userDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable(value = "id") Long userId) {
        return userService.delete(userId);
    }

}
