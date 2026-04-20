package tower;

/**
 * Variante de tapa con comportamiento inusual que se posiciona debajo de su taza asociada.
 * @author gaitan - lasso
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

    @Override
    public void placeInTower(Tower tower, Cup targetCup) throws TowerException {
        if (!validatePresence(tower, targetCup)) {
            throw new TowerException(TowerException.MISSING_COMPANION);
        }
        
        // CrazyLid siempre se agrega como standalone para ubicarse debajo de la taza
        tower.getStandaloneLids().add(this);
        tower.getLidInsertionOrder().add(this);
        tower.rebuildTower();

        if (tower.getCurrentHeight() > tower.getMaxHeight()) {
            tower.getStandaloneLids().remove(this);
            tower.getLidInsertionOrder().remove(this);
            tower.rebuildTower();
            throw new TowerException(TowerException.HEIGHT_EXCEEDED);
        }
    }

    @Override
    public int getOffsetContribution(Cup cup) {
        return (cup != null && cup.getId() == getId()) ? Tower.BLOCK_SIZE : 0;
    }

    @Override
    public void positionSpecial(Tower tower) {
        Cup companionCup = tower.getCupById(getId());
        if (companionCup != null) {
            int lidX = companionCup.getX() + ((companionCup.getSize() - getSize()) * Tower.BLOCK_SIZE) / 2;
            int lidY = companionCup.getY() + companionCup.getRealPixelHeight();
            moveTo(lidX, lidY);
            if (tower.isVisible()) {
                makeVisible();
            }
        }
    }
}