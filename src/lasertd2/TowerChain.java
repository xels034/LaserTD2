/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lasertd2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Xels
 */
public class TowerChain extends BaseTower{


    //DOKUMENTATION -> SIEHE BASETOWER
    private int AOErange=50;
    private LinkedList<BaseMob> chainTargets;
    public TowerChain(int xPos,int yPos){
        this.xPos=xPos;
        this.yPos=yPos;
        type=6;
        speed = 1.5f;
        dmg=30;
        range=100;
        costBase= 65;
        costDMG=65;
        costRange=65;
        costSpeed = 65;
        PR_fireCountMax=15;
        PR_fireCount =15;
        name="Chain";
        dots=new ArrayList<>();
        dots.add(new BaseDOT());
        chainTargets = new LinkedList<>();
        baseC=Color.cyan;
    }

    @Override
    public void fire(){
        if(coolDown >= 100){
            if(target != null){
                //arbeitet wie BaseTower, blos zusätzlich mit weiteren zielen
                //in einer kette
            chainTargets=Grid.ChainHelper(target, AOErange, 4);
            target.hit(this,dots);
            for(BaseMob m:chainTargets){
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
            isFireing = false;
         }
      }else{
         PR_manageFireCount();
         coolDown+=speed;
      }
   }

    @Override
    public void drawFire(Graphics2D g2){
        //linie zum target zeichnen, und sofern es chainTargets gibt,
        //zu denen eine Kette zeichnen
        if(isFireing && target != null){
            g2.setStroke(new BasicStroke(2));
            g2.setColor(baseC);
            g2.drawLine(xPos, yPos, target.xPos, target.yPos);
            if(chainTargets.get(0).equals(target)){
                for(int i=0;i<chainTargets.size()-1;i++){
                    g2.drawLine(chainTargets.get(i).xPos,
                                chainTargets.get(i).yPos,
                                chainTargets.get(i+1).xPos,
                                chainTargets.get(i+1).yPos);
                }
            }
        }
    }
}
