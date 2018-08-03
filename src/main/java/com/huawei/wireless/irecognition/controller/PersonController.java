package com.huawei.wireless.irecognition.controller;

import com.huawei.wireless.irecognition.model.Person;
import com.huawei.wireless.irecognition.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Controller
@RequestMapping("person")
@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
public class PersonController {

    @Autowired
    private IPersonService personService;

    private Map<String, Person> persons = new HashMap<>();

    @GetMapping("{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable("id") Integer id) {
        Person person = personService.getPersonById(id);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }
    @GetMapping("persons")
    public ResponseEntity<List<Person>> getAllPersons() {
        List<Person> list = personService.getAllPersons();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("addPerson")
    public ResponseEntity<Boolean> addPerson(@RequestBody Person person) {
        System.out.println("Hey " + person.getImage() + "  " + person.getName());

        if ( personService.addPerson(person))
            persons.put(person.getImage(), person);
        else
            new ResponseEntity<>(false, HttpStatus.CONFLICT);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping("addImage")
    public ResponseEntity<Person> addImage(@RequestParam("image") MultipartFile file) {
        Person person = persons.get(file.getOriginalFilename());
        person.setImage(file.getOriginalFilename());
        System.out.println("HeyUpdate " + person.getImage() + "  " + person.getName());
        personService.updatePerson(person, file);

        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @PutMapping("update")
    public ResponseEntity<Person> updatePerson(@RequestBody Person person, @RequestParam("image") MultipartFile file) {
        personService.updatePerson(person, file);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable("id") Integer id) {
        personService.deletePerson(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("deletePerson/{image}")
    public ResponseEntity<Void> deletePersonByImage(@PathVariable("image") String image) {
        personService.deletePersonByImage(image);
        persons.remove(image);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
