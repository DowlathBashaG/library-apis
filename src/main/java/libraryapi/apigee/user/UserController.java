package libraryapi.apigee.user;

import libraryapi.apigee.exception.LibraryResourceAlreadyExistException;
import libraryapi.apigee.exception.LibraryResourceBadRequestException;
import libraryapi.apigee.exception.LibraryResourceNotFoundException;
import libraryapi.apigee.exception.LibraryResourceUnauthorizedException;
import libraryapi.apigee.util.LibraryApiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;

/**
 * @Author Dowlath
 * @create 5/23/2020 5:44 PM
 */
@RestController
@RequestMapping(path = "/v1/users")
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Integer userId,
                                     @RequestHeader(value = "Trace-Id",defaultValue = " ") String traceId,
                                     @RequestHeader(value = "Authorization") String bearerToken)
            throws LibraryResourceNotFoundException, LibraryResourceUnauthorizedException{
        if(!LibraryApiUtils.doesStringValueExist(traceId)){
            traceId = UUID.randomUUID().toString();
        }
        logger.debug("Added TraceId: {} ",traceId);
        if(LibraryApiUtils.isUserAdmin(bearerToken)){
            logger.error("Trace Id: {} ,even an admin user is not allowed to get a user's details", traceId);
            throw new LibraryResourceUnauthorizedException(traceId,"Not allowed to get the details of another user");
        }
        return new ResponseEntity<>(userService.getUser(userId,traceId), HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody User user,
                                     @RequestHeader(value = "Trace-Id",defaultValue = " ") String traceId)
                           throws LibraryResourceAlreadyExistException{

        logger.debug("Request to add user : {} ",user);
        if(!LibraryApiUtils.doesStringValueExist(traceId)){
            traceId = UUID.randomUUID().toString();
        }
        logger.debug("Added TraceId: {} ",traceId);
        userService.addUser(user,traceId);

        logger.debug("Returning response for TraceId: {} ",traceId);
        return new ResponseEntity<>(user,HttpStatus.CREATED);
     }

     @PutMapping(path="/{userId}")
     public ResponseEntity<?> updateUser(@PathVariable Integer userId,
                                         @Valid @RequestBody User user,
                                         @RequestHeader(value = "Trace-Id",defaultValue = " ") String traceId,
                                         @RequestHeader(value = "Authorization") String bearerToken)
             throws LibraryResourceNotFoundException, LibraryResourceUnauthorizedException{

         if(!LibraryApiUtils.doesStringValueExist(traceId)){
             traceId = UUID.randomUUID().toString();
         }
         logger.debug("Added TraceId: {} ",traceId);

         if(LibraryApiUtils.isUserAdmin(bearerToken)){
             logger.error("Trace Id: {} , even an admin user is not allowed to update a user's details",traceId);
             throw new LibraryResourceUnauthorizedException(traceId,"Even an admin user is not allowed to update a user's details");
         }
         int userIdInClaim = LibraryApiUtils.getUserIdFromClaim(bearerToken);
         if(userIdInClaim != userId){
             logger.error("Trace Id: {}, UserId {} not allowed to updated the details of another user {} "
                     ,traceId,userIdInClaim,userId);
             throw new LibraryResourceUnauthorizedException(traceId,"Not allowed to update the details of another user");
         }
         user.setUserId(userId);
         userService.updateUser(user,traceId);
         logger.debug("Returning response for TraceId: {} ",traceId);
         return new ResponseEntity<>(user,HttpStatus.OK);
     }


     @DeleteMapping(path = "/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId,
                                        @RequestHeader(value = "Trace-Id",defaultValue = " ") String traceId,
                                        @RequestHeader(value = "Authorization") String bearerToken)
             throws LibraryResourceNotFoundException, LibraryResourceUnauthorizedException{
         if(!LibraryApiUtils.doesStringValueExist(traceId)){
             traceId = UUID.randomUUID().toString();
         }

         logger.debug("Added TraceId: {} ",traceId);

         int userIdInClaim = LibraryApiUtils.getUserIdFromClaim(bearerToken);
         if(userIdInClaim != userId){
             logger.error("Trace Id: {}, UserId {} not allowed to delete another user {} "
                     ,traceId);
             throw new LibraryResourceUnauthorizedException(traceId,"Even an admin user is not allowed to delete another user");
         }

         userService.deleteUser(userId,traceId);
         logger.debug("Returning response for TraceId: {} ",traceId);
         return new ResponseEntity<>(HttpStatus.ACCEPTED);
     }

     @GetMapping(path = "/search")
     public ResponseEntity<?> searchUser(@RequestParam String firstName,@RequestParam String lastName,
                                         @RequestHeader(value = "Trace-Id",defaultValue = " ") String traceId)
             throws LibraryResourceBadRequestException{

         if(!LibraryApiUtils.doesStringValueExist(traceId)){
             traceId = UUID.randomUUID().toString();
         }
        if(!LibraryApiUtils.doesStringValueExist(firstName) && !LibraryApiUtils.doesStringValueExist(lastName)){
            logger.error("TraceId: {} , Please enter at lease one search criteria to search users !!!",traceId);
            throw new LibraryResourceBadRequestException(traceId,"Please enter a name to search user.");
        }
        logger.debug("Returning response for TraceId: {} " , traceId);
        return new ResponseEntity<>(userService.searchUser(firstName,lastName,traceId),HttpStatus.OK);
     }

     @PutMapping(path = "/{userId}/books")
     public ResponseEntity<?> issueBooks(@PathVariable int userId,@RequestBody Set<Integer> bookIds,
                                         @RequestHeader(value = "Trace-Id",defaultValue = " ") String traceId,
                                         @RequestHeader(value = "Authorization") String bearerToken)
             throws LibraryResourceBadRequestException, LibraryResourceNotFoundException, LibraryResourceUnauthorizedException{
         if(!LibraryApiUtils.doesStringValueExist(traceId)){
             traceId = UUID.randomUUID().toString();
         }
         if(!LibraryApiUtils.isUserAdmin(bearerToken)){
             //Logging UserId for security audit trail.
             logger.error(traceId+ LibraryApiUtils.getUserIdFromClaim(bearerToken)+
                        "attempted to issue Books. Disallowed"+"User is not a Admin.");
             throw new LibraryResourceUnauthorizedException(traceId,"attempted to issue Books,Disallowed.");
         }
         if(bookIds == null || bookIds.size() == 0){
             logger.error(traceId+ "Invalid Book list, list is either not present or empty");
             throw new LibraryResourceBadRequestException(traceId,"Invalid Book list,List is either not present or empty");
         }
         IssueBookResponse issueBookResponse = null;
         try{
             issueBookResponse =userService.issueBooks(userId,bookIds,traceId);
         }catch (LibraryResourceNotFoundException e){
             logger.error(traceId + e.getMessage());
             throw e;
         }
         return new ResponseEntity<>(issueBookResponse,HttpStatus.OK);
     }

     @DeleteMapping(path = "/{userId}/books/{bookId}")
     public ResponseEntity<?> returnBooks(@PathVariable int userId,@PathVariable int bookId,
                                          @RequestHeader(value = "Trace-Id",defaultValue = " ") String traceId,
                                          @RequestHeader(value = "Authorization") String bearerToken)
             throws LibraryResourceBadRequestException, LibraryResourceNotFoundException, LibraryResourceUnauthorizedException{

         if(!LibraryApiUtils.doesStringValueExist(traceId)){
             traceId = UUID.randomUUID().toString();
         }
         if(!LibraryApiUtils.isUserAdmin(bearerToken)){
             //Logging UserId for security audit trail.
             logger.error(traceId+ LibraryApiUtils.getUserIdFromClaim(bearerToken)+
                     "attempted to issue Books. Disallowed"+"User is not a Admin.");
             throw new LibraryResourceUnauthorizedException(traceId,"attempted to issue Books,Disallowed.");
         }
         try{
             userService.returnBooks(userId,bookId,traceId);
         }catch(LibraryResourceNotFoundException e){
             logger.error(traceId + e.getMessage());
             throw e;
         }
         return new ResponseEntity<>(HttpStatus.ACCEPTED);
     }
}