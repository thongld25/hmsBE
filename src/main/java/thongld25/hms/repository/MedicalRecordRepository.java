package thongld25.hms.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import thongld25.hms.entity.Doctor;
import thongld25.hms.entity.MedicalRecord;
import thongld25.hms.entity.Patient;
import thongld25.hms.enums.StayType;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, UUID> {
    List<MedicalRecord> findByStayType(StayType stayType);

    @Query("SELECT mr.patient FROM MedicalRecord mr WHERE mr.stayType = :stayType")
    List<Patient> findPatientsByStayType(@Param("stayType") StayType stayType);

    @Query("SELECT mr.patient FROM MedicalRecord mr WHERE mr.doctor = :doctor")
    List<Patient> findPatientsOfDoctor(@Param("doctor") Doctor doctor);

    List<MedicalRecord> findByPatientId(String patientId, PageRequest pageRequest);
}
