package com.huawei.wireless.irecognition.service;

import com.huawei.wireless.irecognition.entity.PersonEntity;
import com.huawei.wireless.irecognition.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService implements IPersonService{

    @Autowired
    private PersonRepository personRepository;

    @Override
    public List<PersonEntity> getAllPeople() {
        List<PersonEntity> list = new ArrayList<>();
        personRepository.findAll().forEach(e -> {
            list.add(e);});

        return list;
    }

    @Override
    public List<PersonEntity> searchPeople(String key) {
        List<PersonEntity> list = new ArrayList<>();
        personRepository.findAllByPersonIdLike("%" + key + "%").forEach(e -> {
            list.add(e);});

        return list;
    }

    @Override
    public PersonEntity getPersonById(long personId) {
        return personRepository.findOne(personId);
    }

    @Override
    public long addPerson(PersonEntity person) {

        List<PersonEntity> list = personRepository.findByPersonId(person.getPersonId());
        if (list.size() > 0) {
            return 0;
        } else {
            person = personRepository.save(person);
        }

        return person.getId();
    }

    @Override
    public void updatePerson(PersonEntity person) {
        personRepository.save(person);
    }



    @Override
    public void deletePerson(long personId) {

        personRepository.delete(personId);
    }

}
