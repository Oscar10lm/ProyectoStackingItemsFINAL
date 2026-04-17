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
}
