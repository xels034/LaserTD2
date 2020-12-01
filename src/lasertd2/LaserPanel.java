/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LaserPanel.java
 *
 * Created on 11.10.2009, 14:13:52
 */

package lasertd2;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xels
 */
public class LaserPanel extends javax.swing.JPanel {

  private static final long serialVersionUID = 1L;

  /** Creates new form LaserPanel */
    public LaserPanel() {
        initComponents();
    }

    //das grid mit allen relevanten spielinformationen
    Grid g = new Grid();
    Button selectedButton;
    //innerhalb des spiels, und nicht im begrüsungs(fail) fenster
    boolean start = false;
    boolean inGame = true;
    boolean prospected = false;
    boolean dead = false;
    //liste der buttons, die es gibt
    ArrayList<Button> bl = new ArrayList<>();
    //der repainter, mehr oder weniger die clocktime
    GridManager gm;
    //der selektierte turm, für angaben im info fenster und zum zeichnen der
    //range
    BaseTower selectedTower;
    //liste mit "dummy" towern für die beschreibungen
    ArrayList<BaseTower> prospect;
    int selectedType=-1;
    int globMouseX;
    int globMouseY;

    long timeStamp=0;
    long timeDiff=(long)0.01;

   @SuppressWarnings("deprecation")
@Override
   protected void paintComponent(@SuppressWarnings("hiding") Graphics g) {
      super.paintComponent(g);
      if(!dead){
         timeStamp=System.currentTimeMillis();
         Graphics2D g2 = (Graphics2D)g;
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         drawGridTrack(g2);
         drawFire(g2);
         drawTowers(g2);
         drawMobs(g2);
         g2.setStroke(new BasicStroke(1));
         g2.setColor(Color.white);
         g2.fillRect(1000, 0, 500, 800);
         g2.setColor(Color.black);
         g2.drawLine(1000, 0, 1000, 800);
         drawMenue(g2);
         drawInfo(g2);
         timeDiff=System.currentTimeMillis()-timeStamp;
         g2.setColor(Color.black);
         double t = 1000d/timeDiff;
         g2.drawString(""+t, 1000, 650);
      }else{
          //sollte man nicht verwenden, is mir aber egal xD
         gm.stop();
         drawFailMessage(g);
      }
   }

   private void drawFailMessage(Graphics g2){
      g2.drawString("Haha ... haha ..... You got OWNED!!", 500, 400);
      g2.drawString("Points: "+g.points, 500, 440);
      g2.drawString("Press LMB to restart ...", 500, 460);
   }

   private void drawGridTrack(Graphics2D g2){
      g2.setStroke(new BasicStroke((g.mt.radius+2)*2));
      g2.drawPolyline(g.mt.xList, g.mt.yList, g.mt.xList.length);
      g2.setColor(Color.LIGHT_GRAY);
      g2.setStroke(new BasicStroke(g.mt.radius*2));
      g2.drawPolyline(g.mt.xList, g.mt.yList, g.mt.xList.length);
      g2.setColor(Color.white);
      g2.fillRect(1000, 0, 300, 900);
      g2.setColor(Color.BLACK);
   }

   private void drawTowers(Graphics2D g2){
      for(BaseTower t:g.tl){
         if(t == selectedTower){//range zeichnen
            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.blue);
            Composite tmpC = g2.getComposite();
            AlphaComposite a = (AlphaComposite)tmpC;
            g2.setComposite(a.derive(0.15f));
            g2.fillOval(t.xPos-t.range, t.yPos-t.range,t.range*2, t.range*2);
            g2.setComposite(tmpC);
            g2.drawOval(t.xPos-t.range, t.yPos-t.range,t.range*2, t.range*2);
            g2.setColor(Color.black);
            g2.setStroke(new BasicStroke(2));
         }
         t.drawTower(g2);
         g2.setColor(Color.black);
      }
      if(prospected){
         selectedTower.drawTower(g2,globMouseX,globMouseY);
         g2.setStroke(new BasicStroke(1));
         g2.setColor(Color.blue);
         Composite tmpC = g2.getComposite();
         AlphaComposite a = (AlphaComposite)tmpC;
         g2.setComposite(a.derive(0.15f));
         g2.fillOval(globMouseX-selectedTower.range, globMouseY-selectedTower.range,
                     selectedTower.range*2, selectedTower.range*2);
         g2.setComposite(tmpC);
         g2.drawOval(globMouseX-selectedTower.range, globMouseY-selectedTower.range,
                     selectedTower.range*2, selectedTower.range*2);
         g2.setStroke(new BasicStroke(2));
         g2.setColor(Color.black);
      }
   }

   private void drawMobs(Graphics2D g2){
      if(g.inWave){
          for(int i=0;i<Grid.ml.size();i++){
              Grid.ml.get(i).drawMob(g2);
          }
      }
   }

   private void drawFire(Graphics2D g2){
      BaseTower t;
      for(int i=0;i<g.tl.size();i++){
         t = g.tl.get(i);
         t.drawFire(g2);
         g2.setColor(Color.black);
      }
   }

   private void drawMenue(Graphics2D g2){
      Button b;
      g2.setStroke(new BasicStroke(1));
      if(!bl.isEmpty()){
         //------Tower Buttons-------
         for(int i=0;i<7;i++){
            b=bl.get(i);
            g2.setColor(Color.getHSBColor(0.75f, 0.5f, 0.4f));
            g2.fillRoundRect(b.xPos, b.yPos, b.width, b.height, 15, 15);
            g2.setColor(Color.BLACK);
            g2.drawRoundRect(b.xPos, b.yPos, b.width, b.height, 15, 15);
            g2.setColor(Color.WHITE);
            g2.drawString(BaseTower.getName(b.type), b.xPos+20, b.yPos+30);
         }
         g2.setColor(Color.BLACK);
         //------Info Screen--------
         g2.setColor(Color.getHSBColor(0.75f, 0.25f, 0.7f));
         g2.fillRoundRect(1030, 200, 250, 40, 20, 20);
         g2.setColor(Color.LIGHT_GRAY);
         g2.fillRoundRect(1030, 250, 250, 310, 20, 20);
         g2.fillRoundRect(1030, 650, 250, 90, 20, 20);
         g2.setColor(Color.getHSBColor(0.7f, 0.25f, 0.75f));
         g2.fillRoundRect(1040, 350, 230, 90, 20, 20);
         g2.setColor(Color.getHSBColor(0.684f, 0.348f, 1));
         g2.fillRoundRect(1040, 460, 230, 90, 20, 20);
         g2.setColor(Color.getHSBColor(0.342f, 0.311f, 1));
         g2.fillRoundRect(1040, 260, 230, 80, 20, 20);
         g2.setColor(Color.BLACK);
         g2.drawRoundRect(1030, 250, 250, 310, 20, 20);
         g2.drawRoundRect(1030, 200, 250, 40, 20, 20);
         g2.drawRoundRect(1030, 650, 250, 90, 20, 20);
         g2.drawRoundRect(1040, 350, 230, 90, 20, 20);
         g2.drawRoundRect(1040, 460, 230, 90, 20, 20);
         g2.drawRoundRect(1040, 260, 230, 80, 20, 20);
         //-----action buttons------
         for(int i=7;i<12;i++){
            b=bl.get(i);
            g2.setColor(Color.getHSBColor(0.5f, 0.5f, 1));
            g2.fillRoundRect(b.xPos, b.yPos, b.width, b.height, 15, 15);
            g2.setColor(Color.BLACK);
            g2.drawRoundRect(b.xPos, b.yPos, b.width, b.height, 15, 15);
         }
         g2.drawString("SELL", bl.get(7).xPos+25, bl.get(7).yPos+30);
         g2.drawString("+ DMG", bl.get(8).xPos+15, bl.get(8).yPos+15);
         g2.drawString("+ Range", bl.get(9).xPos+15, bl.get(9).yPos+15);
         g2.drawString("+ Speed", bl.get(10).xPos+15, bl.get(10).yPos+15);
         g2.drawString("Next Wave", bl.get(11).xPos+15, bl.get(11).yPos+30);
      }else{
         g2.setColor(Color.getHSBColor(0.75f, 0.5f, 0.4f));
         g2.fillRoundRect(1010, 250, 275, 90, 20, 20);
         g2.setColor(Color.BLACK);
         g2.drawRoundRect(1010, 250, 275, 90, 20, 20);
      }
   }

   private void drawInfo(Graphics2D g2){
      g2.setStroke(new BasicStroke(1));
      if(start){
         g2.drawString("Selected Tower Type: "+BaseTower.getName(selectedType), 1050, 223);
         g2.drawString("Money: "+g.money, 1050, 670);
         g2.drawString("Lives "+g.lives, 1200, 670);
         g2.drawString("Points: "+g.points, 1050, 690);
         g2.drawString("Level :"+g.level, 1200, 690);
         g2.drawLine(1040,700,1260,700);
         if(g.inWave){
            g2.setColor(Color.red);
            g2.drawString("Wave is running!",1050,720);
            g2.setColor(Color.black);
            g2.drawString("Mobs left: "+Grid.ml.size(),1200,720);
         }else{
            g2.setColor(Color.getHSBColor(0.35f, 1, 0.35f));
            g2.drawString("Wave is waiting!",1050,720);
         }
         g2.setColor(Color.black);
         if(selectedTower != null){
            int upgradeCost;
            //green window
            g2.drawString("-- SOME INFO HERE --", 1050, 280);
            g2.drawLine(1045, 290, 1185, 290);
            g2.drawString("Type: "+selectedTower.name, 1050, 310);
            g2.drawString("Cost: "+selectedTower.costBase, 1050, 330);
            g2.drawString("Sell Cost: "+selectedTower.detSellCost(), 1150, 330);
            //dark blue window
            g2.drawString("DMG-LVL: "+selectedTower.levelDMG, 1050, 380);
            g2.drawString("Range-LVL: "+selectedTower.levelRange,1050, 400);
            g2.drawString("Speed-LVL: "+selectedTower.levelSpeed,1050, 420);
            upgradeCost = (int)(selectedTower.costDMG*(1+(selectedTower.levelDMG/15.0)));
            g2.drawString("Up. Cost: "+upgradeCost, 1150, 380);
            upgradeCost = (int)(selectedTower.costRange*(1+(selectedTower.levelRange/15.0)));
            g2.drawString("Up. Cost: "+upgradeCost, 1150, 400);
            upgradeCost = (int)(selectedTower.costSpeed*(1+(selectedTower.levelSpeed/15.0)));
            g2.drawString("Up. Cost: "+upgradeCost, 1150, 420);
            //blue window
            g2.drawString("Range: "+selectedTower.range, 1050, 480);
            g2.drawString("Damage: "+selectedTower.dmg, 1050, 500);
            g2.drawString("Cooldown: "+(1/selectedTower.speed)+" sek", 1050, 520);
            g2.drawLine(1045, 525, 1185, 525);
            g2.drawString("DPS: "+(selectedTower.dmg*selectedTower.speed), 1050, 540);
         }else{
            g2.drawString("-- NOTHING SELECTED --", 1050, 280);
         }
      }else{
         g2.setColor(Color.white);
         g2.drawString("WELCOME TO   LaserTD2: Time to Kill!",1030,280);
         g2.drawString("Copyright by Lisowski Dominik!",1030,300);
         g2.drawString("Press Left Mouse Button und start and PWN",1030,320);
      }
   }

   private void fillButtons(){
      //tower buttons
      bl.add(new Button(1030,15,80,50,0));
      bl.add(new Button(1115,15,80,50,1));
      bl.add(new Button(1200,15,80,50,2));
      bl.add(new Button(1030,75,80,50,3));
      bl.add(new Button(1115,75,80,50,4));
      bl.add(new Button(1200,75,80,50,5));
      bl.add(new Button(1115,135,80,50,6));
      //action buttons
      bl.add(new Button(1030,580,80,50,7));//sell
      bl.add(new Button(1115,580,80,20,8));//upgradeRange
      bl.add(new Button(1115,600,80,20,9));//upgradeDmg
      bl.add(new Button(1115,620,80,20,10));//upgradeCooldown
      bl.add(new Button(1200,580,80,50,11));//nextWave
   }

   private void fillProspect(){
      prospect = new ArrayList<>();
      prospect.add(new TowerPulse(0,0));
      prospect.add(new TowerImpulse(0,0));
      prospect.add(new TowerSlower(0,0));
      prospect.add(new TowerCharger(0,0));
      prospect.add(new TowerRailgun(0,0));
      prospect.add(new TowerFreezer(0,0));
      prospect.add(new TowerChain(0,0));
   }

   private int findButton(int x, int y){
      Button b;
      for(int i=0;i<bl.size();i++){
         b = bl.get(i);
         if(x>= (b.xPos-b.width) && x<= (b.xPos+b.width) &&
            y>= (b.yPos-b.height) && y<= (b.yPos+b.height)){
            return i;
         }
      }
      return -1;
   }

   private void doButtonAction(int x, int y){
      int action = findButton(x,y);
      if(action > -1 && action < 7){
         selectedType = action;
         selectedTower = prospect.get(action);
         prospected = true;
      }else if(action == 7 && selectedTower != null){//Sell
         g.removeTower(selectedTower.xPos, selectedTower.yPos);
         selectedTower=null;
         selectedType =-1;
      }else if(action == 8 && selectedTower != null){//+ DMG
         g.upgradeDMG(selectedTower.xPos, selectedTower.yPos);
      }else if(action == 9 && selectedTower != null){//+ Range
         g.upgradeRange(selectedTower.xPos, selectedTower.yPos);
      }else if(action == 10 && selectedTower != null){//+ Speed
         g.upgradeSpeed(selectedTower.xPos, selectedTower.yPos);
      }else if(action == 11){//NextWave
         if(!g.inWave){
            g.startMobs();
            g.level++;
         }
      }
   }

   private void reset(){
      g = new Grid();
      start = false;
      inGame = true;
      prospected = false;
      dead =false;
      bl = new ArrayList<>();
      fillButtons();
      gm = new GridManager();
      gm.start();
      selectedButton = null;
      selectedType = -1;
      selectedTower = null;
      prospect = new ArrayList<>();
      fillProspect();
   }

   class GridManager extends Thread{
       @Override
       public void run(){
          while (true){
             if(g.inWave){
               g.moveMobs();
               g.updateTargets();
               g.fireTowers();
             }
             if(g.lives<=0){
                dead = true;
                start = false;
             }
             repaint();
            try {
               Thread.sleep(20);
            } catch (InterruptedException ex) {
               Logger.getLogger(LaserPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
       }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      addMouseListener(new java.awt.event.MouseAdapter() {
         @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
            LaserPanel.this.mouseClicked(evt);
         }
         @Override
    public void mousePressed(java.awt.event.MouseEvent evt) {
            LaserPanel.this.mousePressed(evt);
         }
         @Override
    public void mouseReleased(java.awt.event.MouseEvent evt) {
            LaserPanel.this.mouseReleased(evt);
         }
      });
      addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
         @Override
    public void mouseMoved(java.awt.event.MouseEvent evt) {
            LaserPanel.this.mouseMoved(evt);
         }
      });

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 400, Short.MAX_VALUE)
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 300, Short.MAX_VALUE)
      );
   }// </editor-fold>//GEN-END:initComponents

    private void mouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseMoved
       globMouseX = evt.getX();
       globMouseY = evt.getY();
       if(globMouseX>1000){
          inGame = false;
       }else{
          inGame = true;
       }
    }//GEN-LAST:event_mouseMoved

    private void mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseClicked
       if(evt.getButton() == MouseEvent.BUTTON1){
          if(!start){
            reset();
            start = true;
            System.out.println("started");
          }else{
             if(inGame){
               if(!g.addTower(globMouseX, globMouseY, selectedType)){
                  int i=g.findTower(globMouseX, globMouseY);
                  if(i>=0){
                     selectedTower = g.tl.get(i);
                     selectedType = selectedTower.type;
                  }
               }
             }else{
                doButtonAction(globMouseX,globMouseY);
             }
          }
       }else if(evt.getButton() == MouseEvent.BUTTON3){
          selectedTower = null;
          selectedType = -1;
          prospected = false;
       }
    }//GEN-LAST:event_mouseClicked

    private void mousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mousePressed
       if(selectedButton == null){
          int action=findButton(globMouseX,globMouseY);
          if(action>-1){
             selectedButton = bl.get(action);
             selectedButton.xPos-=2;
             selectedButton.yPos+=2;
          }
       }
    }//GEN-LAST:event_mousePressed

    private void mouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseReleased
       if(selectedButton != null){
          selectedButton.xPos+=2;
          selectedButton.yPos-=2;
          selectedButton = null;
       }
    }//GEN-LAST:event_mouseReleased

   // Variables declaration - do not modify//GEN-BEGIN:variables
   // End of variables declaration//GEN-END:variables
 
}
