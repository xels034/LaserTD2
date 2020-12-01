/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lasertd2;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author Xels
 */
public class BaseMob {
    //Liste der Tower, die diesen Mob als Ziel haben
   public ArrayList<BaseTower> targeted;
   //Liste der DOTs, die auf das ziel wirken
   public ArrayList<BaseDOT> dots;
   //Das Grid, welches der Mob für div. funktionen braucht
   private Grid g;
   //da es 3 Tracks gibt, hat jeder mob eine referenz auf seine zugewiesene
   //track
   private MobTrack mt;
   public int xPos;
   public int yPos;
   public int type;
   public int health;
   public int baseHealth;
   public float shield;
   public float baseShield;
   public float shieldRegen;
   public float shieldOffset;
   //radius zum zeichnen des mobs
   public int radius;
   public int points;
   //zahl, wie viele pixel pro repaint gefahren wird
   public float stepSize;
   public float baseStepSize;
   //welche koordinate aus der MobTrack die nächste ist
   private int nextX;
   private int nextY;
   //welche koordinate aus der MobTrack die vorherige ist
   private int prevX;
   private int prevY;
   //die koordinate aus der MobTrack, zu der gefahren werden soll
   private int xBegin;
   private int yBegin;
   //die koordinate, aus der MobTrack, von der der Mob kommt
   private int xEnd;
   private int yEnd;
   //offset von der "Hauptstrecke" wird für das spawnen benötigt
   public int offset;


private void initBase(@SuppressWarnings("hiding") Grid g){
      xPos=0;
      yPos=70;
      type=0;
      baseHealth=350;
      health=baseHealth;
      baseShield=100;
      shield =baseShield;
      shieldRegen=0.2f;
      shieldOffset=0;
      stepSize=5f;
      baseStepSize=stepSize;
      nextX =1;
      nextY=1;
      prevX=0;
      prevY=0;
      radius=5;
      points = 5;
      targeted = new ArrayList<>();
      this.g = g;
      dots= new ArrayList<>();
   }

   private void initAdd(@SuppressWarnings("hiding") MobTrack mt, int o){
      offset=o;
      yPos=70-offset;
      this.mt=mt;
   }

   public BaseMob(Grid g){
      initBase(g);
   }

   public BaseMob(Grid g,MobTrack mt, int o){
      initBase(g);
      initAdd(mt,o);
   }

   public void drawMob(Graphics2D g2){
        float hWidth;
        float sWidth;
        //Mob Body
        g2.setColor(Color.getHSBColor(0.75f, 1, 0.5f));
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(xPos-radius, yPos-radius, radius*2, radius*2);
        //---------------
        //Mob Shield
        if(shield>0){
            //rand
            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.blue);
            g2.drawOval(xPos-radius*2, yPos-radius*2, radius*4, radius*4);
            //--------
            //transparentes schild
            Composite tmpC = g2.getComposite();
            AlphaComposite a = (AlphaComposite)tmpC;
            g2.setComposite(a.derive(0.15f));
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(xPos-(radius+2), yPos-(radius+2), (radius+2)*2, (radius+2)*2);
            g2.setComposite(tmpC);
            //--------
        }
        //---------------
        //Mob Healthbar
        //Lebensbalken
        g2.setStroke(new BasicStroke(1));
        hWidth=25*((float)health/(float)baseHealth);
        g2.setColor(Color.green);
        g2.fillRect(xPos-12, yPos, Math.round(hWidth), 3);
        //--------------------------
        //rahmen um den lebensbalken
        g2.setColor(Color.black);
        g2.drawRect(xPos-12, yPos, 25, 3);
        //----------------
        //Mob Shieldbar
        if(baseShield>0){
            //schildbalken
            sWidth=25*(shield/baseShield);
            g2.setColor(Color.blue);
            g2.fillRect(xPos-12, yPos+3, Math.round(sWidth), 3);
            //---------------------------
            //rahmen um den schildbalken
            g2.setColor(Color.black);
            g2.drawRect(xPos-12, yPos+3, 25, 3);
            //-------------------------
        }
   }

   public void move(){
       //schild regenerieren, sofern vorhanden
      if(baseShield>0 && shield<baseShield && shieldOffset == 0){
        shield+=shieldRegen;
      }else if(shieldOffset > 0){
        shieldOffset--;
      }
      //-------------
      //das fortbewegen:
      //
      //
      //sofern nicht am ende der strecke:
      if(nextX < mt.xList.length){
          //von wo der mob kommt:
         xBegin = mt.xList[prevX];
         yBegin = mt.yList[prevY];
         //wohin er fahren soll:
         xEnd = mt.xList[nextX];
         yEnd = mt.yList[nextY];
         //strecke ist vertikal:
         if(xBegin == xEnd){
             //nach oben
            if(yBegin > yEnd){
               yPos=(int)(yPos-stepSize);
               update(0);
               //nach unten
            }else{
               yPos=(int)(yPos+stepSize);
               update(2);
            }
            //strecke ist horizontal:
         }else if(yBegin == yEnd){
             //nach links
            if(xBegin > xEnd){
               xPos=(int)(xPos-stepSize);
               update(3);
               //nach rechts
            }else{
               xPos=(int)(xPos+stepSize);
               update(1);
            }
         }
         //wenn am ende angekommen, wird die strecke wieder von vorne befahren
      }else{
         xPos = 0;
         yPos = 70-offset;
         prevX = 0;
         prevY = 0;
         nextX = 1;
         nextY = 1;
         g.lives--;
      }
      //alle dots durcticken lassen, und decursen, wenn sie abgelaufen sind
      BaseDOT d;
      for(int i=0;i<dots.size();i++){
         d = dots.get(i);
         if(d.time == 0){
            d.decurse();
            dots.remove(i);
         }else{
            d.work();
         }
      }
   }

   private void update(int dir){
      boolean curve = false;
      switch(dir){
          //den mob tatsächlich bewegen, und kurven situationen erkennen und
          //behandeln
         case 0:// ^
            if(yPos-stepSize <= yEnd){
               curve =true;
            }
            break;
         case 1:// >
            if(xPos+stepSize >= xEnd){
               curve = true;
            }
            break;
         case 2:// v
            if(yPos+stepSize >= yEnd){
               curve = true;
            }
            break;
         case 3:// <
            if(xPos-stepSize <= xEnd){
               curve = true;
            }
            break;
      }
      //kurven situation, wenn durch die stepsize der eckpunkt überschritten
      //werden könnte. position auf den eckpunkt setzen, und richtung anpassen
      if(curve){
            xPos = xEnd;
            yPos = yEnd;
            nextX++;
            nextY++;
            prevX++;
            prevY++;
      }
   }

   public void hit(BaseTower parent,ArrayList<BaseDOT> givenDots){
       //wenn schild vorhanden ist, wird dieser zuerst abgezogen
       shieldOffset=250;
        if(shield>0){
            if(parent.dmg>shield){
                //sollte der dmg nicht ganz absorbiert werden, so wird der rest
                //dem health abgezogen
                health-=(parent.dmg-shield);
                shield=0;
            }else{
                shield-=parent.dmg;
            }
        }else{
            //ansonsten nur das health abziehen
            health-=parent.dmg;
        }
        BaseDOT tmp;
        //den DOT des towers auf den mob kopieren, da die "vorlage" des towers
        //nicht verwendet werden darf
        for(BaseDOT b:givenDots){
            tmp=b.clone();
            tmp.PR_carrier=this;
            dots.add(tmp);
        }
   }

   public void notifyTowers(){
       //wenn der mob stirbt, müssen die targets der tower auf null gesetzt
       //werden, da sonst nullpointer exceptions auftreten würden
      for(int i=0;i<targeted.size();i++){
         targeted.get(i).target = null;
      }
   }
}
