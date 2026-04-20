package tests;

import org.junit.jupiter.api.Test;
import tower.TowerContest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Valida el algoritmo matemático de distribución de alturas para el concurso de tazas.
 * @author gaitan - lasso
 */
public class StackingCupsTest {

    @Test
    public void shouldReturnConfigurationWhenHeightEqualsCupCount() {
        // Arrange & Act
        List<Integer> result = TowerContest.algorithmStackingCups(4, 4);

        // Assert
        assertEquals(List.of(4, 3, 2, 1), result);
    }

    @Test
    public void shouldReturnConfigurationForMaximumHeight() {
        // Arrange & Act
        List<Integer> result = TowerContest.algorithmStackingCups(4, 16);

        // Assert
        assertEquals(List.of(1, 3, 5, 7), result);
    }

    @Test
    public void shouldntReturnConfigurationWhenHeightIsBelowMinimum() {
        // Arrange & Act
        List<Integer> result = TowerContest.algorithmStackingCups(4, 1);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldntReturnConfigurationWhenHeightIsAboveMaximum() {
        // Arrange & Act
        List<Integer> result = TowerContest.algorithmStackingCups(3, 20);

        // Assert
        assertTrue(result.isEmpty());
    }
}
