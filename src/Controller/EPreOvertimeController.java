package Controller;

import SelfTools.AutoCompleteComboBox;
import SelfTools.SQLTools;
import SelfTools.StageControll;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 * @author Sora
 */
public class EPreOvertimeController implements Initializable {
    
    public static String ApprP_ID;
    @FXML public AnchorPane EAP;
    @FXML public ComboBox appr;
    @FXML public Button back, submit;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        submit.setDisable(true);
        try{
            String sql = "use MileStoneHRMS select a.Name from AdminPersonel as a ";
            SQLTools.comboboxSetItem(sql, appr);
        }catch(Exception e){
        }
        appr.setEditable(true);
        AutoCompleteComboBox autoCompleteComboBox = new AutoCompleteComboBox(appr);
        StageControll.ComboBoxCtrl(appr);
        StageControll.ButtonCtrl(back, appr);
        StageControll.ButtonCtrl(submit, back);
        StageControll.Keydirect(back, appr);
    }
    
    @FXML public void set_submitenable(){
        if(!appr.getSelectionModel().isEmpty()){
            submit.setDisable(false);
        }
    }
    
    @FXML public void btn_submit(MouseEvent event) throws Exception{
        String sql = "use MileStoneHRMS select a.ApprP_ID from AdminPersonel as a where a.Name = ";
        ApprP_ID = SQLTools.ValueGetId(sql ,appr);
        StageControll.open(EOvertimeController.class, "/View/EOvertime.fxml");
        StageControll.close(EAP);
    }
    
    @FXML public void  btn_back(MouseEvent event) throws Exception{
        StageControll.open(MenuEController.class, "/View/MenuE.fxml");
        StageControll.close(EAP);
    }
}
