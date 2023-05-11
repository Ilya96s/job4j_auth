package ru.job4j.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.auth.dto.PersonDTO;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.service.PersonService;
import ru.job4j.auth.validation.Operation;

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

    /**
     * Функционал для работы с JSON
     */
    private final ObjectMapper objectMapper;

    /**
     * Хешировать пароль пользователя и сохранить пользователя в базу данных
     *
     * @param personDTO пользователь
     * @return объект типа ResponseEntity
     */

    @PostMapping("/sign-up")
    public ResponseEntity<PersonDTO> signUp(@Validated(Operation.OnCreate.class) @RequestBody PersonDTO personDTO) {
        return personService.signUp(personDTO).isPresent()
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    /**
     * Найти всех пользователей
     *
     * @return список пользователей
     */
    @GetMapping("/")
    public List<PersonDTO> findAll() {
        return personService.findAll();
    }

    /**
     * Найти пользователя по идентификатору
     *
     * @param id идентификатор
     * @return объект типа ResponseEntity
     */
    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> findById(@PathVariable int id) {
        var person = personService.findById(id);
        if (person.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person with this id not found");
        }
        return new ResponseEntity<PersonDTO>(
                person.orElse(new PersonDTO()),
                HttpStatus.OK
        );
    }

    /**
     * Сохранить пользователя в базу данных
     *
     * @param personDTO объект типа PersonDTO
     * @return объект типа ResponseEntity
     */
    @PostMapping("/")
    public ResponseEntity<PersonDTO> create(@Validated(Operation.OnCreate.class) @RequestBody PersonDTO personDTO) {
        var optionalPerson = personService.save(personDTO)
                .map(p -> new ModelMapper().map(p, PersonDTO.class));
        return new ResponseEntity<PersonDTO>(
                optionalPerson.get(),
                HttpStatus.CREATED
        );
    }

    /**
     * Обновить пользователя в базе данных
     *
     * @param personDTO объект типа PersonDTO
     * @return объект типа ResponseEntity
     */
    @PutMapping("/")
    public ResponseEntity<PersonDTO> update(@Validated(Operation.OnUpdate.class) @RequestBody PersonDTO personDTO) {
        return personService.update(personDTO)
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
    public ResponseEntity<PersonDTO> updatePassword(@Validated(Operation.OnUpdate.class) @RequestBody PersonDTO personDTO) {
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
    public ResponseEntity<PersonDTO> delete(@PathVariable int id) {
        var person = new Person();
        person.setId(id);
        return personService.delete(id)
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
