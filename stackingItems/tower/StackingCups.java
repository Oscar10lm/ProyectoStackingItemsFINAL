package tower;

 

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;

public interface StackingCups {
    default List<Integer> algorithmStackingCups(int n, int h) {
        
        long minH = 2 * n - 1;
        long maxH = n * n; // La suma de los primeros n impares es n^2
        
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
}
