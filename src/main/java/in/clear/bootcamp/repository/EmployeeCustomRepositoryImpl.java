package in.clear.bootcamp.repository;

import in.clear.bootcamp.dto.DepartmentCount;
import in.clear.bootcamp.dto.EmployeeSummaryDto;
import in.clear.bootcamp.model.EmployeeModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.bson.Document;


@RequiredArgsConstructor
@Repository
public class EmployeeCustomRepositoryImpl implements EmployeeCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void saveEmployees(List<EmployeeModel> employees){
        mongoTemplate.insertAll(employees);
    }

    @Override
    public EmployeeSummaryDto getSummary(String userId) {
        MatchOperation matchOperation = Aggregation.match(Criteria.where("userid").is(userId));
        GroupOperation groupOperation = Aggregation.group("userid").count().as("count")
                                            .sum("taxValue").as("totalTaxValue")
                                            .sum("totalValue").as("totalValue");

        Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation);

        return mongoTemplate.aggregate(aggregation, EmployeeModel.class, EmployeeSummaryDto.class)
                   .getUniqueMappedResult();
    }

    @Override
    public void updateEmployee(String userId, Map<String,Object> updateFields) {
        Query query = new Query(Criteria.where("_id").is(userId));
        Update update = new Update();

        updateFields.forEach((field, value) -> {
            // Set the field and its value to update
            update.set(field, value);
        });

        mongoTemplate.updateFirst(query, update, EmployeeModel.class);
    }

    @Override
    public void removeEmployee(String userId){
        Query query = new Query(Criteria.where("_id").is(userId));
        mongoTemplate.remove(query, EmployeeModel.class);
    }

    @Override
    public boolean existance(String userId){
        return mongoTemplate.exists(Query.query(Criteria.where("userid").is(userId)), EmployeeModel.class);
    }

    @Override
    public double findExp(){
        List<EmployeeModel> employees = mongoTemplate.findAll(EmployeeModel.class);
        double exp = 0;
        int count = 0;
        for (EmployeeModel employee : employees) {
            // Extract the desired field from each document
            String fieldValue = employee.getJoinDate();
            LocalDate givenDate = LocalDate.parse(fieldValue, DateTimeFormatter.ISO_DATE);
            LocalDate currentDate = LocalDate.now();

            Period period = Period.between(givenDate, currentDate);
            exp += period.getYears();
            count += 1;
        }
        return exp/count;
    }

    @Override
    public List<DepartmentCount> findDepts()
    {
        GroupOperation groupOperation = Aggregation.group("department","designation").count().as("employeeCount");

        // Projection operation to include department name in the output
        ProjectionOperation projectOperation = Aggregation.project("department","designation").andInclude("employeeCount");
        //ProjectionOperation projectOperation = Aggregation.project().and("_id").as("departmentName").and("employeeCount").previousOperation();

        AggregationOperation projectOperation1 = Aggregation.project()
                .and("_id").as("departmentName").andInclude("employeeCount")
                .andExclude("_id");

        // Perform aggregation
        Aggregation aggregation = Aggregation.newAggregation(groupOperation, projectOperation , projectOperation1);
        AggregationResults<DepartmentCount> results = mongoTemplate.aggregate(aggregation, "employeeModel", DepartmentCount.class);

        // Get the results
        List<DepartmentCount> departmentCounts = results.getMappedResults();

        return departmentCounts;
    }

}
