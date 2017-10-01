package Controller;

import SelfTools.AuditLog;
import SelfTools.SQLTools;
import SelfTools.StageControll;
import SelfTools.StringVariation;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 * @author Sora
 */
public class AddSchoolDepController implements Initializable {

    @FXML public AnchorPane SubAP;
    @FXML public TextField schname, schid, depname, depid;
    @FXML public Button back, set1, set2, submit;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        schid.setDisable(true);
        depid.setDisable(true);
        submit.setDisable(true);
        StageControll.TextFieldhandle(schname);
        StageControll.TextFieldhandle(depname);
        StageControll.ButtonCtrl(back, schname);
        StageControll.ButtonCtrl(set1, depname);
        StageControll.ButtonCtrl(set2, submit);
        StageControll.ButtonCtrl(submit, back);
    }
    
    @FXML
    public void set_schoolid(MouseEvent event) throws Exception{
        if(!schname.getText().isEmpty() || schname.getText().trim().length()!=0){
            String ask = "use MileStoneHRMS select s.School_Name from GSch as s where s.School_Name = ";
            if(SQLTools.ValueGetId(ask, schname).isEmpty()){
                String sql = "use MileStoneHRMS select cast(substring(s.School_ID, 4, 8) as int) from GSch as s";
                String num = Integer.toString(SQLTools.idAutoIncrease(((int)SQLTools.sqlQuerySetId(sql))));
                String id = "SCH" + StringVariation.right(("00000" + num), 5);
                schid.setText(id);
                submit.setDisable(false);
            }
            else {
                NoticeController.noticecontent = "資料已經存在！";
                StageControll.open(NoticeController.class, "/View/Notice.fxml");
            }
        } else {
            NoticeController.noticecontent = "請輸入必要資料！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");
        }
    }
    
    @FXML
    public void set_depid(MouseEvent event) throws Exception{
        if(!depname.getText().isEmpty()){
            String ask = "use MileStoneHRMS select d.Dep_Name from GDep as d where d.Dep_Name = ";
            if(SQLTools.ValueGetId(ask, depname).isEmpty()){
                String sql = "use MileStoneHRMS select cast(substring(d.Dep_ID, 4, 8) as int) from GDep as d";
                String num = Integer.toString(SQLTools.idAutoIncrease(((int)SQLTools.sqlQuerySetId(sql))));
                String id = "DEP" + StringVariation.right(("00000" + num), 5);
                depid.setText(id);
                submit.setDisable(false);
            }
            else {
                NoticeController.noticecontent = "資料已經存在！";
                StageControll.open(NoticeController.class, "/View/Notice.fxml");
            }
        } else {
            NoticeController.noticecontent = "請輸入必要資料！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");
        }
    }
    
    @FXML
    public void btn_submit(MouseEvent event) throws Exception{
        if(!schid.getText().isEmpty()){
            Data_Save("GSch", "School_ID, School_Name", schid, schname);
        }
        if(!depid.getText().isEmpty()){
            Data_Save("GDep", "Dep_ID, Dep_Name", depid, depname);
        }
        StageControll.close(SubAP);
        StageControll.open(AddSchoolDepController.class, "/View/AddSchoolDep.fxml");
    }
    
    public void Data_Save(String table, String itemnum, TextField id, TextField name) throws Exception{
        String sql = "use MileStoneHRMS insert into " + table + "( " + itemnum + " ) values (?,?)";
        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id.getText());
                pstmt.setString(2, name.getText());
                pstmt.execute();
                AuditLog.Audit("準備程序-科系所登錄", id, name);
            }
        }
    }
    
    @FXML
    public void btn_back(MouseEvent event) throws Exception{
        StageControll.close(SubAP);
        StageControll.open(PreparationController.class, "/View/Preparation.fxml");
    }
}