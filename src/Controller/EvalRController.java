package Controller;

import SelfTools.AuditLog;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import SelfTools.SQLTools;
import SelfTools.StageControll;
import SelfTools.StringVariation;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 * @author Sora
 */
public class EvalRController implements Initializable {

    @FXML  public AnchorPane EVAP;
    @FXML  public TextField id, comment;
    @FXML  public ComboBox name, rating;
    @FXML  public TextField evy, evm, evd, sy, sm, sd, ey, em, ed;
    @FXML  public Button back, check, submit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setDisable(true);
        submit.setDisable(true);
        String sql = "use MileStoneHRMS select r.Rating_Name from Rating as r";
        String sql_name = "use MileStoneHRMS select distinct p.Name_CH from Personel as p "
                + " left join PStatus as ps on ps.P_ID = p.P_ID left join Activity as a on a.Act_ID = ps.Act_ID\n"
                + " where a.Act_Name like '在職' and ps.EndD is null ";
        try {
            SQLTools.comboboxSetItem(sql, rating);
            SQLTools.comboboxSetItem(sql_name, name);
        } catch (Exception e) {
        }
        name.setEditable(true);
        rating.setEditable(true);
        name.setOnAction(e -> set_id());
        StageControll.AnchorPaneKeyCtrl(EVAP);
        StageControll.Keydirect(back, name);
        StageControll.Keydirect(name, rating);
        StageControll.TextFieldhandle(comment);
        StageControll.TextFieldhandle(evy);
        StageControll.TextFieldhandle(evm);
        StageControll.TextFieldhandle(evd);
        StageControll.TextFieldhandle(sy);
        StageControll.TextFieldhandle(sm);
        StageControll.TextFieldhandle(sd);
        StageControll.TextFieldhandle(ey);
        StageControll.TextFieldhandle(em);
        StageControll.TextFieldhandle(ed);
        StageControll.Buttonhandle(back);
        StageControll.Buttonhandle(check);
        StageControll.Buttonhandle(submit);
    }

    public void set_id() {
        try {
            String sql = "use MileStoneHRMS select p.P_ID from Personel as p where p.Name_CH = ";
            id.setText(SQLTools.ValueGetId(sql, name));
        } catch (Exception e) {
        }
    }

    @FXML
    public void btn_checkdata(MouseEvent event) throws Exception {
        if (id.getText().isEmpty() || evy.getText().isEmpty() || evm.getText().isEmpty() || evd.getText().isEmpty()
                || sy.getText().isEmpty() || sm.getText().isEmpty() || sd.getText().isEmpty()) {
            NoticeController.noticecontent = "請輸入必要資料！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");
        } else {
            String sql = "use MileStoneHRMS select e.P_ID from Eval as e where e.P_ID = "
                    + "'" + id.getText() + "'" + " and e.EvalDay = "
                    + "'" + StringVariation.datecom(evy, evm, evd) + "'" + " and e.StartD = "
                    + "'" + StringVariation.datecom(sy, sm, sd) + "'";

            try (Connection conn = SQLTools.MSSQL()) {
                try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                    if (rs.next()) {
                        NoticeController.noticecontent = "資料已存在！";
                        StageControll.open(NoticeController.class, "/View/Notice.fxml");
                    } else {
                        submit.setDisable(false);
                    }
                }
            }
        }
    }

    @FXML
    public void btn_submit(MouseEvent event) throws Exception {
        Data_Save();
        StageControll.close(EVAP);
        StageControll.open(EvalRController.class, "/View/EvalR.fxml");
    }

    public void Data_Save() throws Exception {
        String sql = "use MileStoneHRMS insert into Eval(P_ID, EvalDay, StartD, EndD, Ra_ID, Comment)"
                + " values(?,?,?,?,?,?)";
        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id.getText());
                pstmt.setString(2, StringVariation.datecom(evy, evm, evd));
                SQLTools.emptyUnitSetNull(StringVariation.datecom(sy, sm, sd), pstmt, 3);
                SQLTools.emptyUnitSetNull(StringVariation.datecom(ey, em, ed), pstmt, 4);
                SQLTools.emptyUnitSetNull(rating, pstmt, 5);
                SQLTools.emptyUnitSetNull(comment, pstmt, 6);
                pstmt.execute();
                AuditLog.Audit("主管/HR-員工考核", id, rating, StringVariation.datecom(sy, sm, sd));
            }
        }
    }

    public String set_ratingid() throws Exception {
        String sql = "use MileStoneHRMS select r.Ra_ID from Rating as r where r.Rating_Name = ";
        return (SQLTools.ValueGetId(sql, rating));
    }

    @FXML
    public void btn_back() throws Exception {
        StageControll.close(EVAP);
        StageControll.open(MenuSController.class, "/View/MenuS.fxml");
    }
}
