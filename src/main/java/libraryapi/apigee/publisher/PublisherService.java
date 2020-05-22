package libraryapi.apigee.publisher;

import libraryapi.apigee.exception.LibraryResourceAlreadyExistsException;
import libraryapi.apigee.exception.LibraryResourceNotFoundException;
import libraryapi.apigee.util.LibraryApiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author Dowlath
 * @create 5/20/2020 11:53 PM
 */
@Service
public class PublisherService {

    private static Logger logger = LoggerFactory.getLogger(PublisherService.class);


    private PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public void addPublisher(Publisher publisherToBeAdded, String traceId)
                    throws LibraryResourceAlreadyExistsException {

        logger.debug("TraceId: {} , Request to add Publisher: {}",traceId,publisherToBeAdded);

        PublisherEntity publisherEntity = new PublisherEntity(
                publisherToBeAdded.getName(),
                publisherToBeAdded.getEmailId(),
                publisherToBeAdded.getPhoneNumber()
        );

        PublisherEntity addedPublisher = null;

        try {
            addedPublisher = publisherRepository.save(publisherEntity);
        }catch(DataIntegrityViolationException e){
            logger.error("TraceId: {}, Publisher already exists!!!",traceId,e);
            throw new LibraryResourceAlreadyExistsException("TraceId: "+ traceId+", Publisher already exists!!!");
        }
        publisherToBeAdded.setPublisherId(addedPublisher.getPublisherId());
        logger.info("TraceId: {},Publisher added: {} ",traceId,publisherToBeAdded);
    }

    public Publisher getPublisher(Integer publisherId, String traceId) throws LibraryResourceNotFoundException {
        Optional<PublisherEntity> publisherEntity = publisherRepository.findById(publisherId);
        Publisher publisher = null;
        if(publisherEntity.isPresent()){
            PublisherEntity pe = publisherEntity.get();
            publisher = createPublisherFromEntity(pe);
        }else{
            throw new LibraryResourceNotFoundException("TraceId: "+ traceId+", Publisher Id: "+publisherId+" Not found");
        }
        return publisher;
    }

    public void updatePublisher(Publisher publisherToBeUpdated, String traceId) throws LibraryResourceNotFoundException {
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
            throw new LibraryResourceNotFoundException("TraceId: "+ traceId+", Publisher Id: "+publisherToBeUpdated.getPublisherId()+" Not found");
        }
    }

    public void deletePublisher(Integer publisherId, String traceId) throws LibraryResourceNotFoundException {
        try{
            publisherRepository.deleteById(publisherId);
        }catch(EmptyResultDataAccessException e){
            throw new LibraryResourceNotFoundException("TraceId: "+ traceId+", Publisher Id:"+ publisherId+" Not found");
        }

    }

    public List<Publisher> searchPublisher(String s, String name) {
        List<PublisherEntity> publisherEntities = null;
        if(LibraryApiUtils.doesStringValueExist(name)){
            publisherEntities = publisherRepository.findByNameContaining(name);
        }
        if(publisherEntities != null && publisherEntities.size() > 0){
            return createPublisherForSearchResponse(publisherEntities);
        }else{
            return Collections.emptyList();
        }
    }

    private List<Publisher> createPublisherForSearchResponse(List<PublisherEntity> publisherEntities) {
        return publisherEntities.stream()
                         .map(pe->createPublisherFromEntity(pe))
                         .collect(Collectors.toList());
    }

    private Publisher createPublisherFromEntity(PublisherEntity pe) {
        return new Publisher(pe.getPublisherId(),pe.getName(),pe.getEmailId(),pe.getPhoneNumber());
    }

}
