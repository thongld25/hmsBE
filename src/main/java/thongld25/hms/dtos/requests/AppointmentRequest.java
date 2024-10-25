package thongld25.hms.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentRequest {
    @NotNull(message = "Date can not be null")
    Date time;
    String note;
    @NotNull(message = "doctorId can not be null")
    String doctorId;
}
