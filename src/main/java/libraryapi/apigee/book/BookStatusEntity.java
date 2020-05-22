package libraryapi.apigee.book;

import javax.persistence.*;

/**
 * @Author Dowlath
 * @create 5/22/2020 11:58 PM
 */
@Entity
@Table(name = "BOOK_STATUS")
public class BookStatusEntity {

    @Column(name = "Book_Id")
    @Id
    private int bookId;

    @Column(name = "State")
    @Enumerated(EnumType.STRING)
    private BookStatusState state;

    @Column(name = "Total_Number_Of_Copies")
    private int totalNumberOfCopies;

    @Column(name = "Number_Of_Copies_Issued")
    private int numberOfCopiesIussed;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "Book_Id",nullable = false)
    private BookEntity bookEntity;

    public BookStatusEntity() {
    }

    public BookStatusEntity(int bookId) {
        this.bookId = bookId;
    }

    public BookStatusEntity(int bookId, BookStatusState state, int totalNumberOfCopies, int numberOfCopiesIussed) {
        this.bookId = bookId;
        this.state = state;
        this.totalNumberOfCopies = totalNumberOfCopies;
        this.numberOfCopiesIussed = numberOfCopiesIussed;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public BookStatusState getState() {
        return state;
    }

    public void setState(BookStatusState state) {
        this.state = state;
    }

    public int getTotalNumberOfCopies() {
        return totalNumberOfCopies;
    }

    public void setTotalNumberOfCopies(int totalNumberOfCopies) {
        this.totalNumberOfCopies = totalNumberOfCopies;
    }

    public int getNumberOfCopiesIussed() {
        return numberOfCopiesIussed;
    }

    public void setNumberOfCopiesIussed(int numberOfCopiesIussed) {
        this.numberOfCopiesIussed = numberOfCopiesIussed;
    }

    public BookEntity getBookEntity() {
        return bookEntity;
    }

    public void setBookEntity(BookEntity bookEntity) {
        this.bookEntity = bookEntity;
    }
}
