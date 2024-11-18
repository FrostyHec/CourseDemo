package org.frosty.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
@Getter
@Setter
public class ExternalExceptionOnResponseEntity extends RuntimeException {
    private ResponseEntity<?> responseEntity;
}
