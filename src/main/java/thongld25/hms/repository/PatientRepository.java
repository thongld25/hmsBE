package thongld25.hms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thongld25.hms.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
    boolean existsByPhone(String phone);
    Patient findByPhone(String phone);

}
