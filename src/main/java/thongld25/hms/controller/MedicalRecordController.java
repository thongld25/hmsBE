package thongld25.hms.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thongld25.hms.dtos.requests.MedicalRecordRequest;
import thongld25.hms.dtos.responses.ApiResponse;
import thongld25.hms.dtos.responses.MedicalRecordResponse;
import thongld25.hms.service.MedicalRecordService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/medicalRecords")
public class MedicalRecordController {
    MedicalRecordService medicalRecordService;

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public ApiResponse<List<MedicalRecordResponse>> getForPatient(
            @RequestParam String id,
            @RequestParam int pageNo,
            @RequestParam int pageSize
    ) {
        return ApiResponse.<List<MedicalRecordResponse>>builder()
                .result(medicalRecordService.getForPatientId(id, pageNo, pageSize))
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public ApiResponse<MedicalRecordResponse> getById(@PathVariable UUID id) {
        return ApiResponse.<MedicalRecordResponse>builder()
                .result(medicalRecordService.getById(id))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<MedicalRecordResponse> createMedicalRecord(@RequestBody MedicalRecordRequest request) {
        return ApiResponse.<MedicalRecordResponse>builder()
                .result(medicalRecordService.createMedicalRecord(request))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<MedicalRecordResponse> updateMedicalRecord(@PathVariable UUID id, @RequestBody MedicalRecordRequest request)
            throws BadRequestException {
        return ApiResponse.<MedicalRecordResponse>builder()
                .result(medicalRecordService.updateMedicalRecord(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<HttpStatus> deleteMedicalRecord(@PathVariable UUID id)
            throws BadRequestException {
        medicalRecordService.deleteMedicalRecord(id);
        return ApiResponse.<HttpStatus>builder()
                .result(HttpStatus.OK)
                .build();
    }
}
