package libraryapi.apigee.book;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Dowlath
 * @create 5/23/2020 2:54 AM
 */
@Repository
public interface BookRepository extends CrudRepository<BookEntity,Integer> {
    List<BookEntity> findByTitleContaining(String title);
    BookEntity findByIsbn(String isbn);
}
