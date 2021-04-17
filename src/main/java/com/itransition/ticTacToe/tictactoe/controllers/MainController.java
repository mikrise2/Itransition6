package com.itransition.ticTacToe.tictactoe.controllers;

import com.itransition.ticTacToe.tictactoe.models.Game;
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
    public String createGame(Game game, @ModelAttribute("tagsString") String tags, Map<String, Object> model) {
        System.out.println(2);
        game.setMap(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0));
        String[] splittedTags = tags.split(" ");
        game.setTags(new HashSet<>(Arrays.asList(splittedTags)));
        game.setGameStatus(Collections.singleton(GameStatus.WAITING_FOR_USER));
        System.out.println(game);
        gameRepo.save(game);
        List<Game> allGames = gameRepo.findAll();
        model.put("games", allGames);
        return "main";
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
        System.out.println(filteredGames);
        model.put("games", filteredGames);
        return "main";
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Game updatedGame(String id) {
        return gameRepo.findById(Long.valueOf(id)).get();
//        return new Game()"Hello, " + HtmlUtils.htmlEscape(message + "!");
    }

}
