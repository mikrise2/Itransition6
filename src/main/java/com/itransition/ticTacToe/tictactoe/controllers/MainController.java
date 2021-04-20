package com.itransition.ticTacToe.tictactoe.controllers;

import com.itransition.ticTacToe.tictactoe.models.Game;
import com.itransition.ticTacToe.tictactoe.models.Move;
import com.itransition.ticTacToe.tictactoe.repos.GameRepo;
import com.itransition.ticTacToe.tictactoe.types.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MainController {
    @Autowired
    private GameRepo gameRepo;

    @GetMapping("/")
    public String getMainPage(Map<String, Object> model) {
        List<Game> games = gameRepo.findAll();
        model.put("games", games);
        return "main";
    }

    @PostMapping(value = "/game", params = "create")
    public String createGame(Game game, @ModelAttribute("tagsString") String tags, @ModelAttribute("username") String username, Map<String, Object> model) {
        game.setMap(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0));
        String[] splittedTags = tags.split(" ");
        game.setTags(new HashSet<>(Arrays.asList(splittedTags)));
        game.setGameStatus(Collections.singleton(GameStatus.WAITING_FOR_USER));
        game.setPlayer1(username);
        game.setUserTurn(username);
        game.setFinished(false);
        gameRepo.save(game);
        model.put("game", game);
        model.put("username", username);
        model.put("isSecond", "false");
        return "game";
    }

    @PostMapping(value = "/game", params = "find")
    public String findGame(@ModelAttribute("tagsString") String tagsString, Map<String, Object> model) {
        System.out.println("1");
        List<Game> allGames = gameRepo.findAll();
        Set tags = new HashSet<>(Arrays.asList(tagsString.split(" ")));
        List<Game> filteredGames;
        if (!tagsString.equals(""))
            filteredGames = allGames.stream().filter(game -> game.getTags().stream().anyMatch(tags::contains)).collect(Collectors.toList());
        else
            filteredGames = allGames;
        model.put("games", filteredGames);
        return "main";
    }

    @PostMapping(value = "/game", name = "playing")
    public String playing(@ModelAttribute("gameId") String gameId, @ModelAttribute("username") String username, Map<String, Object> model) {
        Game game = gameRepo.findById(Long.valueOf(gameId)).get();
        if (game.getGameStatus().stream().findFirst().get() != GameStatus.WAITING_FOR_USER)
            return "redirect:/";
        game.setPlayer2(username);
        game.getGameStatus().clear();
        game.getGameStatus().add(GameStatus.IN_PROCESS);
        gameRepo.save(game);
        model.put("game", game);
        model.put("username", username);
        model.put("isSecond", "true");
        return "game";
    }

    @MessageMapping("/changeGame")
    @SendTo("/topic/changeGame")
    public Game updatedGame(Move move) {
        Game game = gameRepo.findById(Long.valueOf(move.getId())).get();
        if (game.getGameStatus().stream().findFirst().get() == GameStatus.IN_PROCESS && game.getUserTurn().equals(move.getUsername()) && game.getMap().get(move.getCell().intValue() - 1) == 0) {
            game.getMap().set((move.getCell().intValue() - 1), game.getFigure());
            game.changeTurn();

            if (game.getWinner() != null) {
                game.getGameStatus().clear();
                game.getGameStatus().add(GameStatus.FINISHED);
                game.setWinner(game.getWinner());
                game.setFinished(true);
            }
            if (game.getMap().stream().noneMatch(it -> it == 0))
                game.setFinished(true);
            gameRepo.save(game);
        }
        return game;
    }

    @MessageMapping("/enter")
    @SendTo("/topic/enter")
    public Game entered(String id) {
        Game game = gameRepo.findById(Long.valueOf(id)).get();
        return game;
    }
}
