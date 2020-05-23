package libraryapi.apigee.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Dowlath
 * @create 5/23/2020 1:22 PM
 */

public interface UserBookEntityRepository extends CrudRepository<UserBookEntity,Integer> {
    List<UserBookEntity> findByUserIdAndBookId(int userId, int bookId);
}
