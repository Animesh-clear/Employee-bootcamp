package in.clear.bootcamp.repository;

import in.clear.bootcamp.dto.DepartmentCount;
import in.clear.bootcamp.dto.EmployeeSummaryDto;
import in.clear.bootcamp.model.EmployeeModel;

import java.util.List;
import java.util.Map;

public interface EmployeeCustomRepository {

    void saveEmployees(List<EmployeeModel> employees);

    void updateEmployee(String userId,  Map<String,Object> updateFields);

    void removeEmployee(String userId);

    boolean existance(String userId);

    double findExp();

    List<DepartmentCount> findDepts();
}
