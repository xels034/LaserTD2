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
public class TowerFreezer extends BaseTower{

    //DOKUMENTATION -> SIEHE BASETOWER
    //range, in der gefreezt wird
    private int AOErange=30;
    public TowerFreezer(int xPos,int yPos){
        this.xPos=xPos;
        this.yPos=yPos;
        type=5;
        speed = 0.7f;
        dmg=5;
        range=100;
        costBase= 55;
        costDMG=55;
        costRange=55;
        costSpeed = 55;
        PR_fireCountMax=25;
        PR_fireCount =25;
        name="Freezer";
        dots=new ArrayList<>();
        dots.add(new DOTFreeze());
        baseC=Color.blue;
    }

    @Override
    public void fire(){
        ArrayList<BaseMob> addList;
        if(coolDown >= 100){
            if(target != null){
                //arbeitet wie BaseTower, nur mit weiteren targets in der AOE-
                //range
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
            //laser zum target zeichnen
            g2.setStroke(new BasicStroke(2));
            g2.setColor(baseC);
            g2.drawLine(xPos,yPos,target.xPos,target.yPos);
            g2.setStroke(new BasicStroke(3));
            //zeichnet einen kreis mit rand, und die aoe range des freez
            //zu visualisieren
            Composite tmpC = g2.getComposite();
            AlphaComposite a = (AlphaComposite)tmpC;
            g2.setComposite(a.derive(0.1f));
            g2.fillOval(target.xPos-AOErange, target.yPos-AOErange, AOErange*2, AOErange*2);
            g2.setComposite(a.derive(0.5f));
            g2.setStroke(new BasicStroke(1));
            g2.drawOval(target.xPos-AOErange, target.yPos-AOErange, AOErange*2, AOErange*2);
            g2.setComposite(tmpC);
        }
    }
}
