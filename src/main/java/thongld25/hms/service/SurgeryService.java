package thongld25.hms.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import thongld25.hms.dtos.requests.SurgeryRequest;
import thongld25.hms.dtos.responses.SurgeryResponse;
import thongld25.hms.entity.Surgery;
import thongld25.hms.exceptions.AppException;
import thongld25.hms.exceptions.ErrorCode;
import thongld25.hms.repository.SurgeryRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SurgeryService {
    SurgeryRepository surgeryRepository;
    ModelMapper modelMapper;

    public List<SurgeryResponse> getAllSurgery() {
        List<Surgery> surgeries = surgeryRepository.findAll();
        if(surgeries.isEmpty()) {
            throw new AppException(ErrorCode.NO_SURGERY_FOUND);
        }
        return surgeries.stream()
                .map(surgery -> modelMapper.map(surgery, SurgeryResponse.class))
                .collect(Collectors.toList());
    }

    public List<SurgeryResponse> getSurgeryInWeek() {
        Date startWeek = getStartOfCurrentWeek();
        Date endWeek = getEndOfCurrentWeek();

        List<Surgery> surgeries = surgeryRepository.findSurgeryInWeek(startWeek, endWeek);
        if(surgeries.isEmpty()) {
            throw new AppException(ErrorCode.NO_SURGERY_FOUND);
        }
        return surgeries.stream()
                .map(surgery -> modelMapper.map(surgery, SurgeryResponse.class))
                .collect(Collectors.toList());
    }

    private Date getStartOfCurrentWeek() {
        LocalDate today = LocalDate.now();

        LocalDate monday = today.with(DayOfWeek.MONDAY);

        return Date.from(monday.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Date getEndOfCurrentWeek() {
        LocalDate today = LocalDate.now();
        LocalDate sunday = today.with(DayOfWeek.SUNDAY);

        return Date.from(sunday.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
    }

    public SurgeryResponse createSurgery(SurgeryRequest request) {
        Surgery surgery = new Surgery();
        modelMapper.map(request, surgery);
        surgeryRepository.save(surgery);

        return modelMapper.map(surgery, SurgeryResponse.class);


    }

    public SurgeryResponse updateSurgery(UUID surgeryId, SurgeryRequest request) {
        Surgery surgery = surgeryRepository.findById(surgeryId)
                .orElseThrow(()-> new AppException(ErrorCode.NO_SURGERY_FOUND));
        modelMapper.map(request, surgery);
        surgeryRepository.save(surgery);

        return modelMapper.map(surgery, SurgeryResponse.class);
    }

    public void deleteSurgery(UUID surgeryId) {
        surgeryRepository.deleteById(surgeryId);
    }

    public List<SurgeryResponse> getSurgeryForDoctor(String doctorId) {
        List<Surgery> surgeries = surgeryRepository.findByDoctorId(doctorId);
        if(surgeries.isEmpty()) {
            throw new AppException(ErrorCode.NO_SURGERY_FOUND);
        }

        return surgeries.stream()
                .map(surgery -> modelMapper.map(surgery, SurgeryResponse.class))
                .collect(Collectors.toList());
    }
}
