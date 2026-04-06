package tower;

import java.util.List;

/**
 * Operaciones de consulta y análisis de la torre.
 */
public interface TowerInfo {
    int height();

    int getCurrentHeight();

    int[] liddedCups();

    String[][] stackingItems();

    String[][] swapToReduce();

    boolean ok();

    List<Integer> targetConfiguration(int n, int h);
}