package libraryapi.apigee.publisher;

import libraryapi.apigee.exception.LibraryResourceAlreadyExistsException;
import libraryapi.apigee.exception.LibraryResourceNotFoundException;
import libraryapi.apigee.util.LibraryApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Dowlath
 * @create 5/20/2020 2:13 PM
 */
@RestController
@RequestMapping(path="/v1/{publishers}")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping(path="/{publisherId}")
    public ResponseEntity<?> getPublisher(@PathVariable Integer publisherId){
        Publisher publisher = null;
        try{
            publisher = publisherService.getPublisher(publisherId);
        }catch(LibraryResourceNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
       return new ResponseEntity<>(publisher,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addPublisher(@RequestBody Publisher publisher){
        try {
            publisherService.addPublisher(publisher);
        }
        catch(LibraryResourceAlreadyExistsException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(publisher,HttpStatus.CREATED);
    }

    @PutMapping (path="/{publisherId}")
    public ResponseEntity<?> updatePublisher(@PathVariable Integer publisherId,@RequestBody Publisher publisher){
        try{
            publisher.setPublisherId(publisherId);
            publisherService.updatePublisher(publisher);
        }catch(LibraryResourceNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(publisher,HttpStatus.OK);
    }

    @DeleteMapping(path="/{publisherId}")
    public ResponseEntity<?> deletePublisher(@PathVariable Integer publisherId){

        try{
           publisherService.deletePublisher(publisherId);
        }catch(LibraryResourceNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(path="/search")
    public ResponseEntity<?> searchPublisher(@RequestParam String name){
        if(!LibraryApiUtils.doesStringValueExist(name)){
            return new ResponseEntity<>("Please enter a name to search publisher",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(publisherService.searchPublisher(name),HttpStatus.OK);
    }

}
