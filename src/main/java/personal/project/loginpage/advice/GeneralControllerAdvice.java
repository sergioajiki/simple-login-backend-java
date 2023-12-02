package personal.project.loginpage.advice;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import personal.project.loginpage.exception.NotFoundException;

@ControllerAdvice
public class GeneralControllerAdvice {

  private final MessageSource messageSource;

  @Autowired
  public GeneralControllerAdvice(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler
  public ResponseEntity<Problem> handleNotFoundException(NotFoundException exception) {
    Problem problem = new Problem(
        HttpStatus.NOT_FOUND.value(),
        "User Not Found",
        exception.getMessage()
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
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

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<ErrorMessageDto>> handleNotFoundField(
      MethodArgumentNotValidException exception) {

    List<ErrorMessageDto> probList = new ArrayList<>();
    exception.getBindingResult().getFieldErrors().forEach(e -> {
      String message = messageSource.getMessage(e, LocaleContextHolder.getLocale());
      ErrorMessageDto problem = new ErrorMessageDto(
          e.getField(),
          message
      );
      probList.add(problem);
    });

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(probList);
  }
}
