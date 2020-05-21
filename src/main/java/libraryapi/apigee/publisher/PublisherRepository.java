package libraryapi.apigee.publisher;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Dowlath
 * @create 5/21/2020 3:44 AM
 */
@Repository
public interface PublisherRepository extends CrudRepository<PublisherEntity,Integer> {
    List<PublisherEntity> findByNameContaining(String name);
}
