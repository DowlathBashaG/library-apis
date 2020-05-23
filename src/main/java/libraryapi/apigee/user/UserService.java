package libraryapi.apigee.user;

import libraryapi.apigee.book.*;
import libraryapi.apigee.exception.LibraryResourceAlreadyExistException;
import libraryapi.apigee.exception.LibraryResourceNotFoundException;
import libraryapi.apigee.security.SecurityConstants;
import libraryapi.apigee.util.LibraryApiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Dowlath
 * @create 5/23/2020 1:27 PM
 */
@Service
public class UserService {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private BookRepository bookRepository;
    private BookStatusRepository bookStatusRepository;
    private BookService bookService;
    private UserBookEntityRepository userBookEntityRepository;

    @Value("${Library.rule.user.book.max.times.issue: 3}")
    private int maxNumberOfTimesIssue;

    public UserService( BCryptPasswordEncoder bCryptPasswordEncoder,
                        UserRepository userRepository,
                        BookRepository bookRepository,
                        BookStatusRepository bookStatusRepository,
                        BookService bookService,
                        UserBookEntityRepository userBookEntityRepository ) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.bookStatusRepository = bookStatusRepository;
        this.bookService = bookService;
        this.userBookEntityRepository = userBookEntityRepository;
    }

    public void addUser(User userToBeAdded,String traceId)
                 throws LibraryResourceAlreadyExistException{
        logger.debug("TraceId: {}, Request to add User: {} ",traceId,userToBeAdded);
        UserEntity userEntity = new UserEntity(
                userToBeAdded.getUsername(),
                bCryptPasswordEncoder.encode(SecurityConstants.NEW_USER_DEFAULT_PASSWORD),
                userToBeAdded.getFirstName(),
                userToBeAdded.getLastName(),
                userToBeAdded.getDateOfBirth(),
                userToBeAdded.getGenter(),
                userToBeAdded.getPhoneNumber(),
                userToBeAdded.getEmailId(),"USER");

        userToBeAdded.setPassword(SecurityConstants.NEW_USER_DEFAULT_PASSWORD);
        UserEntity addedUser = null;

        try{
              addedUser = userRepository.save(userEntity);
        }catch(DataIntegrityViolationException e){
            logger.error("TraceId: {} , User already exists !!! ", traceId,e);
            throw new LibraryResourceAlreadyExistException(traceId,"User already exists !!!");
        }

        userToBeAdded.setUserId(addedUser.getUserId());
        userToBeAdded.setRole(Role.USER);
        logger.info("TraceId: {] , User added: {} ",traceId,userToBeAdded);
    }

    public User getUser(Integer userId,String traceId) throws LibraryResourceNotFoundException{
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        User user = null;
        if(userEntity.isPresent()){
            UserEntity pe = userEntity.get();
            user = createUserFromEntity(pe);
        } else {
            throw new LibraryResourceNotFoundException(traceId,"User Id: "+ userId +" Not found");
        }
        return user;
    }

    public void updateUser(User userToBeUpdated,String traceId) throws LibraryResourceNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findById(userToBeUpdated.getUserId());

        if (userEntity.isPresent()) {
            UserEntity pe = userEntity.get();
            if (LibraryApiUtils.doesStringValueExist(userToBeUpdated.getEmailId())) {
                pe.setEmailId(userToBeUpdated.getEmailId());
            }
            if (LibraryApiUtils.doesStringValueExist(userToBeUpdated.getPhoneNumber())) {
                pe.setPhoneNumber(userToBeUpdated.getPhoneNumber());
            }
            if (LibraryApiUtils.doesStringValueExist(userToBeUpdated.getPassword())) {
                pe.setPassword(bCryptPasswordEncoder.encode(userToBeUpdated.getPassword()));
            }
            userRepository.save(pe);
            userToBeUpdated = createUserFromEntity(pe);
        } else {
            throw new LibraryResourceNotFoundException(traceId,"User Id: "+ userToBeUpdated.getUserId()+"Not found" );
        }
    }

    public void deleteUser(Integer userId,String traceId) throws LibraryResourceNotFoundException{
        try{
            userRepository.deleteById(userId);
        }catch(EmptyResultDataAccessException e){
            logger.error("TraceId: {} , UserId: {] not found ",traceId,userId,e);
            throw new LibraryResourceNotFoundException(traceId,"User Id: "+ userId +"Not found");
        }
    }

    public List<User> searchUser(String firstName , String lastName , String traceId){
        List<UserEntity> userEntities = null;
        if(LibraryApiUtils.doesStringValueExist(firstName) && LibraryApiUtils.doesStringValueExist(lastName)){
            userEntities = userRepository.findByFirstNameAndLastNameContaining(firstName,lastName);
        } else if(LibraryApiUtils.doesStringValueExist(firstName) && !LibraryApiUtils.doesStringValueExist(lastName)){
            userEntities = userRepository.findByFirstNameContaining(firstName);
        } else if(!LibraryApiUtils.doesStringValueExist(firstName) && LibraryApiUtils.doesStringValueExist(lastName)) {
            userEntities = userRepository.findByLastNameContaining(lastName);
        }
        if(userEntities != null && userEntities.size() > 0){
            return createUsersForSearchResponse(userEntities);
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional
    public IssueBookResponse issueBooks(int userId, Set<Integer> bookIds,String traceId)
                                        throws LibraryResourceNotFoundException{
       Optional<UserEntity> userEntity = userRepository.findById(userId);
       if(userEntity.isPresent()){
           Set<IssueBookStatus> issueBookStatuses = new HashSet<>(bookIds.size());
           //Find out if the supplied lisf of books is issue-able or not
           bookIds.stream()
                   .forEach(bookId -> {
                            Optional<BookEntity> be = bookRepository.findById(bookId);
                            IssueBookStatus bookStatus;
                            if(!be.isPresent()){
                                 bookStatus = new IssueBookStatus(bookId,"Not Issued","Book Not Found");
                            } else{
                                BookStatusEntity bse = be.get().getBookStatus();
                                if((bse.getTotalNumberOfCopies() - bse.getNumberOfCopiesIussed()) == 0){
                                    bookStatus = new IssueBookStatus(bookId,"Not issued","No copies available");
                                } else {
                                    // check if the book has already been issued to the user , and this can be re-issued
                                    List<UserBookEntity> byUserIdAndBookId = userBookEntityRepository.findByUserIdAndBookId(userId,bookId);
                                    if(byUserIdAndBookId != null && byUserIdAndBookId.size() > 0){
                                        // Book can be re-issued
                                        UserBookEntity userBookEntity = byUserIdAndBookId.get(0);
                                        if(userBookEntity.getNumberOfTimesIssued() < maxNumberOfTimesIssue){
                                            userBookEntity.setNumberOfTimesIssued(userBookEntity.getNumberOfTimesIssued()+1);
                                            userBookEntity.setIssuedDate(LocalDate.now());
                                            userBookEntity.setReturnDate(LocalDate.now().plusDays(14));
                                            userBookEntityRepository.save(userBookEntity);
                                            bookStatus = new IssueBookStatus(bookId,"Issued ","Book Re-Issued");
                                        } else{
                                            // Book can not be re-issued as it has already been issued max number of times
                                            bookStatus = new IssueBookStatus(bookId,"Not Issued","Book already" +
                                                                "issued to the user for "+ maxNumberOfTimesIssue + "times");
                                        }
                                    } else{
                                        // This is the first time book is being issued
                                        // Issue the books to the user
                                        UserBookEntity userBookEntity = new UserBookEntity(userId, bookId, LocalDate.now(),LocalDate.now().plusDays(14),1 );
                                        userBookEntityRepository.save(userBookEntity);

                                        // Manage the number of issued copies

                                        BookStatusEntity bs = be.get().getBookStatus();
                                        bs.setNumberOfCopiesIussed(bs.getNumberOfCopiesIussed()+1);
                                        bookStatusRepository.save(bs);

                                        bookStatus = new IssueBookStatus(bookId,"Issued","Book Issued");
                                      }
                                    }
                                }
                             issueBookStatuses.add(bookStatus);
                            });
                 // Set and return final response
                 return new IssueBookResponse(issueBookStatuses);
       } else{
            throw new LibraryResourceNotFoundException(traceId,"Library User Id: "+ userId+ "Not found");
       }
    }

    @Transactional
    public void returnBooks(int userId,Integer bookId,String traceId) throws LibraryResourceNotFoundException{

        Optional<UserEntity> userEntity = userRepository.findById(userId);

        if(userEntity.isPresent()){
            List<UserBookEntity> byUserIdAndBookId = userBookEntityRepository.findByUserIdAndBookId(userId,bookId);
            if(byUserIdAndBookId != null && byUserIdAndBookId.size() > 0){
                // return the book
                userBookEntityRepository.delete(byUserIdAndBookId.get(0));

                Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
                BookStatusEntity bs = bookEntity.get().getBookStatus();
                bs.setNumberOfCopiesIussed(bs.getNumberOfCopiesIussed() -1);
                bookStatusRepository.save(bs);
            } else {
                throw new LibraryResourceNotFoundException(traceId,"Book Id: "+bookId +
                        "has not been issued to UserId: "+userId+". So can not be returned");
            }
        } else {
            throw new LibraryResourceNotFoundException(traceId,"Library User Id: "+userId + "Not found");
        }
    }

    public User getUserByUserName(String userName) throws  LibraryResourceNotFoundException{
        UserEntity userEntity = userRepository.findByUsername(userName);
        if(userEntity != null){
            return createUserFromEntityForLogin(userEntity);
        } else {
            throw new LibraryResourceNotFoundException(null,"LibraryUserName: "+userName+"Not found");
        }
    }

    private User createUserFromEntity(UserEntity pe) {
        return new User(pe.getUserId(),pe.getUserName(),pe.getFirstName(),pe.getLastName(),
                     pe.getDateOfBirth(),pe.getGender(),pe.getPhoneNumber(),pe.getEmailId(),
                     Role.valueOf(pe.getRole()));
    }

    private List<User> createUsersForSearchResponse(List<UserEntity> userEntities) {
        return userEntities.stream()
                 .map(ue -> new User(ue.getUserName(),ue.getFirstName(),ue.getLastName()))
                .collect(Collectors.toList());
    }

    private User createUserFromEntityForLogin(UserEntity ue){
        return new User(ue.getUserId(),ue.getUserName(),ue.getPassword(),ue.getFirstName(),
                    ue.getLastName(),ue.getDateOfBirth(),ue.getGender(),ue.getPhoneNumber(),
                    ue.getEmailId(),Role.valueOf(ue.getRole()));

    }
}
