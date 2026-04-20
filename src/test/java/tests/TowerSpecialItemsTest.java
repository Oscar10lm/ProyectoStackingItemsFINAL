package tests;

import tower.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

/**
 * Pruebas unitarias para cubrir el comportamiento de los ítems especiales y sus interacciones.
 */
public class TowerSpecialItemsTest {

    @Test
    public void shouldCreateIronCup() {
        IronCup cup = new IronCup(10, 10, "black");
        assertEquals(10, cup.getId());
        assertEquals(10, cup.getSize());
        assertTrue(cup.isIronCup());
        assertTrue(cup.shouldPurgeSmallerItems());
    }

    @Test
    public void shouldIronCupPurgeSmallerItemsOnStack() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(1); // Size 1
        tower.pushCup(2); // Size 2
        tower.pushLid(2);
        tower.pushLid(8); // Standalone lid size 8
        
        // Iron Cup with id 5 (size 5)
        tower.pushCup(5, "iron");
        
        String[][] items = tower.stackingItems();
        
        boolean hasCup1 = false;
        boolean hasCup2 = false;
        boolean hasLid2 = false;
        boolean hasLid8 = true; // Expected to stay
        boolean hasCup5 = false;
        
        for (String[] item : items) {
            if ("Cup".equals(item[0]) && "1".equals(item[1])) hasCup1 = true;
            if ("Cup".equals(item[0]) && "2".equals(item[1])) hasCup2 = true;
            if ("Lid".equals(item[0]) && "2".equals(item[1])) hasLid2 = true;
            if ("Lid".equals(item[0]) && "8".equals(item[1])) hasLid8 = true;
            if ("Cup".equals(item[0]) && "5".equals(item[1])) hasCup5 = true;
        }
        
        assertFalse(hasCup1, "Cup 1 should be purged");
        assertFalse(hasCup2, "Cup 2 should be purged");
        assertFalse(hasLid2, "Lid 2 should be purged");
        assertTrue(hasLid8, "Lid 8 should remain");
        assertTrue(hasCup5, "Iron Cup 5 should be present");
    }

    @Test
    public void shouldIronCupNotPurgeOtherIronCups() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(2, "iron"); 
        tower.pushCup(4, "iron"); 
        
        String[][] items = tower.stackingItems();
        assertEquals(2, items.length);
    }

    @Test
    public void shouldOpenerCupBeBlockedByFearfulLid() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(5);
        tower.pushLid(5, "fearful"); 
        
        // Opener cup size 3
        tower.pushCup(3, "opener");
        
        String[][] items = tower.stackingItems();
        boolean hasLid5 = Arrays.stream(items).anyMatch(i -> "Lid".equals(i[0]) && "5".equals(i[1]));
        assertTrue(hasLid5, "Fearful lid should block Opener cup removal");
    }

    @Test
    public void shouldOpenerCupRemoveNormalLids() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(5);
        tower.pushLid(5); 
        tower.pushLid(8); // Standalone
        
        // Opener cup size 3
        tower.pushCup(3, "opener");
        
        String[][] items = tower.stackingItems();
        boolean hasLid5 = Arrays.stream(items).anyMatch(i -> "Lid".equals(i[0]) && "5".equals(i[1]));
        boolean hasLid8 = Arrays.stream(items).anyMatch(i -> "Lid".equals(i[0]) && "8".equals(i[1]));
        
        assertFalse(hasLid5, "Normal lid should be removed by Opener cup");
        assertFalse(hasLid8, "Standalone lid should be removed by Opener cup");
    }

    @Test
    public void shouldHierarchicalCupDisplaceSmallerItems() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(5); 
        tower.pushCup(1); 
        tower.pushCup(2); 
        
        // Hierarchical cup size 4
        tower.pushCup(4, "hierarchical");
        
        String[][] items = tower.stackingItems();
        // Expected order in cups: 5, 4, 1, 2
        assertEquals("5", items[0][1]);
        assertEquals("4", items[1][1]);
        assertEquals("1", items[2][1]);
        assertEquals("2", items[3][1]);
    }

    @Test
    public void shouldHierarchicalCupBeLockedAtBase() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(4, "hierarchical");
        
        tower.removeCup(4);
        
        String[][] items = tower.stackingItems();
        assertTrue(Arrays.stream(items).anyMatch(i -> "Cup".equals(i[0]) && "4".equals(i[1])));
    }

    @Test
    public void shouldCrazyLidRequireCompanion() {
        Tower tower = new Tower(500, 1000);
        tower.pushLid(3, "crazy");
        assertEquals(0, tower.stackingItems().length);
    }

    @Test
    public void shouldCrazyLidPositionUnderCup() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(3);
        tower.pushLid(3, "crazy");
        
        String[][] items = tower.stackingItems();
        assertEquals(2, items.length);
        // Crazy lid is standalone, so it appears after all cups and their lids in stackingItems
        assertEquals("Lid", items[1][0]);
        assertEquals("3", items[1][1]);
    }

    @Test
    public void shouldOpenerCupHandleDifferentLidTypes() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(5);
        tower.pushLid(5); // Size 9, normal lid
        
        // Add a lid that prefers base placement (e.g. standalone lid with id 7)
        tower.pushLid(7); 
        
        // Opener cup size 3 (id 2 -> size 3)
        tower.pushCup(2, "opener");
        
        String[][] items = tower.stackingItems();
        // Normal lid size 9 > 3, should be removed
        assertFalse(Arrays.stream(items).anyMatch(i -> "Lid".equals(i[0]) && "5".equals(i[1])));
        // Lid 7 size 13 > 3, should be removed
        assertFalse(Arrays.stream(items).anyMatch(i -> "Lid".equals(i[0]) && "7".equals(i[1])));
    }

    @Test
    public void shouldOpenerCupIgnoreSmallerLids() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(5);
        tower.pushLid(1); // Size 1, smaller than Opener size 3
        
        tower.pushCup(2, "opener");
        
        String[][] items = tower.stackingItems();
        assertTrue(Arrays.stream(items).anyMatch(i -> "Lid".equals(i[0]) && "1".equals(i[1])));
    }

    @Test
    public void shouldHierarchicalCupHandleNoDisplacement() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(4); // Size 7
        tower.pushCup(2, "hierarchical"); // Size 3
        
        String[][] items = tower.stackingItems();
        // Order should be 4, 2 (no displacement because 4 is not smaller than 2)
        assertEquals("4", items[0][1]);
        assertEquals("2", items[1][1]);
    }

    @Test
    public void shouldCrazyLidOffsetContribution() {
        CrazyLid lid = new CrazyLid(3, 5, "red");
        Cup cup3 = new Cup(3, 5, "red");
        Cup cup4 = new Cup(4, 7, "blue");
        
        assertEquals(Tower.BLOCK_SIZE, lid.getOffsetContribution(cup3));
        assertEquals(0, lid.getOffsetContribution(cup4));
        assertEquals(0, lid.getOffsetContribution(null));
    }

    @Test
    public void shouldCrazyLidPositionSpecialVisible() {
        Tower tower = new Tower(500, 1000);
        tower.makeVisible();
        tower.pushCup(3);
        tower.pushLid(3, "crazy");
        
        // Should be visible
        assertTrue(tower.isVisible());
    }

    @Test
    public void shouldFearfulLidNotBlockIfDifferentCup() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(3);
        tower.pushCup(5);
        
        FearfulLid lid = new FearfulLid(3, 5, "red");
        Cup cup5 = tower.getCupById(5);
        
        assertFalse(lid.blocksRemovalFromCompanionCup(cup5));
        assertFalse(lid.blocksRemovalFromCompanionCup(null));
    }
    @Test
    public void shouldFearfulLidBlockRemovalOnlyIfSizeMatches() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(5);
        tower.pushLid(3, "fearful"); // size 3 < cup size 5
        
        tower.removeLid(3);
        
        String[][] items = tower.stackingItems();
        assertFalse(Arrays.stream(items).anyMatch(i -> "Lid".equals(i[0]) && "3".equals(i[1])));
    }

    @Test
    public void shouldFearfulLidBlockRemovalIfSizeIsGreaterOrEqual() {
        Tower tower = new Tower(500, 1000);
        tower.pushCup(3);
        tower.pushLid(3, "fearful"); // size 3 == cup size 3
        
        tower.removeLid(3);
        
        String[][] items = tower.stackingItems();
        assertTrue(Arrays.stream(items).anyMatch(i -> "Lid".equals(i[0]) && "3".equals(i[1])));
    }

}
