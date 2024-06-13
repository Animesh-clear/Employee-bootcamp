package in.clear.bootcamp.repository;

import in.clear.bootcamp.model.EmployeeModel;
import org.springframework.data.mongodb.repository.MongoRepository;

//Talks with database
public interface EmployeeRepository extends MongoRepository<EmployeeModel, String>, EmployeeCustomRepository {

    EmployeeModel findByname(String name);
    EmployeeModel findByUserId(String id);

}
