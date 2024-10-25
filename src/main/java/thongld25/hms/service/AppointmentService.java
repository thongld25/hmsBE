package thongld25.hms.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import thongld25.hms.dtos.requests.AppointmentRequest;
import thongld25.hms.dtos.responses.AppointmentResponse;
import thongld25.hms.entity.Appointment;
import thongld25.hms.entity.Doctor;
import thongld25.hms.entity.Patient;
import thongld25.hms.enums.AppointmentStatus;
import thongld25.hms.exceptions.AppException;
import thongld25.hms.exceptions.ErrorCode;
import thongld25.hms.repository.AppointmentRepository;
import thongld25.hms.repository.DoctorRepository;
import thongld25.hms.repository.PatientRepository;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentService {
    AppointmentRepository appointmentRepository;
    ModelMapper modelMapper;
    DoctorRepository doctorRepository;
    PatientRepository patientRepository;

    public List<AppointmentResponse> getAppointmentOfDoctor(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Doctor doctor = doctorRepository.findByPhoneNumber(username);
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctor.getId());
        if(appointments.isEmpty()) {
            throw new AppException(ErrorCode.APPOINTMENT_EXISTED);
        }

        return appointments.stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentResponse.class))
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getAppointmentOfPatient(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Patient patient = patientRepository.findByPhone(username);
        List<Appointment> appointments = appointmentRepository.findByPatientId(patient.getId());
        System.out.println(appointments);
        if(appointments.isEmpty()) {
            throw new AppException(ErrorCode.APPOINTMENT_EXISTED);
        }

        return appointments.stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentResponse.class))
                .collect(Collectors.toList());
    }

    public AppointmentResponse createAppointment(AppointmentRequest request) {
        Appointment appointment = new Appointment();
        appointment.setNote(request.getNote());
        appointment.setTime(request.getTime());
        appointment.setDoctor(doctorRepository.findById(request.getDoctorId())
                .orElseThrow(()-> new AppException(ErrorCode.NO_DOCTOR_FOUND)));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Patient patient = patientRepository.findByPhone(username);
        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.PENDING);
        appointmentRepository.save(appointment);

        return modelMapper.map(appointment, AppointmentResponse.class);
    }

    public AppointmentResponse updateAppointment(UUID appointmentId, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(()-> new AppException(ErrorCode.APPOINTMENT_EXISTED));
        appointment.setNote(request.getNote());
        appointment.setTime(request.getTime());
        appointment.setDoctor(doctorRepository.findById(request.getDoctorId())
                .orElseThrow(()-> new AppException(ErrorCode.NO_DOCTOR_FOUND)));
        appointment.setStatus(AppointmentStatus.PENDING);
        appointmentRepository.save(appointment);
        return modelMapper.map(appointment, AppointmentResponse.class);
    }

    public AppointmentResponse acceptAppointment(UUID appointmentId) throws BadRequestException {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(()-> new AppException(ErrorCode.APPOINTMENT_EXISTED));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Doctor doctor = doctorRepository.findByPhoneNumber(username);
        if(doctor.getId().compareTo(appointment.getDoctor().getId()) != 0) {
            throw new AppException(ErrorCode.NO_DOCTOR_FOUND);
        }
        if(appointment.getStatus() != AppointmentStatus.PENDING){
            throw new BadRequestException("Action not allowed!");
        }
        appointment.setStatus(AppointmentStatus.ACCEPT);
        appointmentRepository.save(appointment);
        return modelMapper.map(appointment, AppointmentResponse.class);
    }

    public AppointmentResponse rejectAppointment(UUID appointmentId) throws BadRequestException {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(()-> new AppException(ErrorCode.APPOINTMENT_EXISTED));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Doctor doctor = doctorRepository.findByPhoneNumber(username);
        if(doctor.getId().compareTo(appointment.getDoctor().getId()) != 0) {
            throw new AppException(ErrorCode.NO_DOCTOR_FOUND);
        }
        if(appointment.getStatus() != AppointmentStatus.PENDING){
            throw new BadRequestException("Action not allowed!");
        }
        appointment.setStatus(AppointmentStatus.REJECT);
        appointmentRepository.save(appointment);
        return modelMapper.map(appointment, AppointmentResponse.class);
    }

    public void deleteAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.APPOINTMENT_EXISTED));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Patient patient = patientRepository.findByPhone(username);
        if(patient.getId().compareTo(appointment.getPatient().getId()) != 0) {
            throw new AppException(ErrorCode.NO_PATIENT_FOUND);
        }
        appointmentRepository.delete(appointment);
    }
}
