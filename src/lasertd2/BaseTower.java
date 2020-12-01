/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lasertd2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author Xels
 */
public class BaseTower {
   public int xPos;
   public int yPos;
   public int type;
   //range, in der der turm feuern kann
   public int range;
   //kosten des turms an sich
   public int costBase;
   public int costDMG;
   public int costRange;
   public int costSpeed;
   //wie viele repaint intervalle, um wieder feuern zu können
   public float coolDown=100;
   //wie vielpro intervall zum cooldown dazu gezählt wird, um wieder 100 zu
   //erreichen und feuern zu können
   public float speed;
   public float dmg;
   public float baseDmg;
   //radius zum zeichnen
   public int radius = 10;
   public int levelDMG = 1;
   public int levelRange = 1;
   public int levelSpeed =1;
   public String name ="";
   public BaseMob target=null;
   //ist diese variable true, wird drawFire() aufgerufen
   public boolean isFireing = false;
   //dots, die der tower weitergibt
   public ArrayList<BaseDOT> dots;
   //von diesem wert wird PR_fireCount subtrahiert
   public int PR_fireCountMax;
   //solange nicht 0, ist isFireing true
   public int PR_fireCount;
   //farbe des towers, zum zeichen div. dinge
   public Color baseC=Color.red;
   //rotation in Grad, die die 3 kugeln am turm haben
   public int rotModyfier=0;

   public static int getType(String name){
      if(name.equals("Pulse")){
         return 0;
      }else if(name.equals("Impulse")){
         return 1;
      }else if(name.equals("Slower")){
         return 2;
      }else if(name.equals("Charge")){
         return 3;
      }else if(name.equals("Railgun")){
         return 4;
      }else if(name.equals("Freezer")){
         return 5;
      }else if(name.equals("Chain")){
         return 6;
      }
      return -1;
   }

   public static String getName(int type){
      switch(type){
         case 0:
            return "Pulse";
         case(1):
            return "Impulse";
         case(2):
            return "Slower";
         case(3):
            return "Charge";
         case(4):
            return "Railgun";
         case(5):
            return "Freezer";
         case(6):
            return "Chain";
         default:
            return "-- UNKNOWN --";
      }
   }

   public int upgradeDMG(int money){
      if(money>(costDMG*(1.0+(levelDMG/10.0)))){
         costDMG+=(int)(1.0+(levelDMG/10.0));
         levelDMG++;
         dmg+=levelDMG*7;
         return costDMG;
      }
      return 0;
   }

   public int upgradeRange(int money){
      if(money>(costRange*(1.0+(levelRange/10.0)))){
         costRange+=(int)(1.0+(levelRange/10.0));
         levelRange++;
         range+=levelRange*7;
         return costRange;
      }
      return 0;
   }

   public int upgradeSpeed(int money){
      if(money>(costSpeed*(1.0+(levelSpeed/10.0)))){
         costSpeed+=(int)(1.0+(levelSpeed/10.0));
         levelSpeed++;
         speed+=(levelSpeed*7)/30f;
         return costSpeed;
      }
      return 0;
   }

   public int detSellCost(){
       //das geld, welches man beim verkaufen bekommt, ermitteln
      return (costDMG+costSpeed+costRange)/6;
   }

   public void PR_manageFireCount(){
       //den firecount subtrahieren, und bei bedarf aufhören drawFire()
       //aufzurufen
        PR_fireCount--;
        if(PR_fireCount == 0){
            isFireing = false;
        }
   }

   public void fire(){
       //sofern der cooldown ready ist:
      if(coolDown >= 100){
          //und ein ziel vorhanden ist:
         if(target != null){
             //wird das target getroffen, und die dots weiter gegeben
            target.hit(this,dots);
            coolDown = 0;
            //wenn noch nicht angefanen wird zu feuern, wird dies eingeleitet
            if(!isFireing){
               PR_fireCount = PR_fireCountMax;
               isFireing = true;
            }else{
                //ansonsten der Count behandelt
                PR_manageFireCount();
            }
         }else{
             //wenn keintarget vorhanden, wird nicht mehr gefeuert
            isFireing = false;
         }
      }else{
          //den count managen und den CD wieder auffüllen
        PR_manageFireCount();
         coolDown+=speed;
      }
   }

   public void drawFire(Graphics2D g2){
       if(isFireing && target!=null){
            g2.setStroke(new BasicStroke(2));
            g2.setColor(baseC);
            g2.drawLine(xPos, yPos, target.xPos, target.yPos);
       }
   }

   public void drawTower(Graphics2D g2){
       //aufruf mit der position des turmes möglich, oder mit fixen werten
       //also sind die fixen werte die des turmes
      drawTower(g2,xPos,yPos);
   }

   public void drawTower(Graphics2D g2,int x, int y){
       //"kern" des turmes zeichnen
       g2.setStroke(new BasicStroke(1));
       g2.setColor(baseC);
       g2.fillOval(x-radius/4, y-radius/4, radius/2, radius/2);
       g2.setColor(Color.black);
       g2.drawOval(x-radius/4, y-radius/4, radius/2, radius/2);
       //"rand des turmes
       g2.drawOval(x-radius, y-radius, radius*2, radius*2);
       //rotierendes
       //--------------------------
       //rotation der kugeln aktualisieren
       rotModyfier-=2;
       if(rotModyfier<=0){
           rotModyfier=360;
       }
       //1
       drawBall(g2,x,y,rotModyfier);
       //2
       drawBall(g2,x,y,rotModyfier+120);
       //3
       drawBall(g2,x,y,rotModyfier+240);
   }

   private void drawBall(Graphics2D g2, int x, int y,int rotDegree){
       double radiant;
       double tmpVal;
       int xOffs;
       int yOffs;
       //die rotation von Grad auf Rad umrechnen
       radiant= ((rotDegree)*Math.PI)/180;
       //davon den sinus -> x-Abstand vom kern
       tmpVal =x+((radius-radius/4)*Math.sin(radiant));
       tmpVal=Math.rint(tmpVal);
       xOffs = (int)tmpVal;
       //davon den cosinus .> y-Abstand vom kern
       tmpVal =  y+((radius-radius/4)*Math.cos(radiant));
       tmpVal = Math.rint(tmpVal);
       yOffs = (int)tmpVal;
       //kugel mit ermittelten x und y abständen vom kern zeichnen
       g2.setColor(baseC);
       g2.fillOval(xOffs-radius/4, yOffs-radius/4, radius/3, radius/3);
       g2.setColor(Color.black);
       g2.drawOval(xOffs-radius/4, yOffs-radius/4, radius/3, radius/3);
   }


}
