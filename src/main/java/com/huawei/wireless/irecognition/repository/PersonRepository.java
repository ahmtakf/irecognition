package com.huawei.wireless.irecognition.repository;

import com.huawei.wireless.irecognition.entity.PersonEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepository extends CrudRepository<PersonEntity,Long> {
    List<PersonEntity> findByPersonId(String personId);

    List<PersonEntity> findAllByPersonIdLike(String key);

}
