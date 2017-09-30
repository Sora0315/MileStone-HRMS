package Controller;

import SelfTools.StageControll;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 * @author Sora
 */
public class PreparationController implements Initializable {
    
    @FXML public AnchorPane PmenuAP;
    @FXML public Button back, school, lo, post, status, sdytype, wkexp,
            dep, blood, property, eval, bonustype, spec;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        StageControll.ButtonCtrl(back, school);
        StageControll.ButtonCtrl(school, lo);
        StageControll.ButtonCtrl(lo, post);
        StageControll.ButtonCtrl(post, status);
        StageControll.ButtonCtrl(status, sdytype);
        StageControll.ButtonCtrl(sdytype, wkexp);
        StageControll.ButtonCtrl(wkexp, dep);
        StageControll.ButtonCtrl(dep, blood);
        StageControll.ButtonCtrl(blood, property);
        StageControll.ButtonCtrl(property, eval);
        StageControll.ButtonCtrl(eval, bonustype);
        StageControll.ButtonCtrl(bonustype, spec);
        StageControll.ButtonCtrl(spec, back);
        StageControll.Keydirect(back, school);
        StageControll.Keydirect(school, lo);
//        Keydirect(lo, post);
//        Keydirect(post, status);
//        Keydirect(status, sdytype);
//        Keydirect(sdytype, wkexp);
        StageControll.Keydirect(wkexp, dep);
//        Keydirect(dep, blood);
//        Keydirect(blood, property);
//        Keydirect(property, eval);
//        Keydirect(eval, bonustype);
//        Keydirect(bonustype, spec);
    }
    
    @FXML
    public void btn_back(MouseEvent event) throws Exception{
        StageControll.open(MenuSController.class, "/View/MenuS.fxml");
        StageControll.close(PmenuAP);
    }
    
    @FXML
    public void btn_SchDep(MouseEvent event) throws Exception{
        StageControll.open(AddSchoolDepController.class, "/View/AddSchoolDep.fxml");
        StageControll.close(PmenuAP);
    }
    
    @FXML
    public void btn_LO(MouseEvent event) throws Exception{
        StageControll.open(LOController.class, "/View/LO.fxml");
        StageControll.close(PmenuAP);
    }
    
    @FXML
    public void btn_BloodType(MouseEvent event) throws Exception{
        StageControll.open(AddBloodtypeController.class, "/View/AddBloodtype.fxml");
        StageControll.close(PmenuAP);
    }
    
    @FXML
    public void btn_Post(MouseEvent event) throws Exception{
        StageControll.open(AddPostController.class, "/View/AddPost.fxml");
        StageControll.close(PmenuAP);
    }
    
    @FXML
    public void btn_Property(MouseEvent event) throws Exception{
        StageControll.open(AddPropertyController.class, "/View/AddProperty.fxml");
        StageControll.close(PmenuAP);
    }
    
    @FXML
    public void btn_Activity(MouseEvent event) throws Exception{
        StageControll.open(AddActivityController.class, "/View/AddActivity.fxml");
        StageControll.close(PmenuAP);
    }
    
    @FXML
    public void btn_Rating(MouseEvent event) throws Exception{
        StageControll.open(AddRatingController.class, "/View/AddRating.fxml");
        StageControll.close(PmenuAP);
    }
    
    @FXML
    public void btn_sdy() throws Exception{
        StageControll.open(AddSdyTypeController.class, "/View/AddSdyType.fxml");
        StageControll.close(PmenuAP);
    }
    
    @FXML
    public void btn_Bonus(MouseEvent event) throws Exception{
        StageControll.open(AddBonusController.class, "/View/AddBonus.fxml");
        StageControll.close(PmenuAP);
    }
    
    @FXML
    public void btn_Speciality(MouseEvent event) throws Exception{
        StageControll.open(AddSpecController.class, "/View/AddSpec.fxml");
        StageControll.close(PmenuAP);
    }
    
    @FXML
    public void btn_WkExp(MouseEvent event) throws Exception{
        StageControll.open(AddWkExpController.class, "/View/AddWkExp.fxml");
        StageControll.close(PmenuAP);
    }
}
