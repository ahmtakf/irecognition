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
    public ResponseEntity<String> addPerson(@RequestBody Person person) {

        long id = personService.addPerson(person);

        person.setId(id);
        String image = person.getImage();
        if (image.substring(image.length() - 3, image.length()).equals("jpg"))
            person.setImage(id + ".jpg");
        else if (image.substring(image.length() - 4, image.length()).equals("jpeg"))
            person.setImage(id + ".jpeg");
        else if (image.substring(image.length() - 3, image.length()).equals("png"))
            person.setImage(id + ".png");

        persons.put(person.getImage(), person);

        return new ResponseEntity<>( person.getImage() , HttpStatus.OK);
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

        long id = 0;
        if (image.substring(image.length() - 4, image.length()).equals(".jpg"))
            id = Long.parseLong(image.substring(0, image.length() - 4));
        else if (image.substring(image.length() - 5, image.length()).equals(".jpeg"))
            id = Long.parseLong(image.substring(0, image.length() - 5));
        else if (image.substring(image.length() - 4, image.length()).equals(".png"))
            id = Long.parseLong(image.substring(0, image.length() - 4));

        personService.deletePerson(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
