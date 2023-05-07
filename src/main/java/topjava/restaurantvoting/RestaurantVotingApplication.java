package topjava.restaurantvoting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import topjava.restaurantvoting.repository.RestaurantRepository;
import topjava.restaurantvoting.repository.UserRepository;

@SpringBootApplication
public class RestaurantVotingApplication extends SpringBootServletInitializer implements ApplicationRunner {
    @Autowired
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public RestaurantVotingApplication(UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(RestaurantVotingApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(userRepository.findAll());
        System.out.println(restaurantRepository.getTodayMenu());
    }
}
