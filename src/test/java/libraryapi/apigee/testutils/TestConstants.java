package libraryapi.apigee.testutils;

import libraryapi.apigee.model.common.Gender;

/**
 * @Author Dowlath
 * @create 5/22/2020 4:12 PM
 */
public class TestConstants {
    // Base URL
    public static final String API_BASE_URL = "http://localhost:";

    // Test Publisher details
    public static final String API_TRACE_ID = "Test-Trace-Id";
    public static final String TEST_PUBLISHER_NAME = "TestPublisherName";
    public static final String TEST_PUBLISHER_EMAIL = "TestPublisherName@email.com";
    public static final String TEST_PUBLISHER_PHONE = "123-456-789";

    public static final String TEST_PUBLISHER_EMAIL_UPDATED = "TestUPDATEDPublisherName@email.com";
    public static final String TEST_PUBLISHER_PHONE_UPDATED = "999-999-999";

    // Test Author Details
    public static final String TEST_AUTHOR_FIRST_NAME = "TestAuthorFn";
    public static final String TEST_AUTHOR_LAST_NAME =  "TestAuthorLn";

    // Test User Details
    public static final String TEST_USER_FIRST_NAME = "TestUserFn";
    public static final String TEST_USER_LAST_NAME  = "TestUserLn";
    public static final String TEST_USER_USERNAME = "test.username";
    public static final String TEST_USER_PASSWORD = "test.password";
    public static final String TEST_USER_EMAIL = TEST_USER_USERNAME + "@email.com" ;
    public static final String TEST_USER_PHONE = "223-344-566";
    public static final String TEST_USER_PHONE_UPDATED = "111-344-908";
    public static final Gender TEST_USER_GENDER = Gender.Female;


    // Test Book Details
    public static final String TEST_BOOK_ISBN = "978-3-16-148410-";
    public static final String TEST_BOOK_TITLE = "SpringBoot is fun";
    public static final int TEST_BOOK_YEAR_PUBLISHED = 2010;
    public static final String TEST_BOOK_EDITION = "First Edition";

    // User API URIs
    public static final String USER_API_REGISTER_URI = "/v1/users";
    public static final String LOGIN_URI = "/login";
    public static final String USER_API_BASE_URI = "/v1/users";
    public static final String USER_API_SEARCH_URI = USER_API_BASE_URI + "/search";

}
