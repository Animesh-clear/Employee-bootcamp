package in.clear.bootcamp.controllers;

import in.clear.bootcamp.dto.Creation;
import in.clear.bootcamp.dto.DepartmentCount;
import in.clear.bootcamp.dto.Employee;
import in.clear.bootcamp.dto.EmployeeSummaryDto;
import in.clear.bootcamp.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;


    @PostMapping(path = "/api/employees")
    public Creation uploadFile(
            @RequestBody Employee file){
        return employeeService.upload(file);
    }

    @GetMapping(path = "/api/employees/{id}")
    public Employee getEmployee(
            @PathVariable String id) {
        return employeeService.getEmployeebyId(id);
    }

    @PutMapping(path = "/api/employees/{id}")
    public String changeEmployee(@PathVariable String id , @RequestBody String file){
        return employeeService.modifyEmployee(id,file);
    }

    @DeleteMapping(path = "api/employees/{id}")
    public String throwEmployee(@PathVariable String id){
        return employeeService.deleteEmployee(id);
    }

    @GetMapping(path = "/api/employees/average-experience")
    public double averageExperience(){
        return employeeService.avgExperience();
    }

    @GetMapping(path = "/api/employees/department-count")
    public List<DepartmentCount> countDeps(){
        return employeeService.allDeps();
    }

//    @PutMapping(path = "/upload")
//    public List<Employee> uploadFile(
//        @RequestBody MultipartFile file,
//        @RequestHeader("user-id") String userId) {
//        return employeeService.upload(file, userId);
//    }
//
//
//    @GetMapping(path = "/invoice/{invoiceNumber}")
//    public Employee getInvoice(
//        @PathVariable String invoiceNumber,
//        @RequestHeader("user-id") String userId) {
//        log.info("path variable : {};", invoiceNumber);
//        return employeeService.getInvoice(invoiceNumber, userId);
//    }
//
//    @GetMapping(path = "/invoice")
//    public Employee getInvoiceQueryParam(
//        @RequestParam String invoiceNumber,
//        @RequestHeader("user-id") String userId) {
//        log.info("query parameter : {};", invoiceNumber);
//        return employeeService.getInvoice(invoiceNumber, userId);
//    }
//
//    @GetMapping(path = "/summary")
//    public EmployeeSummaryDto getSummary(@RequestHeader("user-id") String userId) {
//        return employeeService.getSummary(userId);
//    }

}
