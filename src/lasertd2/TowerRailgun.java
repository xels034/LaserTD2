/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lasertd2;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;

/**
 *
 * @author Xels
 */
public class TowerRailgun extends BaseTower{

    //DOKUMENTATION -> SIEHE BASETOWER

    //radius des strahls, evtl veränderbar ... ka
     int rayRad;
     //benötigt, um den strahl weiterhin zu zeichnen, da dieser nicht wirklich
     //an ein bestimmtes target gebunden ist
     BaseMob fakeTarget;

     public TowerRailgun(int xPos,int yPos){
        this.xPos=xPos;
        this.yPos=yPos;
        type=4;
        speed = 0.4f;
        dmg=320;
        range=150;
        costBase= 40;
        costDMG=40;
        costRange=40;
        costSpeed = 40;
        PR_fireCountMax=(75/2);
        PR_fireCount =(75/2);
        name="Railgun";
        rayRad=5;
        dots=new ArrayList<>();
        dots.add(new BaseDOT());
        baseC=Color.getHSBColor(0.925f, 1, 0.75f);
    }

     @Override
     public void fire(){
        if(coolDown >= 100){
         if(target != null){
             //arbeitsweise ähnlich dem BaseTower, der dmg wird auf alle
             //innerhalb des strahles gemacht
            fakeTarget = new BaseMob(null);
            fakeTarget.xPos = target.xPos;
            fakeTarget.yPos = target.yPos;
            ArrayList<BaseMob> addList = Grid.RailHelper(makeRay(rayRad));
            for(BaseMob m:addList){
                m.hit(this, dots);
            }
            coolDown = 0;
            if(!isFireing){
               PR_fireCount = PR_fireCountMax;
               isFireing = true;
            }else{
                PR_manageFireCount();
            }
         }else{
            PR_manageFireCount();
         }
      }else{
         PR_manageFireCount();
         coolDown+=speed;
      }
     }

     @Override
     public void PR_manageFireCount(){
         //das feuern wird nur beendet, wenn der count unten ist, da es nicht
         //vom target abhängt
        PR_fireCount--;
        if(PR_fireCount == 0){
            isFireing = false;
            fakeTarget = null;
        }
     }

     @Override
     public void drawFire(Graphics2D g2){
         if(isFireing && fakeTarget!=null){
             //den berechneten strahl in polygonform, der für das ermitteln der
             //targets benötigt wurde, wird hier nocheinmal gezeichnet
            Composite tmpC = g2.getComposite();
            AlphaComposite a = (AlphaComposite)tmpC;
            g2.setComposite(a.derive(0.8f));
            g2.setColor(baseC);
            g2.fillPolygon(makeRay(5));
            g2.setColor(Color.white);
            g2.setComposite(tmpC);
            g2.fillPolygon(makeRay(2));
         }
     }

     private Polygon makeRay(int rad){
         //Erstellt einen strahl mit einer bestimmten dicke in pixeln, der
         //in richtung des targets des turms geht, und dann noch einmal 20 mal
         //weiter, oder bis max 1500px, um über das gesammte spielfeld zu
         //reichen
       Polygon p = new Polygon();
       if(fakeTarget!=null){
           int dirx = 20*(fakeTarget.xPos-xPos);  //Zielvektor
           int diry = 20*(fakeTarget.yPos-yPos);
           if(dirx>1500){
               dirx=1500;
           }
           if(diry>1500){
               diry=1500;
           }

           int norDirx=diry;  //Normalvektor des Ziels
           int norDiry=-dirx;

           //länge des normalvektors des zielvektors
           double dirValue=Math.hypot(norDirx, norDiry);

           double unitNorDirx=norDirx/dirValue; //Einheitsvektor des
           double unitNorDiry=norDiry/dirValue; //Normalvektors des Zielvektors

           int uNDX=(int)Math.floor(unitNorDirx + 0.5f);
           int uNDy=(int)Math.floor(unitNorDiry + 0.5f); //in int casten

           p.addPoint(xPos-(uNDX*rad), yPos-(uNDy*rad));
           p.addPoint(xPos-(uNDX*rad)+dirx, yPos-(uNDy*rad)+diry);
           p.addPoint(xPos+dirx+(uNDX*rad), yPos+diry+(uNDy*rad));
           p.addPoint(xPos+(uNDX*rad), yPos+(uNDy*rad)); //Polygon des Strahls
       }
       return p;
     }
}
