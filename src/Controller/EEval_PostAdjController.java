package Controller;

import SelfTools.StageControll;
import SelfTools.TableTools;
import SelfTools.UserInfo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 * @author Sora
 */
public class EEval_PostAdjController implements Initializable {
    
    @FXML public AnchorPane EAP;
    @FXML public TableView postadj, eval;
    @FXML public Label l1, l2;
    @FXML public Button back;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            search_settable();
        } catch (Exception e) {}
        StageControll.Buttonhandle(back);
    }
  
    @FXML
    public void search_settable() throws Exception{ 
        String [] col_name = {"職務", "生效日期", "終止日期"};
        String sql = "use MileStoneHRMS select ps.Post_Name, cast(p.StartD as date)"
                + ", cast(p.EndD as date) from PostAdjR as p\n"
                + " left outer join Position as ps on p.Post_ID = ps.Post_ID "
                + " where p.P_ID =  " +  "'" + UserInfo.emp_pid + "'";
        l1.setText("職務調整紀錄");
        TableTools.DataSet(postadj, 3, 120,col_name, sql);
        
        String [] col_name2 = {"考評日期","起始日期","終止日期","評量等級","考核評語"};
        sql = "use MileStoneHRMS select cast(e.EvalDay as date)"
                + ", cast(e.StartD as date), cast(e.EndD as date), r.Rating_Name"
                + ", e.Comment from Eval as e left outer join Rating as r on e.Ra_ID = r.Ra_ID"
                + " where e.P_ID =  " +  "'" + UserInfo.emp_pid + "'";
        l2.setText("歷年考評紀錄");
        TableTools.DataSet(eval, 5, 140,col_name2, sql);
    }
    
    @FXML
    public void btn_back(MouseEvent event) throws Exception{
        StageControll.open(MenuEController.class, "/View/MenuE.fxml");
        StageControll.close(EAP);
    }
}
