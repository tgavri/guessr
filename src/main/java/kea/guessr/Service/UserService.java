package kea.guessr.Service;

import kea.guessr.Model.User;
import kea.guessr.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User user) {
        System.out.println("Updating user with ID: " + id);
        System.out.println("User data before update: " + user);

        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            // Update the fields
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setStatus(user.getStatus());

            // Log the updated data before saving
            System.out.println("Updated user before save: " + existingUser);

            // Save to database
            User updatedUser = userRepository.save(existingUser);

            // Log the saved data
            System.out.println("User saved successfully: " + updatedUser);
            return updatedUser;
        } else {
            System.out.println("User with ID " + id + " not found.");
            return null;
        }
    }
    public Map<String, Object> getUserStatusDistribution() {
        List<User> users = userRepository.findAll();
        Map<String, Long> statusCounts = users.stream()
                .collect(Collectors.groupingBy(User::getStatus, Collectors.counting()));

        List<String> labels = new ArrayList<>(statusCounts.keySet());
        List<Long> data = new ArrayList<>(statusCounts.values());

        Map<String, Object> result = new HashMap<>();
        result.put("labels", labels);
        result.put("data", data);

        return result;
    }

    public Map<String, Object> getUserGrowthData() {
        List<User> users = userRepository.findAll();
        Map<LocalDate, Long> userCounts = users.stream()
                .collect(Collectors.groupingBy(user -> user.getRegistrationDate().toLocalDate(),
                        TreeMap::new, Collectors.counting()));

        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();

        userCounts.forEach((date, count) -> {
            labels.add(date.toString());
            data.add(count);
        });

        Map<String, Object> growthData = new HashMap<>();
        growthData.put("labels", labels);
        growthData.put("data", data);

        return growthData;
    }




    public int getNewSignUpsLastWeek() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return (int) userRepository.countByRegistrationDateAfter(oneWeekAgo);
    }


    public long getActiveUserCount() {
       return userRepository.countByStatus("active");
    }

    public long getTotalUserCount() {
        return userRepository.count();
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

