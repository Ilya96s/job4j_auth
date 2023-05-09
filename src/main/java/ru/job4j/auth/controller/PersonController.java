package ru.job4j.auth.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.service.PersonService;

import java.util.List;

/**
 * PersonController - контроллер, отвечающий за обработку CRUD операций с пользователем
 *
 * @author Ilya Kaltygin
 */
@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {

    /**
     * Сервис по работе с пользователями
     */
    private final PersonService personService;

    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Хешировать пароль пользователя и сохранить пользователя в базу данных
     *
     * @param person пользователь
     * @return объект типа ResponseEntity
     */
    @PostMapping("/sign-up")
    public ResponseEntity<Person> signUp(@RequestBody Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        return personService.save(person) != null ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    /**
     * Найти всех пользователей
     *
     * @return список пользователей
     */
    @GetMapping("/")
    public List<Person> findAll() {
        return personService.findAll();
    }

    /**
     * Найти пользователя по идентификатору
     *
     * @param id идентификатор
     * @return объект типа ResponseEntity
     */
    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = personService.findById(id);
        return new ResponseEntity<Person>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    /**
     * Сохранить пользователя в базу данных
     *
     * @param person пользователь
     * @return объект типа ResponseEntity
     */
    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        return new ResponseEntity<Person>(
                personService.save(person),
                HttpStatus.CREATED
        );
    }

    /**
     * Обновить пользователя в базе данных
     *
     * @param person пользователь
     * @return объект типа ResponseEntity
     */
    @PutMapping("/")
    public ResponseEntity<Person> update(@RequestBody Person person) {
        return personService.update(person) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * Удалить ползователя из базы данных
     *
     * @param id идентификатор пользователя
     * @return объект типа ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Person> delete(@PathVariable int id) {
        var person = new Person();
        person.setId(id);
        return personService.delete(person) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
