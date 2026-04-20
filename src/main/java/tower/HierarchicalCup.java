package tower;

import java.util.ArrayList;
import java.util.List;

/**
 * Variante de taza que impone jerarquía desplazando elementos menores y bloqueándose en la base.
 * @author gaitan - lasso
 */
public class HierarchicalCup extends Cup {
    private boolean lockedAtBase;

    public HierarchicalCup(int id, int size, String color) {
        super(id, size, color);
        this.lockedAtBase = false;
    }

    @Override
    public boolean shouldDisplaceSmallerItems() {
        return true;
    }

    @Override
    public boolean isLockedAtBase() {
        return lockedAtBase;
    }

    @Override
    public void lockAtBase() {
        lockedAtBase = true;
    }

    @Override
    public void applyPostStackEffect(Tower tower) throws TowerException {
        List<Cup> cups = tower.getCups();
        
        // Desplazamiento
        int index = cups.indexOf(this);
        if (index != -1) {
            ArrayList<Cup> displaced = new ArrayList<>();
            for (int i = 0; i < index; i++) {
                Cup existing = cups.get(i);
                if (existing.getSize() < getSize()) {
                    displaced.add(existing);
                }
            }
            if (!displaced.isEmpty()) {
                cups.removeAll(displaced);
                int newIndex = cups.indexOf(this);
                cups.addAll(newIndex + 1, displaced);
            }
        }

        // Refresco de bloqueos jerárquicos
        tower.refreshHierarchicalLocks();
    }
}
