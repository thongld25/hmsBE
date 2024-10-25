package thongld25.hms.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thongld25.hms.entity.Post;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByDoctorId(String doctorId, Pageable pageRequest);

    List<Post> findByCategoryId(UUID categoryId, Pageable pageable);
}
