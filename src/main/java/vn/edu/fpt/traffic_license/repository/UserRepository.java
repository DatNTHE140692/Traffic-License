package vn.edu.fpt.traffic_license.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.traffic_license.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByIdentificationNumber(String identificationNo);
    User findByUsernameAndIdentificationNumber(String username, String identificatioNo);
}
