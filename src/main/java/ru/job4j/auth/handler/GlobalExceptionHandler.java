package ru.job4j.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/***
 * GlobalExceptionHandler - глобальный обработчик исключений, возникших во всех контроллерах
 *
 * @author Ilya Kaltygin
 */
@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Функционал для работы с JSON
     */
    private final ObjectMapper objectMapper;

    /**
     * Метод обрабатывает все исключения NullPointerException, которые возникают во всех контроллерах
     *
     * @param e        исключение, которое было сгенирировано и перехвачено данным методом
     * @param request  запрос
     * @param response ответ
     * @throws IOException exception
     */
    @ExceptionHandler(value = {NullPointerException.class})
    public void handleException(Exception e,
                                HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
            {
                put("message", "Some of fields empty");
                put("details", e.getMessage());
            }
        }));
        log.error(e.getMessage());
    }

    /**
     * Метод обрабатывает все исключения MethodArgumentNotValidException, которые возникают во всех контроллерах
     *
     * @param e исключение, которое было сгенирировано и перехвачено данным методом
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(
                e.getFieldErrors().stream()
                        .map(f -> Map.of(
                                f.getField(),
                                String.format("%s. Actual value: %s", f.getDefaultMessage(), f.getRejectedValue())
                        ))
                        .collect(Collectors.toList())
        );
    }
}
