package thongld25.hms.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thongld25.hms.dtos.requests.DoctorRequest;
import thongld25.hms.dtos.responses.ApiResponse;
import thongld25.hms.dtos.responses.DoctorResponse;
import thongld25.hms.dtos.responses.UserResponse;
import thongld25.hms.service.DoctorService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DoctorController {

    DoctorService doctorService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/doctors", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UserResponse> createDoctor(@ModelAttribute @Valid DoctorRequest request) throws IOException {

        return ApiResponse.<UserResponse>builder()
                .result(doctorService.createDoctor(request))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/doctors/all")
    public ApiResponse<List<DoctorResponse>> getAllDoctor() {
        return ApiResponse.<List<DoctorResponse>>builder()
                .result(doctorService.findAllDoctor())
                .build();
    }

    @GetMapping("/doctors/page")
    public ApiResponse<List<DoctorResponse>> getDoctors(
                @RequestParam(defaultValue = "1") int pageNo,
                @RequestParam(defaultValue = "2") int pageSize
    ) {
        return ApiResponse.<List<DoctorResponse>>builder()
                .result(doctorService.getDoctors(pageNo, pageSize))
                .build();
    }

    @GetMapping("/doctors/byDepartment")
    public ApiResponse<List<DoctorResponse>> getDoctorByDepartment(
                @RequestParam UUID departmentId,
                @RequestParam(defaultValue = "1") int pageNo,
                @RequestParam(defaultValue = "10") int pageSize
            ) {
        return ApiResponse.<List<DoctorResponse>>builder()
                .result(doctorService.getDoctorByDepartment(departmentId, pageNo, pageSize))
                .build();
    }

    @GetMapping("/doctors/{doctorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<DoctorResponse> getDoctorById(@PathVariable String doctorId){
        return ApiResponse.<DoctorResponse>builder()
                .result(doctorService.findById(doctorId))
                .build();
    }

    @DeleteMapping("/doctors/{doctorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<HttpStatus> deleteDoctor(@PathVariable String doctorId){
        doctorService.deleteDoctor(doctorId);
        return ApiResponse.<HttpStatus>builder()
                .result(HttpStatus.OK)
                .build();
    }

    @PutMapping(value = "/doctors/{doctorId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<DoctorResponse> updateDoctor(@PathVariable String doctorId, @ModelAttribute DoctorRequest request)
            throws IOException {
        return ApiResponse.<DoctorResponse>builder()
                .result(doctorService.updateDoctor(doctorId, request))
                .build();
    }
}
