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
public class TowerCharger extends BaseTower{


    //DOKUMENTATION -> SIEHE BASETOWER
    //fireBool ist für das zeichnen des dickeren strahls nach dem dmg machen
    boolean fireBool=false;

    public TowerCharger(int xPos,int yPos){
        this.xPos=xPos;
        this.yPos=yPos;
        type=3;
        speed = 1;
        dmg=95;
        range=120;
        coolDown=0;
        costBase= 35;
        costDMG=35;
        costRange=35;
        costSpeed = 35;
        PR_fireCountMax=20;
        PR_fireCount =20;
        name="Charge";
        dots=new ArrayList<>();
        dots.add(new BaseDOT());
        baseC=Color.magenta;
    }

    @Override
    public void fire(){
        //sobald es ein target gibt, wird gefeuert, und der laser aufgeladen
        if(target != null){
            isFireing=true;
            if(coolDown<100){
                coolDown+=speed;
            }else{
                //danach wird dmg gemacht, und der dicke laser gezeichnet
                coolDown=0;
                target.hit(this, dots);
                PR_fireCount=PR_fireCountMax;
                fireBool=true;
            }
            if(fireBool){
                PR_fireCount--;
                if(PR_fireCount==0){
                    fireBool=false;
                }
            }
        }else{
            //coolDown=0;
            isFireing=false;
            fireBool=false;
        }
   }

    @Override
    public void drawFire(Graphics2D g2){
        if(isFireing && target!=null){
            g2.setColor(baseC);
            if(fireBool){
                //dicker dmglaser
                g2.setStroke(new BasicStroke(4));
                g2.drawLine(xPos,yPos,target.xPos,target.yPos);
                g2.setStroke(new BasicStroke(2));
                g2.setColor(Color.white);
                g2.drawLine(xPos,yPos,target.xPos,target.yPos);
            }else{
                //dünner ziellaser
                g2.setStroke(new BasicStroke(1));
                Composite tmpC = g2.getComposite();
                AlphaComposite a = (AlphaComposite)tmpC;
                g2.setComposite(a.derive((float)0.5));
                g2.drawLine(xPos, yPos, target.xPos, target.yPos);
                g2.setComposite(tmpC);
            }
        }
    }

}
