package in.clear.bootcamp.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document //("nameofdbdocument") Document("naem")
public class EmployeeModel { //MongoDB document
    private String name;
    private String email;
    private String department;
    private String designation;
    private String joinDate;
    private String userId;
}
