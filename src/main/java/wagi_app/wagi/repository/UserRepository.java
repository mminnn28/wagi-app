package wagi_app.wagi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wagi_app.wagi.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(String userId);
}
