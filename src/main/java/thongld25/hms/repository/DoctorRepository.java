package thongld25.hms.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thongld25.hms.entity.Doctor;

import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {
    boolean existsByPhoneNumber(String phoneNumber);
    Doctor findByPhoneNumber(String phoneNumber);
    List<Doctor> findByDepartmentId(UUID departmentId, Pageable pageable);
}
