package thongld25.hms.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TreatmentPlanResponse {
    private UUID id;
    private UUID doctorId;
    private String doctorName;
    private UUID patientId;
    private String patientName;
    private String treatmentMethod;
    private Date lastExaminationDay;
    private Date nextExpectedExaminationDay;
    private String note;
    private UUID medicalRecordId;
}
