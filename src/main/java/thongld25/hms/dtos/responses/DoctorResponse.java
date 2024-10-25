package thongld25.hms.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {
    private UUID departmentId;
    private String departmentName;
    private UUID id;
    private String name;
    private String address;
    private Date birthday;
    private String phoneNumber;
    private String gender;
    private String image;
//    private float rating;
}
