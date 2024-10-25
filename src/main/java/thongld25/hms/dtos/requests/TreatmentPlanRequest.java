package thongld25.hms.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class TreatmentPlanRequest {
    @NotNull
    private String patientId;
    @NotBlank
    private String treatmentMethod;
    @NotNull
    private Date lastExaminationDay;
    @NotNull
    private Date nextExpectedExaminationDay;
    private String note;
    @NotNull
    private UUID medicalRecordId;
}
