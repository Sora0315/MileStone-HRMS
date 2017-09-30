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
public class AddPropertyController implements Initializable {

    @FXML public AnchorPane PAP;
    @FXML public TextField pid, pname, date, memo;
    @FXML public Button back, set,submit;
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pid.setDisable(true);
        submit.setDisable(true);
        StageControll.TextFieldhandle(pname);
        StageControll.TextFieldhandle(date);
        StageControll.TextFieldhandle(memo);
        StageControll.ButtonCtrl(back, pname);
        StageControll.ButtonCtrl(set, submit);
        StageControll.ButtonCtrl(submit, back);
    }
    
    @FXML
    public void set_pid(MouseEvent event) throws Exception{
        if(!pname.getText().isEmpty()){
            String ask = "use MileStoneHRMS select p.Pr_Name from Property as p where p.Pr_Name = ";
            if(SQLTools.ValueGetId(ask, pname).isEmpty()){
                String sql = "use MileStoneHRMS select cast(substring(p.Pr_ID, 3, 7) as int) from Property as p ";
                String num = Integer.toString(SQLTools.id_incre(((int)SQLTools.Sql_Get_ID(sql))));
                String id = "PR" + StringVariation.right(("00000" + num), 5);
                pid.setText(id);
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
        if(!pid.getText().isEmpty()){
            Data_Save("Property", "Pr_ID, Pr_Name, DatePur, Memo", pid, pname, date, memo);
        }
        StageControll.close(PAP);
        StageControll.open(AddPropertyController.class, "/View/AddProperty.fxml");
    }
    
    public void Data_Save(String table, String itemnum, TextField id, TextField name, TextField date, TextField name2) throws Exception {
        String sql = "use MileStoneHRMS insert into " + table + "( " + itemnum + " ) values (?,?,?,?)";
        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id.getText());
                pstmt.setString(2, name.getText());
                SQLTools.emptyslotsetnull(date, pstmt, 3);
                SQLTools.emptyslotsetnull(name2, pstmt, 4);
                pstmt.execute();
                AuditLog.Audit("準備程序-公司財產登錄", id, name);
            }
        }
    }
    
    @FXML
    public void btn_back(MouseEvent event) throws Exception{
        StageControll.close(PAP);
        StageControll.open(PreparationController.class, "/View/Preparation.fxml");
    }
}
