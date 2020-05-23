package libraryapi.apigee;

import libraryapi.apigee.model.common.Gender;
import libraryapi.apigee.user.UserEntity;
import libraryapi.apigee.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

/**
 * @Author Dowlath
 * @create 5/23/2020 9:51 PM
 */
@Component
public class ApplicationInitializer {
    BCryptPasswordEncoder bCryptPasswordEncoder;
    UserRepository userRepository;

    public ApplicationInitializer(BCryptPasswordEncoder bCryptPasswordEncoder,
                                  UserRepository userRepository){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    @Value("${library.api.user.admin.username:lib-admin}")
    String adminUserName;

    @Value("${library.api.user.admin.password:admin-password}")
    String adminPassword;

    @PostConstruct
    private void init(){
        UserEntity admin = userRepository.findByUsername(adminUserName);
        if(admin == null){
            admin = new UserEntity(adminUserName,bCryptPasswordEncoder.encode(adminPassword),
                                  "Library","Admin", LocalDate.now().minusYears(30),
                                  Gender.Female,"000-000-000","library.admin@dowlathlibrary.com","ADMIN");
            userRepository.save(admin);
        }
    }
}
