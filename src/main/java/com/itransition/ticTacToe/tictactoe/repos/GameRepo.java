package com.itransition.ticTacToe.tictactoe.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itransition.ticTacToe.tictactoe.models.Game;

public interface GameRepo extends JpaRepository<Game, Long> {
}
