package libraryapi.apigee.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Dowlath
 * @create 5/23/2020 1:18 PM
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity,Integer> {
    UserEntity findByUsername(String userName);
    List<UserEntity> findByFirstNameAndLastNameContaining(String firstName,String lastName);
    List<UserEntity> findByFirstNameContaining(String firstName);
    List<UserEntity> findByLastNameContaining(String lastName);
}
