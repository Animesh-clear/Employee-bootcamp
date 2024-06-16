package in.clear.bootcamp.service.impl;

import in.clear.bootcamp.dto.Creation;
import in.clear.bootcamp.dto.DepartmentCount;
import in.clear.bootcamp.dto.Employee;
import in.clear.bootcamp.model.EmployeeModel;
import in.clear.bootcamp.repository.EmployeeRepository;
import in.clear.bootcamp.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;

import static java.lang.String.valueOf;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    public Creation upload(Employee employee) {
        List<EmployeeModel> employeeModels = new ArrayList<>();
//        for (Employee employee : employees){
        Random rand = new Random();

        // Generate random integers
        Boolean alreadyExists = true;
        String newuser = "123";
        while (alreadyExists) {
            newuser = valueOf(rand.nextInt(10000000));
            alreadyExists = employeeRepository.existance(newuser);
        }

            EmployeeModel employeeModel = EmployeeModel.builder().name(employee.getName()).
                    email(employee.getEmail()).
                    department(employee.getDepartment()).
                    designation(employee.getDesignation()).
                    joinDate(employee.getJoinDate()).
                    userId(newuser).
                    build();

            employeeModels.add(employeeModel);

       // }

//        data.put(userId, employees);
        employeeRepository.saveEmployees(employeeModels);
        return new Creation(newuser,"Successfully Created");
    }

    @Override
    public Employee getEmployee(String userId) {
//        var invoices = data.get(userId);
//        for (var invoice : invoices) {
//            if (invoice.getInvoiceNumber().equals(invoiceNumber)) {
//                return invoice;
//            }
//        }
        EmployeeModel employeeModel = employeeRepository.findByname(userId);
        Employee employee = new Employee();
        employee.setName(employeeModel.getName());
        employee.setEmail(employeeModel.getEmail());
        employee.setDepartment(employeeModel.getDepartment());
        employee.setDesignation(employeeModel.getDesignation());
        employee.setJoinDate(employeeModel.getJoinDate());

        LocalDate givenDate = LocalDate.parse(employeeModel.getJoinDate(), DateTimeFormatter.ISO_DATE);
        LocalDate currentDate = LocalDate.now();

        Period period = Period.between(givenDate, currentDate);
        int years = period.getYears();

//        System.out.println("Duration in ");
//        System.out.println("Years: " + years);
        log.info("{} years spend at the organization with id : {}",years,employeeModel.getUserId());
        return employee;
    }

    @Override
    public Employee getEmployeebyId(String userId) {
//        var invoices = data.get(userId);
//        for (var invoice : invoices) {
//            if (invoice.getInvoiceNumber().equals(invoiceNumber)) {
//                return invoice;
//            }
//        }
        EmployeeModel employeeModel = employeeRepository.findByUserId(userId);

        if (employeeModel == null) {
            log.warn("Employee not found for userId: {}", userId);
            try {
                throw new Exception("Employee not found for userId: " + userId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        Employee employee = new Employee();
        employee.setName(employeeModel.getName());
        employee.setEmail(employeeModel.getEmail());
        employee.setDepartment(employeeModel.getDepartment());
        employee.setDesignation(employeeModel.getDesignation());
        employee.setJoinDate(employeeModel.getJoinDate());
        employee.setUserId(employeeModel.getUserId());

        LocalDate givenDate = LocalDate.parse(employeeModel.getJoinDate(), DateTimeFormatter.ISO_DATE);
        LocalDate currentDate = LocalDate.now();

        Period period = Period.between(givenDate, currentDate);
        int years = period.getYears();

//        System.out.println("Duration in ");
//        System.out.println("Years: " + years);
        log.info("{} years spend at the organization with id : {}",years,employeeModel.getUserId());
        return employee;
    }


    @Override
    public String modifyEmployee(String userId , String jsonResponse){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String,Object> updateFields = objectMapper.readValue(jsonResponse, new TypeReference<Map<String, Object>>() {});
            System.out.println(updateFields);
            employeeRepository.updateEmployee(userId, updateFields);
            return "Employee Updated Successfully";
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // or handle the exception appropriately
            return "Error: Unable to process JSON data";
        }

    }

    @Override
    public String deleteEmployee(String userId){
        employeeRepository.removeEmployee(userId);
        return "Employee Removed :( ";
    }

    @Override
    public double avgExperience(){
        return employeeRepository.findExp();

    }

    @Override
    public List<DepartmentCount> allDeps(){
        return employeeRepository.findDepts();
    }

}
