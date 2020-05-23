package libraryapi.apigee.security;

import libraryapi.apigee.exception.LibraryResourceNotFoundException;
import libraryapi.apigee.user.User;
import libraryapi.apigee.user.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Author Dowlath
 * @create 5/23/2020 9:29 PM
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

     public UserService userService;

     public UserDetailsServiceImpl(UserService userService){
         this.userService = userService;
     }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = null;
        try{
            user = userService.getUserByUserName(userName);
        }catch(LibraryResourceNotFoundException e){
            throw new UsernameNotFoundException(e.getMessage());
        }
        return user;
    }
}
