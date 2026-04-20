package tower;

/**
 * Variante de tapa que exige la presencia de su taza compañera y bloquea su propia remoción.
 * @author gaitan - lasso
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
