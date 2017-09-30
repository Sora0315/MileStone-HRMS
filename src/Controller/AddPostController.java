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
public class AddPostController implements Initializable {
    
    @FXML public AnchorPane PAP;
    @FXML public TextField pid, pname, duty;
    @FXML public Button back, set, submit;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pid.setDisable(true);
        submit.setDisable(true);
        StageControll.TextFieldhandle(pname);
        StageControll.TextFieldhandle(duty);
        StageControll.ButtonCtrl(back, pname);
        StageControll.ButtonCtrl(set, submit);
        StageControll.ButtonCtrl(submit, back);
    }
    
    @FXML
    public void set_pid(MouseEvent event) throws Exception{
        if(!pname.getText().isEmpty()){
            String ask = "use MileStoneHRMS select p.Post_Name from Position as p where p.Post_Name = ";
            if(SQLTools.ValueGetId(ask, pname).isEmpty()){
                String sql = "use MileStoneHRMS select cast(substring(p.Post_ID, 3, 7) as int) from Position as p ";
                String num = Integer.toString(SQLTools.id_incre(((int)SQLTools.Sql_Get_ID(sql))));
                String id = "PO" + StringVariation.right(("00000" + num), 5);
                pid.setText(id);
                submit.setDisable(false);
            }
            else{
                NoticeController.noticecontent = "請輸入必要資料！";
                StageControll.open(NoticeController.class, "Notice.fxml");
            }
        }
    }
    
    @FXML
    public void btn_submit(MouseEvent event) throws Exception{
        if(!pid.getText().isEmpty()){
            Data_Save("Position", "Post_ID, Post_Name, Duty", pid, pname, duty);
        }
        StageControll.close(PAP);
        StageControll.open(AddPostController.class, "AddPost.fxml");
    }
    
    public void Data_Save(String table, String itemnum, TextField id, TextField name, TextField name2) throws Exception {
        String sql = "use MileStoneHRMS insert into " + table + "( " + itemnum + " ) values (?,?,?)";
        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id.getText());
                pstmt.setString(2, name.getText());
                SQLTools.emptyslotsetnull(name2, pstmt, 3);
                pstmt.execute();
                AuditLog.Audit("準備程序-職務", id, name);
            }
        }
    }
    
    @FXML
    public void btn_back(MouseEvent event) throws Exception{
        StageControll.close(PAP);
        StageControll.open(PreparationController.class, "Preparation.fxml");
    }
}
