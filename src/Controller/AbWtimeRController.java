package Controller;

import SelfTools.AuditLog;
import SelfTools.AutoCompleteComboBox;
import SelfTools.SQLTools;
import SelfTools.StageControll;
import SelfTools.StringVariation;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Sora
 */
public class AbWtimeRController implements Initializable {

    @FXML
    public AnchorPane AWAP;
    @FXML
    public TextField id, name, cause;
    @FXML
    public ComboBox lo;
    @FXML
    public TextField sy, sm, sd, sh, smin, ey, em, ed, eh, emin, d, h;
    @FXML
    public Button back, check, submit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setDisable(true);
        d.setDisable(true);
        h.setDisable(true);
        submit.setDisable(true);
        String sql = "use MileStoneHRMS select l.LO_Name from LO as l";
        try {
            SQLTools.SqlGetItem(sql, lo);
        } catch (Exception e) {
        }
        AutoCompleteComboBox autoCompleteComboBox = new AutoCompleteComboBox(lo);
        StageControll.AnchorPaneKeyCtrl(AWAP);
        StageControll.Keydirect(name, lo);
        TextField[] tfall = {id, name, cause, sy, sm, sd, sh, smin, ey, em, ed, eh, emin, d, h};
        for (TextField items : tfall) {
            StageControll.TextFieldhandle(items);
        }
        StageControll.ButtonCtrl(back, name);
        StageControll.ButtonCtrl(check, submit);
        StageControll.ButtonCtrl(submit, back);
    }

    public void set_id() throws Exception {
        String sql = "use MileStoneHRMS select p.P_ID from Personel as p where p.Name_CH = ";
        id.setText(SQLTools.ValueGetId(sql, name));
    }

    @FXML
    public void btn_checkdata(MouseEvent event) throws Exception {
        if (id.getText().isEmpty() || lo.getSelectionModel().getSelectedItem() == null
                || sy.getText().isEmpty() || sm.getText().isEmpty() || sd.getText().isEmpty()
                || sh.getText().isEmpty() || smin.getText().isEmpty()) {
            NoticeController.noticecontent = "請輸入必要資料！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");
        } else {
            String sql = "use MileStoneHRMS select a.P_ID from AbWorktimeR as a where a.P_ID = "
                    + "'" + id.getText() + "'" + " and a.LO_ID = "
                    + "'" + set_loid() + "'" + " and a.StartD = "
                    + "'" + StringVariation.datetimecom(sy, sm, sd, sh, smin) + "'" + " and a.EndD = "
                    + "'" + StringVariation.datetimecom(ey, em, ed, eh, emin) + "'";

            try (Connection conn = SQLTools.MSSQL()) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    NoticeController.noticecontent = "資料已存在！";
                    StageControll.open(NoticeController.class, "/View/Notice.fxml");
                } else {
                    if (ey.getText().isEmpty() || emin.getText().isEmpty()) {
                        d.clear();
                        h.clear();
                    } else {
                        interval();
                    }
                    submit.setDisable(false);
                }
            }
        }
    }

    @FXML
    public void btn_submit(MouseEvent event) throws Exception {
        Data_Save();
        StageControll.close(AWAP);
        StageControll.open(AbWtimeRController.class, "/View/AbWtimeR.fxml");
    }

    public void Data_Save() throws Exception {
        try (Connection conn = SQLTools.MSSQL()) {
            String sql = "use MileStoneHRMS insert into AbWorktimeR(P_ID, LO_ID, StartD, EndD, Interval, Cause)"
                    + " values(?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id.getText());
            pstmt.setString(2, set_loid());
            pstmt.setString(3, StringVariation.datetimecom(sy, sm, sd, sh, smin));
            SQLTools.emptyslotsetnull(StringVariation.datetimecom(ey, em, ed, eh, emin), pstmt, 4);
            if (d.getText().isEmpty() || h.getText().isEmpty()) {
                pstmt.setNull(5, java.sql.Types.VARCHAR);
            } else {
                pstmt.setString(5, d.getText() + " 日 " + h.getText() + "    時");
            }
            SQLTools.emptyslotsetnull(cause, pstmt, 6);
            pstmt.execute();
            AuditLog.Audit("HR/DBA 出勤紀錄登錄", id, lo, StringVariation.datetimecom(sy, sm, sd, sh, smin));
        }
    }

    public String set_loid() throws Exception {
        String sql = "use MileStoneHRMS select l.LO_ID from LO as l where l.LO_Name = ";
        return (SQLTools.ValueGetId(sql, lo));
    }

    @FXML
    public void interval() throws SQLException {
        String sql = "use MileStoneHRMS select  datediff(MINUTE, ' " + StringVariation.datetimecom(sy, sm, sd, sh, smin) + " ' "
                + ", ' " + StringVariation.datetimecom(ey, em, ed, eh, emin) + " ' ) ";
        try (Connection conn = SQLTools.MSSQL()) {
            int temp = 0;
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                while (rs.next()) {
                    temp = rs.getInt(1);
                }
                double out = temp / 60.0;
                DecimalFormat df = new DecimalFormat("##.#");
                if (out < 24) {
                    h.setText(df.format(out));
                    d.setText("0");
                } else {
                    h.setText(String.valueOf(df.format(out % 24.0)));
                    d.setText(String.valueOf(((int) out) / 24));
                }
            }
        }
    }

    @FXML
    public void btn_back() throws Exception {
        StageControll.close(AWAP);
        StageControll.open(MenuSController.class, "/View/MenuS.fxml");
    }
}
