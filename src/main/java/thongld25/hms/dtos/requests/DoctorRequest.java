package thongld25.hms.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRequest {
    @NotNull
    private UUID departmentId;
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String gender;
    private MultipartFile image;
}