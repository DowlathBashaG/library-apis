package libraryapi.apigee.book;

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
import java.util.Set;
import java.util.UUID;

/**
 * @Author Dowlath
 * @create 5/23/2020 11:54 AM
 */
@RestController
@RequestMapping(path = "/v1/books")
public class BookController {

    private static Logger logger = LoggerFactory.getLogger(BookController.class);
    private BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping(path = "/{bookId}")
    public ResponseEntity<?> getBook(@PathVariable Integer bookId,
                                     @RequestHeader(value = "Trace-Id",defaultValue = "") String traceId)
                              throws LibraryResourceNotFoundException{
        if(!LibraryApiUtils.doesStringValueExist(traceId)){
            traceId = UUID.randomUUID().toString();
        }
        return new ResponseEntity<>(bookService.getBook(bookId,traceId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addBook(@Valid @RequestBody Book book,
                                     @RequestHeader(value = "Trace-Id",defaultValue = "") String traceId,
                                     @RequestHeader(value = "Authorization") String bearerToken)
        throws LibraryResourceAlreadyExistException, LibraryResourceUnauthorizedException,
               LibraryResourceNotFoundException{

        logger.debug("Request to add Book: {} ",book);

        if(!LibraryApiUtils.doesStringValueExist(traceId)){
            traceId = UUID.randomUUID().toString();
        }
        if(!LibraryApiUtils.isUserAdmin(bearerToken)){
            logger.error(LibraryApiUtils.getUserIdFromClaim(bearerToken) +
                        " attempted to add a Book , disallowed because user is not admin");
            throw new LibraryResourceUnauthorizedException(traceId," User not allowed to add a Book");
        }
        logger.debug("Added TraceId: {} ",traceId);
        bookService.addBook(book,traceId);

        logger.debug("Returning  reponse for TraceId: {} ",traceId);
        return new ResponseEntity<>(book,HttpStatus.CREATED);
    }

    @PutMapping(path = "/{bookId}")
    public ResponseEntity<?> updateBook(@PathVariable Integer bookId,
                                        @Valid @RequestBody Book book,
                                        @RequestHeader(value = "Trace-Id",defaultValue = "") String traceId,
                                        @RequestHeader(value = "Authorization") String bearerToken)
            throws LibraryResourceUnauthorizedException, LibraryResourceNotFoundException{

        if(!LibraryApiUtils.doesStringValueExist(traceId)){
            traceId = UUID.randomUUID().toString();
        }
        if(!LibraryApiUtils.isUserAdmin(bearerToken)){
            logger.error(LibraryApiUtils.getUserIdFromClaim(bearerToken) +
                    " attempted to add a Book , disallowed because user is not admin");
            throw new LibraryResourceUnauthorizedException(traceId," User not allowed to add a Book");
        }
        logger.debug("Added TraceId: {} ",traceId);
        bookService.updateBook(book,traceId);

        logger.debug("Returning  reponse for TraceId: {} ",traceId);
        return new ResponseEntity<>(book,HttpStatus.OK);
    }

    @DeleteMapping(path="/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Integer bookId,
                                        @RequestHeader(value = "Trace-Id",defaultValue = "") String traceId,
                                        @RequestHeader(value = "Authorization") String bearerToken)
            throws LibraryResourceUnauthorizedException, LibraryResourceNotFoundException{

        if(!LibraryApiUtils.doesStringValueExist(traceId)){
            traceId = UUID.randomUUID().toString();
        }
        if(!LibraryApiUtils.isUserAdmin(bearerToken)){
            logger.error(LibraryApiUtils.getUserIdFromClaim(bearerToken) +
                    " attempted to add a Book , disallowed because user is not admin");
            throw new LibraryResourceUnauthorizedException(traceId," User not allowed to add a Book");
        }
        logger.debug("Added TraceId: {} ",traceId);
        bookService.deleteBook(bookId,traceId);
        logger.debug("Returning  reponse for TraceId: {} ",traceId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping(path = "/{bookId}/authors")
    public ResponseEntity<?> addBookAuthors(@PathVariable Integer bookId,
                                            @RequestBody Set<Integer> authorIds,
                                            @RequestHeader(value = "Trace-Id",defaultValue = "") String traceId,
                                            @RequestHeader(value = "Authorization") String bearerToken)
            throws LibraryResourceUnauthorizedException,
            LibraryResourceNotFoundException, LibraryResourceBadRequestException {

        if(!LibraryApiUtils.doesStringValueExist(traceId)){
            traceId = UUID.randomUUID().toString();
        }

        if(authorIds == null || authorIds.size() == 0){
            logger.error("TraceId: {}, Please supply at lease one author to be added");
            throw new LibraryResourceBadRequestException(traceId,"Please supply at lease one author to be added");
        }
        if(!LibraryApiUtils.isUserAdmin(bearerToken)){
            logger.error(LibraryApiUtils.getUserIdFromClaim(bearerToken) +
                    " attempted to add a Book , disallowed because user is not admin");
            throw new LibraryResourceUnauthorizedException(traceId," User not allowed to add a Book");
        }
        logger.debug("Added TraceId: {} ",traceId);

        logger.debug("Returning  reponse for TraceId: {} ",traceId);
        return new ResponseEntity<>(bookService.addBookAuthors(bookId,authorIds,traceId),HttpStatus.OK);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<?> searchBookByTitle(@RequestParam String title,
                                               @RequestHeader(value = "Trace-Id",defaultValue = "") String traceId)
                        throws LibraryResourceBadRequestException {
        if(!LibraryApiUtils.doesStringValueExist(traceId)){
            traceId = UUID.randomUUID().toString();
        }
        if(!LibraryApiUtils.isUserAdmin(title)){
            logger.error("TraceId: {},  Plese enter a name to search Book !!!" , traceId);
            throw new LibraryResourceBadRequestException(traceId," Plese enter a name to search Book.");
        }

        logger.debug("Returning  reponse for TraceId: {} ",traceId);
        return new ResponseEntity<>(bookService.searchBookByTitle(title,traceId),HttpStatus.OK);
    }
}
