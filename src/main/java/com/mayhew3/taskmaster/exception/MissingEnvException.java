package com.mayhew3.taskmaster.exception;

public class MissingEnvException extends Exception {
  public MissingEnvException(String errorMessage) {
    super(errorMessage);
  }
}
