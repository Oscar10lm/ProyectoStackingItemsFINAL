package tests;

import tower.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

/**
 * Pruebas adicionales para cubrir métodos en la clase Tower y StackingItem.
 */
public class TowerAdditionalTest {

    @Test
    public void shouldHandleSwapInvalidTypes() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(1);
        tower.pushLid(2);
        
        tower.swap(new String[]{"Cup", "1"}, new String[]{"Lid", "2"});
        assertTrue(tower.ok());
    }

    @Test
    public void shouldHandleSwapInvalidIds() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(1);
        tower.pushCup(2);
        
        tower.swap(new String[]{"Cup", "1"}, new String[]{"Cup", "99"});
        assertTrue(tower.ok());
    }

    @Test
    public void shouldHandlePopLockedCup() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(4, "hierarchical"); 
        
        tower.popCup();
        
        String[][] items = tower.stackingItems();
        assertEquals(1, items.length);
    }

    @Test
    public void shouldHandlePopLockedLid() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(3);
        tower.pushLid(3, "fearful"); 
        
        tower.popLid();
        
        String[][] items = tower.stackingItems();
        assertTrue(Arrays.stream(items).anyMatch(i -> "Lid".equals(i[0]) && "3".equals(i[1])));
    }

    @Test
    public void shouldTestStackingItemBasicMethods() {
        Cup cup = new Cup(1, 10, "red");
        cup.makeVisible();
        assertTrue(cup.isVisible());
        cup.makeInvisible();
        assertFalse(cup.isVisible());
        
        assertEquals("red", cup.getColor());
        cup.moveTo(100, 200);
        assertEquals(100, cup.getX());
        assertEquals(200, cup.getY());
    }

    @Test
    public void shouldHandleTowerConstructorWithZeroCups() {
        Tower tower = new Tower(0);
        assertEquals(0, tower.height());
    }

    @Test
    public void shouldHandleRemoveNonExistentLid() {
        Tower tower = new Tower(300, 1000);
        tower.removeLid(99);
        assertTrue(tower.ok());
    }

    @Test
    public void shouldHandleRemoveLidFromCupWithoutLid() {
        Tower tower = new Tower(300, 1000);
        tower.pushCup(1);
        tower.removeLid(1);
        assertTrue(tower.ok());
    }

    @Test
    public void shouldHandleRebuildFromHeights() throws tower.TowerException {
        Tower tower = new Tower(500, 1000);
        tower.rebuildFromHeights(Arrays.asList(1, 3, 5));
        assertEquals(3, tower.stackingItems().length);
    }

    @Test
    public void shouldHandleSwapToReduce() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(1);
        tower.pushCup(3);
        tower.pushLid(1);
        tower.pushLid(3);
        
        String[][] swap = tower.swapToReduce();
        assertNotNull(swap);
    }

    @Test
    public void shouldHandleCover() {
        Tower tower = new Tower(500, 1000);
        tower.pushLid(2); // standalone
        tower.pushCup(2); // cup 2 exists but no lid
        
        tower.cover();
        
        int[] lidded = tower.liddedCups();
        boolean found = false;
        for (int id : lidded) if (id == 2) found = true;
        assertTrue(found);
    }

    @Test
    public void shouldTestCupNestingRules() {
        Cup cup4 = new Cup(4, 4, "red");
        Cup cup2 = new Cup(2, 2, "blue");
        Cup cup5 = new Cup(5, 5, "green");
        
        assertTrue(cup4.canNest(cup2));
        assertFalse(cup4.canNest(cup5));
    }

    @Test
    public void shouldHandleStandaloneLidPositioning() {
        Tower tower = new Tower(500, 1000);
        tower.pushLid(5);
        tower.pushLid(7);
        
        String[][] items = tower.stackingItems();
        assertEquals(2, items.length);
        assertTrue(tower.ok());
    }
    @Test
    public void shouldHandleTowerVisibility() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(1);
        tower.pushLid(1);
        
        tower.makeVisible();
        assertTrue(tower.isVisible());
        
        tower.makeInvisible();
        assertFalse(tower.isVisible());
    }

    @Test
    public void shouldHandleTowerHeightExceededOnPush() {
        Tower tower = new Tower(300, 10);
        // Cup 5 size 9, height 2*9-1 = 17 blocks. 17 > 10.
        tower.pushCup(5);
        assertEquals(0, tower.stackingItems().length);
    }

    @Test
    public void shouldHandleTowerDuplicateLid() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(1);
        tower.pushLid(1);
        tower.pushLid(1); // Duplicate
        
        String[][] items = tower.stackingItems();
        assertEquals(2, items.length); // Cup 1 and one Lid 1
    }

    @Test
    public void shouldHandleTowerRemoveNonExistentCup() {
        Tower tower = new Tower(500, 1000);
        tower.removeCup(99);
        assertTrue(tower.ok());
    }

    @Test
    public void shouldHandleTowerSwapSameTypesLid() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(1);
        tower.pushLid(1);
        tower.pushCup(2);
        tower.pushLid(2);
        
        tower.swap(new String[]{"Lid", "1"}, new String[]{"Lid", "2"});
        assertTrue(tower.ok());
    }

    @Test
    public void shouldHandleTowerOrderWithHeightExceeded() {
        // This is hard to trigger but let's try a small tower
        Tower tower = new Tower(300, 5);
        tower.pushCup(1);
        // orderTower might catch it if we had more items
        tower.orderTower();
        assertTrue(tower.ok());
    }

    @Test
    public void shouldHandleTowerExit() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(1);
        tower.pushLid(1);
        tower.exit();
        assertEquals(0, tower.height());
        assertFalse(tower.isVisible());
    }

    @Test
    public void shouldHandleTowerRebuildWithInvalidHeights() throws tower.TowerException {
        Tower tower = new Tower(500, 1000);
        tower.rebuildFromHeights(Arrays.asList(null, 0, 2, 3)); // 0 and 2 are invalid, null is ignored
        assertEquals(1, tower.stackingItems().length); // Only height 3 (id 2) should be added
    }
}
