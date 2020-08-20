package com.taskuser.service;

import com.taskuser.dto.UserUpdateDto;
import com.taskuser.model.User;
import com.taskuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user); }

    public List<User> findAll() {
        return userRepository.findAll(); }

    public User findOne(Long userId) {
        return userRepository.findOne(userId);
    }


    public ResponseEntity<?> create(User userInfoFromRequest) {
        User userTest = userRepository.findByUserName(userInfoFromRequest.getUserName());
        if (userTest != null) {
            return new ResponseEntity<>("User with name " + userInfoFromRequest.getUserName() + " already taken", HttpStatus.CONFLICT);

        }
        User user = userRepository.save(userInfoFromRequest);
        return ResponseEntity.ok().body(user);

    }

    public ResponseEntity<?> getUserById(Long userId) {

        User user = userRepository.findOne(userId);

        if (user == null) {
            return new ResponseEntity<>("User with id " + userId + " doesn't exist", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().body(user);
    }

    public ResponseEntity<?> getUserByUsername(String userName) {

        User user = userRepository.findByUserName(userName);

        if (user == null) {
            return new ResponseEntity<>("User with username " + userName + " doesn't exist", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().body(user);
    }


    public ResponseEntity<?> delete(Long userId) {
        User user = userRepository.findOne(userId);
        if (user == null) {
            return new ResponseEntity<>("User with id " + userId + " doesn't exist", HttpStatus.NOT_FOUND);
        }
        userRepository.delete(user);
        return new ResponseEntity<>("User with id " + userId + " deleted successfully", HttpStatus.OK);
    }

    public ResponseEntity<?> update(Long userId, UserUpdateDto userDetails) {
        User userInTheDb = userRepository.findOne(userId);
        if (userInTheDb == null) {
            return new ResponseEntity<>("User with id " + userId + " doesn't exist", HttpStatus.NOT_FOUND);

        }

        if (userDetails.getFirstName() != null) {
            userInTheDb.setFirstName(userDetails.getFirstName());
        }

        if (userDetails.getLastName() != null) {
            userInTheDb.setLastName(userDetails.getLastName());
        }
        User savedUser = userRepository.save(userInTheDb);

        return ResponseEntity.ok().body(savedUser);
    }

}

