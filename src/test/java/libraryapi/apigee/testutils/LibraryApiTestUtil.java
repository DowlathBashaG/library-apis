package libraryapi.apigee.testutils;

import libraryapi.apigee.publisher.Publisher;
import libraryapi.apigee.publisher.PublisherEntity;

import java.util.Optional;

/**
 * @Author Dowlath
 * @create 5/22/2020 4:10 PM
 */

public class LibraryApiTestUtil {

    public static Publisher createPublisher(){
        return new Publisher(null,
                              TestConstants.TEST_PUBLISHER_NAME,
                              TestConstants.TEST_PUBLISHER_EMAIL,
                              TestConstants.TEST_PUBLISHER_PHONE);
    }

    public static PublisherEntity createPublisherEntity() {
        return new PublisherEntity(
                TestConstants.TEST_PUBLISHER_NAME,
                TestConstants.TEST_PUBLISHER_EMAIL,
                TestConstants.TEST_PUBLISHER_PHONE);
    }

    public static Optional<PublisherEntity> createPublisherEntityOptional() {
        return Optional.of(createPublisherEntity());
    }
}
