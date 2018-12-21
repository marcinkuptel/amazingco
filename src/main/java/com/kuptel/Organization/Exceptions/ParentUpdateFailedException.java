package com.kuptel.Organization.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 Exception thrown when updating the parent of a given node failed.
 */
@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Could not update parent.")
public class ParentUpdateFailedException extends RuntimeException {
}
