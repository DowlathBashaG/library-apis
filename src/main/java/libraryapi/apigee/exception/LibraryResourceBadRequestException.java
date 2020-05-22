package libraryapi.apigee.exception;

/**
 * @Author Dowlath
 * @create 5/22/2020 3:23 PM
 */
public class LibraryResourceBadRequestException extends Exception{

    private String traceId;

    public LibraryResourceBadRequestException(String traceId,String message) {
        super(message);
        this.traceId = traceId;
    }

    public String getTraceId(){
        return traceId;
    }

}
