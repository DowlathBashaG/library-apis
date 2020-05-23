package libraryapi.apigee.book;

import com.fasterxml.jackson.annotation.JsonInclude;
import libraryapi.apigee.author.Author;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author Dowlath
 * @create 5/23/2020 2:34 AM
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book {
    private Integer bookId;

    @NotNull
    @Size(min=1,max=50,message="ISBN name must be between 1 and 50 characters")
    private String isbn;

    @Size(min=1,max=50,message="Title name must be between 1 and 50 characters")
    private String title;

    private Integer publisherId;

    private Integer yearPublished;

    @Size(min=1,max=20,message="Edition name must be between 1 and 50 characters")
    private String edition;

    private BookStatus bookStatus;

    private Set<Author> authors = new HashSet<>();

    public Book() {
    }

    public Book(Integer bookId, String isbn, String title, Integer publisherId, Integer yearPublished, String edition, BookStatus bookStatus, Set<Author> authors) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.publisherId = publisherId;
        this.yearPublished = yearPublished;
        this.edition = edition;
        this.bookStatus = bookStatus;
        this.authors = authors;
    }

    public Book(Integer bookId, String isbn, String title, Integer publisherId, Integer yearPublished, String edition, BookStatus bookStatus) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.publisherId = publisherId;
        this.yearPublished = yearPublished;
        this.edition = edition;
        this.bookStatus = bookStatus;
    }

    public Book(String isbn, String title, Integer publisherId, Integer yearPublished, String edition, BookStatus bookStatus) {
        this.isbn = isbn;
        this.title = title;
        this.publisherId = publisherId;
        this.yearPublished = yearPublished;
        this.edition = edition;
        this.bookStatus = bookStatus;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Integer publisherId) {
        this.publisherId = publisherId;
    }

    public Integer getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(Integer yearPublished) {
        this.yearPublished = yearPublished;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public BookStatus getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(BookStatus bookStatus) {
        this.bookStatus = bookStatus;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }
}
