package in.clear.bootcamp.repository;

import in.clear.bootcamp.dto.AverageDurationResult;
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

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


@RequiredArgsConstructor
@Repository
public class EmployeeCustomRepositoryImpl implements EmployeeCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void saveEmployees(List<EmployeeModel> employees){
        mongoTemplate.insertAll(employees);
    }


    @Override
    public void updateEmployee(String userId, Map<String,Object> updateFields) {
        Query query = new Query(Criteria.where("userId").is(userId));
        Update update = new Update();

        updateFields.forEach((field, value) -> {
            // Set the field and its value to update
            update.set(field, value);
        });

        mongoTemplate.updateFirst(query, update, EmployeeModel.class);
    }

    @Override
    public void removeEmployee(String userId){
        Query query = new Query(Criteria.where("userId").is(userId));
        mongoTemplate.remove(query, EmployeeModel.class);
    }

    @Override
    public boolean existance(String userId){
        return mongoTemplate.exists(Query.query(Criteria.where("userid").is(userId)), EmployeeModel.class);
    }

//    @Override
//    public double findExp(){
////        List<EmployeeModel> employees = mongoTemplate.findAll(EmployeeModel.class);
////        double exp = 0;
////        int count = 0;
////        for (EmployeeModel employee : employees) {
////            // Extract the desired field from each document
////            String fieldValue = employee.getJoinDate();
////            LocalDate givenDate = LocalDate.parse(fieldValue, DateTimeFormatter.ISO_DATE);
////            LocalDate currentDate = LocalDate.now();
////
////            Period period = Period.between(givenDate, currentDate);
////            exp += period.getYears();
////            count += 1;
////        }
////        return exp/count;
//
////        Aggregation aggregation = newAggregation(
////                project()
////                        .andExpression("toDate(joinDate)").as("jDate") // Convert joinDate to Date
////                        .andExpression("(toDate(now()) - jDate) / (1000 * 60 * 60 * 24 * 365)").as("yearsOfExperience"), // Calculate years of experience
////                group().avg("yearsOfExperience").as("averageExperience") // Calculate average experience
////        );
////
////        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "employeeModel", Document.class);
////        Document resultDocument = results.getUniqueMappedResult();
////
////        // Access the average experience
////        Double averageExperience = resultDocument.getDouble("averageExperience");
////
////        return averageExperience != null ? averageExperience : 0.0;
//
//        // Current date
//        LocalDate currentDate = LocalDate.now();
//
//        // MongoDB aggregation pipeline stages
//        Aggregation aggregation = Aggregation.newAggregation(
//                // Stage 1: Project joinDate and calculate difference in days
////                Aggregation.project()
////                        .and("joinDate").as("joinDate")
////                        .andExpression("dateDiff('$$joinDate', '$$currentDate')").as("daysDifference"),
////
////                // Stage 2: Calculate average difference in years
////                Aggregation.group().avg("daysDifference").as("averageYears")
//                Aggregation.addFields()
//                        .addField("daysDifference")
//                        .withValueOf(Aggregation.subtract(Aggregation.dateToString().toString("joinDate","$date"))).as("joinDate")).as("$$NOW"))).as("daysDifference"),
//
//                // Stage 2: Project to calculate average years
//                Aggregation.project()
//                        .andExpression("year($$date) - year('joinDate')").as("yearsDifference"),
//
//                // Stage 2: Calculate average difference in years
//                Aggregation.group().avg("yearsDifference").as("averageYears")
//        );
//        AggregationResults<Double> ans = mongoTemplate.aggregate(aggregation,"employeeModel",double.class);
//        return ans.getUniqueMappedResult();
//
//
//    }

    @Override
    public double findExp() {
        AggregationOperation projectToDateTime = Aggregation.project()
                .andExpression("{$toDate: '$joinDate'}").as("dateConverted");

        // Define the second $project stage to calculate duration in years
        AggregationOperation projectDuration = Aggregation.project().andExpression("($$NOW - $dateConverted)/31536000000L")
                .as("duration");

        // Define the $group stage to calculate the average duration
        GroupOperation groupByNull = Aggregation.group()
                .avg("duration").as("averageDuration");

        // Construct the aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                projectToDateTime,
                projectDuration,
                groupByNull
        );

        // Execute the aggregation
        AggregationResults<AverageDurationResult> aggregationResults =
                mongoTemplate.aggregate(aggregation, "employeeModel", AverageDurationResult.class);

        // Extract the result
        List<AverageDurationResult> results = aggregationResults.getMappedResults();

        // Check if results exist and return the average duration in years
        if (!results.isEmpty()) {
            return results.get(0).getAverageDuration();
        } else {
            return 0.0; // or handle as per your application logic
        }
    }


    @Override
    public List<DepartmentCount> findDepts()
    {
        GroupOperation groupOperation = group("department","designation").count().as("employeeCount");

        // Projection operation to include department name in the output
        ProjectionOperation projectOperation = project("department","designation").andInclude("employeeCount");
        //ProjectionOperation projectOperation = Aggregation.project().and("_id").as("departmentName").and("employeeCount").previousOperation();

        AggregationOperation projectOperation1 = project()
                .and("_id").as("departmentName").andInclude("employeeCount")
                .andExclude("_id");

        // Perform aggregation
        Aggregation aggregation = newAggregation(groupOperation, projectOperation , projectOperation1);
        AggregationResults<DepartmentCount> results = mongoTemplate.aggregate(aggregation, "employeeModel", DepartmentCount.class);

        // Get the results
        List<DepartmentCount> departmentCounts = results.getMappedResults();

        return departmentCounts;
    }

}
