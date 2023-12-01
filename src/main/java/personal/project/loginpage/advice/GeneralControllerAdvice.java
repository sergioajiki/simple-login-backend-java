package personal.project.loginpage.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import personal.project.loginpage.exception.NotFoundException;

@ControllerAdvice
public class GeneralControllerAdvice {

  @ExceptionHandler
  public ResponseEntity<String> handleNotFoundException(NotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<Problem> handleNotCaught(Exception exception) {
    Problem problem = new Problem(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Erro Interno",
        exception.getMessage()
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
  }

  @ExceptionHandler
  public ResponseEntity<Problem> handleInvalidType(MethodArgumentTypeMismatchException exception) {
    Problem problem = new Problem(
        HttpStatus.BAD_REQUEST.value(),
        "Invalid Type Format",
        exception.getMessage()
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
  }

  @ExceptionHandler
  public ResponseEntity<Problem> handleNotFoundField(MethodArgumentNotValidException exception) {
    Problem problem = new Problem(
        HttpStatus.BAD_REQUEST.value(),
        "Invalid argument",
        exception.getBindingResult().getAllErrors().get(0).getDefaultMessage()
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
  }
}
