package thongld25.hms.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import thongld25.hms.dtos.requests.MedicalRecordRequest;
import thongld25.hms.dtos.requests.TreatmentPlanRequest;
import thongld25.hms.dtos.responses.MedicalRecordResponse;
import thongld25.hms.dtos.responses.TreatmentPlanResponse;
import thongld25.hms.entity.Doctor;
import thongld25.hms.entity.MedicalRecord;
import thongld25.hms.entity.TreatmentPlan;
import thongld25.hms.enums.StayType;
import thongld25.hms.exceptions.AppException;
import thongld25.hms.exceptions.ErrorCode;
import thongld25.hms.repository.*;


import java.beans.MethodDescriptor;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MedicalRecordService {
    MedicalRecordRepository medicalRecordRepository;
    ModelMapper modelMapper;
    DoctorRepository doctorRepository;
    PatientRepository patientRepository;
    DepartmentRepository departmentRepository;

    public List<MedicalRecordResponse> getForPatientId(String patientId, int pageNo, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize );
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findByPatientId(patientId, pageRequest);
        if(medicalRecords.isEmpty()) {
            throw new AppException(ErrorCode.NO_MEDICAL_RECORD_FOUND);
        }
        return medicalRecords.stream()
                .map(medicalRecord -> modelMapper.map(medicalRecord, MedicalRecordResponse.class))
                .collect(Collectors.toList());
    }

    public MedicalRecordResponse getById(UUID medicalRecordId) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new AppException(ErrorCode.NO_TREATMENT_PLAN_FOUND));
        return modelMapper.map(medicalRecord, MedicalRecordResponse.class);
    }

    public MedicalRecordResponse createMedicalRecord(MedicalRecordRequest request) {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setBhytCode(request.getBhytCode());
        medicalRecord.setInDay(request.getInDay());
        medicalRecord.setOutDay(request.getOutDay());

        // Kiểm tra ngày hợp lệ
        if (request.getOutDay() != null && request.getOutDay().before(request.getInDay())) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

        medicalRecord.setInDayDiagnose(request.getInDayDiagnose());
        medicalRecord.setOutDayDiagnose(request.getOutDayDiagnose());
        medicalRecord.setMedicalHistory(request.getMedicalHistory());
        medicalRecord.setDiseaseProgress(request.getDiseaseProgress());
        medicalRecord.setTestResults(request.getTestResults());
        medicalRecord.setHospitalDischargeStatus(request.getHospitalDischargeStatus());

        // Kiểm tra và đặt StayType
        try {
            medicalRecord.setStayType(StayType.valueOf(request.getStayType()));
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_STAY_TYPE);
        }

        medicalRecord.setNote(request.getNote());

        // Lấy thông tin bác sĩ từ bảo mật
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Doctor doctor = doctorRepository.findByPhoneNumber(username);
        if (doctor == null) {
            throw new AppException(ErrorCode.NO_DOCTOR_FOUND);
        }
        medicalRecord.setDoctor(doctor);

        // Tìm bệnh nhân và phòng ban
        medicalRecord.setPatient(patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new AppException(ErrorCode.NO_PATIENT_FOUND)));
        medicalRecord.setDepartment(departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.NO_DEPARTMENT_FOUND)));

        medicalRecord.setCreatedDay(new Date());
        medicalRecordRepository.save(medicalRecord);
        return modelMapper.map(medicalRecord, MedicalRecordResponse.class);
    }


    public MedicalRecordResponse updateMedicalRecord(UUID id, MedicalRecordRequest request)
            throws BadRequestException {
        MedicalRecord medicalRecord = modelMapper.map(request, MedicalRecord.class);
        medicalRecord.setId(id);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        medicalRecord.setDoctor(doctorRepository.findByPhoneNumber(username));
        medicalRecord.setPatient(patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new AppException(ErrorCode.NO_PATIENT_FOUND)));
        medicalRecord.setDepartment(departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(()-> new AppException(ErrorCode.NO_DEPARTMENT_FOUND)));

        medicalRecordRepository.save(medicalRecord);
        return modelMapper.map(medicalRecord, MedicalRecordResponse.class);
    }

    public void deleteMedicalRecord(UUID medicalRecordId)
            throws BadRequestException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Doctor doctor = doctorRepository.findByPhoneNumber(username);
        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new AppException(ErrorCode.NO_MEDICAL_RECORD_FOUND));
        if(doctor.getId().compareTo(medicalRecord.getDoctor().getId()) != 0) {
            throw new BadRequestException("Bad Request! Action not allowed!");
        }
        medicalRecordRepository.deleteById(medicalRecordId);
    }
}
