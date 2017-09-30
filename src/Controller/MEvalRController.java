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
public class MEvalRController implements Initializable {
    
    @FXML public AnchorPane SAP;
    @FXML public TableView salary;
    @FXML public TextField name, year;
    @FXML public Button back, modify, search; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        search.setDisable(true);
        StageControll.Buttonhandle(back);
        StageControll.Buttonhandle(modify);
        StageControll.Buttonhandle(search);
        StageControll.TableViewhandle(salary);
        StageControll.TextFieldhandle(name);
        StageControll.TextFieldhandle(year);
    }
    
    @FXML
    public void set_searchenable(){
        if(!name.getText().isEmpty()){
            search.setDisable(false);
        }
    }
  
    @FXML
    public void btn_search(MouseEvent event) throws Exception{
        String [] col_name = {"考評日期","區間起始日期","區間終止日期","評量等級","考核評語"};
        String ask = "use MileStoneHRMS select p.P_ID from Personel as p where p.Name_CH = ";
        String sql = "use MileStoneHRMS select cast(e.EvalDay as date)"
                + ", cast(e.StartD as date), cast(e.EndD as date), r.Rating_Name"
                + ", e.Comment from Eval as e left outer join Rating as r on e.Ra_ID = r.Ra_ID"
                + " where e.P_ID =  " +  "'" + SQLTools.ValueGetId(ask, name) + "'"
                + "and year(cast(e.StartD as date)) = " + "'" + year.getText() + "'";
        TableTools.DataSet(salary, 5, 220,col_name, sql);
    }
    
    @FXML
    public void btn_back(MouseEvent event) throws Exception{
        StageControll.open(MenuS2Controller.class, "/View/MenuS2.fxml");
        StageControll.close(SAP);
    }
    
    @FXML
    public void btn_modify() throws Exception{
        StageControll.open(MEvalR2Controller.class, "/View/MEvalR2.fxml");
        StageControll.close(SAP);        
    }
}
