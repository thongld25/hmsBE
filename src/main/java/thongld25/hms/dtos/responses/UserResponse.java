package thongld25.hms.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thongld25.hms.enums.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String password;
    Role role;
}
