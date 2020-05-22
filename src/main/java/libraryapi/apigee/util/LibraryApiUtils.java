package libraryapi.apigee.util;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;
import libraryapi.apigee.security.SecurityConstants;

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


    public static boolean isUserAdmin(String bearerToken) {
        String role = JWT.require(Algorithm.HMAC512(SecurityConstants.SIGNING_SECRET.getBytes()))
                         .build()
                         .verify(bearerToken.replace(SecurityConstants.BEARER_TOKEN_PREFIX,""))
                         .getClaim("role").asString();

        return role.equals("ADMIN");
    }

    public static int getUserIdFromClaim(String bearerToken) {
        return JWT.require(Algorithm.HMAC512(SecurityConstants.SIGNING_SECRET.getBytes()))
                 .build()
                 .verify(bearerToken.replace(SecurityConstants.BEARER_TOKEN_PREFIX,""))
                 .getClaim("userId").asInt();
    }
}
