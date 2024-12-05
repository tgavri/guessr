package kea.guessr.Repository;

import kea.guessr.Model.Daily;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DailyRepository extends JpaRepository<Daily, Long> {
    Optional<Daily> findByGameNameAndDate(String gameName, String date);
}