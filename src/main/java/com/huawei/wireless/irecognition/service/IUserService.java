package com.huawei.wireless.irecognition.service;

import com.huawei.wireless.irecognition.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<UserEntity> getAllPeople();
    UserEntity getUserById(long userId);
    String addUser(UserEntity user);
    void updateUser(UserEntity user);
    void deleteUser(long userId);
    UserEntity findByUsername(String username);
    Optional<UserEntity> findByToken(final String token);
    String login(UserEntity user);
    void logout(UserEntity user);

}
