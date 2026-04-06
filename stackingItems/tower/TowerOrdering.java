package tower;

/**
 * Operaciones de reorganización y ordenamiento de la torre.
 */
public interface TowerOrdering {
    void orderTower();

    void reverseTower();

    void swap(String[] o1, String[] o2);

    void cover();
}