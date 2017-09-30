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
public class MInvPropertyController implements Initializable {
    
    @FXML public AnchorPane SAP;
    @FXML public TableView salary;
    @FXML public TextField name;
    @FXML public Button back, search, modify; 
    
    Connection conn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        search.setDisable(true);
        StageControll.TableViewhandle(salary);
        StageControll.TextFieldhandle(name);
        StageControll.Buttonhandle(back);
        StageControll.Buttonhandle(search);
        StageControll.Buttonhandle(modify);
    }
    
    @FXML
    public void set_searchenable(){
        if(!name.getText().isEmpty()){
            search.setDisable(false);
        }
    }
  
    @FXML
    public void btn_search(MouseEvent event) throws Exception{
        String [] col_name = {"清冊編號","財產名稱","數量","持有起始日","持有終止日"};
        String ask = "use MileStoneHRMS select p.P_ID from Personel as p where p.Name_CH = ";
        String sql = "use MileStoneHRMS select cast(substring(i.InvP_ID, 2, 6) as int)"
                + " , pt.Pr_Name, i.Amount, cast(i.StartD as date), cast(i.EndD  as date) from InvProperty as i "
                + " left outer join Property as pt on pt.Pr_ID = i.Pr_ID "
                + " left outer join Personel as p on p.P_ID = i.P_ID "
                + " where i.P_ID =  " +  "'" + SQLTools.ValueGetId(ask, name) + "'";
        TableTools.DataSet(salary, 5, 220, col_name, sql);
    }
    
    @FXML
    public void btn_back(MouseEvent event) throws Exception{
        StageControll.open(MenuS2Controller.class, "/View/MenuS2.fxml");
        StageControll.close(SAP);
    }
    
    @FXML
    public void btn_modify() throws Exception{
        StageControll.open(MInvProperty2Controller.class, "/View/MInvProperty2.fxml");
        StageControll.close(SAP);        
    }
}
