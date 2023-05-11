package ru.job4j.auth.dto;

import lombok.Data;
import ru.job4j.auth.validation.Operation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * PersonDTO - Person Data Transfer Object
 *
 * @author Ilya Kaltygin
 */
@Data
public class PersonDTO {

    @NotBlank(
            message = "Login must not be empty",
            groups = {Operation.OnUpdate.class, Operation.OnCreate.class})
    private String login;

    @NotBlank(
            message = "Password must be at least 5 characters long",
            groups = {Operation.OnUpdate.class, Operation.OnCreate.class})
    @Min(
            message = "Password must be at least 5 characters long",
            value = 5, groups = {Operation.OnUpdate.class, Operation.OnCreate.class})
    private String password;
}
