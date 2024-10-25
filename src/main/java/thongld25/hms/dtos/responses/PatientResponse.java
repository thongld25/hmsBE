package thongld25.hms.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {
    private UUID id;
    private String name;
    private String address;
    private Date birthday;
    private String job;
    private String phone;
    private String nation;
    private String gender;
}
