package Controller;

import SelfTools.SQLTools;
import SelfTools.StageControll;
import SelfTools.TableTools;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 * @author Sora
 */
public class MPostAdjRController implements Initializable {
    
    @FXML public AnchorPane PAP;
    @FXML public TableView postadj;
    @FXML public TextField name;
    @FXML public Button back, search, modify; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        StageControll.TableViewhandle(postadj);
        StageControll.TextFieldhandle(name);
        StageControll.ButtonCtrl(back, name);
        StageControll.ButtonCtrl(search, modify);
        StageControll.ButtonCtrl(modify, back);
        search.setDisable(true);
    }
    
    @FXML
    public void set_searchenable(){
        if(!name.getText().isEmpty()){
            search.setDisable(false);
        }
    }
  
    @FXML
    public void btn_search(MouseEvent event) throws Exception{ 
        String [] col_name = {"調整後職務名稱", "職務生效日期", "職務終止日期", "職務內容概要"};
        String ask = "use MileStoneHRMS select p.P_ID from Personel as p where p.Name_CH = ";
        String sql = "use MileStoneHRMS select ps.Post_Name, cast(p.StartD as date)"
                + ", cast(p.EndD as date), ps.Duty from PostAdjR as p\n" +
                "left outer join Position as ps on p.Post_ID = ps.Post_ID "
                + " where p.P_ID =  " +  "'" + SQLTools.ValueGetId(ask, name) + "'";
        TableTools.DataSet(postadj, 4, 270,col_name, sql);
    }
    
    @FXML
    public void btn_back(MouseEvent event) throws Exception{
        StageControll.open(MenuS2Controller.class, "/View/MenuS2.fxml");
        StageControll.close(PAP);
    }
    
    @FXML
    public void btn_modify() throws Exception{
        StageControll.open(MPostAdjR2Controller.class, "/View/MPostAdjR2.fxml");
        StageControll.close(PAP);        
    }
}
