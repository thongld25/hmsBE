package thongld25.hms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import thongld25.hms.entity.Surgery;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface SurgeryRepository extends JpaRepository<Surgery, UUID> {
    @Query("SELECT s FROM Surgery s WHERE s.time BETWEEN :startOfWeek AND :endOfWeek")
    List<Surgery> findSurgeryInWeek(@Param("startOfWeek") Date startOfWeek,
                                    @Param("endOfWeek") Date endOfWeek);

    List<Surgery> findByDoctorId(String doctorId);
}
