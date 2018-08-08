package com.huawei.wireless.irecognition.service;

import com.huawei.wireless.irecognition.entity.UserEntity;
import com.huawei.wireless.irecognition.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements IUserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ITokenService tokenService;

    private Map<String, UserEntity> tokenedUsers = new HashMap<>();

    @Override
    public List<UserEntity> getAllPeople() {
        List<UserEntity> userEntities = new ArrayList<>();
        userRepository.findAll().forEach(e -> {
            userEntities.add(e);
        });
        return userEntities;
    }

    @Override
    public UserEntity getUserById(long userId) {
        return userRepository.findOne(userId);
    }

    @Override
    public String addUser(UserEntity user) {
        List<UserEntity> list = userRepository.findAllByUsername(user.getUsername());
        if (list.size() > 0) {
            return "";//No user found
        } else {
            user = userRepository.save(user);
        }

        return user.getUsername();
    }

    @Override
    public void updateUser(UserEntity user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.delete(userId);
    }

    @Override
    public UserEntity findByUsername(String username) {
        List<UserEntity> list = userRepository.findAllByUsername(username);
        if (list.size() > 0) {
            return null;//No user found
        }

        return list.get(0);
    }

    @Override
    public Optional<UserEntity> findByToken(String token) {
        Optional<String> currentUsername = Optional
                .of(tokenService.verify(token))
                .map(map -> map.get("username"));

        return Optional.of(tokenedUsers.get(currentUsername.get()));
    }

    @Override
    public String login(UserEntity user) {

        Map<String,String> map = new HashMap<>();

        map.put("username", user.getUsername());

        UserEntity temp = userRepository.findAllByUsername(user.getUsername()).get(0);

        String token = tokenService.expiring(map);
        tokenedUsers.put(user.getUsername(), temp);

        Optional.of(temp)
                .filter(u -> Objects.equals(user.getPassword(), u.getPassword()))
                .map(u -> findByToken(token).get());

        return token;
    }

    @Override
    public void logout(UserEntity user) {
        tokenedUsers.remove(user.getUsername());
    }
}
