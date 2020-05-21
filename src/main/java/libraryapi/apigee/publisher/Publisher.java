package libraryapi.apigee.publisher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Dowlath
 * @create 5/20/2020 2:15 PM
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Publisher {
    private Integer publisherId;
    private String name;
    private String emailId;
    private String phoneNumber;
}