package ru.job4j.auth.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

/**
 * PersonServiceImpl - реализация сервиса по работе с пользователями
 *
 * @author Ilya Kaltygin
 */
@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    /**
     * Хранилище пользователей
     */
    private final PersonRepository personRepository;

    /**
     * Найти всех пользователей в базе данных
     *
     * @return список всех пользователей
     */
    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    /**
     * Найти ползователя по идентификатору
     *
     * @param id идентфикатор пользователя
     * @return Optional.of(person) если пользователь найден, иначе Optional.empty()
     */
    @Override
    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    /**
     * Сохранить пользователя в базе данных
     *
     * @param person пользователь
     * @return пользователь
     */
    @Override
    public Person save(Person person) {
        return personRepository.save(person);
    }

    /**
     * Обновить пользователя в базе данных
     *
     * @param person пользователь
     * @return true если пользователь успешно обновлен, иначе false
     */
    @Override
    public boolean update(Person person) {
        boolean result = false;
        if (personRepository.existsById(person.getId())) {
            personRepository.save(person);
            result = true;
        }
        return result;

    }

    /**
     * Удалить пользователя из базы данных
     *
     * @param person пользователь
     * @return true если пользователь успешно удален, иначе false
     */
    @Override
    public boolean delete(Person person) {
        boolean result = false;
        if (personRepository.existsById(person.getId())) {
            personRepository.delete(person);
            result = true;
        }
        return result;
    }
}
