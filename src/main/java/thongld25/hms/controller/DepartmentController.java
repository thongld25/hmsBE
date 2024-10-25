package thongld25.hms.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thongld25.hms.dtos.requests.DepartmentRequest;
import thongld25.hms.dtos.responses.ApiResponse;
import thongld25.hms.dtos.responses.DepartmentResponse;
import thongld25.hms.service.DepartmentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentController {
    DepartmentService departmentService;

    @GetMapping("/departments")
    public ApiResponse<List<DepartmentResponse>> getAll(){
        return ApiResponse.<List<DepartmentResponse>>builder()
                .result(departmentService.getAllDepartment())
                .build();
    }

    @PostMapping("/departments")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<DepartmentResponse> createDepartment(@RequestBody DepartmentRequest request) {
        return ApiResponse.<DepartmentResponse>builder()
                .result(departmentService.createDepartment(request))
                .build();
    }

    @DeleteMapping("/departments/{departmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<HttpStatus> deleteDepartment(@PathVariable UUID departmentId){
        departmentService.deleteDepartment(departmentId);
        return ApiResponse.<HttpStatus>builder()
                .result(HttpStatus.OK)
                .build();
    }

//    @GetMapping(value = "/departments")
//    public ApiResponse<List<DepartmentResponse>>getDepartmentPage(
//            @RequestParam(defaultValue = "1") int pageNo,
//            @RequestParam(defaultValue = "10") int pageSize
//    ){
//     return ApiResponse.<List<DepartmentResponse>>builder()
//             .result(departmentService.getDepartmentPage(pageNo, pageSize))
//             .build();
//    }
}
