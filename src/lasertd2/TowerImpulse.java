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
public class TowerImpulse extends BaseTower{


    //DOKUMENTATION -> SIEHE BASETOWER
 public TowerImpulse(int xPos,int yPos){
        this.xPos=xPos;
        this.yPos=yPos;
        type=1;
        speed = 100;
        dmg=(float)0.3;
        range=100;
        costBase= 45;
        costDMG=45;
        costRange=45;
        costSpeed = 45;
        PR_fireCountMax=1;
        PR_fireCount = 1;
        name="Impulse";
        dots=new ArrayList<>();
        dots.add(new BaseDOT());
        baseC=Color.red;
    }


    @Override
    public void fire(){
        //sobald ein ziel vorhanden ist, wird gefeuert und dmg gemacht
        if(target!=null){
            isFireing=true;
            target.hit(this, dots);
        }else{
            isFireing=false;
        }
    }

    @Override
    public void drawFire(Graphics2D g2){
        if(isFireing && target!=null){
            //einfacher laser zum ziel, dazu ein "kern-strahl" in weiß
            g2.setStroke(new BasicStroke(2));
            g2.setColor(baseC);
            g2.drawLine(xPos,yPos,target.xPos,target.yPos);

            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.white);
            g2.drawLine(xPos,yPos,target.xPos,target.yPos);
        }
    }
}
