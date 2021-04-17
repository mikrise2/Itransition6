package com.itransition.ticTacToe.tictactoe.types;

public enum MoveType {
    X(1),O(2);
    private Integer value;

    MoveType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
