package com.kuptel.Organization.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 Exception used when the node id provided by the api user
 can't be found.
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="The provided node id does not exist")
public class NodeUnknownException extends RuntimeException {
}
