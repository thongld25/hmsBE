package thongld25.hms.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import thongld25.hms.dtos.requests.TreatmentPlanRequest;
import thongld25.hms.dtos.responses.TreatmentPlanResponse;
import thongld25.hms.entity.Doctor;
import thongld25.hms.entity.MedicalRecord;
import thongld25.hms.entity.TreatmentPlan;
import thongld25.hms.exceptions.AppException;
import thongld25.hms.exceptions.ErrorCode;
import thongld25.hms.repository.DoctorRepository;
import thongld25.hms.repository.MedicalRecordRepository;
import thongld25.hms.repository.PatientRepository;
import thongld25.hms.repository.TreatmentPlanRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TreatmentPlanService {
    TreatmentPlanRepository treatmentPlanRepository;
    ModelMapper modelMapper;
    DoctorRepository doctorRepository;
    PatientRepository patientRepository;
    MedicalRecordRepository medicalRecordRepository;

    public List<TreatmentPlanResponse> getForPatient(String patientId, int pageNo, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize );
        List<TreatmentPlan> treatmentPlans = treatmentPlanRepository.findByPatientId(patientId, pageRequest);
        if(treatmentPlans.isEmpty()) {
            throw new AppException(ErrorCode.NO_TREATMENT_PLAN_FOUND);
        }
        return treatmentPlans.stream()
                .map(treatmentPlan -> modelMapper.map(treatmentPlan, TreatmentPlanResponse.class))
                .collect(Collectors.toList());
    }

    public TreatmentPlanResponse getById(UUID treatmentPlanId) {
        TreatmentPlan treatmentPlan = treatmentPlanRepository.findById(treatmentPlanId)
                .orElseThrow(() -> new AppException(ErrorCode.NO_TREATMENT_PLAN_FOUND));
        return modelMapper.map(treatmentPlan, TreatmentPlanResponse.class);
    }

    public TreatmentPlanResponse createTreatmentPlan(TreatmentPlanRequest request) {
        TreatmentPlan treatmentPlan = new TreatmentPlan();
        treatmentPlan.setTreatmentMethod(request.getTreatmentMethod());
        treatmentPlan.setLastExaminationDay(request.getLastExaminationDay());
        treatmentPlan.setExceptedExaminationDay(request.getNextExpectedExaminationDay());
        treatmentPlan.setNote(request.getNote());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Doctor doctor = doctorRepository.findByPhoneNumber(username);
        treatmentPlan.setDoctor(doctor);
        treatmentPlan.setPatient(patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new AppException(ErrorCode.NO_PATIENT_FOUND)));
        treatmentPlan.setMedicalRecord(medicalRecordRepository.findById(request.getMedicalRecordId())
                .orElseThrow(() -> new AppException(ErrorCode.NO_MEDICAL_RECORD_FOUND)));
        treatmentPlanRepository.save(treatmentPlan);
        return modelMapper.map(treatmentPlan, TreatmentPlanResponse.class);
    }

    public TreatmentPlanResponse updateTreatmentPlan(TreatmentPlanRequest request, UUID treatmentPlanId) {
        TreatmentPlan treatmentPlan = new TreatmentPlan();
        treatmentPlan.setTreatmentMethod(request.getTreatmentMethod());
        treatmentPlan.setLastExaminationDay(request.getLastExaminationDay());
        treatmentPlan.setExceptedExaminationDay(request.getNextExpectedExaminationDay());
        treatmentPlan.setNote(request.getNote());
        treatmentPlan.setId(treatmentPlanId);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        treatmentPlan.setDoctor(doctorRepository.findByPhoneNumber(username));
        treatmentPlan.setPatient(patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new AppException(ErrorCode.NO_PATIENT_FOUND)));
        treatmentPlan.setMedicalRecord(medicalRecordRepository.findById(request.getMedicalRecordId())
                .orElseThrow(() -> new AppException(ErrorCode.NO_MEDICAL_RECORD_FOUND)));
        treatmentPlanRepository.save(treatmentPlan);
        return modelMapper.map(treatmentPlan, TreatmentPlanResponse.class);
    }

    public void deleteTreatmentPlan(UUID treatmentPlanId)
            throws BadRequestException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Doctor doctor = doctorRepository.findByPhoneNumber(username);
        TreatmentPlan treatmentPlan = treatmentPlanRepository.findById(treatmentPlanId)
                        .orElseThrow(() -> new AppException(ErrorCode.NO_TREATMENT_PLAN_FOUND));
        if(doctor.getId().compareTo(treatmentPlan.getDoctor().getId()) != 0) {
            throw new BadRequestException("Bad Request! Action not allowed!");
        }
        MedicalRecord medicalRecord = medicalRecordRepository.findById(treatmentPlan.getMedicalRecord().getId())
                        .orElseThrow(() -> new AppException(ErrorCode.NO_MEDICAL_RECORD_FOUND));
        medicalRecord.setTreatmentPlan(null);
        treatmentPlanRepository.deleteById(treatmentPlanId);
    }
}
