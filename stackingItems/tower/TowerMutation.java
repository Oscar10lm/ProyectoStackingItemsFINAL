package tower;

/**
 * Operaciones de mutación básica sobre la torre.
 */
public interface TowerMutation {
    void pushCup(int id);

    void pushLid(int id);

    void popCup();

    void popLid();

    void removeCup(int id);

    void removeLid(int id);
}