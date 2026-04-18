package tower;

 

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
/**
 * Clase de TowerContest 
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TowerContest {
    public static List<Integer> algorithmStackingCups(int n, int h) {
        
        long minH = 2 * n - 1;
        long maxH = (long) n * n; // La suma de los primeros n impares es n^2
        
        // Verificación de imposibilidad matemática
        if (n == h){
             List<Integer> list = new ArrayList<>();
            for(int i = n ; i >= 1 ; i--){
                list.add(i);
            }
            return list;
        }
        
        if (h < minH || h > maxH) {
            return Collections.emptyList();
        }
        
        long R = h - minH;
        
        List<Long> left = new ArrayList<>();
        List<Long> right = new ArrayList<>();
        
        // Procesamos desde el vaso casi más grande hasta el más pequeño
        for (long i = n - 1; i >= 1; i--) {
            long cost = 2 * i - 1;
            
            if (R >= cost) {
                // Aporta su altura completa
                left.add(i);
                R -= cost;
            } else if (R == 0) {
                // Aporta cero, cae al fondo
                right.add(i);
            } else {
                // Aporte parcial: Encontramos el índice exacto para la inversión matemática
                int idx = (int) (2 * i - 2 - R);
                
                if (idx < right.size()) {
                    right.add(idx, i); // Esta operación O(N) ocurre MÁXIMO una vez
                    R = 0;
                } else {
                    right.add(i);
                }
            }
        }
        
        // Reconstruimos la salida imprimiendo las alturas (2 * id - 1)
        List<Integer> result = new ArrayList<>();
        
        // Los de la izquierda deben ir en orden estrictamente creciente
        for (int i = left.size() - 1; i >= 0; i--) {
            result.add((int)(2 * left.get(i) - 1));
        }
        
        // El vaso más grande actúa como el pico
        result.add((int)(2 * n - 1));
        
        // Los de la derecha construyen la caída
        for (long x : right) {
             result.add((int)(2 * x - 1));
        }
        
        return result;
    }

    public String solve (int n, int h){ 
        
        List<Integer> list = algorithmStackingCups(n, h);
        
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)); 
            
            if (i < list.size() - 1) {
                sb.append(" ");
            }
        }
        
        String result = sb.toString();
        
        return (list.size() > 0) ? result : "impossible";
    }
    
    private List<Integer> solveList (int n, int h){ 
        
        List<Integer> list = algorithmStackingCups(n,h);
        return (list.size() > 0) ? list: Collections.emptyList();
        }

    public void simulate (int n, int h){
        List<Integer> listSolve = solveList(n, h);
        if (listSolve.size() >= 1){
            Tower t = new Tower(2500, 2500);
            for (Integer i: listSolve){
                t.pushCup(i);
            }
            t.makeVisible();
        }
        else{
            System.out.println("impossible");
        }
    }
}
