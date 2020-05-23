package libraryapi.apigee.book;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author Dowlath
 * @create 5/23/2020 2:53 AM
 */
@Repository
public interface BookStatusRepository extends CrudRepository<BookStatusEntity,Integer> {
}
