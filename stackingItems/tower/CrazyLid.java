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

    @Override
    public void placeInTower(Tower tower, Cup targetCup) {
        if (!validatePresence(tower, targetCup)) {
            return;
        }
        
        // CrazyLid siempre se agrega como standalone para ubicarse debajo de la taza
        tower.getStandaloneLids().add(this);
        tower.getLidInsertionOrder().add(this);
        tower.rebuildTower();

        if (tower.getCurrentHeight() > tower.getMaxHeight()) {
            tower.getStandaloneLids().remove(this);
            tower.getLidInsertionOrder().remove(this);
            tower.rebuildTower();
            if (tower.isVisible()) {
                javax.swing.JOptionPane.showMessageDialog(null, "No cabe la tapa, supera la altura maxima.");
            }
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