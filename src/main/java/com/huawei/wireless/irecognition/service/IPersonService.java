package com.huawei.wireless.irecognition.service;

import com.huawei.wireless.irecognition.entity.PersonEntity;
import com.huawei.wireless.irecognition.model.Person;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPersonService {
    List<Person> getAllPersons();
    Person getPersonById(long personId);
    boolean addPerson(Person person);
    void updatePerson(Person person, MultipartFile file);
    void deletePerson(long personId);
    void deletePersonByImage(String image);
}
