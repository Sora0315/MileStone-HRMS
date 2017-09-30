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
public class MenuSController implements Initializable {
    
    @FXML public AnchorPane MenuAP;
    @FXML public Label Lname, Lgrp, Lhost;
    @FXML public Button back, basic, salary, lo, spec, salaryadj, eval, post, prep, inv;
    
    
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
        if(UserInfo.group.matches("Supervisor")){
            prep.setDisable(true);
            inv.setDisable(true);
        }
    }
    
    @FXML
    public void btn_back(MouseEvent event) throws Exception{
        StageControll.open(MenuCController.class, "/View/MenuC.fxml");
        StageControll.close(MenuAP);
    }
    
    @FXML
    public void btn_pesronel(MouseEvent event) throws Exception{
        StageControll.open(PersonelController.class, "/View/Personel.fxml");
        StageControll.close(MenuAP);
    }
    
    @FXML
    public void btn_psecpwkexp() throws Exception{
        StageControll.open(PsecPwkexpController.class, "/View/PsecPwkexp.fxml");
        StageControll.close(MenuAP);
    }
    
    @FXML
    public void btn_preparation(MouseEvent event) throws Exception{
        StageControll.open(PreparationController.class, "/View/Preparation.fxml");
        StageControll.close(MenuAP);
    }
    
    @FXML
    public void btn_postadjr(MouseEvent event) throws Exception{
        StageControll.open(PostadjRController.class, "/View/PostadjR.fxml");
        StageControll.close(MenuAP);
    }
    
    @FXML
    public void btn_abwtimer(MouseEvent event) throws Exception{
        StageControll.open(AbWtimeRController.class, "/View/AbWtimeR.fxml");
        StageControll.close(MenuAP);
    }
    
    @FXML
    public void btn_evalr(MouseEvent event) throws Exception{
        StageControll.open(EvalRController.class, "/View/EvalR.fxml");
        StageControll.close(MenuAP);
    }
    
    @FXML
    public void btn_salaryr(MouseEvent event) throws Exception{
        StageControll.open(SalaryRController.class, "/View/SalaryR.fxml");
        StageControll.close(MenuAP);
    }
    
    @FXML
    public void btn_salaryadjr(MouseEvent event) throws Exception{
        StageControll.open(SalaryAdjController.class, "/View/SalaryAdj.fxml");
        StageControll.close(MenuAP);
    }
    
    @FXML
    public void btn_invproperty(MouseEvent event) throws Exception{
        StageControll.open(InvPropertyController.class, "/View/InvProperty.fxml");
        StageControll.close(MenuAP);
    }
}
