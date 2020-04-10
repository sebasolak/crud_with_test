package com.sebastian.learningspringboot.resource;

import com.sebastian.learningspringboot.model.User;
import com.sebastian.learningspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.sebastian.learningspringboot.model.User.Gender.*;

//@RestController
//@RequestMapping(
//        path = "api/v1/users"
//)
public class UserResourceSpringMVC {

    private UserService userService;

    @Autowired
    public UserResourceSpringMVC(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<User> fetchUsers(@QueryParam("gender") String gender) {
        return userService.getAllUsers(Optional.ofNullable(gender));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "{userUid}"
    )
    public ResponseEntity<?> fetchUser(
            @PathVariable("userUid") UUID userUid) {
//        Optional<User> userOptional = userService.getUser(userUid);
//        if (userOptional.isPresent()) {
//            return ResponseEntity.ok(userOptional.get());
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(new ErrorMessage("user "+userUid+" was not found."));-> like funcional

        return userService.getUser(userUid).<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorMessage("user " + userUid + " was not found.")));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Integer> insertNewUser(@RequestBody User user) {
        int result = userService.insertUser(user);
        return getIntegerResponseEntity(result);
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Integer> updateUser(@RequestBody User user) {
        int result = userService.updateUser(user);
        return getIntegerResponseEntity(result);
    }

    @RequestMapping(
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "{userUid}"
    )
    public ResponseEntity<Integer> deleteUser(
            @PathVariable("userUid") UUID userUid) {
        int result = userService.removeUser(userUid);
        return getIntegerResponseEntity(result);
    }


    ////////////////////////////
    private ResponseEntity<Integer> getIntegerResponseEntity(int result) {
        if (result == 1) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }




    @PostMapping(path = "test")
    public List<User> addTestUsers() {
        User ricardo = new User(
                UUID.randomUUID(),
                "Ricardo",
                "Silva",
                MALE,
                67,
                "ricardo.silva@hotmail.com"
        );
        User tiara = new User(
                UUID.randomUUID(),
                "Tiara",
                "Jupiter",
                FEMALE,
                20,
                "tiara.jupiter@gmail.com"
        );
        User carol = new User(
                UUID.randomUUID(),
                "Carol",
                "Jacob",
                FEMALE,
                20,
                "carol.jacob@gmail.com"
        );
        User anna = new User(
                UUID.randomUUID(),
                "Anna",
                "Montana",
                FEMALE,
                40,
                "anna.montana@gmail.com"
        );

        userService.insertUser(ricardo);
        userService.insertUser(tiara);
        userService.insertUser(carol);
        userService.insertUser(anna);
        return  userService.getAllUsers(Optional.empty());
    }
}
