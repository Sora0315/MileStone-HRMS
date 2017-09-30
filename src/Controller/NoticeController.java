package Controller;

import SelfTools.StageControll;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 * @author Sora
 */
public class NoticeController implements Initializable {
    
    public static String noticecontent, noticecontent2;
    @FXML public Label l1, l2;
    @FXML public Button confirm;
    @FXML public AnchorPane AP;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        StageControll.Buttonhandle(confirm);
        if(noticecontent!=null){
            l1.setText(noticecontent);
        }
        if(noticecontent2!=null){
            l2.setText(noticecontent2);            
        }
    }
    
    public void btn_close(MouseEvent event) throws Exception{
        StageControll.close(AP);
        noticecontent = null;
        noticecontent2 = null;
    }
}
