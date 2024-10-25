package thongld25.hms.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import thongld25.hms.enums.AppointmentStatus;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Appointment {
    @Id
    @GeneratedValue
    UUID id;
    Date time;
    @Enumerated(EnumType.STRING)
    AppointmentStatus status;
    @Column(length = 500)
    String note;
    Date createdDay;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    Doctor doctor;
}
