package com.huawei.wireless.irecognition.repository;

import com.huawei.wireless.irecognition.entity.PersonEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonRepository extends CrudRepository<PersonEntity,Long> {
    List<PersonEntity> findByImage(String image);
    void deleteByImage(String image);

}
