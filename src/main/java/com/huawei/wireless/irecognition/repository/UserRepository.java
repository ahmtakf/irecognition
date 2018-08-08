package com.huawei.wireless.irecognition.repository;

import com.huawei.wireless.irecognition.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    List<UserEntity> findAllByUsername(String username);

}
