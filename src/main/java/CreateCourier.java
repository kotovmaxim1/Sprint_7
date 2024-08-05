import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreateCourier {

    private String login;
    private String password;
    private String firstName;
}