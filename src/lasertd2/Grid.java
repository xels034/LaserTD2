/*
*      Author:   Lisowski Dominik
*      Klasse:   4AHDVK
*  Class-Name:   Grid
*
*/

package lasertd2;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 *
 * @author Xels
 */
public class Grid {
    //Liste der Tower
   public ArrayList<BaseTower> tl;
   //Liste der Mobs
   public static ArrayList<BaseMob> ml;
   //"Hauptstrecke"
   public MobTrack mt;
   //strecken, wo mobs weiter außen fahren:
   //--------------------------------------
   //MobTrack die horizontal höher liegt
   private MobTrack mtHigh;
   //MobTrack die horizontal tiefer liegt
   private MobTrack mtLow;
   //track count, für spawnende mobs. die dann eine der 3 bahnen nehmen
   private int track=0;
   //geld des spielers zu beginn
   public int money = 2000;
   public int lives = 30;
   public int points = 0;
   public int level = 0;
   //benötigt für das management von mobs
   public boolean inWave = false;
   //anzahl der mobs, die noch zu spawnen sind
   private int remMobs;
   //zeitintervall, bis zum neuen mobspawn
   private int timer;

   public Grid(){
      tl = new ArrayList<>();
      ml = new ArrayList<>();
      mt = new MobTrack(0);
      mtHigh=new MobTrack(10);
      mtLow=new MobTrack(-10);
   }

   public boolean addTower(int x, int y, int type){
       BaseTower t = new BaseTower();
       switch (type){
           case 0:
               t = new TowerPulse(x,y);
               break;
           case 1:
               t = new TowerImpulse(x,y);
               break;
           case 2:
               t = new TowerSlower(x,y);
               break;
           case 3:
               t = new TowerCharger(x,y);
               break;
           case 4:
               t = new TowerRailgun(x,y);
               break;
           case 5:
               t = new TowerFreezer(x,y);
               break;
           case 6:
               t = new TowerChain(x,y);
               break;
       }

      if(isFree(x,y,t.radius) && money>=t.costDMG && type>-1){
         tl.add(t);
         money-=t.costDMG;
         return true;
      }else{
         return false;
      }
   }

   public void removeTower(int x, int y){
      int i= findTower(x,y);
      if(i>-1){
         money+=tl.get(i).detSellCost();
         tl.remove(i);
      }
   }

   public void upgradeDMG(int x, int y){
      int i= findTower(x,y);
      if(i>-1){
         BaseTower t = tl.get(i);
         money-=t.upgradeDMG(money);
      }
   }

   public void upgradeRange(int x, int y){
      int i= findTower(x,y);
      if(i>-1){
         BaseTower t = tl.get(i);
         money-=t.upgradeRange(money);
      }
   }
   
   public void upgradeSpeed(int x, int y){
      int i= findTower(x,y);
      if(i>-1){
         BaseTower t = tl.get(i);
         money-=t.upgradeSpeed(money);
      }
   }

   public int findTower(int x, int y){
      BaseTower t;
      for(int i=0;i<tl.size();i++){
         t = tl.get(i);
         if(x>= (t.xPos-t.radius*2) && x<= (t.xPos+t.radius*2) &&
            y>= (t.yPos-t.radius*2) && y<= (t.yPos+t.radius*2)){
            return i;
         }
      }
      return -1;
   }

   private boolean isFree(int x, int y, int radius){
      int xBegin;
      int yBegin;
      int xEnd;
      int yEnd;

      //ob innerhalb der track-------------
      for(int i=0;i<mt.xList.length-1;i++){
         xBegin = mt.xList[i];
         yBegin = mt.yList[i];
         xEnd = mt.xList[i+1];
         yEnd = mt.yList[i+1];
         if(xBegin == xEnd && ((x+radius)>xBegin-mt.radius && (x-radius)<xBegin+mt.radius)){
            //wenn track vertikal, und x in der strecke
            if(yBegin < yEnd){//track nachunten
               if((y+radius)>yBegin-mt.radius && (y-radius)<yEnd+mt.radius){
                  return false;
               }
            }else{//track nachoben
               if((y+radius)>yEnd-mt.radius && (y-radius)<yBegin+mt.radius){
                  return false;
               }
            }
         }else if(yBegin == yEnd &&((y+radius)>yBegin-mt.radius && (y-radius)<yBegin+mt.radius)){//horizontale linie
            //wenn track horizintal, und y in der strecke
            if(xBegin < xEnd){//track nach rechts
               if((x+radius)>xBegin-mt.radius && (x-radius)<xEnd+mt.radius){
                  return false;
               }
            }else{//track nach links
               if((x+radius)>xEnd-mt.radius && (x-radius)<xBegin+mt.radius){
                  return false;
               }
            }
         }
      }
      //----------ob innerhalb eines towers
      if(findTower(x,y)>=0){
         return false;
      }
      //----------ob nicht den rand des spielfeldes schneidend
      if((x-radius)<0 || (x+radius)>990 || (y-radius)<0 || (y+radius)>750){
         return false;
      }
      return true;
   }

   public void updateTargets(){
      BaseTower t;
      BaseMob tempM;
      for(int i=0;i<tl.size();i++){
         t=tl.get(i);
         //Mobliste von hinten durchgeben, weil das hinterste target am längsten
         //beschossen werden kann
         for (int j=ml.size()-1;j>=0;j--){
            tempM = ml.get(j);
            if(t.target == null){
               if(detDist(t,tempM) < t.range){
                  t.target = tempM;
                  tempM.targeted.add(t);
               }
            }else{
               if(detDist(t,t.target) > t.range){
                  t.target.targeted.remove(t);
                  t.target = null;
               }
            }
         }
      }
   }

   public void fireTowers(){
      for(BaseTower t:tl){
         t.fire();
      }
   }

   public void moveMobs(){
      BaseMob m;
      for(int i=0;i<ml.size();i++){
         m=ml.get(i);
         if(m.health<1){
            points+=m.points;
            money+=m.points;
            m.notifyTowers();
            ml.remove(i);
         }else{
            m.move();
         }
      }
      //Managen des spawnens einer neuen welle
      timer++;
      if (timer == 10){
         if(remMobs>0){
            timer = 0;
            switch(track){
                case 0:
                    ml.add(new BaseMob(this,mtHigh,mtHigh.offset));
                    break;
                case 1:
                    ml.add(new BaseMob(this,mt,0));
                    break;
                case 2:
                    ml.add(new BaseMob(this,mtLow,mtLow.offset));
                    break;
                case 3:
                    ml.add(new BaseMob(this,mtHigh,mtHigh.offset));
                    track=-1;
                    break;
            }
            track++;
            remMobs--;
         }
      }
      if(ml.size()==0 && remMobs == 0){
         stop();
      }
   }

   private void stop(){
      ml = new ArrayList<>();
      inWave=false;
      System.out.println("got stopped");
   }

   public void startMobs(){
      timer =0;
      remMobs = 20;
      inWave=true;
      System.out.println("Started Wave");
   }

   public static ArrayList<BaseMob> AOEhelper(BaseMob m,int range){
       //für slower und freezer, um weitere mobs im aoe zu finden
        ArrayList<BaseMob>targets = new ArrayList<>();
        for(BaseMob b:ml){
            if(detDist(b,m) < range){
                targets.add(b);
            }
        }
        return targets;
   }

   public static LinkedList<BaseMob> ChainHelper (BaseMob m,int range,int rec){
       //für den Chaintower, eine verbundene Liste von weiteren Mobsfinden
       //-rec ist die anzahl der übersprünge, die noch möglich sind
       //------------------
       //die Liste weiterer Kettenopfer
        LinkedList<BaseMob> chainTargets = new LinkedList<>();
        //Eine geordnete liste von ranges, also von mobs und der entfernung zu
        //denen, um den nähesten zu finden
        TreeSet<Ranges> possibleTargets = new TreeSet<>();
        //das opfer, welches zuletzt als ziel in der kette bestemmt wurde
        BaseMob currentVictim=m;
        //hilfsvariable
        BaseMob tmp;
        //der zu prüfende mob
        BaseMob lookAt;
        //entfernung zw den mobs
        double dist;
        while(rec>0){
            for(int i=0;i<ml.size();i++){
                lookAt=ml.get(i);
                if(!chainTargets.contains(lookAt)){
                    dist=detDist(currentVictim,lookAt);
                    if(dist<range){
                        //alle möglichen ziele in range landen hier
                        possibleTargets.add(new Ranges(dist,lookAt));
                    }
                }
            }
            //-------
            if(possibleTargets.size()==0){
                //ist die liste leer, kann die chain nicht mehr überspringen
                rec=0;
            }else{
                //der näheste mob wird in die chain aufgenommen
                tmp=possibleTargets.first().pointing;
                possibleTargets.clear();
                chainTargets.add(tmp);
                rec--;
                //dieser näheste mob, und nun das zuletzt ermittelte opfer
                currentVictim = tmp;
            }
        }
        return chainTargets;
    }

   public static ArrayList<BaseMob> RailHelper (Polygon ray){
       //der Railgun Tower zeichnet aufgrund seiner "ausrichtung" zu seinem
       //target einen strahl, ein rotiertes rechteck, welches als polygon
       //übermittelt wird. für jeden mob wird geprüft, ob es sich im polygon
       //befindet, und zuletzt return wird
       ArrayList<BaseMob> targets = new ArrayList<>();
       for(BaseMob bm:ml){
           if(ray.contains(bm.xPos, bm.yPos)){
               targets.add(bm);
           }
       }

       return targets;
   }

   private static double detDist (BaseMob a, BaseMob b){
       //der trigonoetrie sei dank, sind die differenzen der x und y
       //koordinaten die katheten eines rechtwinkligen dreiecks, und die
       //tatsächliche entfernung die hypotenuse
       //-gemacht für Mob-zu-Mob entfernungen
       return Math.hypot((double)a.xPos-b.xPos, (double)a.yPos-b.yPos);
   }

   private static double detDist (BaseTower a, BaseMob b){
       //der trigonoetrie sei dank, sind die differenzen der x und y
       //koordinaten die katheten eines rechtwinkligen dreiecks, und die
       //tatsächliche entfernung die hypotenuse
       //-gemacht für Turm-zu-Mob entfernungen
       return Math.hypot((double)a.xPos-b.xPos, (double)a.yPos-b.yPos);
   }
}
