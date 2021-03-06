package libraryapi.apigee.exception;

/**
 * @Author Dowlath
 * @create 5/21/2020 11:36 AM
 */
public class LibraryResourceNotFoundException extends Exception{

    private String traceId;

    public LibraryResourceNotFoundException(String traceId,String message){
        super(message);
        this.traceId = traceId;
    }
    public String getTraceId(){
        return traceId;
    }
}
