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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@RestController()
@RequestMapping("/api/v1/room/{room_id}/user")
@Validated
public class UserController {

    private static final Logger logger = LogManager.getLogger("serverLog");
    private static final Logger eventLogger = LogManager.getLogger("eventLog");

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
    public ResponseEntity<UserInfo> getUserInfoById(@PathVariable("room_id") @NotNull @NotEmpty String roomId,
                                                    @PathVariable("user_id") @NotNull @NotEmpty String userId,
                                                    @RequestHeader("Authorization") @Pattern(regexp = "Bearer [a-zA-Z0-9]{128}") String authorization,
                                                    HttpServletRequest request) {

        logger.trace("getUserById called");

        authorizationHelper.checkAuthorization(
            roomId,
            authorization,
            request.getRemoteAddr(),
            new All(
                new BelongsToRoom()
            ));

        UserInfo userInfo = userRepository.getUserInfoById(userId);

        if (userInfo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        eventLogger.info("\"{}\" requested info of user \"{}\", \"{}\" in room \"{}\"", request.getRemoteAddr(), userId, userInfo.getNickname(), roomId);

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
    public ResponseEntity<List<Integer>> getUpvotesByUser(@PathVariable("room_id") @NotNull @NotEmpty String roomId,
                                                          @PathVariable("user_id") @NotNull @NotEmpty String userId,
                                                          @RequestHeader("Authorization") @Pattern(regexp = "Bearer [a-zA-Z0-9]{128}") String authorization,
                                                          HttpServletRequest request) {
        logger.trace("getUpvotesByUser called");

        authorizationHelper.checkAuthorization(
            roomId,
            authorization,
            request.getRemoteAddr(),
            new All(
                new BelongsToRoom()
            ));

        eventLogger.info("\"{}\" requested the upvotes of user \"{}\" in room \"{}\"", request.getRemoteAddr(), userId, roomId);

        return new ResponseEntity<>(upvoteRepository.getUpvotesForUser(roomId, userId), HttpStatus.OK);
    }


    /**
     * Get all Users in a Room.
     * @param roomId The roomId of the Room of the requested Users.
     * @param authorization The Authorization Header of the one who sent the request.
     * @param request The HttpServletRequest containing the Ip of the one who sent the request.
     * @return A {@link ResponseEntity} containing a relevant Http Status and a list of UserInfo.
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserInfo>> getAllUsersInRoom(@PathVariable("room_id") @NotNull @NotEmpty String roomId,
                                                            @RequestHeader("Authorization") @Pattern(regexp = "Bearer [a-zA-Z0-9]{128}") String authorization,
                                                            HttpServletRequest request) {

        logger.trace("getAllUserInRoom called");

        authorizationHelper.checkAuthorization(
            roomId,
            authorization,
            request.getRemoteAddr(),
            new All(
                new BelongsToRoom()
            ));

        List<UserInfo> users = userRepository.getAllUsersInRoom(roomId);

        if (users == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        eventLogger.info("\"{}\" requested all users in room \"{}\"", request.getRemoteAddr(), roomId);

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
    @PostMapping("/{user_id}/ban")
    public ResponseEntity<Void> banUserById(@PathVariable("room_id") @NotNull @NotEmpty String roomId,
                                            @PathVariable("user_id") @NotNull @NotEmpty String userId,
                                            @RequestBody @NotNull @Valid BanReason reason,
                                            @RequestHeader("Authorization") @Pattern(regexp = "Bearer [a-zA-Z0-9]{128}") String authorization,
                                            HttpServletRequest request) {

        logger.trace("banUserById called");

        authorizationHelper.checkAuthorization(
            roomId,
            authorization,
            request.getRemoteAddr(),
            new All(
                new IsModerator(),
                new NotBanned()
            ));

        User user = userRepository.getUserById(userId);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (reason.getReason().equalsIgnoreCase("")) {
            reason.setReason(DEFAULT_BAN_REASON);
        }

        int linesChanged = banRepository.banUser(roomId, user.getIp(), reason.getReason());

        if (linesChanged == 1) {

            eventLogger.info("\"{}\" banned user \"{}\", \"{}\" for reason \"{}\" in room \"{}\"",
                    request.getRemoteAddr(), userId, user.getNickname(), reason, roomId);

            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
