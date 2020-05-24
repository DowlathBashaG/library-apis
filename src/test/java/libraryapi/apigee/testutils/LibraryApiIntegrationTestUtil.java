package libraryapi.apigee.testutils;

import libraryapi.apigee.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author Dowlath
 * @create 5/24/2020 1:03 PM
 */
@Component
public class LibraryApiIntegrationTestUtil {
    @Autowired
    TestRestTemplate testRestTemplate;

    @Value("${library.api.user.admin.username}")
    private String adminUsername;

    @Value("${library.api.user.admin.password}")
    private String adminPassword;

    private ResponseEntity<String> adminLoginResponse;

    public ResponseEntity<User> registerNewUser(String userName){
        URI registerUri = null;
        try{
            registerUri = new URI(TestConstants.USER_API_REGISTER_URI);
        }catch(URISyntaxException e){
            e.printStackTrace();
        }
        HttpEntity<User> newUserRequest = new HttpEntity<>(LibraryApiTestUtil.createUser(userName));
        return testRestTemplate.postForEntity(registerUri,newUserRequest,User.class);
    }

    public ResponseEntity<String> loginUser(String userName , String password){
        if(userName.equals("adminUsername") && (adminLoginResponse != null)){
            return adminLoginResponse;
        }
        URI loginUri = null;
        try{
            loginUri = new URI(TestConstants.LOGIN_URI);
        }catch(URISyntaxException e){
            e.printStackTrace();
        }
        HttpEntity<String> loginRequest = new HttpEntity<>(createLoginBody(userName,password));
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(loginUri,loginRequest,String.class);
        if(userName.equals("adminUsername")){
            adminLoginResponse = responseEntity;
        }
        return responseEntity;
    }

    private String createLoginBody(String userName,String password){
        return "{\"userName\":\""+userName+"\",\"password\": \"" + password + "\"}";
    }
    public MultiValueMap<String,String> createAuthorizationHeader(String bearerToken){
        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization",bearerToken);
        return headers;
    }
}
