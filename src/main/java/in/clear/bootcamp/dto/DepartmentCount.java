package in.clear.bootcamp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCount {
    private String departmentName;
    //private String _id;
    private int employeeCount;
}
