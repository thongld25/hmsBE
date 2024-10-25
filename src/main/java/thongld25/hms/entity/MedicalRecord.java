package thongld25.hms.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import thongld25.hms.enums.StayType;

import javax.print.Doc;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "medical_records")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalRecord {
    @Id
    @GeneratedValue
    UUID id;
    String bhytCode;
    Date inDay;
    Date outDay;
    @Column(length = 1000)
    String inDayDiagnose;
    @Column(length = 1000)
    String outDayDiagnose;
    @Column(length = 1000)
    String medicalHistory;
    @Column(length = 1000)
    String diseaseProgress;
    @Column(length = 1000)
    String testResults;
    @Column(length = 1000)
    String hospitalDischargeStatus;
    @Enumerated(EnumType.STRING)
    StayType stayType;
    @Column(length = 1000)
    String note;
    Date createdDay;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "department_id")
    Department department;

    @OneToOne(mappedBy = "medicalRecord", cascade = CascadeType.ALL)
    TreatmentPlan treatmentPlan;
}
