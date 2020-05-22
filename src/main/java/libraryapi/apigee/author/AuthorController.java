package libraryapi.apigee.author;

import libraryapi.apigee.exception.LibraryResourceAlreadyExistException;
import libraryapi.apigee.exception.LibraryResourceBadRequestException;
import libraryapi.apigee.exception.LibraryResourceNotFoundException;
import libraryapi.apigee.exception.LibraryResourceUnauthorizedException;
import libraryapi.apigee.util.LibraryApiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

/**
 * @Author Dowlath
 * @create 5/22/2020 9:09 PM
 */
@RestController
@RequestMapping(path="/v1/authors")
public class AuthorController {

    private static Logger logger = LoggerFactory.getLogger("AuthorController.class");

    private AuthorService authorService;

    public AuthorController(AuthorService authorService){
        this.authorService = authorService;
    }

    @GetMapping(path = "/{authodId}")
    public ResponseEntity<?> getAuthor(@PathVariable Integer authorId,
                                       @RequestHeader(value = "Trace-Id",defaultValue = "") String traceId)
                     throws LibraryResourceNotFoundException {
     if(!LibraryApiUtils.doesStringValueExist(traceId)){
         traceId = UUID.randomUUID().toString();
     }
     logger.debug("Added TraceId: {}",traceId);
     return new ResponseEntity<>(authorService.getAuthor(authorId,traceId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addAuthor(@Valid @RequestBody Author author,
                                       @RequestHeader(value = "Trace-Id",defaultValue = "") String traceId,
                                       @RequestHeader(value = "Authorization") String bearerToken)
                          throws LibraryResourceAlreadyExistException, LibraryResourceUnauthorizedException {
        logger.debug("Request to add Author: {}" ,author);
        if(!LibraryApiUtils.doesStringValueExist(traceId)){
            traceId = UUID.randomUUID().toString();
        }
        logger.debug("Added TraceId: {}",traceId);
        if(!LibraryApiUtils.isUserAdmin(bearerToken)){
            logger.error(LibraryApiUtils.getUserIdFromClaim(bearerToken) + " attempted to add a Author. Disallowed because user is not Admin");
            throw new LibraryResourceUnauthorizedException(traceId,"User not allowed to add a Author");
        }
        authorService.addAuthor(author,traceId);
        logger.debug("Returning response for TraceId: {} ",traceId);
        return new ResponseEntity<>(author,HttpStatus.CREATED);
    }


    @PutMapping(path = "/{authorId}")
    public ResponseEntity<?> updateAuthor(@PathVariable Integer authorId,
                                          @Valid @RequestBody Author author,
                                          @RequestHeader(value = "Trace-Id",defaultValue = "") String traceId,
                                          @RequestHeader(value = "Authorization") String bearerToken)
                   throws LibraryResourceNotFoundException,LibraryResourceUnauthorizedException{
        if(!LibraryApiUtils.doesStringValueExist(traceId)){
            traceId = UUID.randomUUID().toString();
        }
        logger.debug("Added TraceId: {}",traceId);
        if(!LibraryApiUtils.isUserAdmin(bearerToken)){
            logger.error(LibraryApiUtils.getUserIdFromClaim(bearerToken) + " attempted to update a author. Disallowed because user is not Admin");
            throw new LibraryResourceUnauthorizedException(traceId,"User not allowed to add a author");
        }
        author.setAuthorId(authorId);
        authorService.updateAuthor(author,traceId);
        logger.debug("Returning response for TraceId: {} ",traceId);
        return new ResponseEntity<>(author,HttpStatus.OK);

    }

    @DeleteMapping(path = "/{authorId}")
    public ResponseEntity<?> deleteAuthor(@PathVariable Integer authorId,
                                          @RequestHeader(value = "Trace-Id",defaultValue = "") String traceId,
                                          @RequestHeader(value = "Authorization") String bearerToken)
            throws LibraryResourceNotFoundException,LibraryResourceUnauthorizedException{
        if(!LibraryApiUtils.isUserAdmin(bearerToken)){
            logger.error(LibraryApiUtils.getUserIdFromClaim(bearerToken) + "attempted to delete a author. Disallowed because user is not Admin");
            throw new LibraryResourceUnauthorizedException(traceId,"User not allowed to add a author");
        }

        logger.debug("Added TraceId: {} ",traceId);
        authorService.deleteAuthor(authorId,traceId);
        logger.debug("Returning response for TraceId: {} ",traceId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


    @GetMapping(path = "/search")
    public ResponseEntity<?> searchAuthor(@RequestParam String firstName,@RequestParam String lastName,
                                          @RequestHeader(value = "Trace-Id",defaultValue = "") String traceId)
            throws LibraryResourceBadRequestException {

        if(!LibraryApiUtils.doesStringValueExist(traceId)){
            traceId = UUID.randomUUID().toString();
        }
        logger.debug("Added TraceId: {}",traceId);
        if(!LibraryApiUtils.doesStringValueExist(firstName) && !LibraryApiUtils.doesStringValueExist(lastName)){
            logger.error("TraceId: {}, Please enter at least one search criteria to search Authors !!!",traceId);
            throw new LibraryResourceBadRequestException(traceId,"Please enter a name to search Author.");
        }
        logger.debug("Returnning response for TraceId: {} ",traceId);
        return new ResponseEntity<>(authorService.searchAuthor(firstName,lastName,traceId),HttpStatus.OK);
    }
}
