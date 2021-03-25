package nl.tudelft.oopp.g7.server.controllers;

import nl.tudelft.oopp.g7.common.BanReason;
import nl.tudelft.oopp.g7.common.User;
import nl.tudelft.oopp.g7.common.UserInfo;
import nl.tudelft.oopp.g7.server.repositories.BanRepository;
import nl.tudelft.oopp.g7.server.repositories.UserRepository;
import nl.tudelft.oopp.g7.server.utility.authorization.AuthorizationHelper;
import nl.tudelft.oopp.g7.server.utility.authorization.conditions.All;
import nl.tudelft.oopp.g7.server.utility.authorization.conditions.BelongsToRoom;
import nl.tudelft.oopp.g7.server.utility.authorization.conditions.IsModerator;
import nl.tudelft.oopp.g7.server.utility.authorization.conditions.NotBanned;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController()
@RequestMapping("/api/v1/room/{room_id}/user")
public class UserController {
    Logger logger = LoggerFactory.getLogger(RoomController.class);

    private static final String DEFAULT_BAN_REASON = "You have been banned from this room!";

    private final UserRepository userRepository;
    private final BanRepository banRepository;
    private final AuthorizationHelper authorizationHelper;

    public UserController(UserRepository userRepository, BanRepository banRepository, AuthorizationHelper authorizationHelper) {
        this.userRepository = userRepository;
        this.banRepository = banRepository;
        this.authorizationHelper = authorizationHelper;
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<UserInfo> getUserInfoById(@PathVariable("room_id") String roomId,
                                                    @PathVariable("user_id") String userId,
                                                    @RequestHeader("Authorization") String authorization,
                                                    HttpServletRequest request) {
        logger.trace("getUserById called");
        if (!authorizationHelper.isAuthorized(
                roomId,
                authorization,
                request.getRemoteAddr(),
                new All(
                        new BelongsToRoom()
                )
        )) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UserInfo userInfo = userRepository.getUserInfoById(userId);

        if (userInfo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(userInfo, HttpStatus.OK);


    }

    @GetMapping("/all")
    public ResponseEntity<List<UserInfo>> getAllUsersInRoom(@PathVariable("room_id") String roomId,
                                                            @RequestHeader("Authorization") String authorization,
                                                            HttpServletRequest request) {
        logger.trace("getAllUserInRoom called");

        if (!authorizationHelper.isAuthorized(
                roomId,
                authorization,
                request.getRemoteAddr(),
                new All(
                        new BelongsToRoom()
                )
        )) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<UserInfo> users = userRepository.getAllUsersInRoom(roomId);

        if (users == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(users, HttpStatus.OK);

    }


    @DeleteMapping("/{user_id}")
    public ResponseEntity<Void> banUserById(@PathVariable("room_id") String roomId,
                                            @PathVariable("user_id") String userId,
                                            @RequestBody BanReason reason,
                                            @RequestHeader("Authorization") String authorization,
                                            HttpServletRequest request) {

        logger.trace("banUserById called");

        if (!authorizationHelper.isAuthorized(
                roomId,
                authorization,
                request.getRemoteAddr(),
                new All(
                        new IsModerator(),
                        new NotBanned()
                )
        )) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User user = userRepository.getUserById(userId);

        if (reason.getReason() == null || reason.getReason().equalsIgnoreCase("")) {
            reason.setReason(DEFAULT_BAN_REASON);
        }

        if(userId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        int linesChanged = banRepository.banUser(roomId, user.getIp(), reason.getReason());

        if (linesChanged == 1) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
