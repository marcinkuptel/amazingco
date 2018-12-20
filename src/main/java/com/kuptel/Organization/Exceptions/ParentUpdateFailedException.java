package com.kuptel.Organization.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Could not update parent.")
public class ParentUpdateFailedException extends RuntimeException {
}
