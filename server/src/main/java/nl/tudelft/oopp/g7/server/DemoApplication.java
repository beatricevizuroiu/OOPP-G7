package nl.tudelft.oopp.g7.server;

import nl.tudelft.oopp.g7.server.repositories.BanRepository;
import nl.tudelft.oopp.g7.server.repositories.QuestionRepository;
import nl.tudelft.oopp.g7.server.repositories.RoomRepository;
import nl.tudelft.oopp.g7.server.repositories.UserRepository;
import org.hibernate.sql.ordering.antlr.OrderingSpecification;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class DemoApplication {

    @Bean("roomRepository")
    public RoomRepository roomRepository(JdbcTemplate jdbcTemplate) {
        return new RoomRepository(jdbcTemplate);
    }

    @Bean("userRepository")
    @DependsOn("roomRepository")
    public UserRepository userRepository(JdbcTemplate jdbcTemplate) {
        return new UserRepository(jdbcTemplate);
    }

    @Bean("banRepository")
    @DependsOn("roomRepository")
    public BanRepository banRepository(JdbcTemplate jdbcTemplate) {
        return new BanRepository(jdbcTemplate);
    }

    @Bean("questionRepository")
    @DependsOn({"userRepository", "roomRepository"})
    public QuestionRepository questionRepository(JdbcTemplate jdbcTemplate) {
        return new QuestionRepository(jdbcTemplate);
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
