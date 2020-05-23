package libraryapi.apigee.user;

import java.time.LocalDate;
import java.util.Collection;

import libraryapi.apigee.model.common.Gender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @Author Dowlath
 * @create 5/23/2020 12:26 PM
 */
public class User implements UserDetails {

    private Integer userId;

    @Size(min =1,max = 50,message = "User Name must be between 1 and 50 characters")
    private String userName;

    @Size(min =8,max = 28,message = "Password must be between 1 and 50 characters")
    private String password;

    @Size(min =1,max = 50,message = "First Name must be between 1 and 50 characters")
    private String firstName;

    @Size(min =1,max = 50,message = "Last Name must be between 1 and 50 characters")
    private String lastName;

    @Past(message = "Date of birth must be a past date")
    private LocalDate dateOfBirth;

    private Gender genter;

    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{3}" , message = "Please enter phone number in format123-456-789")
    private String phoneNumber;

    @Email(message = "Please enter a valid EmailId")
    private String emailId;

    private Role role;

    public User() {
    }

    public User(Integer userId,String userName, String password,String firstName,
                String lastName,LocalDate dateOfBirth, Gender genter,String phoneNumber,
                String emailId, Role role) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.genter = genter;
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
        this.role = role;
    }


    public User(Integer userId,
                String userName,String firstName,String lastName,
                LocalDate dateOfBirth, Gender genter,String phoneNumber,
                String emailId, Role role) {
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.genter = genter;
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
        this.role = role;
    }

    public User( String userName,String firstName,String lastName,
                LocalDate dateOfBirth, Gender genter,String phoneNumber,
                String emailId) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.genter = genter;
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
    }

    public User( String userName,String firstName,String lastName ) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;

    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
       return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Gender getGenter() {
        return genter;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", genter=" + genter +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", emailId='" + emailId + '\'' +
                ", role=" + role +
                '}';
    }
}
