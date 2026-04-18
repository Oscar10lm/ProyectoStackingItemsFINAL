package tests;

import org.junit.jupiter.api.Test;
import tower.TowerContest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para TowerContest.
 * Enfoque agil/XP: pruebas pequenas, claras y centradas en comportamiento esencial.
 */
public class TowerContestTest {

    @Test
    public void shouldSolveWhenConfigurationIsPossible() {
        // Arrange
        TowerContest towerContest = new TowerContest();

        // Act
        String result = towerContest.solve(3, 5);

        // Assert
        assertEquals("5 3 1", result);
    }

    @Test
    public void shouldSolveAtMaximumHeight() {
        // Arrange
        TowerContest towerContest = new TowerContest();

        // Act
        String result = towerContest.solve(4, 16);

        // Assert
        assertEquals("1 3 5 7", result);
    }

    @Test
    public void shouldSolveTrivialCaseWithOneCup() {
        // Arrange
        TowerContest towerContest = new TowerContest();

        // Act
        String result = towerContest.solve(1, 1);

        // Assert
        assertEquals("1", result);
    }

    @Test
    public void shouldntSolveWhenHeightIsBelowMinimum() {
        // Arrange
        TowerContest towerContest = new TowerContest();

        // Act
        String result = towerContest.solve(4, 1);

        // Assert
        assertEquals("impossible", result);
    }

    @Test
    public void shouldntSolveWhenHeightIsAboveMaximum() {
        // Arrange
        TowerContest towerContest = new TowerContest();

        // Act
        String result = towerContest.solve(3, 20);

        // Assert
        assertEquals("impossible", result);
    }
}