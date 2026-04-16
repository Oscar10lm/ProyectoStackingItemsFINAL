package tower;

/**
 * Taza que desplaza objetos de menor tamano y puede bloquearse en la base.
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
}
