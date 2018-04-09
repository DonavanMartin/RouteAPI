/**
 * @date 2018-03-26 
 * @author Algorithme Solutions inc. ©
 * @email DonavanMartin@AlgorithmeSolutions.com
 */

package Graph;
import javafx.scene.control.TextField;

public class NumberTextField extends TextField{
    public NumberTextField(){
        this.setPromptText("Enter Only numbers");
    }
    @Override
    public void replaceText(int i, int il, String string){
        if(string.matches("[0-9]") || string.isEmpty() ){
            super.replaceText(i, il, string);
        }
    }

    @Override
    public void replaceSelection(String string){
        super.replaceSelection(string);
    }
}