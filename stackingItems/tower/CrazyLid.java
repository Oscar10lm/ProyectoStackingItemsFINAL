package tower;

/**
 * Tapa que se usa como base de la torre.
 */
public class CrazyLid extends Lid {

    public CrazyLid(int id, int size, String color) {
        super(id, size, color);
    }

    @Override
    public boolean prefersBasePlacement() {
        return true;
    }
}
