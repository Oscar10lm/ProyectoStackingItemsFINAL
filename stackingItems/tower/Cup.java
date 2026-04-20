package tower;

 

import java.util.ArrayList;
import shapes.Rectangle;

/**
 * Modela una taza que puede contener tapas o anidarse en otras tazas.
 * @author gaitan - lasso
 */
public class Cup extends StackingItem {

    private final ArrayList<Lid> lids;

    /**
     * Inicializa una nueva taza con su identificador, tamaño y color.
     */
    public Cup(int id, int size, String color) {
        super(id, size, color);
        lids = new ArrayList<>();
    }

    public Cup(int id, String color) {
        this(id, (2 * id) - 1, color);
    }

    @Override
    protected void buildParts() {
        int heightBlocks = getHeight();
        int widthBlocks = getSize();

        int baseYIndex = heightBlocks - 1;
        for (int c = 0; c < widthBlocks; c++) {
            Rectangle r = buildBlock(getX() + (c * BLOCK_SIZE), getY() + (baseYIndex * BLOCK_SIZE));
            parts.add(r);
        }

        for (int r = 0; r < heightBlocks - 1; r++) {
            Rectangle left = buildBlock(getX(), getY() + (r * BLOCK_SIZE));
            parts.add(left);

            if (getSize() > 1) {
                Rectangle right = buildBlock(getX() + ((widthBlocks - 1) * BLOCK_SIZE), getY() + (r * BLOCK_SIZE));
                parts.add(right);
            }
        }
    }

    @Override
    protected void afterMove() {
        int nestedLids = 0;
        int coverLids = 0;

        for (Lid lid : lids) {
            int lidX = getX() + ((getSize() - lid.getSize()) * BLOCK_SIZE) / 2;
            int lidY;
            if (lid.getSize() < getSize()) {
                int innerFloorY = getY() + getRealPixelHeight() - (2 * BLOCK_SIZE);
                lidY = innerFloorY - (nestedLids * BLOCK_SIZE);
                nestedLids++;
            } else {
                lidY = getY() - BLOCK_SIZE - (coverLids * BLOCK_SIZE);
                coverLids++;
            }
            lid.moveTo(lidX, lidY);
        }
    }

    @Override
    public void makeVisible() {
        super.makeVisible();
        for (Lid lid : lids) {
            lid.makeVisible();
        }
    }

    @Override
    public void makeInvisible() {
        super.makeInvisible();
        for (Lid lid : lids) {
            lid.makeInvisible();
        }
    }

    /**
     * Vincula una tapa específica a esta taza.
     */
    public void addLid(Lid lid) {
        lids.add(lid);
    }

    /**
     * Desvincula y oculta la tapa que se encuentre en la posición superior.
     */
    public void removeTopLid() {
        if (!lids.isEmpty()) {
            Lid lid = lids.remove(lids.size() - 1);
            lid.makeInvisible();
        }
    }

    /**
     * Desvincula y oculta todas las tapas asociadas a esta taza.
     */
    public void removeAllLids() {
        for (Lid lid : lids) {
            lid.makeInvisible();
        }
        lids.clear();
    }

    public boolean hasLids() {
        return !lids.isEmpty();
    }

    /**
     * Determina si una taza de menor tamaño puede introducirse en esta.
     */
    public boolean canNest(Cup innerCup) {
        // Una taza puede anidarse si esta taza no tiene tapas que lo impidan
        if (!hasLids()) return true;
        
        // Si tiene tapas, solo se puede anidar si la tapa superior es menor que la taza que entra
        // (Regla específica del dominio: si hay una tapa pequeña adentro, aún puede caber algo)
        Lid topLid = lids.get(lids.size() - 1);
        return topLid.getSize() < innerCup.getSize();
    }

    public ArrayList<Lid> getLids() {
        return lids;
    }

    public int getHeight() {
        return (2 * getSize() - 1);
    }

    public int getRealPixelHeight() {
        return getHeight() * BLOCK_SIZE;
    }

    public int getPixelWidth() {
        return getSize() * BLOCK_SIZE;
    }

    /**
     * Indica si la taza tiene la capacidad de remover tapas que bloqueen su paso.
     */
    public boolean shouldClearBlockingLids() {
        return false;
    }

    /**
     * Indica si la taza desplaza otros objetos menores al ser insertada.
     */
    public boolean shouldDisplaceSmallerItems() {
        return false;
    }
    
    /**
     * Indica si la taza elimina permanentemente elementos menores al entrar.
     */
    public boolean shouldPurgeSmallerItems() {
        return false;
    }

    /**
     * Indica si la taza posee las propiedades especiales de tipo Iron.
     */
    public boolean isIronCup() {
        return false;
    }
    
    /**
     * Indica si la taza se encuentra bloqueada en la base de la torre.
     */
    public boolean isLockedAtBase() {
        return false;
    }

    /**
     * Aplica un bloqueo a la taza cuando se sitúa en la base de la torre.
     */
    public void lockAtBase() {
        // Hook polimorfico.
    }

    @Override
    public void applyPreStackEffect(Tower tower) throws TowerException {
        // Implementación por defecto: sin efecto previo.
    }

    @Override
    public void applyPostStackEffect(Tower tower) throws TowerException {
        // Implementación por defecto: sin efecto posterior.
    }

    @Override
    public boolean canBeRemoved(Tower tower) {
        return !isLockedAtBase();
    }
}
