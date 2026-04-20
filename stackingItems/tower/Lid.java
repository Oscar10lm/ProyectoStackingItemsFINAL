package tower;

 

import shapes.Rectangle;

/**
 * Modela una tapa que puede colocarse sobre tazas o permanecer independiente.
 * @author gaitan - lasso
 */
public class Lid extends StackingItem {

    /**
     * Inicializa una nueva tapa con su identificador, tamaño y color.
     */
    public Lid(int id, int size, String color) {
        super(id, size, color);
    }

    /**
     * Define la estructura visual de la tapa a partir de bloques.
     */
    @Override
    protected void buildParts() {
        for (int i = 0; i < getSize(); i++) {
            Rectangle r = buildBlock(getX() + (i * BLOCK_SIZE), getY());
            parts.add(r);
        }
    }

    /**
     * Indica si la tapa requiere obligatoriamente una taza compañera.
     */
    public boolean requiresCompanionCup() {
        return false;
    }

    /**
     * Determina si la tapa impide que se retire su taza asociada.
     */
    public boolean blocksRemovalFromCompanionCup(Cup cup) {
        return false;
    }

    /**
     * Indica si la tapa prefiere ubicarse en la base de la torre.
     */
    public boolean prefersBasePlacement() {
        return false;
    }
    
    /**
     * Indica si la tapa debe posicionarse justo debajo de su taza compañera.
     */
    public boolean prefersUnderCupPlacement() {
        return false;
    }

    @Override
    public void applyPreStackEffect(Tower tower) throws TowerException {
        // Implementación por defecto.
    }

    @Override
    public void applyPostStackEffect(Tower tower) throws TowerException {
        // Implementación por defecto.
    }

    @Override
    public boolean canBeRemoved(Tower tower) {
        Cup companion = tower.getCupById(getId());
        return !blocksRemovalFromCompanionCup(companion);
    }

    /**
     * Valida si se cumplen las condiciones para añadir esta tapa a la torre.
     */
    public boolean validatePresence(Tower tower, Cup companion) {
        if (requiresCompanionCup() && companion == null) {
            return false;
        }
        return true;
    }

    /**
     * Ejecuta la lógica para colocar la tapa en la posición que le corresponde.
     */
    public void placeInTower(Tower tower, Cup targetCup) throws TowerException {
        if (!validatePresence(tower, targetCup)) {
            throw new TowerException(TowerException.MISSING_COMPANION);
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
             throw new TowerException(TowerException.HEIGHT_EXCEEDED);
        }
    }

    /**
     * Calcula el desplazamiento vertical adicional que genera esta tapa.
     */
    public int getOffsetContribution(Cup cup) {
        return 0;
    }

    /**
     * Gestiona la colocación de la tapa si posee reglas de ubicación atípicas.
     */
    public void positionSpecial(Tower tower) {
        // Por defecto no hace nada.
    }
}
