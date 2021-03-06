package libraryapi.apigee.user;

import libraryapi.apigee.testutils.LibraryApiIntegrationTestUtil;
import libraryapi.apigee.testutils.LibraryApiTestUtil;
import libraryapi.apigee.testutils.TestConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * @Author Dowlath
 * @create 5/24/2020 4:48 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    LibraryApiIntegrationTestUtil libraryApiIntegrationTestUtil;

    @Autowired
    Environment environment;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void registerUser_success() {
        ResponseEntity<User> response = libraryApiIntegrationTestUtil.registerNewUser("register.user.success");
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        User responseLibUser = response.getBody();

        Assert.assertNotNull(responseLibUser);
        Assert.assertNotNull(responseLibUser.getUserId());
        Assert.assertNotNull(responseLibUser.getPassword());
        Assert.assertTrue(responseLibUser.getUsername().contains("register.user.success"));
        Assert.assertNotNull(responseLibUser.getRole());
    }

    @Test
    public void getUser_success() {
        // First we register a user
        ResponseEntity<User> response = libraryApiIntegrationTestUtil.registerNewUser("get.user.success");
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        User user = response.getBody();
        Assert.assertNotNull(user);
        Integer userId = user.getUserId();

        // Login with the credentials
        ResponseEntity<String> loginResponse = libraryApiIntegrationTestUtil
                .loginUser(user.getUsername(), user.getPassword());
        Assert.assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        URI getUserUri = null;  // /v1/users/{userId}
        try {
            getUserUri = new URI(TestConstants.USER_API_BASE_URI + "/" + userId);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        MultiValueMap<String, String> headers = libraryApiIntegrationTestUtil
                .createAuthorizationHeader(loginResponse.getHeaders().get("Authorization").get(0));

        ResponseEntity<User> libUserResponse = testRestTemplate.exchange(getUserUri,
                HttpMethod.GET, new HttpEntity<Object>(headers), User.class);

        Assert.assertEquals(HttpStatus.OK, libUserResponse.getStatusCode());
        User libUser = libUserResponse.getBody();

        Assert.assertNotNull(libUser);
        Assert.assertNotNull(libUser.getUserId());
        Assert.assertTrue(libUser.getUsername().contains("get.user.success"));
        Assert.assertNotNull(libUser.getRole());
    }


    @Test
    public void getUser_user_doesnot_exist() {
        // First we register a user
        ResponseEntity<User> response = libraryApiIntegrationTestUtil.registerNewUser("get.user.doesnotexist");
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        User user = response.getBody();
        Assert.assertNotNull(user);
        Integer userId = user.getUserId();

        // Login with the credentials
        ResponseEntity<String> loginResponse = libraryApiIntegrationTestUtil
                .loginUser(user.getUsername(), user.getPassword());
        Assert.assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        URI getUserUri = null;  // /v1/users/{userId}
        try {
            getUserUri = new URI(TestConstants.USER_API_BASE_URI + "/" + 1234);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        MultiValueMap<String, String> headers = libraryApiIntegrationTestUtil
                .createAuthorizationHeader(loginResponse.getHeaders().get("Authorization").get(0));

        ResponseEntity<User> libUserResponse = testRestTemplate.exchange(getUserUri,
                HttpMethod.GET, new HttpEntity<Object>(headers), User.class);

        Assert.assertEquals(HttpStatus.FORBIDDEN, libUserResponse.getStatusCode());

    }

    @Test
    public void getUser_unauthorized_for_different_user() {
        String port = environment.getProperty("local.server.port");

        // First we register a user
        ResponseEntity<User> response = libraryApiIntegrationTestUtil.registerNewUser("getUser_unauthorized_for_differnt.user");
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        User user = response.getBody();
        Assert.assertNotNull(user);

        Integer userId = user.getUserId() + 1;

        // Login with the credentials
        ResponseEntity<String> loginResponse = libraryApiIntegrationTestUtil
                .loginUser(user.getUsername(), user.getPassword());
        Assert.assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        URI getUserUri = null;  // /v1/users/{userId}
        try {
            getUserUri = new URI(TestConstants.API_BASE_URL+port +TestConstants.USER_API_BASE_URI + "/" + userId);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        MultiValueMap<String, String> headers = libraryApiIntegrationTestUtil
                .createAuthorizationHeader(loginResponse.getHeaders().get("Authorization").get(0));

        ResponseEntity<String> libUserResponse = testRestTemplate.exchange(getUserUri,
                HttpMethod.GET, new HttpEntity<Object>(headers), String.class);

        //Unauthorized to get another user details
        Assert.assertEquals(HttpStatus.FORBIDDEN, libUserResponse.getStatusCode());

    }

    @Test
    public void updateUser_success() {
        String port = environment.getProperty("local.server.port");

        // First we register a user
        ResponseEntity<User> response = libraryApiIntegrationTestUtil.registerNewUser("update.user.success");
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        User responseUser = response.getBody();
        Assert.assertNotNull(responseUser);

        Integer userId = responseUser.getUserId();

        // Login with the credentials
        ResponseEntity<String> loginResponse = libraryApiIntegrationTestUtil
                .loginUser(responseUser.getUsername(), responseUser.getPassword());
        Assert.assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        responseUser.setPassword("NewPassword");
        responseUser.setEmailId("newemailaddress@email.com");
        responseUser.setEmailId(TestConstants.TEST_PUBLISHER_EMAIL_UPDATED);

        URI getUserUri = null;  // /v1/users/{userId}
        try {
            getUserUri = new URI(TestConstants.API_BASE_URL+port +TestConstants.USER_API_BASE_URI + "/" + userId);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        MultiValueMap<String, String> headers = libraryApiIntegrationTestUtil
                .createAuthorizationHeader(loginResponse.getHeaders().get("Authorization").get(0));

        HttpEntity<User> request = new HttpEntity<>(responseUser,headers);
        ResponseEntity<User> libUserResponse = testRestTemplate.exchange(getUserUri,HttpMethod.PUT,
                                                    request,User.class);


        //Unauthorized to get another user details
        Assert.assertEquals(HttpStatus.OK, libUserResponse.getStatusCode());

        // Now login with changed password
        loginResponse = libraryApiIntegrationTestUtil.loginUser(responseUser.getUsername(),
                       responseUser.getPassword());
        Assert.assertEquals(HttpStatus.OK,loginResponse.getStatusCode());

        // Now we get the user. should have update email and phone number
        // put the new Auth token
        headers.replace("Authorization",loginResponse.getHeaders().get("Authorization"));

        libUserResponse = testRestTemplate.exchange(
                     getUserUri,HttpMethod.GET,new HttpEntity<Object>(headers),User.class);

        Assert.assertEquals(HttpStatus.OK,libUserResponse.getStatusCode());
        User libraryUser = libUserResponse.getBody();
        Assert.assertEquals("newemailaddress@email.com",libraryUser.getEmailId());
        Assert.assertEquals(TestConstants.TEST_USER_PHONE_UPDATED,libraryUser.getPhoneNumber());
    }

    @Test
    public void updateUser_unauthorized_for_different_user(){

        String port = environment.getProperty("local.server.port");

        // First we register a user
        ResponseEntity<User> response = libraryApiIntegrationTestUtil.registerNewUser("update.user.unauthorized.for.different.user");
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        User responseUser = response.getBody();
        Assert.assertNotNull(responseUser);
        // update the details of another userId (userId +1)
        Integer userId = responseUser.getUserId() + 1;

        // Login with the credentials to perform update
        ResponseEntity<String> loginResponse = libraryApiIntegrationTestUtil
                .loginUser(responseUser.getUsername(), responseUser.getPassword());
        Assert.assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        // set the values to be updated
        responseUser.setPassword("NewPassword");
        responseUser.setEmailId("newemailaddress@email.com");
        responseUser.setEmailId(TestConstants.TEST_USER_PHONE_UPDATED);

        URI getUserUri = null;  // /v1/users/{userId}
        try {
            getUserUri = new URI(TestConstants.API_BASE_URL+port +TestConstants.USER_API_BASE_URI + "/" + userId);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        MultiValueMap<String, String> headers = libraryApiIntegrationTestUtil
                .createAuthorizationHeader(loginResponse.getHeaders().get("Authorization").get(0));

        HttpEntity<User> request = new HttpEntity<>(responseUser,headers);

        // We need to user RestTemplate because we need to set the ErrorHandler because TestRestTemplate
        // throws HttpRetryException when there is a response body with response status 401.
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            public boolean hasError(ClientHttpResponse response) throws IOException {
               HttpStatus statusCode = response.getStatusCode();
                return statusCode.series() == HttpStatus.Series.SERVER_ERROR;
            }
        });

        ResponseEntity<String> libUserResponse = restTemplate.exchange(getUserUri,HttpMethod.PUT,
                request,String.class);

        //Update for another userId should not be allowed
        Assert.assertEquals(HttpStatus.FORBIDDEN, libUserResponse.getStatusCode());
    }

    @Test
    public void deleteUser_success(){
        String port = environment.getProperty("local.server.port");

        // First we register a user
        ResponseEntity<User> response = libraryApiIntegrationTestUtil.registerNewUser("delete.user.success");
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        User responseUser = response.getBody();
        Assert.assertNotNull(responseUser);
        Integer userId = responseUser.getUserId();

        // Login with the credentials
        ResponseEntity<String> loginResponse = libraryApiIntegrationTestUtil
                .loginUser(responseUser.getUsername(), responseUser.getPassword());
        Assert.assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

        // Delete the user
        URI getUserUri = null;  // /v1/users/{userId}
        try {
            getUserUri = new URI(TestConstants.API_BASE_URL+port +TestConstants.USER_API_BASE_URI + "/" + userId);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        MultiValueMap<String, String> headers = libraryApiIntegrationTestUtil
                .createAuthorizationHeader(loginResponse.getHeaders().get("Authorization").get(0));

        HttpEntity<User> request = new HttpEntity<>(responseUser,headers);
        ResponseEntity<String> libUserResponse = testRestTemplate.exchange(getUserUri,HttpMethod.DELETE,
                request,String.class);

        //Unauthorized to get another user details
        Assert.assertEquals(HttpStatus.ACCEPTED, libUserResponse.getStatusCode());

        // Now login with changed password
        loginResponse = libraryApiIntegrationTestUtil.loginUser(responseUser.getUsername(),
                responseUser.getPassword());
        Assert.assertEquals(HttpStatus.FORBIDDEN,loginResponse.getStatusCode());
    }
    @Test
    public void deleteUser_unAuthorized_for_different_user(){

        String port = environment.getProperty("local.server.port");

        // First we register a user
        ResponseEntity<User> response = libraryApiIntegrationTestUtil.registerNewUser("update.user.unauthorized.for.different.user");
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        User responseUser = response.getBody();
        Assert.assertNotNull(responseUser);
        // update the details of another userId (userId +1)
        Integer userId = responseUser.getUserId() + 1;

        // Login with the credentials to perform update
        ResponseEntity<String> loginResponse = libraryApiIntegrationTestUtil
                .loginUser(responseUser.getUsername(), responseUser.getPassword());
        Assert.assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        // set the values to be updated
        responseUser.setPassword("NewPassword");
        responseUser.setEmailId("newemailaddress@email.com");
        responseUser.setEmailId(TestConstants.TEST_USER_PHONE_UPDATED);

        URI getUserUri = null;  // /v1/users/{userId}
        try {
            getUserUri = new URI(TestConstants.API_BASE_URL+port +TestConstants.USER_API_BASE_URI + "/" + userId);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        MultiValueMap<String, String> headers = libraryApiIntegrationTestUtil
                .createAuthorizationHeader(loginResponse.getHeaders().get("Authorization").get(0));

        HttpEntity<User> request = new HttpEntity<>(responseUser,headers);

        // We need to user RestTemplate because we need to set the ErrorHandler because TestRestTemplate
        // throws HttpRetryException when there is a response body with response status 401.
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            public boolean hasError(ClientHttpResponse response) throws IOException {
                HttpStatus statusCode = response.getStatusCode();
                return statusCode.series() == HttpStatus.Series.SERVER_ERROR;
            }
        });

        ResponseEntity<String> libUserResponse = restTemplate.exchange(getUserUri,HttpMethod.PUT,
                request,String.class);

        //Update for another userId should not be allowed
        Assert.assertEquals(HttpStatus.FORBIDDEN, libUserResponse.getStatusCode());
    }

    @Test
    public void deleteUser_Unauthorized_for_differnt_user(){

        String port = environment.getProperty("local.server.port");

        // First we register a user
        ResponseEntity<User> response = libraryApiIntegrationTestUtil.registerNewUser("delete.user.unauthorized.for.differnt.user");
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        User responseUser = response.getBody();
        Assert.assertNotNull(responseUser);

        // Change the userId to something else
        Integer userId = responseUser.getUserId() +1 ;

        // Login with the credentials
        ResponseEntity<String> loginResponse = libraryApiIntegrationTestUtil
                .loginUser(responseUser.getUsername(), responseUser.getPassword());
        Assert.assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

        // Delete the user
        URI getUserUri = null;  // /v1/users/{userId}
        try {
            getUserUri = new URI(TestConstants.API_BASE_URL+port +TestConstants.USER_API_BASE_URI + "/" + userId);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        MultiValueMap<String, String> headers = libraryApiIntegrationTestUtil
                .createAuthorizationHeader(loginResponse.getHeaders().get("Authorization").get(0));

        HttpEntity<User> request = new HttpEntity<>(responseUser,headers);
        ResponseEntity<String> libUserResponse = testRestTemplate.exchange(getUserUri,HttpMethod.DELETE,
                request,String.class);

        Assert.assertEquals(HttpStatus.FORBIDDEN, libUserResponse.getStatusCode());
    }

    @Test
    public void searchUsers_success(){
        // Register 10 users
        for(int i=0;i<10;i++){
            libraryApiIntegrationTestUtil.registerNewUser("searchUsers.success" + i);
        }
        URI searchUri = null;

        try{
            searchUri = new URI(TestConstants.USER_API_SEARCH_URI + "?firstName=" +
                     TestConstants.TEST_USER_FIRST_NAME + "&lastName=" +
                      TestConstants.TEST_USER_LAST_NAME);
        }catch(URISyntaxException e){
            e.printStackTrace();
        }

        ResponseEntity<User[]> responseEntity = testRestTemplate.getForEntity(searchUri,User[].class);
        Assert.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        // Comparing with >=10 because depending upon which other test methods
        // run the number of users may vary
        Assert.assertTrue(responseEntity.getBody().length >= 10);
        for( User libraryUser : responseEntity.getBody()){
            Assert.assertTrue(libraryUser.getFirstName().contains(TestConstants.TEST_USER_FIRST_NAME));
            Assert.assertTrue(libraryUser.getLastName().contains(TestConstants.TEST_USER_LAST_NAME));
        }
    }
}
