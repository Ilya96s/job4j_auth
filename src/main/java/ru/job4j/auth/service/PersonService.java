package ru.job4j.auth.service;

import ru.job4j.auth.model.Person;

import java.util.List;
import java.util.Optional;

/**
 * PersonService - интерфейс, описывающий бизнес логику по работе с пользователями
 *
 * @author Ilya Kaltygin
 */
public interface PersonService {

    /**
     * Найти всех пользователей в базе данных
     *
     * @return список всех пользователей
     */
    List<Person> findAll();

    /**
     * Найти ползователя по идентификатору
     *
     * @param id идентфикатор пользователя
     * @return Optional.of(person) если пользователь найден, иначе Optional.empty()
     */
    Optional<Person> findById(int id);

    /**
     * Сохранить пользователя в базе данных
     *
     * @param person пользователь
     * @return Optional.of(person) если пользователь сохранен успешно, иначе Optional.empty()
     */
    Optional<Person> save(Person person);

    /**
     * Обновить пользователя в базе данных
     *
     * @param person пользователь
     * @return true если пользователь успешно обновлен, иначе false
     */
    boolean update(Person person);

    /**
     * Удалить пользователя из базы данных
     *
     * @param person пользователь
     * @return true если пользователь успешно удален, иначе false
     */
    boolean delete(Person person);
}
