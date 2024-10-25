package thongld25.hms.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurgeryResponse {
    private UUID id;
    private String doctorId;
    private String doctorName;
    private String patientId;
    private String patientName;
    private Date time;
    private String content;
    private float expectedTime;
}
