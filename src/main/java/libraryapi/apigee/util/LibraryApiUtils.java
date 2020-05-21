package libraryapi.apigee.util;

/**
 * @Author Dowlath
 * @create 5/21/2020 12:37 PM
 */
public class LibraryApiUtils {

    public static boolean doesStringValueExist(String str) {
        if(str != null && str.trim().length() > 0){
            return true;
        } else{
            return false;
        }
    }


}
