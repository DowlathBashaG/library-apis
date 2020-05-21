package libraryapi.apigee.publisher;

import libraryapi.apigee.exception.LibraryResourceAlreadyExistsException;
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
    public Publisher getPublisher(@PathVariable Integer publisherId){
       return new Publisher(publisherId,"BPB Publications","bpb@email.com","123-456-789");
    }

    @PostMapping
    public ResponseEntity<?> addPublisher(@RequestBody Publisher publisher){
        try {
            publisher = publisherService.addPublisher(publisher);
        }
        catch(LibraryResourceAlreadyExistsException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(publisher,HttpStatus.CREATED);
    }

}
