package ru.job4j.auth.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.auth.dto.PersonDTO;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.repository.PersonRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * PersonServiceImpl - реализация сервиса по работе с пользователями
 *
 * @author Ilya Kaltygin
 */
@Slf4j
@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    /**
     * Хранилище пользователей
     */
    private final PersonRepository personRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Найти всех пользователей в базе данных
     *
     * @return список всех пользователей
     */
    @Override
    public List<PersonDTO> findAll() {
        return personRepository.findAll().stream()
                .map(p -> new ModelMapper().map(p, PersonDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Найти ползователя по идентификатору
     *
     * @param id идентфикатор пользователя
     * @return Optional.of(personDTO) если пользователь найден, иначе Optional.empty()
     */
    @Override
    public Optional<PersonDTO> findById(int id) {
        return personRepository.findById(id)
                .map(p -> new ModelMapper().map(p, PersonDTO.class));
    }

    /**
     * Найти пользователя в базе данных по логину
     *
     * @param login логин
     * @return Optional.of(personDTO) если пользователь найден, иначе Optional.empty()
     */
    @Override
    public Optional<PersonDTO> findByLogin(String login) {
        return personRepository.findByLogin(login)
                .map(p -> new ModelMapper().map(p, PersonDTO.class));
    }

    /**
     * Хешировать пароль пользователя и сохранить пользователя в базу данных
     *
     * @param personDTO объект типа PersonDTO
     * @return Optional.of(person) если пользователь сохранен успешно, иначе Optional.empty()
     */
    @Override
    public Optional<Person> signUp(PersonDTO personDTO) {
        Optional<Person> result = Optional.empty();
        try {
            var person = Person.builder()
                    .login(personDTO.getLogin())
                    .password(passwordEncoder.encode(personDTO.getPassword()))
                    .build();
            result = Optional.of(personRepository.save(person));
        } catch (Exception e) {
            log.error("Exception in the ignUp(PersonDTO personDTO) method", e);
        }
        return result;
    }

    /**
     * Сохранить пользователя в базе данных
     *
     * @param personDTO объект типа PersonDTO
     * @return пользователь
     */
    @Override
    public Optional<Person> save(PersonDTO personDTO) {
        Optional<Person> result = Optional.empty();
        var person = Person.builder()
                .login(personDTO.getLogin())
                .password(personDTO.getPassword())
                .build();
        try {
            personRepository.save(person);
            result = Optional.of(person);
        } catch (Exception e) {
            log.error("Exception in the save(PersonDTO personDTO) method", e);
        }
        return result;
    }

    /**
     * Обновить пользователя в базе данных
     *
     * @param personDTO объект типа PersonDTO
     * @return true если пользователь успешно обновлен, иначе false
     */
    @Override
    public boolean update(PersonDTO personDTO) {
        boolean result = false;
        if (personRepository.findByLogin(personDTO.getLogin()).isPresent()) {
            var person = Person.builder()
                    .login(personDTO.getLogin())
                    .password(personDTO.getPassword())
                    .build();
            personRepository.save(person);
            result = true;
        }
        return result;

    }

    /**
     * Удалить пользователя из базы данных
     *
     * @param id идентификатор пользователя
     * @return true если пользователь успешно удален, иначе false
     */
    @Override
    public boolean delete(int id) {
        boolean result = false;
        if (personRepository.existsById(id)) {
            var person = new Person();
            person.setId(id);
            personRepository.delete(person);
            result = true;
        }
        return result;
    }

    /**
     * Обновить пароль пользователя в базе данных
     *
     * @param personDTO объект типа PersonDTO
     * @return true если пользователь успешно обновлен, иначе false
     */
    @Override
    public boolean updatePassword(PersonDTO personDTO) {
        boolean result = false;
        var optionalPerson = personRepository.findByLogin(personDTO.getLogin());
        if (optionalPerson.isPresent()) {
            optionalPerson.get().setPassword(personDTO.getPassword());
            personRepository.save(optionalPerson.get());
            result = true;
        }
        return result;
    }
}
