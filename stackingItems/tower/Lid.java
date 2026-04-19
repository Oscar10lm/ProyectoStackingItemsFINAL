package tower;

 

import shapes.Rectangle;

/**
 * Representa una tapa asociable a una taza.
 * @author Juan Gaitán and Oscar LasLidso
 */
public class Lid extends StackingItem {

    /**
     * Construye una tapa con identificador, tamaño y color.
     */
    public Lid(int id, int size, String color) {
        super(id, size, color);
    }

    /**
     * Construye la geometría de la tapa.
     */
    @Override
    protected void buildParts() {
        for (int i = 0; i < getSize(); i++) {
            Rectangle r = buildBlock(getX() + (i * BLOCK_SIZE), getY());
            parts.add(r);
        }
    }

    /**
     * Indica si la tapa exige que exista su taza companera.
     */
    public boolean requiresCompanionCup() {
        return false;
    }

    /**
     * Indica si la tapa no se puede retirar cuando tapa a su taza.
     */
    public boolean blocksRemovalFromCompanionCup(Cup cup) {
        return false;
    }

    /**
     * Indica si la tapa debe ubicarse como base de la torre.
     */
    public boolean prefersBasePlacement() {
        return false;
    }
    
    /**
     * Indica si la tapa debe ubicarse debajo de la taza companera.
     */
    public boolean prefersUnderCupPlacement() {
        return false;
    }

    @Override
    public void applyPreStackEffect(Tower tower) {
        // Implementación por defecto.
    }

    @Override
    public void applyPostStackEffect(Tower tower) {
        // Implementación por defecto.
    }

    @Override
    public boolean canBeRemoved(Tower tower) {
        Cup companion = tower.getCupById(getId());
        return !blocksRemovalFromCompanionCup(companion);
    }

    /**
     * Valida si la tapa puede ser agregada según la presencia de otros objetos.
     */
    public boolean validatePresence(Tower tower, Cup companion) {
        if (requiresCompanionCup() && companion == null) {
            return false;
        }
        return true;
    }

    /**
     * Posiciona la tapa en la torre.
     */
    public void placeInTower(Tower tower, Cup targetCup) {
        if (!validatePresence(tower, targetCup)) {
            return;
        }

        Cup actualTarget = tower.resolveTargetCupForLid(targetCup, this);
        
        if (actualTarget != null) {
            actualTarget.addLid(this);
        } else {
            tower.getStandaloneLids().add(this);
        }
        
        tower.getLidInsertionOrder().add(this);
        tower.rebuildTower();

        if (tower.getCurrentHeight() > tower.getMaxHeight()) {
             // Rollback
             if (actualTarget != null) {
                 actualTarget.getLids().remove(this);
             } else {
                 tower.getStandaloneLids().remove(this);
             }
             tower.getLidInsertionOrder().remove(this);
             tower.rebuildTower();
             if (tower.isVisible()) {
                 javax.swing.JOptionPane.showMessageDialog(null, "No cabe la tapa, supera la altura maxima.");
             }
        }
    }

    /**
     * Retorna el espacio extra que esta tapa ocupa debajo de una taza.
     */
    public int getOffsetContribution(Cup cup) {
        return 0;
    }

    /**
     * Posiciona la tapa si tiene reglas especiales de ubicación.
     */
    public void positionSpecial(Tower tower) {
        // Por defecto no hace nada.
    }
}
