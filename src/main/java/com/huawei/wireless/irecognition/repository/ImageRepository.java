package com.huawei.wireless.irecognition.repository;

import com.huawei.wireless.irecognition.entity.ImageEntity;
import com.huawei.wireless.irecognition.entity.PersonEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ImageRepository extends CrudRepository<ImageEntity, Long> {

    List<ImageEntity> findImageEntitiesByPersonId(long personId);

}
