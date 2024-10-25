package thongld25.hms.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class MedicalRecordRequest {
    @NotNull
    private String patientId;
    @NotNull
    private UUID departmentId;
    @NotBlank
    private String bhytCode;
    @NotNull
    private Date inDay;
    private Date outDay;
    @NotBlank
    private String inDayDiagnose;
    private String outDayDiagnose;
    @NotBlank
    private String medicalHistory;
    @NotBlank
    private String diseaseProgress;
    @NotBlank
    private String testResults;
    private String hospitalDischargeStatus;
    @NotBlank
    @Pattern(regexp = "^(STAY|NOT_STAY|DAYTIME_STAY)$")
    private String stayType;
    private String note;
    private TreatmentPlanRequest treatmentPlan;
}
