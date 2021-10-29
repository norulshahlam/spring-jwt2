package shah.springjwt2.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shah.springjwt2.model.Role;
import shah.springjwt2.model.User;
import shah.springjwt2.repository.RoleRepo;
import shah.springjwt2.repository.UserRepo;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    public ResponseEntity<?> getAllUsers(){
        return new ResponseEntity<Object>(userRepo.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<?> saveUser(User user) {
        log.info("Saving new user {} to the database", user.getName());
        return new ResponseEntity<Object>(userRepo.save(user), HttpStatus.CREATED);
    }

    public ResponseEntity<?> saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return new ResponseEntity<Object>(roleRepo.save(role), HttpStatus.CREATED);
    }

    public ResponseEntity<?> addRoleToUser(String username, String rolename){
        log.info("Adding role {} to user {}", rolename, username);
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(rolename);

        return new ResponseEntity<Object>(user.getRoles().add(role), HttpStatus.CREATED);
    }
}
