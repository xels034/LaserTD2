/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lasertd2;

/**
 *
 * @author Xels
 */
public class BaseDOT {
   public BaseMob PR_carrier;
   public int time;
   public int type;

   public void setCarrier(BaseMob car){
       PR_carrier=car;
   }

   public void work(){
        //Just nothing to do
   }

   public void decurse(){
        //Justnothing to do
   }

   @Override
public BaseDOT clone(){
       return new BaseDOT();
   }
}
