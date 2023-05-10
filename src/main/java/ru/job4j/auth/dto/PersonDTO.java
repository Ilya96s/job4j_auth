package ru.job4j.auth.dto;

import lombok.Data;

/**
 * PersonDTO - Person Data Transfer Object
 *
 * @author Ilya Kaltygin
 */
@Data
public class PersonDTO {

    private String login;

    private String password;
}
