package ru.job4j.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.auth.dto.PersonDTO;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * PersonController - контроллер, отвечающий за обработку CRUD операций с пользователем
 *
 * @author Ilya Kaltygin
 */
@Slf4j
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
     * Функционал для работы с JSON
     */
    private final ObjectMapper objectMapper;

    /**
     * Хешировать пароль пользователя и сохранить пользователя в базу данных
     *
     * @param person пользователь
     * @return объект типа ResponseEntity
     */
    @PostMapping("/sign-up")
    public ResponseEntity<Person> signUp(@RequestBody Person person) {
        if (person.getLogin() == null || person.getPassword() == null) {
            throw new NullPointerException("Login and password must not be empty");
        }
        if (person.getPassword().length() < 5) {
            throw new IllegalStateException("Password must be at least 5 characters long");
        }
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        return personService.save(person).isPresent()
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.CONFLICT).build();
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
        if (person.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person with this id not found");
        }
        return new ResponseEntity<Person>(
                person.orElse(new Person()),
                HttpStatus.OK
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
        if (person.getLogin() == null || person.getPassword() == null) {
            throw new NullPointerException("Login and password must not be empty");
        }
        if (person.getPassword().length() < 5) {
            throw new IllegalArgumentException("Password must be at least 5 characters long");
        }
        return new ResponseEntity<Person>(
                personService.save(person).get(),
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
        if (person.getLogin() == null || person.getPassword() == null) {
            throw new NullPointerException("Login and password must not be empty");
        }
        if (person.getPassword().length() < 5) {
            throw new IllegalArgumentException("Password must be at least 5 characters long");
        }
        return personService.update(person)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    /**
     * Обновить пароль пользователя
     *
     * @param personDTO объект типа PersonDTO
     * @return объект типа ResponseEntity
     */
    @PatchMapping("/")
    public ResponseEntity<PersonDTO> updatePassword(@RequestBody PersonDTO personDTO) {
        var person = personService.findByLogin(personDTO.getLogin());
        if (person.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this login was not found");
        }
        return personService.updatePassword(personDTO)
                ? ResponseEntity.status(HttpStatus.OK).build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
        return personService.delete(person)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    /**
     * Метод обрабатывает все исключения IllegalArgumentException, которые возникают в методах контроллера
     *
     * @param e        исключение, которое было сгенирировано и перехвачено данным методом
     * @param request  запрос
     * @param response ответ
     * @throws IOException exception
     */
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void handleException(Exception e,
                                HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
            {
                put("message", e.getMessage());
                put("details", e.getMessage());
            }
        }));
        log.error(e.getMessage());
    }
}
