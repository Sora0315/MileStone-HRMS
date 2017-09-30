package Controller;

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
public class MenuS2Controller implements Initializable {
    
    @FXML public AnchorPane MAP;
    @FXML public Label Lname, Lgrp, Lhost;
    @FXML public Button back, basic, salary, lo, post, spec, salaryadj, eval, inv;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        StageControll.ButtonCtrl(basic, salary);
        StageControll.ButtonCtrl(salary, lo);
        StageControll.ButtonCtrl(lo, post);
        StageControll.ButtonCtrl(post, spec);
        StageControll.ButtonCtrl(spec, salaryadj);
        StageControll.ButtonCtrl(salaryadj, eval);
        StageControll.ButtonCtrl(eval, inv);
        StageControll.ButtonCtrl(inv, back);
        StageControll.ButtonCtrl(back, basic);
        StageControll.Keydirect(back, basic);
        StageControll.Keydirect(post, spec);
        StageControll.Keydirect(inv, back);
        Lname.setText(UserInfo.account);
        Lgrp.setText(UserInfo.group);
        Lhost.setText(UserInfo.ip);
    }
    
    @FXML
    public void btn_back(MouseEvent event) throws Exception{
        StageControll.open(MenuCController.class, "/View/MenuC.fxml");
        StageControll.close(MAP);
    }
    
    @FXML 
    public void btn_personel(MouseEvent event) throws Exception{
        StageControll.open(MPersonelController.class, "/View/MPersonel.fxml");
        StageControll.close(MAP);
    }
    
    @FXML
    public void btn_MPsecPwkexp() throws Exception{
        StageControll.open(MPsecPwkexpController.class, "/View/MPsecPwkexp.fxml");
        StageControll.close(MAP);
    }
    
    @FXML 
    public void btn_salary(MouseEvent event) throws Exception{
        StageControll.open(MSalaryController.class, "/View/MSalary.fxml");
        StageControll.close(MAP);
    }
    
    @FXML
    public void btn_salryadj(MouseEvent event) throws Exception{
        StageControll.open(MSalaryAdjController.class, "/View/MSalaryAdj.fxml");
        StageControll.close(MAP);
    }
    
    @FXML
    public void btn_postadjr(MouseEvent event) throws Exception{
        StageControll.open(MPostAdjRController.class, "/View/MPostAdjR.fxml");
        StageControll.close(MAP);
    }
    
    @FXML
    public void btn_evalr(MouseEvent event) throws Exception{
        StageControll.open(MEvalRController.class, "/View/MEvalR.fxml");
        StageControll.close(MAP);
    }
    
    @FXML
    public void btn_invproperty(MouseEvent event) throws Exception{
        StageControll.open(MInvPropertyController.class, "/View/MInvProperty.fxml");
        StageControll.close(MAP);
    }

    @FXML
    public void btn_abwtimer() throws Exception{
        StageControll.open(MAbWtimeRController.class, "/View/MAbWtimeR.fxml");
        StageControll.close(MAP);
    }
}