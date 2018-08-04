package com.huawei.wireless.irecognition.service;

import com.huawei.wireless.irecognition.entity.PersonEntity;
import com.huawei.wireless.irecognition.model.Person;
import com.huawei.wireless.irecognition.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService implements IPersonService{

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private StorageService storageService;

    @Override
    public List<Person> getAllPersons() {
        List<Person> list = new ArrayList<>();
        personRepository.findAll().forEach(e -> {
            list.add(new Person(e.getId(), e.getImage(), e.getName(),e.getSurname(),e.getGender(),e.getAge()));
        });
        return list;
    }

    @Override
    public Person getPersonById(long personId) {
        PersonEntity e = personRepository.findOne(personId);
        return new Person(e.getId(), e.getImage(), e.getName(),e.getSurname(),e.getGender(),e.getAge());
    }

    @Override
    public long addPerson(Person person) {

            PersonEntity personEntity = new PersonEntity();
            personEntity.setId(person.getId());
            personEntity.setImage("");
            System.out.println(person.getImage() + " original");
            personEntity.setName(person.getName());
            personEntity.setSurname(person.getSurname());
            personEntity.setGender(person.getGender());
            personEntity.setAge(person.getAge());

            personEntity = personRepository.save(personEntity);

        return personEntity.getId();
    }

    @Override
    public void updatePerson(Person person, MultipartFile file) {
        add(person, file);
    }

    private void add(Person person, MultipartFile file){

        try {
            storageService.store(file);
            System.out.println( "You successfully uploaded " + file.getOriginalFilename() + "!");
        } catch (Exception e) {
            System.out.println( "FAIL to upload " + file.getOriginalFilename() + "!");
        }

        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(person.getId());
        personEntity.setImage(file.getOriginalFilename());
        System.out.println(person.getImage() + " original");
        personEntity.setName(person.getName());
        personEntity.setSurname(person.getSurname());
        personEntity.setGender(person.getGender());
        personEntity.setAge(person.getAge());
        personRepository.save(personEntity);
    }

    @Override
    public void deletePerson(long personId) {
        personRepository.delete(personId);
    }

}
