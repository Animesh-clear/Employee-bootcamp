package in.clear.bootcamp.service;

import in.clear.bootcamp.dto.Creation;
import in.clear.bootcamp.dto.DepartmentCount;
import in.clear.bootcamp.dto.Employee;
import in.clear.bootcamp.dto.EmployeeSummaryDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeService {
    Creation upload(Employee employee);

    Employee getEmployee(String userId);
    Employee getEmployeebyId(String userId);

    String modifyEmployee (String userId, String jsonResponse);
    String deleteEmployee(String userId);
    double avgExperience();
    List<DepartmentCount> allDeps();
}
