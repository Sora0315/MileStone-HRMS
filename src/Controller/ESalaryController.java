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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 * @author Sora
 */
public class ESalaryController implements Initializable {
    @FXML public AnchorPane EAP;
    @FXML public TableView salary, sdetail, bdetail;
    @FXML public Label label, label_s, label_b;
    @FXML public TextField year;
    @FXML public Button back, search;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        search.setDisable(true);
        StageControll.TableViewhandle(salary);
        StageControll.TableViewhandle(sdetail);
        StageControll.TableViewhandle(bdetail);
        StageControll.TextFieldhandle(year);
        StageControll.Buttonhandle(back);
        StageControll.Buttonhandle(search);
    }
    
    @FXML public void btn_search() throws Exception{
        if(!year.getText().isEmpty()){
            label.setText(year.getText() + " 年薪資概要一覽表");
            String [] col_name = {"發薪日期","計算始期","計算終期","本薪","津貼總額","獎金總額","勞健保","實領金額"};
            String sql = "use MileStoneHRMS\n" +
                    "select cast(s.PayDay as date), cast(s.Cal_StartD as date), cast(s.Cal_EndD as date), cast(s.BasePay as int), isnull(cast(sd.l as int), 0)\n" +
                    ", cast(isnull(s.NetTotal, 0)+isnull(s.LH_Ins,0)-isnull(s.BasePay, 0)-isnull(sd.l,0) as int), cast(s.LH_Ins as int), cast(s.NetTotal as int)\n" +
                    "from SalaryR as s  left join (select sd.P_ID, sum(Subsidy) as 'l', sd.PayDay\n" +
                    "from Sdy as sd where year(cast(sd.PayDay as date)) = " + "'" + year.getText() + "'\n" +
                    "or year(cast(sd.PayDay as date)) = " + "'" + String.valueOf(Integer.parseInt(year.getText())-1) + "' or year(cast(sd.PayDay as date)) = " + 
                    "'" + String.valueOf(Integer.parseInt(year.getText())+1) + "'\n"  +
                    "and sd.P_ID = " + "'" + UserInfo.emp_pid + "'\n" +
                    "group by sd.PayDay, sd.P_ID\n" +
                    ")  as sd on sd.PayDay = s.PayDay and sd.P_ID = s.P_ID where year(cast(s.Cal_StartD as date)) = " + "'" + year.getText() + "'\n" +
                    "and s.P_ID = " + "'" + UserInfo.emp_pid + "'\n" ;
            TableTools.DataSet(salary, 8, 150, col_name, sql);
            
            label_s.setText(year.getText() + " 年津貼明細一覽表");
            String [] col_name2 = {"津貼月份", "津貼項目名稱", "津貼"};
            String sql2 = "use MileStoneHRMS select cast(month(cast(s.Cal_StartD as date)) as varchar)+' 月份', st.Type_Name, cast(sd.Subsidy as int) from Sdy as sd \n"
                    + " left outer join SdyType as st on st.Type_ID = sd.Type_ID\n"
                    + " left outer join SalaryR as s on s.P_ID = sd.P_ID and s.PayDay = sd.PayDay "
                    + " where sd.P_ID = " + "'" + UserInfo.emp_pid + "'"
                    + " and year(cast(s.Cal_StartD as Date)) = " + "'" + year.getText() + "'";
            TableTools.DataSet(sdetail, 3, 200, col_name2, sql2);
            
            label_b.setText(year.getText() + " 年獎金明細一覽表");
            String [] col_name3 = {"獎金月份", "獎金項目名稱", "獎金"};
            String sql3 = "use MileStoneHRMS select cast(month(cast(s.Cal_StartD as date)) as varchar)+' 月份', st.Type_Name, cast(sd.Bonus as int) from Bonus as sd \n"
                    + " left outer join BonusType as st on st.Type_ID = sd.Type_ID\n"
                    + " left outer join SalaryR as s on s.P_ID = sd.P_ID and s.PayDay = sd.PayDay "
                    + " where sd.P_ID = " + "'" + UserInfo.emp_pid + "'"
                    + " and year(cast(s.Cal_StartD as Date)) = " + "'" + year.getText() + "'";
            TableTools.DataSet(bdetail, 3, 200, col_name3, sql3); 
        }
    }
    
    @FXML public void btn_setsearchenable(){
        search.setDisable(false);
    }
 
    @FXML public void btn_back(MouseEvent event) throws Exception{
        StageControll.open(MenuEController.class, "/View/MenuE.fxml");
        StageControll.close(EAP);
    }
}
