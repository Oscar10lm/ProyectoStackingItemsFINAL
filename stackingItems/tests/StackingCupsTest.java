package tests;

import org.junit.jupiter.api.Test;
import tower.TowerContest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para el algoritmo de StackingCups.
 * Enfoque agil/XP: validar reglas de negocio clave con casos pequenos.
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
    public void shouldReturnConfigurationMatchingTargetHeight() {
        // Arrange & Act
        List<Integer> result = TowerContest.algorithmStackingCups(5, 9);

        // Assert
        int sum = result.stream().mapToInt(Integer::intValue).sum();
        assertEquals(5, result.size());
        assertEquals(9, sum);
        assertTrue(result.stream().allMatch(value -> value > 0), "Todas las alturas deben ser positivas.");
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