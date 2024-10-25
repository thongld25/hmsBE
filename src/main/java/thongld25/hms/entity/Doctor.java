package thongld25.hms.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import thongld25.hms.enums.Gender;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Doctor {
    @Id
    String id;
    String name;
    String address;
    Date birthday;
    String phoneNumber;
    @Enumerated(EnumType.STRING)
    Gender gender;
    String image;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "id")
    User user;

    @ManyToOne
    @JoinColumn(name = "department_id")
    Department department;

}
