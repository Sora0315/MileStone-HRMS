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
public class AddActivityController implements Initializable {

    @FXML public AnchorPane AAP;
    @FXML public TextField aid, aname;
    @FXML public Button back, set, submit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        aid.setDisable(true);
        submit.setDisable(true);
        StageControll.TextFieldhandle(aname);
        StageControll.ButtonCtrl(back, aname);
        StageControll.ButtonCtrl(set, submit);
        StageControll.ButtonCtrl(submit, back);
    }

    @FXML
    public void set_aid(MouseEvent event) throws Exception {
        if (!aname.getText().isEmpty()) {
            String ask = "use MileStoneHRMS select a.Act_Name from Activity as a where a.Act_Name = ";
            if (SQLTools.ValueGetId(ask, aname).isEmpty()) {
                String sql = "use MileStoneHRMS select cast(substring(a.Act_ID, 4, 8) as int) from Activity as a ";
                String num = Integer.toString(SQLTools.id_incre(((int) SQLTools.Sql_Get_ID(sql))));
                String id = "ACT" + StringVariation.right(("00000" + num), 5);
                aid.setText(id);
                submit.setDisable(false);
            } else {
                NoticeController.noticecontent = "資料已經存在！";
                StageControll.open(NoticeController.class, "/View/Notice.fxml");
            }
        } else {
            NoticeController.noticecontent = "請輸入必要資料！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");
        }
    }

    @FXML
    public void btn_submit(MouseEvent event) throws Exception {
        if (!aid.getText().isEmpty()) {
            Data_Save("Activity", "Act_ID, Act_Name", aid, aname);
        }
        StageControll.close(AAP);
        StageControll.open(AddActivityController.class, "/View/AddActivity.fxml");
    }

    public void Data_Save(String table, String itemnum, TextField id, TextField name) throws Exception {
        String sql = "use MileStoneHRMS insert into " + table + "( " + itemnum + " ) values (?,?)";
        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id.getText());
                pstmt.setString(2, name.getText());
                pstmt.execute();
                AuditLog.Audit("準備程序-服務狀態", id, name);
            }
        }
    }

    @FXML
    public void btn_back(MouseEvent event) throws Exception {
        StageControll.close(AAP);
        StageControll.open(PreparationController.class, "/View/Preparation.fxml");
    }
}
