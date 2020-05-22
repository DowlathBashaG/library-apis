package libraryapi.apigee.exception;

/**
 * @Author Dowlath
 * @create 5/21/2020 10:36 AM
 */
public class LibraryResourceAlreadyExistException extends Exception {

    private String traceId;

    public LibraryResourceAlreadyExistException(String traceId,String message) {
        super(message);
        this.traceId = traceId;
    }

    public String getTraceId(){
        return traceId;
    }
}
