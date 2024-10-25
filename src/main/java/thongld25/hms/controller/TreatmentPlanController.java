package thongld25.hms.controller;

import com.cloudinary.Api;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thongld25.hms.dtos.requests.TreatmentPlanRequest;
import thongld25.hms.dtos.responses.ApiResponse;
import thongld25.hms.dtos.responses.TreatmentPlanResponse;
import thongld25.hms.service.TreatmentPlanService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/treatmentPlans")
public class TreatmentPlanController {
    TreatmentPlanService treatmentPlanService;

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public ApiResponse<List<TreatmentPlanResponse>> getForPatient(
            @RequestParam String patientId,
            @RequestParam int pageNo,
            @RequestParam int pageSize
    ) {
        return ApiResponse.<List<TreatmentPlanResponse>>builder()
                .result(treatmentPlanService.getForPatient(patientId, pageNo, pageSize))
                .build();
    }

    @GetMapping("/{treatmentPlanId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public ApiResponse<TreatmentPlanResponse> getBId(@PathVariable UUID treatmentPlanId) {
        return ApiResponse.<TreatmentPlanResponse>builder()
                .result(treatmentPlanService.getById(treatmentPlanId))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<TreatmentPlanResponse> createTreatmentPlan(@RequestBody TreatmentPlanRequest request){
        return ApiResponse.<TreatmentPlanResponse>builder()
                .result(treatmentPlanService.createTreatmentPlan(request))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<TreatmentPlanResponse> updateTreatmentPlan(
            @PathVariable UUID id,
            @RequestBody TreatmentPlanRequest request) {
        return ApiResponse.<TreatmentPlanResponse>builder()
                .result(treatmentPlanService.updateTreatmentPlan(request, id))
                .build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<HttpStatus> deleteTreatmentPlan(UUID id)
            throws BadRequestException {
        treatmentPlanService.deleteTreatmentPlan(id);
        return ApiResponse.<HttpStatus>builder()
                .result(HttpStatus.OK)
                .build();
    }
}
