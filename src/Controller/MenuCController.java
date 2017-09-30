package Controller;

import SelfTools.ExportTools;
import SelfTools.StageControll;
import SelfTools.UserInfo;
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
public class MenuCController implements Initializable {
    
    @FXML public AnchorPane MenuAP;
    @FXML public Label Lname, Lgrp, Lhost;
    @FXML public Button add, view_modify, settings, pmenu, exit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Lname.setText(UserInfo.account);
        Lgrp.setText(UserInfo.group);
        Lhost.setText(UserInfo.ip);
        StageControll.ButtonCtrl(pmenu, exit);
        StageControll.Keydirect(exit, add);
        StageControll.ButtonCtrl(add, view_modify);
        StageControll.ButtonCtrl(view_modify, settings);
        StageControll.ButtonCtrl(settings, pmenu);
        StageControll.ButtonCtrl(exit, add);
    }
    
    @FXML
    public void btn_add(MouseEvent event) throws Exception{
        StageControll.open(MenuSController.class, "/View/MenuS.fxml");
        ExportTools.exportpath();
        StageControll.close(MenuAP);
    }
    
    @FXML
    public void btn_modfiy(MouseEvent event) throws Exception{
        StageControll.open(MenuS2Controller.class, "/View/MenuS2.fxml");
        ExportTools.exportpath();
        StageControll.close(MenuAP);
    }
    
    @FXML
    public void btn_setting(MouseEvent event) throws Exception{
        StageControll.open(ExportController.class, "/View/Export.fxml");
        StageControll.close(MenuAP);
    }
    
    @FXML
    public void btn_person(MouseEvent event) throws Exception{
        StageControll.open(MenuEController.class, "/View/MenuE.fxml");
        StageControll.close(MenuAP);
    }
    
    @FXML
    public void btn_exit(MouseEvent event) throws Exception{
        StageControll.close(MenuAP);
    }
}
