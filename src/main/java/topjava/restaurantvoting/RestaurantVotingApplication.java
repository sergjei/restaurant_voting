package topjava.restaurantvoting;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("topjava.restaurantvoting.repository")
@EntityScan("topjava.restaurantvoting.model")
public class RestaurantVotingApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantVotingApplication.class, args);
    }
}
