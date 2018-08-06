package com.huawei.wireless.irecognition.controller;

import com.huawei.wireless.irecognition.entity.PersonEntity;
import com.huawei.wireless.irecognition.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Controller
@RequestMapping("person")
public class PersonController {

    @Autowired
    private IPersonService personService;

    @GetMapping("{id}")
    public ResponseEntity<PersonEntity> getPersonById(@PathVariable("id") long id) {
        PersonEntity person = personService.getPersonById(id);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @GetMapping("search/{key}")
    public ResponseEntity<List<PersonEntity>> searchPeople(@PathVariable("key") String key) {
        List<PersonEntity> list = personService.searchPeople(key);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("persons")
    public ResponseEntity<List<PersonEntity>> getAllPeople() {
        List<PersonEntity> list = personService.getAllPeople();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<Long> addPerson(@RequestBody PersonEntity person) {

        long id = personService.addPerson(person);

        if (id == 0)
            new ResponseEntity<>(HttpStatus.CONFLICT);
        else
            person.setId(id);

        return new ResponseEntity<>(  person.getId() , HttpStatus.OK);
    }

    @PutMapping("update")
    public ResponseEntity<PersonEntity> updatePerson(@RequestBody PersonEntity person) {
        personService.updatePerson(person);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable("id") long id) {
        personService.deletePerson(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
