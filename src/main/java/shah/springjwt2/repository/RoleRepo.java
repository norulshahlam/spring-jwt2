package shah.springjwt2.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import shah.springjwt2.model.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByName(String name);
}