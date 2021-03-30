package nl.tudelft.oopp.g7.server.utility.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "unauthorized")
public class UnauthorizedException extends RuntimeException {
}
