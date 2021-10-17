package vn.edu.fpt.traffic_license.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseStatusCodeConst {

    SUCCESS("00", HttpStatus.OK.value()),
    BUSINESS_ERROR("0001", HttpStatus.BAD_REQUEST.value()),
    VALIDATION_ERROR("0002", HttpStatus.BAD_REQUEST.value()),
    INTERNAL_SERVER_ERROR("0003", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    DUPLICATE_ERROR("0004", HttpStatus.BAD_REQUEST.value()),
    DATA_NOT_FOUND_ERROR("0005", HttpStatus.NOT_FOUND.value()),
    UNAUTHORIZED("0006", HttpStatus.UNAUTHORIZED.value()),
    MISSING_CLIENT_CODE("0007", HttpStatus.UNAUTHORIZED.value()),
    MISSING_DATA_SIGNATURE("0008", HttpStatus.BAD_REQUEST.value()),
    MISSING_REQUEST_DATE("0009", HttpStatus.BAD_REQUEST.value()),
    INVALID_CLIENT_CODE("0010", HttpStatus.BAD_REQUEST.value()),
    MISSING_PUBLIC_KEY("0011", HttpStatus.BAD_REQUEST.value()),
    INVALID_SIGNATURE("0012", HttpStatus.BAD_REQUEST.value()),
    FORBIDDEN("0013", HttpStatus.FORBIDDEN.value());

    private final String code;
    private final int httpCode;

}