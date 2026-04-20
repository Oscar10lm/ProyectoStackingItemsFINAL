package tower;

import java.util.ArrayList;
import java.util.List;

/**
 * Variante de taza que tiene la capacidad de retirar tapas bloqueantes al ser insertada.
 * @author gaitan - lasso
 */
public class OpenerCup extends Cup {

    public OpenerCup(int id, int size, String color) {
        super(id, size, color);
    }

    @Override
    public boolean shouldClearBlockingLids() {
        return true;
    }

    @Override
    public void applyPreStackEffect(Tower tower) throws TowerException {
        List<Cup> cups = tower.getCups();
        List<Lid> standaloneLids = tower.getStandaloneLids();
        List<Lid> lidInsertionOrder = tower.getLidInsertionOrder();
        
        boolean blockedByFearful = false;

        ArrayList<Lid> removedStandalone = new ArrayList<>();
        for (Lid lid : standaloneLids) {
            if (lid.prefersBasePlacement() || lid.prefersUnderCupPlacement() || lid.getSize() < getSize()) {
                continue;
            }

            Cup companionCup = tower.getCupById(lid.getId());
            if (companionCup != null && lid.blocksRemovalFromCompanionCup(companionCup)) {
                blockedByFearful = true;
                continue;
            }

            removedStandalone.add(lid);
        }
        for (Lid lid : removedStandalone) {
            lid.makeInvisible();
            standaloneLids.remove(lid);
            lidInsertionOrder.remove(lid);
        }

        for (Cup cup : cups) {
            ArrayList<Lid> removedFromCup = new ArrayList<>();
            for (Lid lid : cup.getLids()) {
                if (lid.getSize() < getSize()) {
                    continue;
                }
                if (lid.blocksRemovalFromCompanionCup(cup)) {
                    blockedByFearful = true;
                    continue;
                }
                removedFromCup.add(lid);
            }
            for (Lid lid : removedFromCup) {
                lid.makeInvisible();
                cup.getLids().remove(lid);
                lidInsertionOrder.remove(lid);
            }
        }

        if (blockedByFearful) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "No es posible retirar la tapa, es de tipo fearful.");
        }
    }
}
