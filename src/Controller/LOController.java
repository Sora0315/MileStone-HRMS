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
public class LOController implements Initializable {

    @FXML  public AnchorPane LOAP;
    @FXML  public TextField loid, name, limit, source, memo;
    @FXML  public Button back, check, submit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loid.setDisable(true);
        submit.setDisable(true);
        StageControll.TextFieldhandle(name);
        StageControll.TextFieldhandle(limit);
        StageControll.TextFieldhandle(source);
        StageControll.TextFieldhandle(memo);
        StageControll.Buttonhandle(back);
        StageControll.Buttonhandle(check);
        StageControll.Buttonhandle(submit);
    }

    @FXML
    public void set_loid(MouseEvent event) throws Exception {
        if (!name.getText().isEmpty()) {
            String ask = "use MileStoneHRMS select l.LO_Name from LO as l where l.LO_Name = ";
            if (SQLTools.ValueGetId(ask, name).isEmpty()) {
                String sql = "use MileStoneHRMS select cast(substring(l.LO_ID, 3, 7) as int) from LO as l";
                String num = Integer.toString(SQLTools.idAutoIncrease(((int) SQLTools.sqlQuerySetId(sql))));
                String id = "LO" + StringVariation.right(("00000" + num), 5);
                loid.setText(id);
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
        Data_Save();
        StageControll.close(LOAP);
        StageControll.open(LOController.class, "/View/LO.fxml");
    }

    public void Data_Save() throws Exception {
        String sql = "use MileStoneHRMS insert into LO(LO_ID, LO_Name, LO_Limit, Source, Memo)"
                + "values(?,?,?,?,?)";

        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, loid.getText());
                pstmt.setString(2, name.getText());
                SQLTools.emptyUnitSetNull(limit, pstmt, 3);
                SQLTools.emptyUnitSetNull(source, pstmt, 4);
                SQLTools.emptyUnitSetNull(memo, pstmt, 3);
                pstmt.execute();
                AuditLog.Audit("準備程序-異常工時類別", loid, name);
            }
        }
    }

    @FXML
    public void btn_back(MouseEvent event) throws Exception {
        StageControll.open(PreparationController.class, "/View/Preparation.fxml");
        StageControll.close(LOAP);
    }
}
