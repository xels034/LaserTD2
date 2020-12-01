/*
*      Author:   Lisowski Dominik
*      Klasse:   4AHDVK
*  Class-Name:   MobTrack
*
*/

package lasertd2;


/**
 *
 * @author Xels
 */
public class MobTrack {

   public int[] xList = new int[22];
   public int[] yList = new int[22];
   //radius der strecke, nur zum zeichnen benötigt
   public int radius = 25;
   //offset einer strecke, kann benutzt werden, um von einer "hauptstrecke"
   //links und rechts weitere bahnen zu erzeugen
   public int offset;

   public MobTrack(int offs){
      //------1------
      this.offset=offs;
      xList[0] = 0;
      yList[0] = 70-offset;
      //------2------
      xList[1] = 850+offset;
      yList[1] = 70-offset;
      //------3------
      xList[2] = 850+offset;
      yList[2] = 250+offset;
      //------4------
      xList[3] = 700-offset;
      yList[3] = 250+offset;
      //------5------
      xList[4] = 700-offset;
      yList[4] = 190+offset;
      //------6------
      xList[5] = 100+offset;
      yList[5] = 190+offset;
      //------7------
      xList[6] = 100+offset;
      yList[6] = 300-offset;
      //------8------
      xList[7] = 550+offset;
      yList[7] = 300-offset;
      //------9------
      xList[8] = 550+offset;
      yList[8] = 410+offset;
      //-----10------
      xList[9] = 90+offset;
      yList[9] = 410+offset;
      //-----11------
      xList[10] = 90+offset;
      yList[10] = 700-offset;
      //-----12------
      xList[11] = 600-offset;
      yList[11] = 700-offset;
      //-----13------
      xList[12] = 600-offset;
      yList[12] = 600+offset;
      //-----14------
      xList[13] = 200-offset;
      yList[13] = 600+offset;
      //-----15------
      xList[14] = 200-offset;
      yList[14] = 520-offset;
      //-----16------
      xList[15] = 730+offset;
      yList[15] = 520-offset;
      //-----17------
      xList[16] = 730+offset;
      yList[16] = 680-offset;
      //-----18------
      xList[17] = 900-offset;
      yList[17] = 680-offset;
      //-----19------
      xList[18] = 900-offset;
      yList[18] = 420+offset;
      //-----20------
      xList[19] = 700-offset;
      yList[19] = 420+offset;
      //-----21------
      xList[20] = 700-offset;
      yList[20] = 340-offset;
      //-----22------
      xList[21] = 1000;
      yList[21] = 340-offset;
   }
}
