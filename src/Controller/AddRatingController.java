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
public class AddRatingController implements Initializable {
    
    @FXML public AnchorPane RAP;
    @FXML public TextField rid, rname;
    @FXML public Button back, set, submit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rid.setDisable(true);
        submit.setDisable(true);
        StageControll.TextFieldhandle(rname);
        StageControll.ButtonCtrl(back, rname);
        StageControll.ButtonCtrl(set, submit);
        StageControll.ButtonCtrl(submit, back);
    }
    
    @FXML
    public void set_rid(MouseEvent event) throws Exception{
        if(!rname.getText().isEmpty()){
            String ask = "use MileStoneHRMS select r.Rating_Name from Rating as r where r.Rating_Name = ";
            if(SQLTools.ValueGetId(ask, rname).isEmpty()){
                String sql = "use MileStoneHRMS select cast(substring(r.Ra_ID, 3, 7) as int) from Rating as r ";
                String num = Integer.toString(SQLTools.id_incre(((int)SQLTools.Sql_Get_ID(sql))));
                String id = "RA" + StringVariation.right(("00000" + num), 5);
                rid.setText(id);
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
        if(!rid.getText().isEmpty()){
            Data_Save("Rating", "Ra_ID, Rating_Name", rid, rname);
        }
        StageControll.close(RAP);
        StageControll.open(AddRatingController.class, "/View/AddRating.fxml");
    }
    
    public void Data_Save(String table, String itemnum, TextField id, TextField name) throws Exception{
        String sql = "use MileStoneHRMS insert into " + table + "( " + itemnum + " ) values (?,?)";
        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id.getText());
                pstmt.setString(2, name.getText());
                pstmt.execute();
                AuditLog.Audit("準備程序-評量等級登錄", id, name);
            }
        }    
    }
    
    @FXML
    public void btn_back(MouseEvent event) throws Exception{
        StageControll.close(RAP);
        StageControll.open(PreparationController.class, "/View/Preparation.fxml");
    }
}
