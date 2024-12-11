package kea.guessr;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class RunningGameTest {
    private Character character;
    private List<Obstacle> obstacles;
    private int gameSpeed;
    private int score;
    private boolean isGameOver;

    private class Character {
        private double x;
        private double y;
        private double width;
        private double height;
        private double velocityY;
        private double gravity;
        private boolean jumping;

        public Character(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.velocityY = 0;
            this.gravity = 0.8;
            this.jumping = false;
        }

        public void jump() {
            if (!jumping) {
                velocityY = -15;
                jumping = true;
            }
        }

        public void applyGravity() {
            y += velocityY;
            velocityY += gravity;
        }


    }

    private class Obstacle {
        private double x;
        private double y;
        private double width;
        private double height;
        private ObstacleType type;

        public Obstacle(double x, double y, double width, double height, ObstacleType type) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.type = type;
        }
    }

    private enum ObstacleType {
        SQUARE, TALL, FLYING, MULTIPLE
    }

    @BeforeEach
    public void setUp() {
        character = new Character(50, 300, 30, 50);
        obstacles = new ArrayList<>();
        gameSpeed = 4;
        score = 0;
        isGameOver = false;
    }

    @Test
    public void testCharacterJump() {
        // Initial state
        assertFalse(character.jumping);
        assertEquals(0, character.velocityY);

        // Perform jump
        character.jump();

        // Verify jump state
        assertTrue(character.jumping);
        assertEquals(-15, character.velocityY);
    }

    @Test
    public void testCharacterGravity() {
        double initialY = character.y;
        character.jump();
        character.applyGravity();

        // Verify gravity effect
        assertTrue(character.y < initialY);
        assertTrue(character.velocityY > -15);
    }

    @Test
    public void testObstacleCreation() {
        // Test creating different types of obstacles
        for (ObstacleType type : ObstacleType.values()) {
            Obstacle obstacle = createObstacle(type);
            obstacles.add(obstacle);
        }

        assertEquals(4, obstacles.size());
    }

    @Test
    public void testCollisionDetection() {
        // Create an obstacle that overlaps with character
        Obstacle obstacle = new Obstacle(50, 300, 30, 50, ObstacleType.SQUARE);

        boolean collision = checkCollision(character, obstacle);

        assertTrue(collision);
    }

    @Test
    public void testGameSpeedIncrease() {
        int initialSpeed = gameSpeed;
        increaseGameSpeed();

        assertTrue(gameSpeed > initialSpeed);
    }

    @Test
    public void testGameOver() {
        // Simulate collision
        Obstacle obstacle = new Obstacle(50, 300, 30, 50, ObstacleType.SQUARE);
        isGameOver = checkCollision(character, obstacle);

        assertTrue(isGameOver);
    }

    @Test
    public void testScoreIncrement() {
        int initialScore = score;
        incrementScore();

        assertTrue(score > initialScore);
    }

    private Obstacle createObstacle(ObstacleType type) {
        switch (type) {
            case SQUARE:
                return new Obstacle(400, 250, 30, 30, ObstacleType.SQUARE);
            case TALL:
                return new Obstacle(400, 200, 20, 80, ObstacleType.TALL);
            case FLYING:
                return new Obstacle(400, 150, 20, 20, ObstacleType.FLYING);
            case MULTIPLE:
                return new Obstacle(400, 100, 40, 40, ObstacleType.MULTIPLE);
            default:
                throw new IllegalArgumentException("Unknown obstacle type");
        }
    }

    private boolean checkCollision(Character character, Obstacle obstacle) {
        return character.x < obstacle.x + obstacle.width &&
                character.x + character.width > obstacle.x &&
                character.y < obstacle.y + obstacle.height &&
                character.y + character.height > obstacle.y;
    }

    private void increaseGameSpeed() {
        gameSpeed += 1;
    }

    private void incrementScore() {
        score++;
    }
}
