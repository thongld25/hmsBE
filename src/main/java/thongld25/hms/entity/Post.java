package thongld25.hms.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {
    @Id
    @GeneratedValue
    UUID id;
    String title;
    @Column(length = 1000000)
    String content;
    @Column(length = 1000)
    String summary;
    String cover;
    @Column(length = 500)
    String coverContent;
    Date createdDay;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
}
