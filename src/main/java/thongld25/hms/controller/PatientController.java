package thongld25.hms.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thongld25.hms.dtos.requests.PatientRequest;
import thongld25.hms.dtos.responses.ApiResponse;
import thongld25.hms.dtos.responses.PatientResponse;
import thongld25.hms.dtos.responses.UserResponse;
import thongld25.hms.service.PatientService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/patients")
public class PatientController {
    PatientService patientService;

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping
    public ApiResponse<UserResponse> createPatient(@Valid @RequestBody PatientRequest patientRequest){
        return ApiResponse.<UserResponse>builder()
                .result(patientService.createPatient(patientRequest))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ApiResponse<List<PatientResponse>> getAllPatient() {
        return ApiResponse.<List<PatientResponse>>builder()
                .result(patientService.getAllPatient())
                .build();
    }

    @GetMapping("/byType")
    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<List<PatientResponse>> getPatientByType(String type) {
        return ApiResponse.<List<PatientResponse>>builder()
                .result(patientService.getPatientByType(type))
                .build();
    }

    @GetMapping("/doctor")
    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<List<PatientResponse>> getPatientOfDoctor(String doctorId){
        return ApiResponse.<List<PatientResponse>>builder()
                .result(patientService.getPatientOfDoctor(doctorId))
                .build();
    }

    @GetMapping("/{patientId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public ApiResponse<PatientResponse> getPatientById(@PathVariable String patientId){
        return ApiResponse.<PatientResponse>builder()
                .result(patientService.getPatientById(patientId))
                .build();
    }

    @PutMapping("/{patientId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<PatientResponse> updatePatient(@PathVariable String patientId, @Valid @RequestBody PatientRequest request){
        return ApiResponse.<PatientResponse>builder()
                .result(patientService.updatePatient(patientId, request))
                .build();
    }


}
