package com.huawei.wireless.irecognition.repository;

import com.huawei.wireless.irecognition.entity.ImageEntity;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

public interface ImageTableRepository extends DataTablesRepository<ImageEntity, Long> {

}