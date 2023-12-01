package personal.project.loginpage.advice;

public record Problem (
    int status,
    String message,
    String detail
) {}
