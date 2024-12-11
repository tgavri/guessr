package kea.guessr.Service;

import kea.guessr.Model.ImageGuessrDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ImageGuessrService {

    private final List<Integer> usedIndices = new ArrayList<>();
    private final Random random = new Random();
    private int totalPoints;

    private List<JsonNode> jsonObjects = null;

    public ImageGuessrDTO getImageGuessrDTO(String filePath) throws IOException {
        if (jsonObjects == null) {
            loadJsonObjects(filePath);
        }

        if (usedIndices.size() == jsonObjects.size()) {
            throw new RuntimeException("All images have been used!");
        }

        int randomIndex;
        do {
            randomIndex = random.nextInt(jsonObjects.size());
        } while (usedIndices.contains(randomIndex));

        usedIndices.add(randomIndex);
        JsonNode chosenObject = jsonObjects.get(randomIndex);

        String imageUrl = chosenObject.get("url").asText();
        int imageYear = chosenObject.get("year").asInt();
        String imageDesc = chosenObject.get("desc").asText();
        System.out.println(imageYear);

        return new ImageGuessrDTO(imageUrl, imageYear, imageDesc);
    }

    private void loadJsonObjects(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new File(filePath));

        if (jsonNode.isArray()) {
            jsonObjects = new ArrayList<>();
            jsonNode.forEach(jsonObjects::add);
        } else {
            throw new IOException("Invalid JSON format: Root is not an array.");
        }
    }

    public void addPoints(int points) {
        totalPoints += points;
        System.out.println("Total Points: " + totalPoints);
    }

    public int getTotalPoints() {
        return totalPoints;
    }
}
