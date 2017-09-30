package Controller;

import SelfTools.SQLTools;
import SelfTools.StageControll;
import SelfTools.TableTools;
import java.net.URL;
import java.sql.Connection;
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
public class MPsecPwkexpController implements Initializable {
    
    @FXML public AnchorPane SAP;
    @FXML public TableView spec, wk;
    @FXML public TextField name;
    @FXML public Button back, search, modify; 
    
    Connection conn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        search.setDisable(true);
        StageControll.TableViewhandle(spec);
        StageControll.TableViewhandle(wk);
        StageControll.TextFieldhandle(name);
        StageControll.ButtonCtrl(back, name);
        StageControll.ButtonCtrl(search, modify);
        StageControll.ButtonCtrl(modify, back);
    }
    
    @FXML
    public void set_searchenable(){
        if(!name.getText().isEmpty()){
            search.setDisable(false);
        }
    }
  
    @FXML
    public void btn_search(MouseEvent event) throws Exception{
        String [] col_name_spec = {"專長 / 專業"};
        String ask = "use MileStoneHRMS select p.P_ID from Personel as p where p.Name_CH = ";
        String sql_spec = "use MileStoneHRMS select s.Sp_Name from PSpec as p "
                + " left outer join Speciality as s on s.Sp_ID = p.Sp_ID "
                + " where p.P_ID =  " +  "'" + SQLTools.ValueGetId(ask, name) + "'";
        TableTools.DataSet(spec, 1, 1100, col_name_spec, sql_spec);
        String [] col_name_wk = {"履歷序號", "公司名稱", "職務名稱", "任職", "離職"};
        String sql_wk = "use MileStoneHRMS select pw.Exp_Num, w.Exp_Name, e.ExpPost_Name, pw.StartD, pw.EndD from PWkExp as pw "
                + " left outer join WkExperience as w on w.Exp_ID = pw.Exp_ID "
                + " left outer join ExpPost as e on e.ExpPost_ID = w.ExpPost_ID "
                + " left outer join Personel as p on pw.P_ID = p.P_ID "
                + " where pw.P_ID = " + "'" + SQLTools.ValueGetId(ask, name) + "'"
                + " order by pw.Exp_Num Asc ";
        TableTools.DataSet(wk, 5, 220, col_name_wk, sql_wk); 
    }
        
    @FXML
    public void btn_back(MouseEvent event) throws Exception{
        StageControll.open(MenuS2Controller.class, "/View/MenuS2.fxml");
        StageControll.close(SAP);
    }
    
    @FXML
    public void btn_modify() throws Exception{
        StageControll.open(MPsecPwkexp2Controller.class, "/View/MPsecPwkexp2.fxml");
        StageControll.close(SAP);        
    }
}
