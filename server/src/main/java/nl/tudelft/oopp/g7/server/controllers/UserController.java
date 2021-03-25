package nl.tudelft.oopp.g7.server.controllers;

import nl.tudelft.oopp.g7.common.User;
import nl.tudelft.oopp.g7.common.UserInfo;
import nl.tudelft.oopp.g7.server.repositories.BanRepository;
import nl.tudelft.oopp.g7.server.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/room/{room_id}/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(RoomController.class);

    private final UserRepository userRepository;
    private final BanRepository banRepository;

    public UserController(UserRepository userRepository, BanRepository banRepository) {
        this.userRepository = userRepository;
        this.banRepository = banRepository;
    }


    @GetMapping("/{user_id}")
    public ResponseEntity<UserInfo> getUserById(@PathVariable("user_id") String userId) {
        logger.trace("getUserById called");
        //TODO(Authentication)

        UserInfo userInfo = userRepository.getUserById(userId);

        if (userInfo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(userInfo, HttpStatus.OK);


    }

    @GetMapping("/all")
    public ResponseEntity<List<UserInfo>> getAllUsersInRoom(@PathVariable("room_id") String roomId) {
        logger.trace("getAllUserInRoom called");

        //TODO(Authentication)

        List<UserInfo> users = userRepository.getAllUsersInRoom(roomId);

        if (users == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(users, HttpStatus.OK);

    }


    @DeleteMapping("/{user_id}")
    public ResponseEntity<Void> banUserById(@PathVariable("user_id") String userId) {
        logger.trace("banUserById called");
        //TODO(Authentication)

        if(userId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
