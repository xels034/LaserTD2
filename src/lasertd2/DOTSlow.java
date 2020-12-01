/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lasertd2;


/**
 *
 * @author Xels
 */
public class DOTSlow extends BaseDOT{

    public DOTSlow(){
        time = 300;
    }

    @Override
    public void work(){
        if(PR_carrier!=null){
            if(PR_carrier.stepSize>(PR_carrier.baseStepSize/2))
            PR_carrier.stepSize=PR_carrier.baseStepSize/2;
            time--;
        }
    }

    @Override
    public void decurse(){
        PR_carrier.stepSize=PR_carrier.baseStepSize;
    }

    @Override
    public BaseDOT clone(){
        return new DOTSlow();
    }
}
