package tower;

/**
 * Taza que elimina tapas que le impiden el paso al entrar.
 */
public class OpenerCup extends Cup {

    public OpenerCup(int id, int size, String color) {
        super(id, size, color);
    }

    @Override
    public boolean shouldClearBlockingLids() {
        return true;
    }
}
