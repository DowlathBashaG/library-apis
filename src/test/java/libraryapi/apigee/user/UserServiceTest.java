package libraryapi.apigee.user;

import libraryapi.apigee.book.BookRepository;
import libraryapi.apigee.book.BookService;
import libraryapi.apigee.book.BookStatusRepository;
import libraryapi.apigee.exception.LibraryResourceAlreadyExistException;
import libraryapi.apigee.exception.LibraryResourceNotFoundException;
import libraryapi.apigee.publisher.PublisherRepository;
import libraryapi.apigee.testutils.LibraryApiTestUtil;
import libraryapi.apigee.testutils.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 * @Author Dowlath
 * @create 5/24/2020 2:22 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookStatusRepository bookStatusRepository;

    @Mock
    private BookService bookService;

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private UserBookEntityRepository userBookEntityRepository;

    UserService userService;

    @Before
    public void setUp(){
        userService = new UserService(bCryptPasswordEncoder,userRepository,
                     bookRepository,bookStatusRepository,bookService,userBookEntityRepository);
    }

    @Test
    public void addUser_success() throws LibraryResourceAlreadyExistException{
        when(userRepository.save(any(UserEntity.class)))
                .thenReturn(LibraryApiTestUtil.createUserEntity(TestConstants.TEST_USER_USERNAME));
        User user = LibraryApiTestUtil.createUser(TestConstants.TEST_USER_USERNAME);
        userService.addUser(user,TestConstants.API_TRACE_ID);

        verify(userRepository,times(1)).save(any(UserEntity.class));
        assertNotNull(user.getUserId());
        assertTrue(user.getFirstName().equals(TestConstants.TEST_USER_FIRST_NAME));
    }

    @Test(expected = LibraryResourceAlreadyExistException.class)
    public void addUser_failure() throws LibraryResourceAlreadyExistException{
        doThrow(DataIntegrityViolationException.class).when(userRepository).save(any(UserEntity.class));
        User user = LibraryApiTestUtil.createUser(TestConstants.TEST_USER_USERNAME);
        userService.addUser(user,TestConstants.API_TRACE_ID);
        verify(userRepository,times(1)).save(any(UserEntity.class));
    }

    @Test
    public void getUser_success() throws LibraryResourceNotFoundException{
        when(userRepository.findById(anyInt()))
                .thenReturn(LibraryApiTestUtil.createUserEntityOptional(TestConstants.TEST_USER_USERNAME));
        User user = userService.getUser(123,TestConstants.API_TRACE_ID);
        verify(userRepository,times(1)).findById(123);
        assertNotNull(user);
        assertNotNull(user.getUserId());
    }

    @Test(expected = LibraryResourceNotFoundException.class)
    public void getUser_failure() throws LibraryResourceNotFoundException{
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        userService.getUser(123,TestConstants.API_TRACE_ID);
        verify(userRepository,times(1)).findById(123);
    }

    @Test
    public void updateUser_success() throws LibraryResourceAlreadyExistException,
            LibraryResourceNotFoundException {
            UserEntity userEntity = LibraryApiTestUtil.createUserEntity(TestConstants.TEST_USER_USERNAME);
            when(userRepository.save(any(UserEntity.class)))
                    .thenReturn(userEntity);
            User user = LibraryApiTestUtil.createUser(TestConstants.TEST_USER_USERNAME);
            userService.addUser(user,TestConstants.API_TRACE_ID);
            verify(userRepository,times(1)).save(any(UserEntity.class));

            user.setEmailId("changed.email@email.com");
            user.setPhoneNumber("987-654-321");
            user.setPassword("ChangedPassword");

            when(userRepository.findById(anyInt()))
                    .thenReturn(LibraryApiTestUtil.createUserEntityOptional(TestConstants.TEST_USER_USERNAME));
            userService.updateUser(user,TestConstants.API_TRACE_ID);

            verify(userRepository,times(1)).findById(user.getUserId());
            verify(userRepository,times(2)).save(any(UserEntity.class));

            assertTrue(user.getEmailId().equals("changed.email@email.com"));
            assertTrue(user.getPhoneNumber().equals("987-654-321"));
        }

        @Test
        public void deleteUser_success() throws LibraryResourceNotFoundException{
            doNothing().when(userRepository).deleteById(123);
            userService.deleteUser(123,TestConstants.API_TRACE_ID);
            verify(userRepository,times(1)).deleteById(123);
        }

        @Test(expected = LibraryResourceNotFoundException.class)
        public void deleteUser_failure() throws LibraryResourceNotFoundException{
          doThrow(EmptyResultDataAccessException.class).when(userRepository).deleteById(123);
          userService.deleteUser(123,TestConstants.API_TRACE_ID);
          verify(userRepository,times(1)).deleteById(123);
        }

        @Test
        public void searchUser_success_firstName_lastName(){
            List<UserEntity> userEntityList = Arrays.asList(
                    LibraryApiTestUtil.createUserEntity(TestConstants.TEST_USER_USERNAME + ".1"),
                    LibraryApiTestUtil.createUserEntity(TestConstants.TEST_USER_USERNAME + ".2")
            );
            when(userRepository.findByFirstNameAndLastNameContaining(TestConstants.TEST_USER_FIRST_NAME,
                         TestConstants.TEST_USER_LAST_NAME)).thenReturn(userEntityList);
            List<User> users = userService.searchUser(TestConstants.TEST_USER_FIRST_NAME,
                              TestConstants.TEST_USER_LAST_NAME,
                              TestConstants.API_TRACE_ID);

            verify(userRepository,times(1))
                          .findByFirstNameAndLastNameContaining(TestConstants.TEST_USER_FIRST_NAME,
                                  TestConstants.TEST_USER_LAST_NAME);
            assertEquals(userEntityList.size(),users.size());
            assertEquals(userEntityList.size(),users.stream()
                           .filter(user -> user.getFirstName().contains(TestConstants.TEST_USER_FIRST_NAME))
                           .count());
            when(userRepository.findByFirstNameContaining(TestConstants.TEST_USER_FIRST_NAME))
                    .thenReturn(userEntityList);

            List<User> usersList = userService.searchUser(TestConstants.TEST_USER_FIRST_NAME," ",
                                              TestConstants.API_TRACE_ID);
            verify(userRepository,times(1))
                    .findByFirstNameContaining(TestConstants.TEST_USER_FIRST_NAME);

            assertEquals(userEntityList.size(),usersList.size());
            assertEquals(userEntityList.size(),usersList.stream()
                          .filter(user -> user.getFirstName().contains(TestConstants.TEST_USER_FIRST_NAME))
                          .count());
        }

    @Test
    public void searchUser_success_firstname(){
        List<UserEntity> userEntityList = Arrays.asList(
                LibraryApiTestUtil.createUserEntity(TestConstants.TEST_USER_USERNAME + ".1"),
                LibraryApiTestUtil.createUserEntity(TestConstants.TEST_USER_USERNAME + ".2"),
                LibraryApiTestUtil.createUserEntity(TestConstants.TEST_USER_USERNAME + ".3")
        );
       when(userRepository.findByFirstNameContaining(TestConstants.TEST_USER_LAST_NAME))
                .thenReturn(userEntityList);
       List<User> users = userService.searchUser(TestConstants.TEST_USER_FIRST_NAME,"",TestConstants.API_TRACE_ID);
       verify(userRepository,times(1))
               .findByFirstNameContaining(TestConstants.TEST_USER_FIRST_NAME);
       assertEquals(userEntityList.size(),users.size());
       assertEquals(userEntityList.size(),users.stream()
                 .filter(user -> user.getFirstName().contains(TestConstants.TEST_USER_FIRST_NAME))
                 .count());
    }

     @Test
     public void searchUser_success_lastname(){
        List<UserEntity> userEntityList = Arrays.asList(
                LibraryApiTestUtil.createUserEntity(TestConstants.TEST_USER_USERNAME + ".1"),
                LibraryApiTestUtil.createUserEntity(TestConstants.TEST_USER_USERNAME + ".2"),
                LibraryApiTestUtil.createUserEntity(TestConstants.TEST_USER_USERNAME + ".3")
        );
        when(userRepository.findByLastNameContaining(TestConstants.TEST_USER_LAST_NAME))
                .thenReturn(userEntityList);
         List<User> users = userService.searchUser("",TestConstants.TEST_USER_LAST_NAME,TestConstants.API_TRACE_ID);
         verify(userRepository,times(1))
                 .findByLastNameContaining(TestConstants.TEST_USER_LAST_NAME);
         assertEquals(userEntityList.size(),users.size());
         assertEquals(userEntityList.size(),users.stream()
                 .filter(user -> user.getLastName().contains(TestConstants.TEST_USER_LAST_NAME))
                 .count());
     }

    @Test
    public void searchUser_failure(){
        when(userRepository
            .findByFirstNameAndLastNameContaining(TestConstants.TEST_AUTHOR_FIRST_NAME,TestConstants.TEST_AUTHOR_LAST_NAME))
            .thenReturn(Collections.emptyList());
        List<User> users = userService
                .searchUser(TestConstants.TEST_AUTHOR_FIRST_NAME,TestConstants.TEST_AUTHOR_LAST_NAME,
                        TestConstants.API_TRACE_ID);
        verify(userRepository,times(1))
                .findByFirstNameAndLastNameContaining(TestConstants.TEST_AUTHOR_FIRST_NAME,
                         TestConstants.TEST_AUTHOR_LAST_NAME);
         assertEquals(0,users.size());
    }

    @Test
    public void issueBooks_success() throws LibraryResourceNotFoundException,
            LibraryResourceAlreadyExistException{

        // Adding user
        UserEntity userEntity = LibraryApiTestUtil.createUserEntity(TestConstants.TEST_USER_USERNAME);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        User user = LibraryApiTestUtil.createUser(TestConstants.TEST_USER_USERNAME);
        userService.addUser(user,TestConstants.API_TRACE_ID);
        verify(userRepository,times(1)).save(any(UserEntity.class));

        // Adding book
        bookService.addBook(LibraryApiTestUtil.createBook(1),TestConstants.API_TRACE_ID);
        when(bookRepository.findById(anyInt())).thenReturn(LibraryApiTestUtil.createBookEntityOptional());
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userEntity));

        Set<Integer> books = new HashSet<>(1);
        books.add(1);

        IssueBookResponse issueBookResponse = userService.issueBooks(user.getUserId(),
                       books,TestConstants.API_TRACE_ID);

        assertNotNull(issueBookResponse);
        assertNotNull(issueBookResponse.getIssueBookStatus());
        assertEquals(1,issueBookResponse.getIssueBookStatus().size());
        assertEquals(1,issueBookResponse.getIssueBookStatus().stream()
                        .filter(issueBookStatus -> issueBookStatus.getStatus().equals("Issued"))
                        .count());
        }

    @Test(expected = LibraryResourceNotFoundException.class)
    public void issueBooks_failure_book_not_found() throws LibraryResourceAlreadyExistException,
            LibraryResourceNotFoundException{

        // Adding user
        UserEntity userEntity = LibraryApiTestUtil.createUserEntity(TestConstants.TEST_USER_USERNAME);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        User user = LibraryApiTestUtil.createUser(TestConstants.TEST_USER_USERNAME);

        Set<Integer> books = new HashSet<>(1);
        books.add(1);
        IssueBookResponse issueBookResponse = userService.issueBooks(1234,books,TestConstants.API_TRACE_ID);
        assertNotNull(issueBookResponse);
        assertNotNull(issueBookResponse.getIssueBookStatus());
        assertEquals(1,issueBookResponse.getIssueBookStatus().size());
        assertEquals(1,issueBookResponse.getIssueBookStatus().stream()
                .filter(issueBookStatus -> issueBookStatus.getStatus().equals("Not Issued"))
                .count());
        assertEquals(1,issueBookResponse.getIssueBookStatus().stream()
                .filter(issueBookStatus -> issueBookStatus.getStatus().equals("Book Not Found"))
                .count());
    }

    }



