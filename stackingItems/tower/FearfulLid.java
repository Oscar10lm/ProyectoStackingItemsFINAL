package tower;

/**
 * Tapa que solo entra si su taza companera existe y no sale si la esta tapando.
 */
public class FearfulLid extends Lid {

    public FearfulLid(int id, int size, String color) {
        super(id, size, color);
    }

    @Override
    public boolean requiresCompanionCup() {
        return true;
    }

    @Override
    public boolean blocksRemovalFromCompanionCup(Cup cup) {
        return cup != null && cup.getId() == getId() && getSize() >= cup.getSize();
    }
}
