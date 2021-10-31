package shah.springjwt2.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shah.springjwt2.service.UserService;
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    @GetMapping("/users")
    public ResponseEntity<?>getUsers() {
        return new ResponseEntity<Object>(userService.getAllUsers(), HttpStatus.OK);
    }
}
