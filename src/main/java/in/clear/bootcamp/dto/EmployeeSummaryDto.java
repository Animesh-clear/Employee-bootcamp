package in.clear.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data

//DTO is given to the user , whereas the model interacts with the database fields ,
public class EmployeeSummaryDto {
    //@JsonProperty(value = "userId") // instead of _id it shows userId as we used JsonProperty over it
    private String _id;
    private double experience;
}
