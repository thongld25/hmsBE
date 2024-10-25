package thongld25.hms.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import thongld25.hms.configuration.PasswordGenerator;
import thongld25.hms.dtos.requests.PatientRequest;
import thongld25.hms.dtos.responses.PatientResponse;
import thongld25.hms.dtos.responses.UserResponse;
import thongld25.hms.entity.Patient;
import thongld25.hms.entity.User;
import thongld25.hms.enums.Gender;
import thongld25.hms.enums.Role;
import thongld25.hms.enums.StayType;
import thongld25.hms.exceptions.AppException;
import thongld25.hms.exceptions.ErrorCode;
import thongld25.hms.repository.DoctorRepository;
import thongld25.hms.repository.MedicalRecordRepository;
import thongld25.hms.repository.PatientRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientService {
    PatientRepository patientRepository;
    ModelMapper modelMapper;
    MedicalRecordRepository medicalRecordRepository;
    DoctorRepository doctorRepository;

    public UserResponse createPatient(PatientRequest patientRequest){
        if(patientRepository.existsByPhone(patientRequest.getPhone())){
            throw new AppException(ErrorCode.PHONE_NUMBER_EXISTED);
        }
        Patient patient = new Patient();
        modelMapper.map(patientRequest, patient);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String password = PasswordGenerator.generateRandomPassword();
        System.out.println(password);
        patient.setGender(Gender.valueOf(patientRequest.getGender()));

        User user = new User();
        user.setId(patient.getId());
        user.setUsername(patient.getPhone());
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.PATIENT);

        patient.setUser(user);
        patientRepository.save(patient);

        return UserResponse.builder()
                .id(user.getId())
                .username(patient.getPhone())
                .password(user.getPassword())
                .role(Role.PATIENT)
                .build();
    }

    public List<PatientResponse> getAllPatient() {
        List<Patient> patients = patientRepository.findAll();
        if (patients.isEmpty()) {
            throw new AppException(ErrorCode.NO_PATIENT_FOUND);
        }
        return patients.stream()
                .map(patient -> modelMapper.map(patient, PatientResponse.class))
                .collect(Collectors.toList());
    }

    public PatientResponse getPatientById(String patientId){
        Patient patient = patientRepository.findById(patientId).orElseThrow(()-> new RuntimeException("Patient not existed"));
        return modelMapper.map(patient, PatientResponse.class);
    }

    public PatientResponse updatePatient(String patientId, PatientRequest request){
        Patient patient = patientRepository.findById(patientId).orElseThrow(()-> new RuntimeException("Patient not existed"));

        modelMapper.map(request, patient);
        patient.setGender(Gender.valueOf(request.getGender()));
        Patient updatePatient =  patientRepository.save(patient);

        return modelMapper.map(updatePatient, PatientResponse.class);
    }

    public List<PatientResponse> getPatientByType(String type){
        List<Patient> patients = medicalRecordRepository.findPatientsByStayType(StayType.valueOf(type));
        if (patients.isEmpty()) {
            throw new AppException(ErrorCode.NO_PATIENT_FOUND);
        }
        return patients.stream()
                .map(patient -> modelMapper.map(patient, PatientResponse.class))
                .collect(Collectors.toList());
    }

    public List<PatientResponse> getPatientOfDoctor(String doctorId) {
        List<Patient> patients = medicalRecordRepository.findPatientsOfDoctor(
                doctorRepository.findById(doctorId).orElseThrow(()-> new AppException(ErrorCode.NO_DOCTOR_FOUND))
        );
        if (patients.isEmpty()) {
            throw new AppException(ErrorCode.NO_PATIENT_FOUND);
        }
        return patients.stream()
                .map(patient -> modelMapper.map(patient, PatientResponse.class))
                .collect(Collectors.toList());
    }
}
