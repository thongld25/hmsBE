package thongld25.hms.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurgeryRequest {
    @NotNull
    private UUID doctorId;
    @NotNull
    private UUID patientId;
    @NotNull
    private Date time;
    @NotBlank
    private String content;
    @NotNull
    private float expectedTime;
}
