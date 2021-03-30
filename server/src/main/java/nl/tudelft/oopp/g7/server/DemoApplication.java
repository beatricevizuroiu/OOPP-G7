package nl.tudelft.oopp.g7.server;

import nl.tudelft.oopp.g7.server.repositories.*;
import nl.tudelft.oopp.g7.server.utility.Config;
import org.hibernate.sql.ordering.antlr.OrderingSpecification;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;

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

    @Bean("speedRepository")
    @DependsOn({"userRepository", "roomRepository"})
    public SpeedRepository speedRepository(JdbcTemplate jdbcTemplate) {
        return new SpeedRepository(jdbcTemplate);
    }

    @Bean("questionRepository")
    @DependsOn({"userRepository", "roomRepository"})
    public QuestionRepository questionRepository(JdbcTemplate jdbcTemplate) {
        return new QuestionRepository(jdbcTemplate);
    }

    @Bean("voteRepository")
    @DependsOn({"userRepository", "roomRepository", "questionRepository"})
    public UpvoteRepository upvoteRepository(JdbcTemplate jdbcTemplate) {
        return new UpvoteRepository(jdbcTemplate);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        new Config(new File("./config.yml"));

        SpringApplication.run(DemoApplication.class, args);
    }

}
