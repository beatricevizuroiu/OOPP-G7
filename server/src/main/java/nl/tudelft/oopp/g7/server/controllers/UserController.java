package nl.tudelft.oopp.g7.server.controllers;

import nl.tudelft.oopp.g7.common.BanReason;
import nl.tudelft.oopp.g7.common.User;
import nl.tudelft.oopp.g7.common.UserInfo;
import nl.tudelft.oopp.g7.server.repositories.BanRepository;
import nl.tudelft.oopp.g7.server.repositories.UpvoteRepository;
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
    private final UpvoteRepository upvoteRepository;

    /**
     * The primary constructor for the UserController.
     */
    public UserController(UserRepository userRepository, BanRepository banRepository, AuthorizationHelper authorizationHelper, UpvoteRepository upvoteRepository) {
        this.userRepository = userRepository;
        this.banRepository = banRepository;
        this.authorizationHelper = authorizationHelper;
        this.upvoteRepository = upvoteRepository;
    }

    /**
     * Get the information of a User by UserId.
     * @param roomId The roomId of the Room the User belongs to.
     * @param userId The userId of the User.
     * @param authorization The raw Authorization Header of the one who sent the request.
     * @param request The HttpServletRequest containing the Ip of the one who sent the request.
     * @return A {@link ResponseEntity} containing a relevant Http Status and the UserInfo of the requested User.
     */
    @GetMapping("/{user_id}")
    public ResponseEntity<UserInfo> getUserInfoById(@PathVariable("room_id") String roomId,
                                                    @PathVariable("user_id") String userId,
                                                    @RequestHeader("Authorization") String authorization,
                                                    HttpServletRequest request) {
        logger.trace("getUserById called");

        // Check if the room id field is set.
        if (roomId == null || roomId.equals("")) {
            // Inform that client that they did something wrong.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Check if the user id field is set.
        if (userId == null || userId.equals("")) {
            // Inform that client that they did something wrong.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

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

    /**
     * Get the Questions a specific user has upvoted.
     * @param roomId The roomId of the Room the User, Questions and Upvotes belong to.
     * @param userId The userId of the requested User.
     * @param authorization The Authorization Header of the one who sent the request.
     * @param request The HttpServletRequest containing the Ip of the one who sent the request.
     * @return A {@link ResponseEntity} containing a relevant Http Status and a list of the questionIds of the Questions the User has upvoted.
     */
    @GetMapping("/{user_id}/upvotes")
    public ResponseEntity<List<Integer>> getUpvotesByUser(@PathVariable("room_id") String roomId,
                                                          @PathVariable("user_id") String userId,
                                                          @RequestHeader("Authorization") String authorization,
                                                          HttpServletRequest request) {
        logger.trace("getUpvotesByUser called");

        // Check if the room id field is set.
        if (roomId == null || roomId.equals("")) {
            // Inform that client that they did something wrong.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Check if the user id field is set.
        if (userId == null || userId.equals("")) {
            // Inform that client that they did something wrong.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

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

        return new ResponseEntity<List<Integer>>(upvoteRepository.getUpvotesForUser(roomId,userId), HttpStatus.OK);
    }


    /**
     * Get all Users in a Room.
     * @param roomId The roomId of the Room of the requested Users.
     * @param authorization The Authorization Header of the one who sent the request.
     * @param request The HttpServletRequest containing the Ip of the one who sent the request.
     * @return A {@link ResponseEntity} containing a relevant Http Status and a list of UserInfo.
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserInfo>> getAllUsersInRoom(@PathVariable("room_id") String roomId,
                                                            @RequestHeader("Authorization") String authorization,
                                                            HttpServletRequest request) {
        logger.trace("getAllUserInRoom called");

        // Check if the room id field is set.
        if (roomId == null || roomId.equals("")) {
            // Inform that client that they did something wrong.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

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

    /**
     * Ban a User from interacting with a Room.
     * @param roomId The roomId of the Room to ban the User from.
     * @param userId The userId of the User to ban.
     * @param reason The reason of banning the User.
     * @param authorization The Authorization Header of the one who sent the request.
     * @param request The HttpServletRequest containing the Ip of the one who sent the request.
     * @return A {@link ResponseEntity} containing a relevant Http Status.
     */
    @DeleteMapping("/{user_id}")
    public ResponseEntity<Void> banUserById(@PathVariable("room_id") String roomId,
                                            @PathVariable("user_id") String userId,
                                            @RequestBody BanReason reason,
                                            @RequestHeader("Authorization") String authorization,
                                            HttpServletRequest request) {

        logger.trace("banUserById called");

        // Check if the room id field is set.
        if (roomId == null || roomId.equals("")) {
            // Inform that client that they did something wrong.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Check if the user id field is set.
        if (userId == null || userId.equals("")) {
            // Inform that client that they did something wrong.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

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

        int linesChanged = banRepository.banUser(roomId, user.getIp(), reason.getReason());

        if (linesChanged == 1) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
