package com.kuptel.Organization.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 Exception used when /organization/{nodeId}/parent/{parentId} endpoint
 is called and parentId is a descendant of nodeId.
 */
@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="The provided nodes have a ancestor-descendant relationship")
public class AncestorViolationException extends RuntimeException {
}