package com.huawei.wireless.irecognition.controller;

import com.huawei.wireless.irecognition.entity.UserEntity;
import com.huawei.wireless.irecognition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable("id") long id) {
        UserEntity user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("users")
    public ResponseEntity<List<UserEntity>> getAllPeople() {
        List<UserEntity> list = userService.getAllPeople();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<String> registerUser(@RequestBody UserEntity user) {

        String username = userService.addUser(user);

        if (username.equals(""))
            new ResponseEntity<>(HttpStatus.CONFLICT);

        return loginUser(user);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<String> loginUser(@RequestBody UserEntity user) {

        String token = userService.login(user);

        if (token.equals(""))
            new ResponseEntity<>(HttpStatus.CONFLICT);

        return new ResponseEntity<>( token, HttpStatus.OK);
    }

    @PutMapping("update")
    public ResponseEntity<UserEntity> updateUser(@RequestBody UserEntity user) {
        userService.updateUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
