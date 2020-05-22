package libraryapi.apigee.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @Author Dowlath
 * @create 5/22/2020 8:19 PM
 */
public class LibraryApiUtilsTest {

    @Test
    public void doesStringValueExist(){
        assertTrue(LibraryApiUtils.doesStringValueExist("ValueExist"));
        assertFalse(LibraryApiUtils.doesStringValueExist(" "));
        assertFalse(LibraryApiUtils.doesStringValueExist(null));


    }
}
