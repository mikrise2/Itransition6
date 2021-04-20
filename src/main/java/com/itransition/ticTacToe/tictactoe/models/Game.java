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
    private String userTurn;
    private boolean finished;
    private String winner;

    public Game() {

    }

    public Game(Long id, String name, Set<String> tags, String player1, String player2, List<Integer> map, Set<GameStatus> gameStatus, String userTurn, boolean finished, String winner) {
        this.id = id;
        this.name = name;
        this.tags = tags;
        this.player1 = player1;
        this.player2 = player2;
        this.map = map;
        this.gameStatus = gameStatus;
        this.userTurn = userTurn;
        this.finished = finished;
        this.winner = winner;
    }

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

    public String getUserTurn() {
        return userTurn;
    }

    public void setUserTurn(String userTurn) {
        this.userTurn = userTurn;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String statusString() {
        switch (gameStatus.stream().filter(Objects::nonNull).findFirst().get()) {
            case WAITING_FOR_USER:
                return "waiting for player";
            case FINISHED:
                return "finished";
            case IN_PROCESS:
                return "game is running";
        }
        return "";
    }

    public void changeTurn() {
        if (userTurn.equals(player1))
            userTurn = player2;
        else
            userTurn = player1;
    }

    public int getFigure() {
        if (userTurn.equals(player1))
            return 1;
        else
            return 2;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getWinner() {
        String winner = null;
        winner = checkOnWin(winner, 0, 1, 2);
        winner = checkOnWin(winner, 3, 4, 5);
        winner = checkOnWin(winner, 6, 7, 8);
        winner = checkOnWin(winner, 0, 3, 6);
        winner = checkOnWin(winner, 1, 4, 7);
        winner = checkOnWin(winner, 2, 5, 8);
        winner = checkOnWin(winner, 0, 4, 8);
        winner = checkOnWin(winner, 2, 4, 6);
        return winner;
    }

    private String checkOnWin(String winner, int i1, int i2, int i3) {
        if (winner == null) {
            if (map.get(i1).equals(map.get(i2)) && map.get(i2).equals(map.get(i3)) && map.get(i1) != 0)
                if (map.get(i1) == 1)
                    return player1;
                else
                    return player2;
            return null;
        } else
            return winner;
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
                ", userTurn='" + userTurn + '\'' +
                ", finished=" + finished +
                ", winner='" + winner + '\'' +
                '}';
    }
}
