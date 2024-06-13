package in.clear.bootcamp.helper;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import in.clear.bootcamp.dto.Employee;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class CSVHelper {

    public void readAndPrint(String filePath) {
        try  {
            FileReader fileReader = new FileReader(filePath);
            CSVReader reader = new CSVReader(fileReader);
            List<String[]> records = reader.readAll();
            for(int i = 0; i< records.size(); i++){
                String[] record = records.get(i);
                for (int j=0;j<record.length; j++){
                    System.out.print(record[j] + " ");
                }
                System.out.println("--");
            }
        } catch (IOException | CsvException e) {
            System.out.println("error: " +e.getMessage());
            System.out.println("error class: " +e.getClass());
        }
    }


    public List<Map<String,String>> readAndPrintA(String filePath) {
        List<Map<String,String>> output = new LinkedList<>();
        try  {
            FileReader fileReader = new FileReader(filePath);
            CSVReader reader = new CSVReader(fileReader);
            List<String[]> records = reader.readAll();
            List<String> headers = new LinkedList<>();
            for(int i= 0; i<records.size();i++){
                String[] record = records.get(i);
                Map<String,String> map = new LinkedHashMap<>();
                for (int j=0;j<record.length; j++){
                    if(i==0){
                        headers.add(record[j]);
                    }else {
                        map.put(headers.get(j),record[j]);
                    }
                }
                output.add(map);
            }

        } catch (IOException | CsvException e) {
            System.out.println("error: " +e.getMessage());
            System.out.println("error class: " +e.getClass());
        }
        return output;
    }

    public List<Employee> readAndPrintEmployee(MultipartFile file) {
        List<Employee> employees = new LinkedList<>();
        try  {
            CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()));
            List<String[]> records = reader.readAll();
            // Assuming first row is the header
            for (int i = 1; i < records.size(); i++) {
                String[] record = records.get(i);
                if (record.length < 7) {
                    System.out.println("Skipping incomplete record: " + String.join(", ", record));
                    continue; // Skip incomplete records
                }
                try {
                    Employee employee = new Employee();
                    employee.setName(record[0]);
                    employee.setEmail(record[1]);
                    employee.setDepartment(record[2]);
                    employee.setDesignation(record[3]);
                    employee.setJoinDate(record[4]);
                    employees.add(employee);
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing record: " + String.join(", ", record));
                    System.out.println("Error: " + e.getMessage());
                }
            }

        } catch (IOException | CsvException e) {
            System.out.println("error: " +e.getMessage());
            System.out.println("error class: " +e.getClass());
        }
        return employees;
    }



}
