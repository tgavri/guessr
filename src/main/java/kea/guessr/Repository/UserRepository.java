package kea.guessr.Repository;
import kea.guessr.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    int countByRegistrationDateAfter(LocalDateTime date);
    long countByStatus(String status);

}