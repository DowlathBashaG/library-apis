package libraryapi.apigee.author;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Dowlath
 * @create 5/22/2020 9:46 PM
 */
@Repository
public interface AuthorRepository extends CrudRepository<AuthorEntity,Integer> {
    List<AuthorEntity> findByFirstNameContaining(String firstName);

    List<AuthorEntity> findByLastNameContaining(String lastName);

    List<AuthorEntity> findByFirstNameAndLastNameContaining(String firstName,String lastName);
}
