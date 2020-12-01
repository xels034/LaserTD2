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
public class TowerSlower extends BaseTower{


    //DOKUMENTATION -> SIEHE BASETOWER
    //range des AOE effekts des slowing
    private int AOErange=50;
    public TowerSlower(int xPos,int yPos){
        this.xPos=xPos;
        this.yPos=yPos;
        type=2;
        speed = 2;
        dmg=10;
        range=100;
        costBase= 45;
        costDMG=45;
        costRange=45;
        costSpeed = 45;
        PR_fireCountMax=25;
        PR_fireCount =25;
        name="Slower";
        dots=new ArrayList<>();
        dots.add(new DOTSlow());
        baseC=Color.green;
    }

    @Override
    public void fire(){
        ArrayList<BaseMob> addList;
        if(coolDown >= 100){
            if(target != null){
                //arbeitet ähnlich wie BaseTower, nur mit weiteren zielen zum
                //slowen
                addList=Grid.AOEhelper(target, AOErange);
                target.hit(this, dots);
                for(BaseMob m:addList){
                    m.hit(this,dots);
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
        if(isFireing && target!=null){
            //laserstrahl zum ziel des towers
            g2.setStroke(new BasicStroke(2));
            g2.setColor(baseC);
            g2.drawLine(xPos,yPos,target.xPos,target.yPos);
            //zeichnen der aoe range des slowing
            g2.setStroke(new BasicStroke(3));
            Composite tmpC = g2.getComposite();
            AlphaComposite a = (AlphaComposite)tmpC;
            g2.setComposite(a.derive((float)0.1));
            g2.fillOval(target.xPos-AOErange, target.yPos-AOErange, AOErange*2, AOErange*2);
            g2.setComposite(a.derive((float)0.5));
            g2.setStroke(new BasicStroke(1));
            g2.drawOval(target.xPos-AOErange, target.yPos-AOErange, AOErange*2, AOErange*2);
            g2.setComposite(tmpC);
        }
    }

}
