import static org.junit.jupiter.api.Assertions.*;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.view.Shop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class ShopTest {

    private Shop shop;
    private GameController gameController;

    @BeforeEach
    void setUp() {
        gameController = Mockito.mock(GameController.class);
        // Assuming GameController has necessary methods to simulate or mock
        Mockito.when(gameController.getBoard()).thenReturn(new Board(8, 8)); // Example size
        shop = new Shop(gameController);
    }

    @Test
    void testPurchaseUpgradeReducesEnergy() {
        Player player = new Player(gameController.getBoard(), "Red", "Player1");
        player.setEnergy(100); // Assume starting energy
        Upgrade speedBoost = new Upgrade("Speed Boost", 50, "Increases movement speed.");

        shop.purchaseUpgrade(speedBoost);

        assertEquals(50, player.getEnergy(), "Energy should be reduced by the cost of the upgrade.");
    }

    @Test
    void testUpgradeEffectApplied() {
        Player player = new Player(gameController.getBoard(), "Blue", "Player2");
        Upgrade shield = new Upgrade("Shield", 75, "Blocks one incoming attack.");

        shop.purchaseUpgrade(shield);

        // You need to define how to check if the effect is applied.
        assertTrue(player.hasShield(), "Player should have the shield effect applied.");
    }
}
