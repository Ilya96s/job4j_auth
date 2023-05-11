package ru.job4j.auth.service;

import ru.job4j.auth.dto.PersonDTO;
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
    List<PersonDTO> findAll();

    /**
     * Найти ползователя по идентификатору
     *
     * @param id идентфикатор пользователя
     * @return Optional.of(personDTO) если пользователь найден, иначе Optional.empty()
     */
    Optional<PersonDTO> findById(int id);

    /**
     * Найти пользователя в базе данных по логину
     *
     * @param login логин
     * @return Optional.of(personDTO) если пользователь найден, иначе Optional.empty()
     */
    Optional<PersonDTO> findByLogin(String login);

    /**
     * Хешировать пароль пользователя и сохранить пользователя в базу данных
     *
     * @param personDTO объект типа PersonDTO
     * @return Optional.of(person) если пользователь сохранен успешно, иначе Optional.empty()
     */
    Optional<Person> signUp(PersonDTO personDTO);

    /**
     * Сохранить пользователя в базе данных
     *
     * @param personDTO объект типа PersonDTO
     * @return Optional.of(person) если пользователь сохранен успешно, иначе Optional.empty()
     */
    Optional<Person> save(PersonDTO personDTO);

    /**
     * Обновить пользователя в базе данных
     *
     * @param personDTO объект типа PersonDTO
     * @return true если пользователь успешно обновлен, иначе false
     */
    boolean update(PersonDTO personDTO);

    /**
     * Удалить пользователя из базы данных
     *
     * @param id идентификатор пользователя
     * @return true если пользователь успешно удален, иначе false
     */
    boolean delete(int id);

    /**
     * Обновить пароль пользователя в базе данных
     *
     * @param personDTO объект типа PersonDTO
     * @return true если пароль успешно обновлен, иначе false
     */
    boolean updatePassword(PersonDTO personDTO);
}
