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
public class MAbWtimeRController implements Initializable {
    
    @FXML public AnchorPane SAP;
    @FXML public TableView salary;
    @FXML public TextField name;
    @FXML public Button back, search, modify; 
      
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        search.setDisable(true);
        StageControll.TextFieldhandle(name);
        StageControll.TableViewhandle(salary);
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
        String [] col_name = {"異常出勤項目","開始日期與時間","終止日期與時間","合計區間","補充事由"};
        String ask = "use MileStoneHRMS select p.P_ID from Personel as p where p.Name_CH = ";
        String sql = "select l.LO_Name, cast(cast(a.StartD as date) as varchar) "
                + " + '  ' + cast(datepart(hh, a.StartD) as varchar) + '時' "
                + " + cast(datepart(mi, a.StartD) as varchar)+ '分'"
                + " , cast(cast(a.EndD as date) as varchar) + '  ' "
                + " + cast(datepart(hh, a.EndD) as varchar) + '時' "
                + " + cast(datepart(mi, a.EndD) as varchar)+ '分'"
                + " , (cast(a.Interval/24 as varchar) + ' 日 ' + cast(a.Interval%24 as varchar) + ' 時')"
                + " , a.Cause from AbWorktimeR as a left outer join LO as l on a.LO_ID = l.LO_ID"
                + " where a.P_ID =  " +  "'" + SQLTools.ValueGetId(ask, name) + "'"
                + " order by a.StartD Asc ";
        TableTools.DataSet(salary, 5, 240, col_name, sql);
    }
    
    @FXML
    public void btn_back(MouseEvent event) throws Exception{
        StageControll.open(MenuS2Controller.class, "/View/MenuS2.fxml");
        StageControll.close(SAP);
    }
    
    @FXML
    public void btn_modify() throws Exception{
        StageControll.open(MAbWtimeR2Controller.class, "/View/MAbWtimeR2.fxml");
        StageControll.close(SAP);        
    }
}