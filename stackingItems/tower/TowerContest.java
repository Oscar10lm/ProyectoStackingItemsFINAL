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
public class TowerContest implements StackingCups {
    public String solve (int n, int h){ 
        
        List<Integer> list = algorithmStackingCups(n,h);
        String result = list.stream().map(String::valueOf)
        .collect(java.util.stream.Collectors.joining(" "));
        return (list.size() > 0) ? result: "impossible";
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
