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
public class MSalaryAdjController implements Initializable {
    
    @FXML public AnchorPane SAP;
    @FXML public TableView salary, bfsdy, afsdy;
    @FXML public TextField name;
    @FXML public Button back, search, modify; 
    
    Connection conn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        search.setDisable(true);
        StageControll.TableViewhandle(salary);
        StageControll.TableViewhandle(bfsdy);
        StageControll.TableViewhandle(afsdy);
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
        String [] col_name = {"調整生效日","調整前本薪", "調整前津貼總額", "調整後本薪","調整後津貼總額","調整差額"};
        String ask = "use MileStoneHRMS select p.P_ID from Personel as p where p.Name_CH = ";
        String id = SQLTools.ValueGetId(ask, name);
        String sql = "use MileStoneHRMS select  cast(s.StartD as date), cast(s.Bef_BasePay as int), (select sum(cast(bf.Bef_Subsidy as int)) \n"
                + " from BefSdy as bf where bf.P_ID =  '" + id + "' ), cast(s.Aft_BasePay as int), (select sum(cast(af.Aft_Subsidy as int)) \n" 
                + " from AftSdy as af where af.P_ID =  '" + id + "' ), cast(s.Difference as int)  from SalaryAdjR as s where s.P_ID = " + "'" + id + "'" 
                + " order by s.StartD Asc";
        TableTools.DataSet(salary, 6, 200,col_name, sql);
        
        String [] col_bfsdy = {"調整生效日", "調整前津貼名稱", "金額"};
        String sql2 = "use MileStoneHRMS select cast(bf.StartD as date), sd.Type_Name, cast(bf.Bef_Subsidy as int)  from BefSdy as bf\n"
                + " left outer join SdyType as sd on sd.Type_ID = bf.Type_ID\n"
                + " where bf.P_ID = " + "'" + id + "'" + " order by bf.StartD";
        TableTools.DataSet(bfsdy, 3, 196, col_bfsdy, sql2);
        
        String [] col_afsdy = {"調整生效日", "調整後津貼名稱", "金額"};
        String sql3 = "use MileStoneHRMS select cast(af.StartD as date), sd.Type_Name, cast(af.Aft_Subsidy as int) from AftSdy as af\n"
                + " left outer join SdyType as sd on sd.Type_ID = af.Type_ID\n"
                + " where af.P_ID = " + "'" + id + "'" + " order by af.StartD";
        TableTools.DataSet(afsdy, 3, 196, col_afsdy, sql3);   
    }
    
    @FXML
    public void btn_back(MouseEvent event) throws Exception{
        StageControll.open(MenuS2Controller.class, "/View/MenuS2.fxml");
        StageControll.close(SAP);
    }
    
    @FXML
    public void btn_modify() throws Exception{
        StageControll.open(MSalaryAdj2Controller.class, "/View/MSalaryAdj2.fxml");
        StageControll.close(SAP);        
    }
}
