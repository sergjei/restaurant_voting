package topjava.restaurantvoting;



import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class AppConfig {

    @Bean(initMethod = "start",destroyMethod = "stop")
    public Server h2Server() throws SQLException{
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }

}
