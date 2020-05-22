package libraryapi.apigee.exception;

/**
 * @Author Dowlath
 * @create 5/22/2020 9:34 PM
 */
public class LibraryResourceUnauthorizedException extends Exception {
    private String traceId;

    public LibraryResourceUnauthorizedException(String traceId,String message){
        super(message);
        this.traceId = traceId;
    }

    public String getTraceId(){
        return traceId;
    }
}
