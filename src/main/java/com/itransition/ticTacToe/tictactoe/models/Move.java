package com.itransition.ticTacToe.tictactoe.models;

public class Move {
    private String id;
    private Long cell;
    private String username;

    public Move(String id, Long cell, String username) {
        this.id = id;
        this.cell = cell;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCell() {
        return cell;
    }

    public void setCell(Long cell) {
        this.cell = cell;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Move{" +
                "id='" + id + '\'' +
                ", cell='" + cell + '\'' +
                '}';
    }
}
