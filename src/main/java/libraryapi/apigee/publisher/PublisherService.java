package libraryapi.apigee.publisher;

import libraryapi.apigee.exception.LibraryResourceAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * @Author Dowlath
 * @create 5/20/2020 11:53 PM
 */
@Service
public class PublisherService {

    @Autowired
    private PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher addPublisher(Publisher publisherToBeAdded)
                    throws LibraryResourceAlreadyExistsException {

        PublisherEntity publisherEntity = new PublisherEntity(
                publisherToBeAdded.getName(),
                publisherToBeAdded.getEmailId(),
                publisherToBeAdded.getPhoneNumber()
        );

        PublisherEntity addedPublisher = null;

        try {
            addedPublisher = publisherRepository.save(publisherEntity);
        }catch(DataIntegrityViolationException e){
            throw new LibraryResourceAlreadyExistsException("Publisherd already exists!!!");
        }
        publisherToBeAdded.setPublisherId(addedPublisher.getPublisherId());
        return publisherToBeAdded;
    }
}
