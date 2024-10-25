package thongld25.hms.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thongld25.hms.dtos.requests.SurgeryRequest;
import thongld25.hms.dtos.responses.ApiResponse;
import thongld25.hms.dtos.responses.SurgeryResponse;
import thongld25.hms.service.SurgeryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SurgeryController {
    SurgeryService surgeryService;

    @GetMapping("/surgeries")
    public ApiResponse<List<SurgeryResponse>> getAllSurgery(){
        return ApiResponse.<List<SurgeryResponse>>builder()
                .result(surgeryService.getAllSurgery())
                .build();
    }
    @GetMapping("/surgeries/inweek")
    public ApiResponse<List<SurgeryResponse>> getSurgeryInWeek() {
        return ApiResponse.<List<SurgeryResponse>>builder()
                .result(surgeryService.getSurgeryInWeek())
                .build();
    }

    @GetMapping("/surgeries/{doctorId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ApiResponse<List<SurgeryResponse>> getSurgeryForDoctor(@PathVariable String doctorId) {
        return ApiResponse.<List<SurgeryResponse>>builder()
                .result(surgeryService.getSurgeryForDoctor(doctorId))
                .build();
    }

    @PostMapping("/surgeries")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<SurgeryResponse> addSurgery(@RequestBody SurgeryRequest surgeryRequest) {
        return ApiResponse.<SurgeryResponse>builder()
                .result(surgeryService.createSurgery(surgeryRequest))
                .build();
    }

    @PutMapping("/surgeries/{surgeryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<SurgeryResponse> updateSurgery(@PathVariable UUID surgeryId, SurgeryRequest request) {
        return ApiResponse.<SurgeryResponse>builder()
                .result(surgeryService.updateSurgery(surgeryId, request))
                .build();
    }

    @DeleteMapping("/surgeries/{surgeryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<HttpStatus> deleteSurgery(@PathVariable UUID surgeryId) {
        surgeryService.deleteSurgery(surgeryId);
        return ApiResponse.<HttpStatus>builder()
                .result(HttpStatus.OK)
                .build();
    }

}
