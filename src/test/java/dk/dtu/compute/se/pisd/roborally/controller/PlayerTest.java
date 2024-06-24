package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Board board;
    private Player player;
    private GameController gameController;

    @BeforeEach
    void setUp() {
        board = new Board(10, 10);
        player = new Player(board, "red", "Player1");
        board.addPlayer(player);
        gameController = new GameController(board);
        player.setSpace(board.getSpace(1, 1));  // Set initial position
        player.setEnergy(100);  // Initial energy for the tests
    }

    @Test
    void testEnergyConsumption() {
        int initialEnergy = player.getEnergy();
        gameController.moveForward(player, 1, true);
        assertTrue(player.getEnergy() < initialEnergy, "Energy should decrease after a move");
    }


    @Test
    void testNoEnergyMove() {
        player.setEnergy(0);  // No energy left
        gameController.moveForward(player, 1,true);
        assertEquals(board.getSpace(1, 1), player.getSpace(), "Player should not move if no energy");
    }

    @Test
    void testEnergyRecharge() {
        player.setEnergy(80);
        player.changeEnergy(20);  // Recharge or power-up scenario
        assertEquals(100, player.getEnergy(), "Energy should correctly increase after recharge");
    }
}
