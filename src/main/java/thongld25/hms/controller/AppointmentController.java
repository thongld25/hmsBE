package thongld25.hms.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thongld25.hms.dtos.requests.AppointmentRequest;
import thongld25.hms.dtos.responses.ApiResponse;
import thongld25.hms.dtos.responses.AppointmentResponse;
import thongld25.hms.repository.AppointmentRepository;
import thongld25.hms.service.AppointmentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/appointments")
public class AppointmentController {
    AppointmentService appointmentService;

    @GetMapping("/doctor")
    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<List<AppointmentResponse>> getAppointmentOfDoctor() {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .result(appointmentService.getAppointmentOfDoctor())
                .build();
    }

    @GetMapping("/patient")
    @PreAuthorize("hasRole('PATIENT')")
    public ApiResponse<List<AppointmentResponse>> getAppointmentOfPatient() {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .result(appointmentService.getAppointmentOfPatient())
                .build();
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('PATIENT')")
    public ApiResponse<AppointmentResponse> createAppointment(@RequestBody AppointmentRequest request) {
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.createAppointment(request))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ApiResponse<AppointmentResponse> updateAppointment(@PathVariable UUID id, @RequestBody AppointmentRequest request) {
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.updateAppointment(id ,request))
                .build();
    }

    @PutMapping("/accept/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<AppointmentResponse> acceptAppointment(@PathVariable UUID id) throws BadRequestException {
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.acceptAppointment(id))
                .build();
    }

    @PutMapping("/reject/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<AppointmentResponse> rejectAppointment(@PathVariable UUID id) throws BadRequestException {
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.rejectAppointment(id))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ApiResponse<HttpStatus> deleteAppointment(@PathVariable UUID id) {
        appointmentService.deleteAppointment(id);
        return ApiResponse.<HttpStatus>builder()
                .result(HttpStatus.OK)
                .build();
    }
}
