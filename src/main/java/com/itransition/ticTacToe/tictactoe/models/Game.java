package com.itransition.ticTacToe.tictactoe.models;

import javax.persistence.*;

import com.itransition.ticTacToe.tictactoe.types.GameStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.itransition.ticTacToe.tictactoe.types.GameStatus.*;


@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private Set<String> tags;
    private String player1;
    private String player2;
    @ElementCollection(targetClass = Integer.class, fetch = FetchType.EAGER)
    private List<Integer> map;
    @ElementCollection(targetClass = GameStatus.class, fetch = FetchType.EAGER)
    @Enumerated
    private Set<GameStatus> gameStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<GameStatus> getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(Set<GameStatus> gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public List<Integer> getMap() {
        return map;
    }

    public void setMap(List<Integer> map) {
        this.map = map;
    }

    public StringBuilder tagsToString() {
        StringBuilder tagsS = new StringBuilder();
        tags.forEach(tag -> tagsS.append(tag).append(" "));
        return tagsS;
    }

    public String statusString(){
        switch (gameStatus.stream().filter(Objects::nonNull).findFirst().get()){
            case WAITING_FOR_USER:
                return "waiting for player";
            case FINISHED:
                return "finished";
            case IN_PROCESS:
                return "game is running";
        }
        return "";
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                ", player1='" + player1 + '\'' +
                ", player2='" + player2 + '\'' +
                ", map=" + map +
                ", gameStatus=" + gameStatus +
                '}';
    }
}
