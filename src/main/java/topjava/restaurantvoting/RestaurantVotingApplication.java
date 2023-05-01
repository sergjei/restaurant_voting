package topjava.restaurantvoting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import topjava.restaurantvoting.model.Role;
import topjava.restaurantvoting.model.User;

import java.util.Set;

@SpringBootApplication
public class RestaurantVotingApplication implements ApplicationRunner {
    @Autowired
    private final UserRepository userRepository;

    public RestaurantVotingApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(RestaurantVotingApplication.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
//        userRepository.save(new User("User_First", "user@gmail.com", "password", Role.USER));
//        userRepository.save(new User("Admin_First", "admin@javaops.ru", "admin", Role.USER, Role.ADMIN));
        System.out.println(userRepository.findAll());
    }
}
