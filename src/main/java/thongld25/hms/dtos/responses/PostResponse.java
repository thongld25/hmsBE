package thongld25.hms.dtos.responses;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private UUID doctorId;
    private String doctorName;
    private UUID id;
    private String title;
    private String content;
    private String summary;
    private String cover;
    private String coverContent;
    private UUID categoryId;
    private String categoryName;
}
