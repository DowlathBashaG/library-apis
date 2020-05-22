package libraryapi.apigee.publisher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @Author Dowlath
 * @create 5/20/2020 2:15 PM
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Publisher {

    private Integer publisherId;

    @Size(min = 1, max = 50,message="Publisher name must be between 1 and 50 characters")
    private String name;

    @Email(message = "Please enter a valid email id")
    private String emailId;

    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{3}",message = "Please enter phone number is format 123-456-789")
    private String phoneNumber;

}
