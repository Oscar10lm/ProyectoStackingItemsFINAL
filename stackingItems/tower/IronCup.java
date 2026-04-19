package tower;

import java.util.ArrayList;
import java.util.List;

/**
 * Taza que elimina elementos menores al colocarse,
 * excepto otras tazas de tipo iron.
 */
public class IronCup extends Cup {

    public IronCup(int id, int size, String color) {
        super(id, size, color);
    }

    @Override
    public boolean shouldPurgeSmallerItems() {
        return true;
    }

    @Override
    public boolean isIronCup() {
        return true;
    }

    @Override
    public void applyPreStackEffect(Tower tower) {
        List<Cup> cups = tower.getCups();
        List<Lid> standaloneLids = tower.getStandaloneLids();
        List<Lid> lidInsertionOrder = tower.getLidInsertionOrder();

        ArrayList<Cup> removedCups = new ArrayList<>();
        for (Cup cup : cups) {
            if (cup.getSize() < getSize() && !cup.isIronCup()) {
                removedCups.add(cup);
            }
        }

        for (Cup cup : removedCups) {
            tower.removeLidReferencesByCup(cup);
            cup.removeAllLids();
            cup.makeInvisible();
            cups.remove(cup);
        }

        ArrayList<Lid> removedStandalone = new ArrayList<>();
        for (Lid lid : standaloneLids) {
            if (lid.getSize() < getSize()) {
                removedStandalone.add(lid);
            }
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
                    removedFromCup.add(lid);
                }
            }
            for (Lid lid : removedFromCup) {
                lid.makeInvisible();
                cup.getLids().remove(lid);
                lidInsertionOrder.remove(lid);
            }
        }
    }
}