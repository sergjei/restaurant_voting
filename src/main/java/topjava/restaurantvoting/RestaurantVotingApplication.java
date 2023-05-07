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

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        // Customize the application or call application.sources(...) to add sources
//        // Since our example is itself a @Configuration class (via @SpringBootApplication)
//        // we actually don't need to override this method.
//        return application;
//    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
//        userRepository.save(new User("User_First", "user@gmail.com", "password", Role.USER));
//        userRepository.save(new User("Admin_First", "admin@javaops.ru", "admin", Role.USER, Role.ADMIN));
        System.out.println(userRepository.findAll());
        System.out.println(restaurantRepository.getTodayMenu());
    }
}
