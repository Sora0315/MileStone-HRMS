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
public class MenuEController implements Initializable {
    
    @FXML public AnchorPane MenuAP;
    @FXML public Label Lname, Lgrp, Lhost;
    @FXML public Button basic, leave, overtime, salary, post, exit;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        StageControll.ButtonCtrl(basic, leave);
        StageControll.ButtonCtrl(leave, overtime);
        StageControll.ButtonCtrl(overtime, salary);
        StageControll.ButtonCtrl(salary, post);
        StageControll.ButtonCtrl(post, exit);
        StageControll.ButtonCtrl(exit, basic);
        StageControll.Keydirect(exit, basic);
        Lname.setText(UserInfo.account);
        Lgrp.setText(UserInfo.group);
        Lhost.setText(UserInfo.ip);
        if(UserInfo.group.matches("Staff")){
            exit.setText("離開系統");
        }
        else{
            exit.setText("回上層選單");
        }
    }
    
    @FXML
    public void btn_exit(MouseEvent event) throws Exception{
        if(UserInfo.group.matches("Staff")){
            StageControll.close(MenuAP);
        }
        else{
            StageControll.open(MenuCController.class, "/View/MenuC.fxml");
            StageControll.close(MenuAP);
        }
    }
    
    @FXML
    public void btn_personel(MouseEvent event) throws Exception{
        StageControll.open(EPersonelController.class, "/View/EPersonel.fxml");
        StageControll.close(MenuAP);
    }
    
    @FXML
    public void btn_leave(MouseEvent event) throws Exception{
        StageControll.open(ELeaveController.class, "/View/ELeave.fxml");
        StageControll.close(MenuAP);
    }
    
    @FXML
    public void btn_overtime(MouseEvent event) throws Exception{
        StageControll.open(EPreOvertimeController.class, "/View/EPreOvertime.fxml");
        StageControll.close(MenuAP);        
    }
    
    @FXML
    public void btn_salary(MouseEvent event) throws Exception{
        StageControll.open(ESalaryController.class, "/View/ESalary.fxml");
        StageControll.close(MenuAP);
    }
    
    @FXML
    public void btn_eval_postadj() throws Exception{
        StageControll.open(EEval_PostAdjController.class, "/View/EEval_PostAdj.fxml");
        StageControll.close(MenuAP);
    }
}
