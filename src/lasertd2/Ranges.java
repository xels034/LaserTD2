/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lasertd2;

/**
 *
 * @author Xels
 */
public class Ranges implements Comparable<Ranges>{


    //benötigt für listen von mobs, die nach ihrer entfernung zu einem punkt
    //sortierbar sein müssen
    public double range;
    public BaseMob pointing;
    
    public Ranges(double r,BaseMob b){
        range=r;
        pointing=b;
    }

    @Override
  public int compareTo(Ranges b2) {
        if(this.range>b2.range){
            return 1;
        }else if(this.range<b2.range){
            return -1;
        }else{
            return 0;
        }
    }

}
