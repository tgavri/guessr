package kea.guessr.Repository;
import kea.guessr.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    int countByRegistrationDateAfter(LocalDateTime date);
    long countByStatus(String status);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByMmrGreaterThanEqual(int mmr);
}