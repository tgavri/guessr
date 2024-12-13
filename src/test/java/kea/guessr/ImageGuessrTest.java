package kea.guessr;

import kea.guessr.Model.ImageGuessrDTO;
import kea.guessr.Service.ImageGuessrService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ImageGuessrTest {

    private ImageGuessrService imageGuessrService;

    @BeforeEach
    void setUp() {
        imageGuessrService = new ImageGuessrService();
    }

    @Test
    void testGetImageGuessrDTO_Success() throws IOException {
        // Arrange
        File mockFile = ResourceUtils.getFile("src/main/resources/static/data/imagejson.json");
        System.out.println("File Path: " + mockFile.getAbsolutePath());
        System.out.println("File Exists: " + mockFile.exists());

        // Act
        ImageGuessrDTO result = imageGuessrService.getImageGuessrDTO(mockFile.getAbsolutePath());

        // Assert
        assertNotNull(result);
        assertNotNull(result.getUrl());
        assertTrue(result.getYear() > 0);
        assertNotNull(result.getDesc());
    }

    @Test
    void testGetImageGuessrDTO_AllImagesUsed() throws IOException {
        // Arrange
        File mockFile = ResourceUtils.getFile("src/main/resources/static/data/imagejson.json");
        assertTrue(mockFile.exists(), "The file does not exist at the given path.");

        try {
            while (true) {
                imageGuessrService.getImageGuessrDTO(mockFile.getAbsolutePath());
            }
        } catch (RuntimeException e) {
            // Assert
            System.out.println("Caught exception: " + e.getMessage());
            assertEquals("All images have been used!", e.getMessage());
        }
    }

    @Test
    void testAddPoints() {
        // Act
        imageGuessrService.addPoints(10);

        // Assert
        assertEquals(10, imageGuessrService.getTotalPoints());
    }

    @Test
    void testGetTotalPoints() {
        // Arrange
        imageGuessrService.addPoints(5);
        imageGuessrService.addPoints(15);

        // Act
        int totalPoints = imageGuessrService.getTotalPoints();

        // Assert
        assertEquals(20, totalPoints);
    }
}
