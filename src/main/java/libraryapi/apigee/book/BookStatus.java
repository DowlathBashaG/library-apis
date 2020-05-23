package libraryapi.apigee.book;

import javax.validation.constraints.Pattern;

/**
 * @Author Dowlath
 * @create 5/23/2020 2:38 AM
 */
public class BookStatus {

    private Integer bookId;

    private BookStatusState state;

    @Pattern(regexp = "[1-9][0-9]")
    private int totalNumberOfCopies;

    @Pattern(regexp = "[1-9][0-9]")
    private int numberOfCopiesIssued;

    public BookStatus() {
    }

    public BookStatus(BookStatusState state, int totalNumberOfCopies,int numberOfCopiesIssued) {
        this.state = state;
        this.totalNumberOfCopies = totalNumberOfCopies;
        this.numberOfCopiesIssued = numberOfCopiesIssued;
    }

    public BookStatus(Integer bookId, BookStatusState state,int totalNumberOfCopies,int numberOfCopiesIssued) {
        this.bookId = bookId;
        this.state = state;
        this.totalNumberOfCopies = totalNumberOfCopies;
        this.numberOfCopiesIssued = numberOfCopiesIssued;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
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

    public int getNumberOfCopiesIssued() {
        return numberOfCopiesIssued;
    }

    public void setNumberOfCopiesIssued(int numberOfCopiesIssued) {
        this.numberOfCopiesIssued = numberOfCopiesIssued;
    }
}
