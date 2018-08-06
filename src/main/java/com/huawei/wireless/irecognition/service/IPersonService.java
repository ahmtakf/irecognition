package com.huawei.wireless.irecognition.service;

import com.huawei.wireless.irecognition.entity.PersonEntity;

import java.util.List;

public interface IPersonService {
    List<PersonEntity> getAllPeople();
    List<PersonEntity> searchPeople(String key);
    PersonEntity getPersonById(long personId);
    long addPerson(PersonEntity person);
    void updatePerson(PersonEntity person);
    void deletePerson(long personId);

}
