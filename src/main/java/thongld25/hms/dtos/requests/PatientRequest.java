package thongld25.hms.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientRequest {
    @NotBlank
    String name;
    @NotBlank
    String address;
    @NotNull
    Date birthday;
    @NotBlank
    String job;
    @NotBlank
    String phone;
    @NotBlank
    String nation;
    @NotBlank
    @Pattern(regexp = "^(MALE|FEMALE)$")
    String gender;
}
