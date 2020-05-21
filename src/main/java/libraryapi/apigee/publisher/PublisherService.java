package libraryapi.apigee.publisher;

import libraryapi.apigee.exception.LibraryResourceAlreadyExistsException;
import libraryapi.apigee.exception.LibraryResourceNotFoundException;
import libraryapi.apigee.util.LibraryApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public void addPublisher(Publisher publisherToBeAdded)
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
    }

    public Publisher getPublisher(Integer publisherId) throws LibraryResourceNotFoundException {
        Optional<PublisherEntity> publisherEntity = publisherRepository.findById(publisherId);
        Publisher publisher = null;
        if(publisherEntity.isPresent()){
            PublisherEntity pe = publisherEntity.get();
            publisher = createPublisherFromEntity(pe);
        }else{
            throw new LibraryResourceNotFoundException("Publisher Id: "+publisherId+" Not found");
        }
        return publisher;
    }

    public void updatePublisher(Publisher publisherToBeUpdated) throws LibraryResourceNotFoundException {
        Optional<PublisherEntity> publisherEntity = publisherRepository.findById(publisherToBeUpdated.getPublisherId());
        Publisher publisher = null;
        if(publisherEntity.isPresent()){
            PublisherEntity pe = publisherEntity.get();
            if(LibraryApiUtils.doesStringValueExist(publisherToBeUpdated.getEmailId())){
                pe.setEmailId(publisherToBeUpdated.getEmailId());
            }
            if(LibraryApiUtils.doesStringValueExist(publisherToBeUpdated.getPhoneNumber())){
                pe.setPhoneNumber(publisherToBeUpdated.getPhoneNumber());
            }
            publisherRepository.save(pe);
            publisherToBeUpdated = createPublisherFromEntity(pe);
        }else{
            throw new LibraryResourceNotFoundException("Publisher Id: "+publisherToBeUpdated.getPublisherId()+" Not found");
        }
    }

    public void deletePublisher(Integer publisherId) throws LibraryResourceNotFoundException {
        try{
            publisherRepository.deleteById(publisherId);
        }catch(EmptyResultDataAccessException e){
            throw new LibraryResourceNotFoundException("Publisher Id:"+ publisherId+" Not found");
        }

    }


    private Publisher createPublisherFromEntity(PublisherEntity pe) {
        return new Publisher(pe.getPublisherId(),pe.getName(),pe.getEmailId(),pe.getPhoneNumber());
    }



}
