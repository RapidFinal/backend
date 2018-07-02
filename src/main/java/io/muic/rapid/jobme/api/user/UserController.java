package io.muic.rapid.jobme.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/whoami")
    public ResponseEntity whoami(Authentication auth){

        return ResponseEntity.ok(this.userService.getUserDTO(auth));
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody WebRegisterDAO webRegisterDAO){
        return ResponseEntity.ok(this.userService.createWebUser(webRegisterDAO));
    }
}
