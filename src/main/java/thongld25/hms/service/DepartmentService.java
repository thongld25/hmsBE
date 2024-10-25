package thongld25.hms.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import thongld25.hms.dtos.requests.DepartmentRequest;
import thongld25.hms.dtos.responses.DepartmentResponse;
import thongld25.hms.dtos.responses.UserResponse;
import thongld25.hms.entity.Department;
import thongld25.hms.exceptions.AppException;
import thongld25.hms.exceptions.ErrorCode;
import thongld25.hms.repository.DepartmentRepository;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentService {
    DepartmentRepository departmentRepository;
    ModelMapper modelMapper;

    public List<DepartmentResponse> getAllDepartment(){
        List<Department> departments  = departmentRepository.findAll();
        if(departments.isEmpty()){
            throw new AppException(ErrorCode.NO_DEPARTMENT_FOUND);
        }
        return departments.stream()
                .map(department -> modelMapper.map(department, DepartmentResponse.class))
                .collect(Collectors.toList());
    }

    public DepartmentResponse createDepartment(DepartmentRequest request){
        Department department = new Department();
        modelMapper.map(request, department);
        departmentRepository.save(department);
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .build();
    }

    public void deleteDepartment(UUID id){
        departmentRepository.deleteById(id);
    }

//    public List<DepartmentResponse> getDepartmentPage(int pageNo, int pageSize) {
//        PageRequest pageRequest =PageRequest.of(pageNo - 1 , pageSize, Sort.by("name").descending());
//        List<Department> departments = departmentRepository.findAll(pageRequest).stream().toList();
//        if (departments.isEmpty()) {
//            throw new AppException(ErrorCode.NO_DEPARTMENT_FOUND);
//        }
//        return departments.stream()
//                .map(department -> modelMapper.map(department, DepartmentResponse.class))
//                .collect(Collectors.toList());
//    }
}
