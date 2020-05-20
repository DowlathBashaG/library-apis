package libraryapi.apigee.controller;

import libraryapi.apigee.model.Publisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Dowlath
 * @create 5/20/2020 2:13 PM
 */
@RestController
@RequestMapping(path="/v1/{publishers}")
public class publishercontroller {

    @GetMapping(path="/{publisherId}")
    public Publisher getPublisher(@PathVariable String publisherId){
       return new Publisher(publisherId,"BPB Publications","bpb@email.com","123-456-789");
    }
}
