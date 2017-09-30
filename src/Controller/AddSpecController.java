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
public class AddSpecController implements Initializable {
    
    @FXML public AnchorPane SAP;
    @FXML public TextField sid, sname;
    @FXML public Button back, set, submit;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sid.setDisable(true);
        submit.setDisable(true);
        StageControll.TextFieldhandle(sname);
        StageControll.ButtonCtrl(back, sname);
        StageControll.ButtonCtrl(set, submit);
        StageControll.ButtonCtrl(submit, back);
    }
    
    @FXML
    public void set_sid(MouseEvent event) throws Exception{
        if(!sname.getText().isEmpty()){
            String ask = "use MileStoneHRMS select s.Sp_Name from Speciality as s where s.Sp_Name = ";
            if(SQLTools.ValueGetId(ask, sname).isEmpty()){
                String sql = "use MileStoneHRMS select cast(substring(s.Sp_ID, 4, 8) as int) from Speciality as s ";
                String num = Integer.toString(SQLTools.id_incre(((int)SQLTools.Sql_Get_ID(sql))));
                String id = "SPE" + StringVariation.right(("00000" + num), 5);
                sid.setText(id);
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
        if(!sid.getText().isEmpty()){
            Data_Save("Speciality", "Sp_ID, Sp_Name", sid, sname);
        }
        StageControll.close(SAP);
        StageControll.open(AddSpecController.class, "/View/AddSpec.fxml");
    }
    
    public void Data_Save(String table, String itemnum, TextField id, TextField name) throws Exception{
        String sql = "use MileStoneHRMS insert into " + table + "( " + itemnum + " ) values (?,?)" ;
        try(Connection conn = SQLTools.MSSQL()){
            try(PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1, id.getText());
                pstmt.setString(2, name.getText());
                pstmt.execute();
                AuditLog.Audit("準備程序-專業專長登錄", id, name);                
            }
        }
    }
    
    @FXML
    public void btn_back(MouseEvent event) throws Exception{
        StageControll.close(SAP);
        StageControll.open(PreparationController.class, "/View/Preparation.fxml");
    }
}
