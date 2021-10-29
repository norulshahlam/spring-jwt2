package shah.springjwt2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import shah.springjwt2.model.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}