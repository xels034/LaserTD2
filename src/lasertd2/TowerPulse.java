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
public class TowerPulse extends BaseTower{


    //DOKUMENTATION -> SIEHE BASETOWER
    public TowerPulse(int xPos,int yPos){
        this.xPos=xPos;
        this.yPos=yPos;
        type=0;
        speed = 2;
        dmg=50;
        range=100;
        costBase= 25;
        costDMG=25;
        costRange=25;
        costSpeed = 25;
        PR_fireCountMax=15;
        PR_fireCount = 15;
        name="Pulse";
        dots=new ArrayList<>();
        dots.add(new BaseDOT());
        baseC=Color.orange;
    }

    @Override
    public void drawFire(Graphics2D g2){
        if(isFireing && target!=null){
            //simpler laser
            g2.setStroke(new BasicStroke(2));
            g2.setColor(baseC);
            g2.drawLine(xPos, yPos, target.xPos, target.yPos);
        }
    }
}
