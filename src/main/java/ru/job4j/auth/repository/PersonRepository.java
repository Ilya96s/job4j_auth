package ru.job4j.auth.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.auth.model.Person;

/**
 * PersonRepository - хранилище пользователей
 *
 * @author Ilya Kaltygin
 */
public interface PersonRepository extends CrudRepository<Person, Integer> {

    /**
     * Найти всех пользователей в базе данных
     *
     * @return список всех пользователей
     */
    Iterable<Person> findAll();
}
