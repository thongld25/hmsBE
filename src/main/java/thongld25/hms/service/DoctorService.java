package thongld25.hms.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import thongld25.hms.configuration.PasswordGenerator;
import thongld25.hms.dtos.requests.DefaultImage;
import thongld25.hms.dtos.requests.DoctorRequest;
import thongld25.hms.dtos.responses.DoctorResponse;
import thongld25.hms.dtos.responses.UserResponse;
import thongld25.hms.entity.Department;
import thongld25.hms.entity.Doctor;
import thongld25.hms.entity.User;
import thongld25.hms.enums.Gender;
import thongld25.hms.enums.Role;
import thongld25.hms.exceptions.AppException;
import thongld25.hms.exceptions.ErrorCode;
import thongld25.hms.repository.DepartmentRepository;
import thongld25.hms.repository.DoctorRepository;


import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DoctorService {

    DoctorRepository doctorRepository;
    ModelMapper modelMapper;
    UploadFile uploadFile;
    DepartmentRepository departmentRepository;

    public UserResponse createDoctor(DoctorRequest request) throws IOException {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        if(doctorRepository.existsByPhoneNumber(request.getPhoneNumber())){
            throw new RuntimeException("Phone number existed");
        }
        String url;
        if(request.getImage() == null){
            url = Gender.valueOf(request.getGender()) == Gender.MALE ? DefaultImage.MALE : DefaultImage.FEMALE;
        } else url = uploadFile.uploadImage(request.getImage());

        Doctor doctor = new Doctor();
        modelMapper.map(request, doctor);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String password = PasswordGenerator.generateRandomPassword();
        System.out.println(password);
        String text = doctor.getName() + "\n" + "Username: " + doctor.getPhoneNumber() + "\n" + "Password: " + password;
        sendSmsHMS(doctor.getPhoneNumber(), text);
        doctor.setImage(url);
        doctor.setGender(Gender.valueOf(request.getGender()));

        User user = new User();
        user.setId(doctor.getId());
        user.setUsername(doctor.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.DOCTOR);

        doctor.setUser(user);
        doctorRepository.save(doctor);

        return UserResponse.builder()
                .id(user.getId())
                .username(doctor.getPhoneNumber())
                .password(user.getPassword())
                .role(Role.DOCTOR)
                .build();
    }

    public void sendSmsHMS(String phoneNumber, String password) throws IOException, BadRequestException {
        SpeedSMSAPI api = new SpeedSMSAPI("CbIbCtZNZ065XSlnkbJpibxFCmGoH_rN");
        String result = api.sendSMS(phoneNumber, password, 5, null);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(result);
        String status = jsonNode.get("status").asText();
        if(status.equals("error")) {
            String errorMessage = jsonNode.get("message").asText();
            throw new BadRequestException(errorMessage);
        }
    }


    public List<DoctorResponse> getDoctors(int pageNo, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize);
        List<Doctor> doctors = doctorRepository.findAll(pageRequest).stream().toList();
        if (doctors.isEmpty()) {
            throw new AppException(ErrorCode.NO_DOCTOR_FOUND);
        }

        return doctors.stream()
                .map(doctor -> modelMapper.map(doctor, DoctorResponse.class))
                .collect(Collectors.toList());
    }

    public List<DoctorResponse> findAllDoctor(){
        List<Doctor> doctors = doctorRepository.findAll();
        if (doctors.isEmpty()){
            throw new AppException(ErrorCode.NO_DOCTOR_FOUND);
        }
        return doctors.stream()
                .map(doctor -> modelMapper.map(doctor, DoctorResponse.class))
                .collect(Collectors.toList());
    }

    public List<DoctorResponse> getDoctorByDepartment(UUID departmentId, int pageNo, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize);
        List<Doctor> doctors = doctorRepository.findByDepartmentId(departmentId, pageRequest).stream().toList();
        if (doctors.isEmpty()) {
            throw new AppException(ErrorCode.NO_DOCTOR_FOUND);
        }

        return doctors.stream()
                .map(doctor -> modelMapper.map(doctor, DoctorResponse.class))
                .collect(Collectors.toList());
    }

    public DoctorResponse findById(String doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(()-> new AppException(ErrorCode.NO_DOCTOR_FOUND));

        return modelMapper.map(doctor, DoctorResponse.class);
    }

    public DoctorResponse updateDoctor(String doctorId, DoctorRequest request) throws IOException {
        Doctor doctor = new Doctor();
        modelMapper.map(request, doctor);
        doctor.setId(doctorId);
        doctor.setDepartment(departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(()-> new AppException(ErrorCode.NO_DEPARTMENT_FOUND)));
        if(request.getImage() != null) {
            String url = uploadFile.uploadImage(request.getImage());
            doctor.setImage(url);
        } else doctor.setImage(null);
        doctorRepository.save(doctor);
        return modelMapper.map(doctor, DoctorResponse.class);
    }

    public void deleteDoctor(String doctorId){
        doctorRepository.deleteById(doctorId);
    }
}
