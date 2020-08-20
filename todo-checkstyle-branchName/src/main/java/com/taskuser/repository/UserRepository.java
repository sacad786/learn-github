package com.taskuser.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskuser.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
}

