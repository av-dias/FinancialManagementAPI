package utility.protection;

import com.example.structure.userclient.UserClient;
import com.example.structure.userclient.UserService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserProtection {

    private final UserService userService;

    public UserProtection(UserService userService){
        this.userService = userService;
    }

    public Optional<UserClient> hasUser(Long userId){
        //CHECK IF USER IS DEFINED
        if(userId==null)
            throw new IllegalStateException("No user defined.");
        Optional<UserClient> user = userService.findUser(userId);
        //CHECK IF USER IS EXISTS
        if(!user.isPresent())
            throw new IllegalStateException("User does not exist.");
        return user;
    }

    public Optional<UserClient> hasUser(String userEmail){
        //CHECK IF USER IS DEFINED
        if(userEmail==null)
            throw new IllegalStateException("No user defined.");
        Optional<UserClient> user = userService.getUser(userEmail);
        //CHECK IF USER IS EXISTS
        if(!user.isPresent())
            throw new IllegalStateException("User does not exist.");
        return user;
    }
}
