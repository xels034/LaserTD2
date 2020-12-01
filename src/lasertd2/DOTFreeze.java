/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lasertd2;

/**
 *
 * @author Xels
 */
public class DOTFreeze extends BaseDOT{

    public DOTFreeze(){
        time=200;
    }

    @Override
    public void work(){
        if(PR_carrier!=null){
            PR_carrier.stepSize=0;
            time--;
        }
    }

    @Override
    public void decurse(){
        PR_carrier.stepSize=PR_carrier.baseStepSize;
    }

    @Override
    public BaseDOT clone(){
        return new DOTFreeze();
    }
}
