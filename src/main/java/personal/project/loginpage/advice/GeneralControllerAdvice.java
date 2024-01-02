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
import personal.project.loginpage.exception.DuplicateEntryException;
import personal.project.loginpage.exception.InvalidEmailFormatException;
import personal.project.loginpage.exception.InvalidLoginException;
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
        exception.getMessage(),
        null
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
  }

  @ExceptionHandler
  public ResponseEntity<Problem> handleInvalidEmailFormat(InvalidEmailFormatException exception) {
    Problem problem = new Problem(
        HttpStatus.BAD_REQUEST.value(),
        "Invalid Email Format",
        exception.getMessage(),
        null
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
  }
  @ExceptionHandler
  public ResponseEntity<Problem> handleNotCaught(Exception exception) {
    Problem problem = new Problem(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Internal Server Error",
        exception.getMessage(),
        null
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
  }

  @ExceptionHandler
  public ResponseEntity<Problem> handleInvalidType(MethodArgumentTypeMismatchException exception) {
    Problem problem = new Problem(
        HttpStatus.BAD_REQUEST.value(),
        "Invalid Type Format",
        exception.getMessage(),
        null
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Problem> handleNotFoundField(MethodArgumentNotValidException exception) {

    List<ErrorMessageDto> probList = new ArrayList<>();

    exception.getBindingResult().getFieldErrors().forEach(e -> {
      String detail = messageSource.getMessage(e, LocaleContextHolder.getLocale());
      ErrorMessageDto messageDetail = new ErrorMessageDto(
          e.getField(),
          detail
      );
      probList.add(messageDetail);
    });
    Problem problem = new Problem(
        HttpStatus.BAD_REQUEST.value(),
        "Invalid Parameters",
        " Invalid Request Body",
        probList
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
  }

  @ExceptionHandler
  public ResponseEntity<Problem> handleInvalidLoginException(InvalidLoginException exception) {
    Problem problem = new Problem(
        HttpStatus.UNAUTHORIZED.value(),
        "Unauthorized Login",
        exception.getMessage(),
        null
    );
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
  }

  @ExceptionHandler
  public ResponseEntity<Problem> handleDuplicateEntryException(DuplicateEntryException exception) {
    Problem problem = new Problem(
        HttpStatus.CONFLICT.value(),
        "Duplicate Entry User",
        exception.getMessage(),
        null
    );
    return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
  }

}
