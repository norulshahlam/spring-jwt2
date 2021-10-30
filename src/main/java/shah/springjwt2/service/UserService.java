package shah.springjwt2.service;

import java.util.ArrayList;
import java.util.Collection;

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
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;


    public ResponseEntity<?> getAllUsers(){
        System.out.println(userRepo.findAll());
        return new ResponseEntity<Object>(userRepo.findAll(), HttpStatus.OK);
    }

    public User getUser(String username) {
        log.info("Fetching user {}", username);
        return userRepo.findByUsername(username);
    }

    public ResponseEntity<?> saveUser(User user) {
        log.info("Saving new user {} to the database", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if(user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", username);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        }
    }
}
