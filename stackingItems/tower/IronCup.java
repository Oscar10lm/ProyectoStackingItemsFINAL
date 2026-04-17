package tower;

/**
 * Taza que elimina elementos menores al colocarse,
 * excepto otras tazas de tipo iron.
 */
public class IronCup extends Cup {

    public IronCup(int id, int size, String color) {
        super(id, size, color);
    }

    @Override
    public boolean shouldPurgeSmallerItems() {
        return true;
    }

    @Override
    public boolean isIronCup() {
        return true;
    }
}