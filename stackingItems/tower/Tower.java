package tower;

 
import shapes.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa una torre de tazas y tapas.
 * Controla apilamiento, anidamiento, altura máxima,
 * ordenamiento y representación visual.
 * @author Juan Gaitán and Oscar Lasso
 */
public class Tower {

    //Constantes de posicionamiento

    public static final int TOWER_X = 500;
    public static final int BASE_Y = 700;
    public static final int BLOCK_SIZE = 25;

    //Estructuras principales

    private List<Cup> cups;
    private List<Lid> standaloneLids;
    private List<Rectangle> heightMarks;
    private List<Lid> lidInsertionOrder;
    
    
    //Estado de la torre
       

    private boolean isVisible;
    private int width;
    private int maxHeight;
    
    private static final int DefaultWidth = 300;
    private static final int DefaultMaxHeight = 1000;
    
    private static int CupsCount = 0;
    private static int lidsCount = 0;


    /**
     * Construye una torre con ancho visual y altura máxima.
     */
    public Tower(int width, int maxHeight) {
        this.width = width;
        this.maxHeight = maxHeight;
        cups = new ArrayList<>();
        heightMarks = new ArrayList<>();
        standaloneLids = new ArrayList<>();
        lidInsertionOrder = new ArrayList<>();
        drawHeightMarks();
        isVisible = false;
    }
    
    /**
     * Construye una torre con el número de tazas dado.
     */
    public Tower(int cups) {
        this(DefaultWidth, DefaultMaxHeight);

        if (cups <= 0) {
            return;
        }
        int vc = 1;
        for (int id = cups; vc <= id; vc++) {
            pushCup(vc);
        }
    }

    
    /**
     * Agrega una tapa a la taza indicada.
     * Valida altura máxima antes de insertarla.
     */
    public void pushLid(int id) {
        pushLid(id, "normal");
    }

    /**
     * Agrega una tapa de tipo especifico.
     */
    public void pushLid(int id, String type) {
        try {
            Cup cup = getCupById(id);

            if (lidExistsInTower(id)) {
                throw new TowerException(TowerException.DUPLICATE_ITEM);
            }

            int lidSize = sizeFromId(id);
            Lid lid = createLidByType(id, lidSize, getColorForSize(), type);

            lid.placeInTower(this, cup);
        } catch (TowerException e) {
            if (isVisible) {
                javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }
    

    /**
     * Agrega una taza respetando reglas de anidamiento y altura máxima.
     */
    public void pushCup(int id) {
        pushCup(id, "normal");
    }

    /**
     * Agrega una taza de tipo especifico.
     */
    public void pushCup(int id, String type) {
        try {
            if (id == 0) return; 
            if (id < 0) {
                throw new TowerException("El identificador de la taza debe ser mayor que 0.");
            }

            int size = sizeFromId(id);
    
            for (Cup c : cups) {
                if (c.getId() == id) {
                    throw new TowerException(TowerException.DUPLICATE_ITEM);
                }
            }
            if (CupsCount >= 6){CupsCount = 0;}
            Cup newCup = createCupByType(id, size, getColorForSize(), type);
            
            int targetX = TOWER_X - (size * BLOCK_SIZE) / 2;
            int currentTopY = findTopElementY();
            int targetY;

            if (cups.isEmpty()) {
                targetY = BASE_Y - newCup.getRealPixelHeight();
            } else {
                Cup topCup = cups.get(cups.size() - 1);
                boolean fitsInsideTop = newCup.getSize() < topCup.getSize();
                boolean canNestWithCurrentTop = topCup.canNest(newCup);

                if (hasStandaloneLidAtTop(currentTopY)) {
                    targetY = currentTopY - newCup.getRealPixelHeight();
                } else if (fitsInsideTop && canNestWithCurrentTop) {
                    targetY = getNestedTargetY(topCup, newCup);
                } else {
                    Cup ancestorContainer = findAncestorContainerFor(topCup, newCup.getSize(), cups.size() - 1);
                    if (ancestorContainer != null) {
                        Cup supportCup = findBestSupportInsideAncestor(ancestorContainer, newCup.getSize(), cups.size() - 1);
                        if (supportCup != null) {
                            targetY = getStackedAboveCupY(supportCup, newCup);
                        } else {
                            targetY = getNestedTargetY(ancestorContainer, newCup);
                        }
                    } else {
                        targetY = currentTopY - newCup.getRealPixelHeight();
                    }
                }
            }
                    
            int projectedHeight = BASE_Y - targetY;

            if (projectedHeight > maxHeight) {
                throw new TowerException(TowerException.HEIGHT_EXCEEDED);
            }

            newCup.moveTo(targetX, targetY);
            
            newCup.applyPreStackEffect(this);
            
            cups.add(newCup);
            rebuildTower();
            
            newCup.applyPostStackEffect(this);
            
            if (isVisible) newCup.makeVisible();
            CupsCount++;
        } catch (TowerException e) {
            if (isVisible) {
                javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    //Eliminación de elementos
       

    /**
     * Elimina una taza por identificador y reconstruye la torre.
     */
    public void removeCup(int id) {
        try {
            Cup toRemove = getCupById(id);

            if (toRemove == null) {
                throw new TowerException(TowerException.ITEM_NOT_FOUND);
            }

            if (!toRemove.canBeRemoved(this)) {
                throw new TowerException(TowerException.ITEM_LOCKED);
            }

            if (toRemove.hasLids()) {
                for (Lid lid : toRemove.getLids()) {
                    lid.makeInvisible();
                    lidInsertionOrder.remove(lid);
                }
                toRemove.getLids().clear();
            }

            toRemove.makeInvisible();
            cups.remove(toRemove);
            rebuildTower();
        } catch (TowerException e) {
            if (isVisible) {
                javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    /**
     * Elimina la tapa superior de la taza indicada.
     */
    public void removeLid(int id) {
        try {
            Cup cup = getCupById(id);

            if (cup != null) {
                if (!cup.hasLids()) {
                    throw new TowerException("La taza " + id + " no tiene tapa.");
                }

                Lid topLid = cup.getLids().get(cup.getLids().size() - 1);
                if (!topLid.canBeRemoved(this)) {
                    throw new TowerException(TowerException.ITEM_LOCKED);
                }

                cup.removeTopLid();
                removeLastInsertedReferenceById(id);
                rebuildTower();     
                return;
            }
            Lid removed = removeStandaloneLidById(id);
            if (removed == null) {
                throw new TowerException(TowerException.ITEM_NOT_FOUND);
            }

            removed.makeInvisible();
            lidInsertionOrder.remove(removed);
            rebuildTower();
        } catch (TowerException e) {
            if (isVisible) {
                javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }


    /**
     * Elimina la última taza insertada.
     */
    public void popCup() {
        try {
            if (cups.isEmpty()) {
                throw new TowerException(TowerException.ITEM_NOT_FOUND);
            }

            Cup removedCup = cups.get(cups.size() - 1);

            if (!removedCup.canBeRemoved(this)) {
                throw new TowerException(TowerException.ITEM_LOCKED);
            }
            
            removeLidReferencesByCup(removedCup);
            removedCup.removeAllLids();
            removedCup.makeInvisible();

            cups.remove(cups.size() - 1);

            rebuildTower();
        } catch (TowerException e) {
            if (isVisible) {
                javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }
    
    /**
     * Elimina la última tapa insertada en la torre.
     */
    public void popLid() {
        try {
            if (lidInsertionOrder.isEmpty()) {
                throw new TowerException(TowerException.ITEM_NOT_FOUND);
            }

            Lid lastInsertedLid = lidInsertionOrder.remove(lidInsertionOrder.size() - 1);

            Cup containingCup = findCupContainingLid(lastInsertedLid);
            if (!lastInsertedLid.canBeRemoved(this)) {
                lidInsertionOrder.add(lastInsertedLid);
                throw new TowerException(TowerException.ITEM_LOCKED);
            }

            if (!standaloneLids.remove(lastInsertedLid)) {
                for (Cup cup : cups) {
                    if (cup.getLids().remove(lastInsertedLid)) {
                        break;
                    }
                }
            }

            lastInsertedLid.makeInvisible();
            rebuildTower();
        } catch (TowerException e) {
            if (isVisible) {
                javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }
    

    //Reordenamiento

    /**
     * Invierte el orden actual de la torre.
     */
    public void reverseTower() {
        try {
            if (cups.isEmpty() && standaloneLids.isEmpty()) return;
            
            Collections.reverse(cups);
            
            for (Cup cup : cups) {
                Collections.reverse(cup.getLids());
            }
            Collections.reverse(standaloneLids);
            
            if (!fitsWithinHeight()) {
                Collections.reverse(cups);
                for (Cup cup : cups) {
                    Collections.reverse(cup.getLids());
                }
                Collections.reverse(standaloneLids);
                
                throw new TowerException(TowerException.HEIGHT_EXCEEDED);
            }

            rebuildTower();
        } catch (TowerException e) {
            if (isVisible) {
                javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    /**
     * Intercambio dos objetos de la torre y deben ser del mismo tipo. Sin cambiar la lógica visual o interna. 
     *
     */
    public void swap(String[] o1, String[] o2) {
        try {
            if (!isValidObjectRef(o1) || !isValidObjectRef(o2)) {
                throw new TowerException("Cada objeto debe tener formato {\"tipo\", \"id\"}.");
            }

            String type1 = o1[0].trim().toLowerCase();
            String type2 = o2[0].trim().toLowerCase();

            int id1;
            int id2;

            try {
                id1 = Integer.parseInt(o1[1].trim());
                id2 = Integer.parseInt(o2[1].trim());
            } catch (NumberFormatException e) {
                throw new TowerException("Los identificadores deben ser numéricos.");
            }

            if (type1.equals("cup") && type2.equals("cup")) {
                int cupIndex1 = findCupIndexById(id1);
                int cupIndex2 = findCupIndexById(id2);

                if (cupIndex1 == -1 || cupIndex2 == -1) {
                    throw new TowerException(TowerException.ITEM_NOT_FOUND);
                }

                Collections.swap(cups, cupIndex1, cupIndex2);
                rebuildTower();
                return;
            }

            if (type1.equals("lid") && type2.equals("lid")) {
                int[] lidRef1 = findLidRefById(id1);
                int[] lidRef2 = findLidRefById(id2);

                if (lidRef1 == null || lidRef2 == null) {
                    throw new TowerException(TowerException.ITEM_NOT_FOUND);
                }

                Cup cup1 = cups.get(lidRef1[0]);
                Cup cup2 = cups.get(lidRef2[0]);

                Lid lid1 = cup1.getLids().get(lidRef1[1]);
                Lid lid2 = cup2.getLids().get(lidRef2[1]);

                cup1.getLids().set(lidRef1[1], lid2);
                cup2.getLids().set(lidRef2[1], lid1);

                rebuildTower();
                return;
            }

            throw new TowerException("Solo se pueden intercambiar objetos del mismo tipo (cup-cup o lid-lid).");
        } catch (TowerException e) {
            if (isVisible) {
                javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }
    
    /**
     * Ordena la torre de mayor a menor tamaño.
     */
    public void orderTower() {
        try {
            if (cups.isEmpty()) return;

            ArrayList<Cup> original = new ArrayList<>(cups);
            ArrayList<Lid> originalStandalone = new ArrayList<>(standaloneLids);
            

            Collections.sort(original,
                    (c1, c2) -> Integer.compare(c2.getSize(), c1.getSize()));

            if (!fitsWithinHeight()) {
                throw new TowerException(TowerException.HEIGHT_EXCEEDED);
            }

            clearTowerVisual();

            for (Cup c : original) {
                pushCup(c.getId());
                for (Lid lid : c.getLids()) {
                    pushLid(c.getId());
                }
            }
            
            for (Lid lid : originalStandalone) {
                pushLid(lid.getId());
            }
        } catch (TowerException e) {
            if (isVisible) {
                javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    /**
     * Cubre las tazas que no tienen tapa usando las tapas ya existentes en la torre.
     */
    public void cover() {
        boolean movedAnyLid = false;

        for (Cup cup : cups) {
            if (cup.hasLids()) {
                continue;
            }

            Lid matchingLid = detachLidById(cup.getId());
            if (matchingLid == null) {
                continue;
            }

            cup.addLid(matchingLid);
            movedAnyLid = true;
        }

        if (movedAnyLid) {
            rebuildTower();
        }
    }
    
    /**
     * Retorna un intercambio que reduzca la altura de la torre.
     *
     * El resultado tiene el formato:
     * {{"cup|lid", "id"}, {"cup|lid", "id"}}
     *
     * Si no existe un intercambio que reduzca la altura al menos en 1,
     * retorna una matriz vacía.
     */
    public String[][] swapToReduce() {
        int currentHeight = height();
        int bestHeight = currentHeight;
        String[][] bestSwap = new String[0][0];

        for (int i = 0; i < cups.size(); i++) {
            for (int j = i + 1; j < cups.size(); j++) {
                Collections.swap(cups, i, j);
                rebuildTower();

                int candidateHeight = height();
                if (candidateHeight < bestHeight) {
                    bestHeight = candidateHeight;
                    bestSwap = new String[][]{
                        {"cup", String.valueOf(cups.get(i).getId())},
                        {"cup", String.valueOf(cups.get(j).getId())}
                    };
                }

                Collections.swap(cups, i, j);
                rebuildTower();
            }
        }

        ArrayList<LidLocation> lidLocations = getAllLidLocations();
        for (int i = 0; i < lidLocations.size(); i++) {
            for (int j = i + 1; j < lidLocations.size(); j++) {
                LidLocation first = lidLocations.get(i);
                LidLocation second = lidLocations.get(j);

                Lid firstLid = first.getLid(this);
                Lid secondLid = second.getLid(this);
                first.setLid(this, secondLid);
                second.setLid(this, firstLid);

                rebuildTower();

                int candidateHeight = height();
                if (candidateHeight < bestHeight) {
                    bestHeight = candidateHeight;
                    bestSwap = new String[][]{
                        {"lid", String.valueOf(firstLid.getId())},
                        {"lid", String.valueOf(secondLid.getId())}
                    };
                }

                firstLid = first.getLid(this);
                secondLid = second.getLid(this);
                first.setLid(this, secondLid);
                second.setLid(this, firstLid);
                rebuildTower();
            }
        }

        return (currentHeight - bestHeight) >= 1 ? bestSwap : new String[0][0];
    }
    
    
    /**
     * Genera una configuración objetivo de alturas para n tazas y altura h.
     * Delega en el algoritmo del contrato StackingCups.
     */
    public List<Integer> targetConfiguration(int n, int h) {
        return TowerContest.algorithmStackingCups(n, h);
    }

    /**
     * Construye la torre a partir de una lista de alturas (2*id-1).
     * Limpia la torre actual y agrega las tazas en el orden dado.
     */
    public void rebuildFromHeights(List<Integer> heights) throws tower.TowerException {
        clearTowerVisual();
        if (heights == null) {
            return;
        }

        for (Integer height : heights) {
            if (height == null || height <= 0 || (height % 2 == 0)) {
                continue;
            }
            int id = (height + 1) / 2;
            pushCup(id);
        }
    }

    // Consultas

    /**
     * Retorna la altura actual en centímetros.
     */
    public int height() {

        return getCurrentHeight() / BLOCK_SIZE;
    }

    /**
     * Retorna la altura actual en píxeles.
     */
    public int getCurrentHeight() {

        int topY = findTopElementY();
        return topY == BASE_Y ? 0 : BASE_Y - topY;
        }
    

    /**
     * Retorna los identificadores de tazas con tapas.
     */
    public int[] liddedCups() {

        ArrayList<Integer> result = new ArrayList<>();

        for (Cup c : cups) {
            if (c.hasLids()) result.add(c.getId());
        }

        int[] array = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            array[i] = result.get(i);
        }

        return array;
    }

    /**
     * Retorna la lista de elementos apilados.
     */
    public String[][] stackingItems() {

        ArrayList<String[]> items = new ArrayList<>();

        for (Cup c : cups) {
            items.add(new String[]{"Cup", String.valueOf(c.getId())});
            for (Lid lid : c.getLids()) {
                items.add(new String[]{"Lid", String.valueOf(lid.getId())});
            }
        }
        
        for (Lid lid : standaloneLids) {
            items.add(new String[]{"Lid", String.valueOf(lid.getId())});
        }
        

        String[][] result = new String[items.size()][2];
        for (int i = 0; i < items.size(); i++) {
            result[i] = items.get(i);
        }

        return result;
    }

    /**
     * Verifica si la torre está en estado válido.
     */
    public boolean ok() {

        if (height() > maxHeight) return false;

        int minY = BASE_Y - maxHeight;

        for (Cup c : cups) {
            if (c.getY() < minY) return false;
            for (Lid lid : c.getLids()) {
                if (lid.getY() < minY) return false;
            }
            if (c.getLids() == null) return false;
        }
        
        for (Lid lid : standaloneLids) {
            if (lid.getY() < minY) return false;
        }
        

        return true;
    }

    //Control visual

    /**
     * Hace visible la torre.
     */
    public void makeVisible() {
        isVisible = true;
        drawHeightMarks();
        for (Cup c : cups) {
            c.makeVisible();
        }
        for (Lid lid : standaloneLids) {
            lid.makeVisible();
        }

        for (Lid lid : getAllLidsInTower()) {
            lid.makeVisible();
        }
        
    }
    
    public boolean isVisible() {
        return isVisible;
    }
    
    public int getMaxHeight() {
        return maxHeight;
    }

    /**
     * Hace invisible la torre.
     */
    public void makeInvisible() {
        isVisible = false;
        for (Cup c : cups) {
            c.makeInvisible();
        }
        for (Lid lid : standaloneLids) {
            lid.makeInvisible();
        }
        for (Lid lid : getAllLidsInTower()) {
            lid.makeInvisible();
        }

        for (Rectangle mark : heightMarks) {
            mark.makeInvisible();
        }
        
    }

    /**
     * Finaliza el simulador y limpia todos los elementos.
     */
    public void exit() {

        for (Cup c : cups) {
            for (Lid lid : c.getLids()) {
                lid.makeInvisible();
            }
            c.makeInvisible();
        }
        
        for (Lid lid : standaloneLids) {
            lid.makeInvisible();
        }
        
        cups.clear();
        standaloneLids.clear();
        lidInsertionOrder.clear();
        if (heightMarks != null) {
            for (Rectangle r : heightMarks) {
                r.makeInvisible();
            }
            heightMarks.clear();
        }

        isVisible = false;
    }

    //Métodos auxiliares

    /**
     * Reconstruye la torre desde la base.
     */
    public void rebuildTower() {

        for (Cup c : cups) c.makeInvisible();
        for (Lid lid : standaloneLids) lid.makeInvisible();
        
        int baseLidsCount = countBaseLids();
        int currentBaseY = BASE_Y;
        for (Lid lid : standaloneLids) {
            if (!lid.prefersBasePlacement()|| lid.prefersUnderCupPlacement()) {
                continue;
            }
            int lidX = TOWER_X - ((lid.getSize() * BLOCK_SIZE) / 2);
            int lidY = currentBaseY - BLOCK_SIZE;
            lid.moveTo(lidX, lidY);
            if (isVisible) {
                lid.makeVisible();
            }
            currentBaseY = lidY;
        }

        int currentTopY = BASE_Y - (baseLidsCount * BLOCK_SIZE);
        Cup topCup = null;

        for (int cupIndex = 0; cupIndex < cups.size(); cupIndex++) {
            Cup c = cups.get(cupIndex);
            int targetX = TOWER_X - (c.getPixelWidth() / 2);
            int targetY;

            if (topCup == null) {
                targetY = currentTopY - c.getRealPixelHeight();
            } else {
                boolean topHasLid = topCup.hasLids();
                boolean fitsInsideTop = c.getSize() < topCup.getSize();
                boolean canNestWithCurrentTop = !topHasLid || canNestAboveInnerLid(topCup);
                
                
                        
                if (hasStandaloneLidAtTop(currentTopY)) {
                    targetY = currentTopY - c.getRealPixelHeight();
                 } else if (fitsInsideTop && canNestWithCurrentTop) {
                    targetY = getNestedTargetY(topCup, c);
                } else {
                    Cup ancestorContainer = findAncestorContainerFor(topCup, c.getSize(), cupIndex - 1);
                    if (ancestorContainer != null) {
                        Cup supportCup = findBestSupportInsideAncestor(ancestorContainer, c.getSize(), cupIndex - 1);
                        if (supportCup != null) {
                            targetY = getStackedAboveCupY(supportCup, c);
                        } else {
                            targetY = getNestedTargetY(ancestorContainer, c);
                        }
                    } else {
                        targetY = currentTopY - c.getRealPixelHeight();
                    }
                }
            }
            
            int extraOffset = 0;
            for (Lid lid : standaloneLids) {
                extraOffset += lid.getOffsetContribution(c);
            }
            targetY -= extraOffset;
            
            c.moveTo(targetX, targetY);
            
            if (isVisible) c.makeVisible();
            
            if (c.getY() < currentTopY) {
                currentTopY = c.getY();
            }
            for (Lid lid : c.getLids()) {
                if (lid.getY() < currentTopY) {
                    currentTopY = lid.getY();
                }
            }

            topCup = c;
        }
        for (Lid lid : standaloneLids) {
            lid.positionSpecial(this);
        }

        for (Lid lid : standaloneLids) {
            if (lid.prefersBasePlacement() || lid.prefersUnderCupPlacement()) {
                continue;
            }
            int lidX = TOWER_X - ((lid.getSize() * BLOCK_SIZE) / 2);
            int lidY = currentTopY - BLOCK_SIZE;
            lid.moveTo(lidX, lidY);
            if (isVisible) lid.makeVisible();
            currentTopY = lidY;
        }
        refreshHierarchicalLocks();
        
    }

    /**
     * Verifica si la torre cabe dentro de la altura máxima.
     */
    private boolean fitsWithinHeight() {

        int currentTop = BASE_Y - (countBaseLids() * BLOCK_SIZE);

        for (Cup c : cups) {
            int projectedY = currentTop - c.getRealPixelHeight();
            currentTop = projectedY;
            for (Lid lid : c.getLids()) {
                currentTop -= BLOCK_SIZE;
            }
        }
        for (Lid lid : standaloneLids) {
            if (!lid.prefersBasePlacement()) {
                currentTop -= BLOCK_SIZE;
            }
        }
        
        int totalHeight = BASE_Y - currentTop;
        return totalHeight <= maxHeight;
    }

    /**
     * Limpia visualmente la torre.
     */
    private void clearTowerVisual() {

        for (Cup c : cups) {
            for (Lid lid : c.getLids()) {
                lid.makeInvisible();
            }
            c.makeInvisible();
        }
        
        for (Lid lid : standaloneLids) {
            lid.makeInvisible();
        }
        
        cups.clear();
        standaloneLids.clear();
        lidInsertionOrder.clear();
    }

    /**
     * Dibuja las marcas de altura.
     */
    private void drawHeightMarks() {

        for (Rectangle r : heightMarks) r.makeInvisible();
        heightMarks.clear();

        int marks = maxHeight / BLOCK_SIZE;

        for (int i = 0; i <= marks; i++) {

            Rectangle mark = new Rectangle();
            mark.changeColor("black");
            mark.changeSize(3, width);

            int y = BASE_Y - (i * BLOCK_SIZE);

            mark.moveHorizontal(TOWER_X - width / 2);
            mark.moveVertical(y);

            if (isVisible) mark.makeVisible();

            heightMarks.add(mark);
        }
    }

    /**
     * Retorna la coordenada superior más alta.
     */
    private int getTopY() {
        return findTopElementY();
    }

    private int findTopElementY() {
        int top = BASE_Y;

        for (Cup cup : cups) {
            top = Math.min(top, cup.getY());
            for (Lid lid : cup.getLids()) {
                top = Math.min(top, lid.getY());
            }
        }

        for (Lid lid : standaloneLids) {
            top = Math.min(top, lid.getY());
        }

        
        return top;
    }
    
    private Lid removeStandaloneLidById(int id) {
        for (int i = 0; i < standaloneLids.size(); i++) {
            Lid lid = standaloneLids.get(i);
            if (lid.getId() == id) {
                standaloneLids.remove(i);
                return lid;
            }
        }
        return null;
    }
    
     private Lid detachLidById(int id) {
        Lid detached = removeStandaloneLidById(id);
        if (detached != null) {
            return detached;
        }

        for (Cup sourceCup : cups) {
            ArrayList<Lid> lids = sourceCup.getLids();
            for (int lidIndex = 0; lidIndex < lids.size(); lidIndex++) {
                Lid lid = lids.get(lidIndex);
                if (lid.getId() == id) {
                    lids.remove(lidIndex);
                    return lid;
                }
            }
        }

        return null;
    }
    
    
    /**
     * Retorna una taza por identificador.
     */
    public Cup getCupById(int id) {
        for (Cup c : cups) {
            if (c.getId() == id) return c;
        }
        return null;
    }
    
    public void removeLidReferencesByCup(Cup cup) {
        for (Lid lid : cup.getLids()) {
            lidInsertionOrder.remove(lid);
        }
    }

    private void removeLastInsertedReferenceById(int id) {
        for (int i = lidInsertionOrder.size() - 1; i >= 0; i--) {
            if (lidInsertionOrder.get(i).getId() == id) {
                lidInsertionOrder.remove(i);
                return;
            }
        }
    }
    

     private int findCupIndexById(int id) {
        for (int i = 0; i < cups.size(); i++) {
            if (cups.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private int[] findLidRefById(int id) {
        for (int cupIndex = 0; cupIndex < cups.size(); cupIndex++) {
            ArrayList<Lid> cupLids = cups.get(cupIndex).getLids();
            for (int lidIndex = 0; lidIndex < cupLids.size(); lidIndex++) {
                if (cupLids.get(lidIndex).getId() == id) {
                    return new int[]{cupIndex, lidIndex};
                }
            }
        }
        return null;
    }

    private boolean isValidObjectRef(String[] objectRef) {
        return objectRef != null
                && objectRef.length == 2
                && objectRef[0] != null
                && objectRef[1] != null;
    }

    
    private int sizeFromId(int id) {
        return (2 * id) - 1;
    }
    
    /**
     * Retorna un color asociado al tamaño.
     */
    private String getColorForSize() {
        String[] colors = {"red", "blue", "green", "yellow", "magenta", "black"};
        return colors[CupsCount % colors.length];
    }

    private Cup createCupByType(int id, int size, String color, String type) {
        String normalizedType = normalizeType(type);
        if ("opener".equals(normalizedType)) {
            return new OpenerCup(id, size, color);
        }
        if ("hierarchical".equals(normalizedType)) {
            return new HierarchicalCup(id, size, color);
        }
        if ("iron".equals(normalizedType)) {
            return new IronCup(id, size, color);
        }
        
        return new Cup(id, size, color);
    }

    private Lid createLidByType(int id, int size, String color, String type) {
        String normalizedType = normalizeType(type);
        if ("fearful".equals(normalizedType)) {
            return new FearfulLid(id, size, color);
        }
        if ("crazy".equals(normalizedType)) {
            return new CrazyLid(id, size, color);
        }
        return new Lid(id, size, color);
    }

    private String normalizeType(String type) {
        if (type == null) {
            return "normal";
        }
        return type.trim().toLowerCase();
    }

    private int countBaseLids() {
        int count = 0;
        for (Lid lid : standaloneLids) {
            if (lid.prefersBasePlacement()) {
                count++;
            }
        }
        return count;
    }

    public void refreshHierarchicalLocks() {
        int floorY = BASE_Y - (countBaseLids() * BLOCK_SIZE);
        for (Cup cup : cups) {
            if (!cup.shouldDisplaceSmallerItems()) {
                continue;
            }
            if (cup.getY() + cup.getRealPixelHeight() == floorY) {
                cup.lockAtBase();
            }
        }
    }

    private Cup findCupContainingLid(Lid lid) {
        for (Cup cup : cups) {
            if (cup.getLids().contains(lid)) {
                return cup;
            }
        }
        return null;
    }
    
    private boolean lidExistsInTower(int id) {
        for (Cup c : cups) {
            for (Lid lid : c.getLids()) {
                if (lid.getId() == id) {
                    return true;
                }
            }
        }

        for (Lid lid : standaloneLids) {
            if (lid.getId() == id) {
                return true;
            }
        }

        return false;
    }
    
    private ArrayList<LidLocation> getAllLidLocations() {
        ArrayList<LidLocation> locations = new ArrayList<>();

        for (int cupIndex = 0; cupIndex < cups.size(); cupIndex++) {
            ArrayList<Lid> cupLids = cups.get(cupIndex).getLids();
            for (int lidIndex = 0; lidIndex < cupLids.size(); lidIndex++) {
                locations.add(new LidLocation(cupIndex, lidIndex, -1));
            }
        }

        for (int standaloneIndex = 0; standaloneIndex < standaloneLids.size(); standaloneIndex++) {
            locations.add(new LidLocation(-1, -1, standaloneIndex));
        }

        return locations;
    }

    private static class LidLocation {
        private final int cupIndex;
        private final int lidIndex;
        private final int standaloneIndex;

        LidLocation(int cupIndex, int lidIndex, int standaloneIndex) {
            this.cupIndex = cupIndex;
            this.lidIndex = lidIndex;
            this.standaloneIndex = standaloneIndex;
        }

        Lid getLid(Tower tower) {
            if (standaloneIndex >= 0) {
                return tower.standaloneLids.get(standaloneIndex);
            }
            return tower.cups.get(cupIndex).getLids().get(lidIndex);
        }

        void setLid(Tower tower, Lid lid) {
            if (standaloneIndex >= 0) {
                tower.standaloneLids.set(standaloneIndex, lid);
            } else {
                tower.cups.get(cupIndex).getLids().set(lidIndex, lid);
            }
        }
    }
    
    
     private int getNestedTargetY(Cup containerCup, Cup nestedCup) {
        int baseSupportY = containerCup.getY() + containerCup.getRealPixelHeight() - BLOCK_SIZE;
        int supportY = baseSupportY;
        
        if (canNestAboveInnerLid(containerCup)) {
            Lid innerLid = containerCup.getLids().get(containerCup.getLids().size() - 1);
            supportY = Math.min(baseSupportY, innerLid.getY());
        }

        return supportY - nestedCup.getRealPixelHeight();
    }

    private boolean canNestAboveInnerLid(Cup cup) {
        if (!cup.hasLids()) {
            return false;
        }

        Lid topLid = cup.getLids().get(cup.getLids().size() - 1);
        return topLid.getSize() < cup.getSize();
    }
    
    private Cup findAncestorContainerFor(Cup topCup, int candidateCupSize, int maxIndex) {
        if (topCup == null || maxIndex <= 0) {
            return null;
        }

        for (int i = maxIndex - 1; i >= 0; i--) {
            Cup container = cups.get(i);
            boolean topIsInsideContainer = topCup.getY() > container.getY();
            boolean candidateFitsContainer = candidateCupSize < container.getSize();
            boolean canNestOnContainerFloor = !container.hasLids() || canNestAboveInnerLid(container);

            if (topIsInsideContainer && candidateFitsContainer && canNestOnContainerFloor) {
                return container;
            }
        }

        return null;
    }
    
     private boolean hasStandaloneLidAtTop(int topY) {
        for (Lid lid : standaloneLids) {
            if (!lid.prefersBasePlacement() && !lid.prefersUnderCupPlacement() && lid.getY() == topY) {
                return true;
            }
        }
        return false;
    }
    
    public int countNestedLids(Cup cup) {
        int nested = 0;
        for (Lid lid : cup.getLids()) {
            if (lid.getSize() < cup.getSize()) {
                nested++;
            }
        }
        return nested;
    }

    public int countCoverLids(Cup cup) {
        int covers = 0;
        for (Lid lid : cup.getLids()) {
            if (lid.getSize() >= cup.getSize()) {
                covers++;
            }
        }
        return covers;
    }
    
    private boolean hasUnderCupLid(Cup cup) {
        for (Lid lid : standaloneLids) {
            if (lid.prefersUnderCupPlacement() && lid.getId() == cup.getId()) {
                return true;
            }
        }
        return false;
    }
    
     private int getStackedAboveCupY(Cup supportCup, Cup newCup) {
        return supportCup.getY() - newCup.getRealPixelHeight();
    }

    private Cup findBestSupportInsideAncestor(Cup ancestorCup, int candidateCupSize, int maxIndex) {
        Cup bestSupport = null;

        for (int i = 0; i <= maxIndex && i < cups.size(); i++) {
            Cup candidate = cups.get(i);

            if (candidate == ancestorCup) {
                continue;
            }

            boolean isInsideAncestor = candidate.getY() > ancestorCup.getY()
                    && candidate.getSize() < ancestorCup.getSize();
            boolean fitsAboveCandidate = candidate.getSize() < candidateCupSize;

            if (!isInsideAncestor || !fitsAboveCandidate) {
                continue;
            }

            if (bestSupport == null || candidate.getSize() > bestSupport.getSize()) {
                bestSupport = candidate;
            }
        }

        return bestSupport;
    }
    
    private ArrayList<Lid> getAllLidsInTower() {
        ArrayList<Lid> lids = new ArrayList<>();

        for (Cup cup : cups) {
            for (Lid lid : cup.getLids()) {
                if (!lids.contains(lid)) {
                    lids.add(lid);
                }
            }
        }

        for (Lid lid : standaloneLids) {
            if (!lids.contains(lid)) {
                lids.add(lid);
            }
        }

        for (Lid lid : lidInsertionOrder) {
            if (!lids.contains(lid)) {
                lids.add(lid);
            }
        }

        return lids;
    }
    
    public Cup resolveTargetCupForLid(Cup requestedCup, Lid lid) {
        Cup targetCup = requestedCup;

        if (cups.isEmpty()) {
            return targetCup;
        }

        Cup topCup = cups.get(cups.size() - 1);
        boolean lidFitsInsideTopCup = lid.getSize() < topCup.getSize();
        boolean canStackInsideTopCup = !topCup.hasLids() || canNestAboveInnerLid(topCup);

        if (targetCup == null) {
            boolean hasElementAboveTopCup = getTopY() < topCup.getY();
            if (hasElementAboveTopCup) {
                return null;
            }
            if (lidFitsInsideTopCup && canStackInsideTopCup) {
                return topCup;
            }
            return null;
        }

        if (targetCup != topCup && targetCup.getY() > topCup.getY()
                && lidFitsInsideTopCup && canStackInsideTopCup) {
            return topCup;
        }

        return targetCup;
    }
    
    
    
    public List<Cup> getCups() {
        return cups;
    }

    public List<Lid> getStandaloneLids() {
        return standaloneLids;
    }

    public List<Lid> getLidInsertionOrder() {
        return lidInsertionOrder;
    }

}
