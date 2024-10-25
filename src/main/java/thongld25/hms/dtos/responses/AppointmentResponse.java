package thongld25.hms.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thongld25.hms.enums.AppointmentStatus;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private UUID id;
    private String doctorId;
    private String patientId;
    private String doctorName;
    private String patientName;
    private Date time;
    private AppointmentStatus status;
    private  String note;
}
