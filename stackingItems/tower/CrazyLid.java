package tower;

/**
 * Tapa que se ubica debajo de su taza companera.
 */
public class CrazyLid extends Lid {

    public CrazyLid(int id, int size, String color) {
        super(id, size, color);
    }

    @Override
    public boolean requiresCompanionCup() {
        return true;
    }

    @Override
    public boolean prefersUnderCupPlacement() {
        return true;
    }
}