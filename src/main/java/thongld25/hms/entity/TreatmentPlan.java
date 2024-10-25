package thongld25.hms.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "treatment_plans")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreatmentPlan {
    @Id
    @GeneratedValue
    UUID id;

    @Column(length = 500)
    String treatmentMethod;
    Date lastExaminationDay;
    Date ExceptedExaminationDay;
    @Column(length = 500)
    String note;
    Date createdDay;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    Doctor doctor;

    @OneToOne
    @JoinColumn(name = "medical_record_id")
    MedicalRecord medicalRecord;
}
