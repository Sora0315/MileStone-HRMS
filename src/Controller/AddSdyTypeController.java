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
public class AddSdyTypeController implements Initializable {
    
    @FXML public AnchorPane BAP;
    @FXML public TextField bid, bname;
    @FXML public Button back, set,submit;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bid.setDisable(true);
        submit.setDisable(true);
        StageControll.TextFieldhandle(bname);
        StageControll.ButtonCtrl(back, bname);
        StageControll.ButtonCtrl(set, submit);
        StageControll.ButtonCtrl(submit, back);
    }
    
    @FXML
    public void set_bid(MouseEvent event) throws Exception{
        if(!bname.getText().isEmpty()){
            String ask = "use MileStoneHRMS select s.Type_Name from SdyType as s where s.Type_Name = ";
            if(SQLTools.ValueGetId(ask, bname).isEmpty()){
                String sql = "use MileStoneHRMS select cast(substring(s.Type_ID, 4, 8) as int) from SdyType as s ";
                String num = Integer.toString(SQLTools.id_incre(((int)SQLTools.Sql_Get_ID(sql))));
                String id = "SDY" + StringVariation.right(("00000" + num), 5);
                bid.setText(id);
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
        if(!bid.getText().isEmpty()){
            Data_Save("SdyType", "Type_ID, Type_Name", bid, bname);
        }
        StageControll.close(BAP);
        StageControll.open(AddSdyTypeController.class, "/View/AddSdyType.fxml");
    }
    
    public void Data_Save(String table, String itemnum, TextField id, TextField name) throws Exception{
        String sql = "use MileStoneHRMS insert into " + table + "( " + itemnum + " ) values (?,?)" ;
        try(Connection conn = SQLTools.MSSQL()){
            try(PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1, id.getText());
                pstmt.setString(2, name.getText());
                pstmt.execute();
                AuditLog.Audit("準備程序-津貼種類登錄", id, name);        
            }
        }
    }
    
    @FXML
    public void btn_back(MouseEvent event) throws Exception{
        StageControll.close(BAP);
        StageControll.open(PreparationController.class, "/View/Preparation.fxml");
    }
}
