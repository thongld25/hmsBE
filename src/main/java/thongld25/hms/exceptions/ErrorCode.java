package thongld25.hms.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid message key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least 4 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    PHONE_NUMBER_EXISTED(1008, "Phone number existed", HttpStatus.BAD_REQUEST),
    NO_PATIENT_FOUND(1009, "No patient exist", HttpStatus.NOT_FOUND),
    NO_DOCTOR_FOUND(1010, "No doctor exist", HttpStatus.NOT_FOUND),
    NO_DEPARTMENT_FOUND(1011, "No department exist", HttpStatus.NOT_FOUND),
    NO_CATEGORY_FOUND(1012, "No category exist", HttpStatus.NOT_FOUND),
    NO_SURGERY_FOUND(1013, "No surgery exist", HttpStatus.NOT_FOUND),
    APPOINTMENT_EXISTED(1014, "Appointment existed", HttpStatus.NOT_FOUND),
    NO_TREATMENT_PLAN_FOUND(1013, "No treatment plan exist", HttpStatus.NOT_FOUND),
    NO_MEDICAL_RECORD_FOUND(1014, "No medical record exist", HttpStatus.NOT_FOUND),
    NO_POST_FOUND(1014, "No post exist", HttpStatus.NOT_FOUND),
    INVALID_DATE_RANGE(1014, "invalid date range", HttpStatus.BAD_REQUEST),
    INVALID_STAY_TYPE(1014, "invalid stay type", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;

}
