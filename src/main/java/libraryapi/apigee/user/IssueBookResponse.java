package libraryapi.apigee.user;

import java.util.Set;

/**
 * @Author Dowlath
 * @create 5/23/2020 1:25 PM
 */

public class IssueBookResponse {

    private Set<IssueBookStatus> issueBookStatus;

    public IssueBookResponse() {
    }

    public IssueBookResponse(Set<IssueBookStatus> issueBookStatus) {
        this.issueBookStatus = issueBookStatus;
    }

    public Set<IssueBookStatus> getIssueBookStatus() {
        return issueBookStatus;
    }

    public void setIssueBookStatus(Set<IssueBookStatus> issueBookStatus) {
        this.issueBookStatus = issueBookStatus;
    }
}
