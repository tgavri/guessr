package kea.guessr.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


// Ej færdig. blot et udkast.
/*
@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;


    public Game createGame(Game game) {
        // Se updaateGame metode
        return gameRepository.save(game);
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game getGameById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    public Game updateGame(Long id, Game updatedGame) {
        Game existingGame = gameRepository.findById(id).orElse(null);
        if (existingGame != null) {
            existingGame.setName(updatedGame.getName());
            existingGame.setImageUrl(updatedGame.getImageUrl());
            existingGame.setQuestionText(updatedGame.getQuestionText());
            existingGame.setCorrectAnswer(updatedGame.getCorrectAnswer());
            existingGame.setWrongAnswers(updatedGame.getWrongAnswers());
            existingGame.setYear(updatedGame.getYear());
            existingGame.setCategories(updatedGame.getCategories());
            existingGame.setType(updatedGame.getType());

            return gameRepository.save(existingGame);
        }
        return null;
    }

    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }
}
*/