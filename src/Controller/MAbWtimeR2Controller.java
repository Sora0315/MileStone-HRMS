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
import javafx.scene.layout.AnchorPane;
import SelfTools.AutoCompleteComboBox;
import SelfTools.SQLTools;
import SelfTools.StageControll;
import SelfTools.StringVariation;
import java.sql.SQLException;
import java.text.DecimalFormat;

/**
 * FXML Controller class
 *
 * @author Sora
 */
public class MAbWtimeR2Controller implements Initializable {

    @FXML public AnchorPane AWAP;
    @FXML public TextField id, name, cause, sy, sm, sd, sh, smin, ey, em, ed, eh, emin, d, h;
    @FXML public ComboBox lo;
    @FXML public Button back, search, check, submit;
    public String olo, osdatetime;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TextField[] list = {id, cause, ey, em, ed, eh, emin, d, h};
        for (TextField items : list) {
            items.setDisable(true);
        }
        TextField[] tfall = {id, name, cause, sy, sm, sd, sh, smin, ey, em, ed, eh, emin, d, h};
        for (TextField items : tfall) {
            StageControll.TextFieldhandle(items);
        }
        StageControll.Buttonhandle(back);
        StageControll.Buttonhandle(search);
        StageControll.Buttonhandle(check);
        StageControll.Buttonhandle(submit);
        StageControll.ComboBoxCtrl(lo);
        StageControll.Keydirect(name, lo);
        lo.setEditable(true);
        search.setDisable(true);
        check.setDisable(true);
        submit.setDisable(true);
        String sql = "use MileStoneHRMS select l.LO_Name from LO as l";
        try {
            SQLTools.SqlGetItem(sql, lo);
            AutoCompleteComboBox autoCompleteComboBox = new AutoCompleteComboBox(lo);
        } catch (Exception e) {
        }
    }

    public void set_id() {
        try {
            String sql = "use MileStoneHRMS select p.P_ID from Personel as p where p.Name_CH = ";
            id.setText(SQLTools.ValueGetId(sql, name));
        } catch (Exception e) {
        }
    }

    public void set_searchenable() {
        search.setDisable(false);
    }

    @FXML
    public void btn_search() throws Exception {
        search_and_setdata(" a.Cause ", cause);
        search_and_setdata(" year(cast(a.EndD as date)) ", ey);
        search_and_setdata(" month(cast(a.EndD as date)) ", em);
        search_and_setdata(" day(cast(a.EndD as date)) ", ed);
        search_and_setdata(" datepart(hh, a.EndD) ", eh);
        search_and_setdata(" datepart(mi, a.EndD) ", emin);
        search_and_setdata(" (a.Interval)/24 ", d);
        search_and_setdata(" (a.Interval)%24 ", h);
        String sql = "use MileStoneHRMS select a.LO_ID "
                + " from AbWorktimeR as a left outer join LO as l on l.LO_ID = a.LO_ID "
                + " where a.P_ID = " + "'" + id.getText() + "'"
                + " and a.LO_ID = " + "'" + set_loid() + "'"
                + " and a.StartD = " + "'" + StringVariation.datetimecom(sy, sm, sd, sh, smin) + "'";

        try (Connection conn = SQLTools.MSSQL()) {
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                String out = null;
                while (rs.next()) {
                    out = rs.getString(1);
                }
                if (out != null) {
                    olo = lo.getSelectionModel().getSelectedItem().toString();
                    osdatetime = StringVariation.datetimecom(sy, sm, sd, sh, smin);
                    TextField[] list = {cause, ey, em, ed, eh, emin};
                    for (TextField items : list) {
                        items.setDisable(false);
                    }
                    check.setDisable(false);
                } else {
                    NoticeController.noticecontent = "找不到資料唷！";
                    StageControll.open(NoticeController.class, "/View/Notice.fxml");
                }
            }
        }
    }

    public void search_and_setdata(String column, TextField target) throws Exception {
        String sql = "use MileStoneHRMS select " + column
                + " from AbWorktimeR as a left outer join LO as l on l.LO_ID = a.LO_ID "
                + " where a.P_ID = " + "'" + id.getText() + "'"
                + " and a.LO_ID = " + "'" + set_loid() + "'"
                + " and a.StartD = " + "'" + StringVariation.datetimecom(sy, sm, sd, sh, smin) + "'";

        try (Connection conn = SQLTools.MSSQL()) {
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                String out = null;
                while (rs.next()) {
                    out = rs.getString(1);
                }
                if (out != null) {
                    target.setText(out);
                } else {
                    target.clear();
                }
            }
        }
    }

    public void search_and_setdata(String column, ComboBox target) throws Exception {
        String sql = "use MileStoneHRMS select " + column
                + " from AbWorktimeR as a left outer join LO as l on l.LO_ID = a.LO_ID "
                + " where a.P_ID = " + "'" + id.getText() + "'"
                + " and a.LO_ID = " + "'" + set_loid() + "'"
                + " and a.StartD = " + "'" + StringVariation.datetimecom(sy, sm, sd, sh, smin) + "'";

        try (Connection conn = SQLTools.MSSQL()) {
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                String out = null;
                while (rs.next()) {
                    out = rs.getString(1);
                }
                if (out != null) {
                    target.getSelectionModel().select(out);
                }
            }
        }
    }

    @FXML
    public void btn_checkdata(MouseEvent event) throws Exception {
        if (id.getText().isEmpty() || lo.getSelectionModel().getSelectedItem() == null
                || sy.getText().isEmpty() || sm.getText().isEmpty() || sd.getText().isEmpty()
                || sh.getText().isEmpty() || smin.getText().isEmpty()) {
            NoticeController.noticecontent = "請輸入必要資料！";
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

    @FXML
    public void btn_submit(MouseEvent event) throws Exception {
        Data_Save();
        StageControll.close(AWAP);
        StageControll.open(MAbWtimeR2Controller.class, "/View/MAbWtimeR2.fxml");
    }

    public void Data_Save() throws Exception {

        String ask = "use MileStoneHRMS select l.LO_ID from LO as l where l.LO_Name = ";
        String sql = "use MileStoneHRMS update AbWorktimeR "
                + " set LO_ID = ?, StartD = ?, EndD = ?, Interval = ?, Cause = ?  "
                + " where P_ID = " + "'" + id.getText() + "'"
                + " and LO_ID = " + "'" + SQLTools.ValueGetId(ask, olo) + "'"
                + " and StartD = " + "'" + osdatetime + "'";

        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, set_loid());
                pstmt.setString(2, StringVariation.datetimecom(sy, sm, sd, sh, smin));
                SQLTools.emptyslotsetnull(StringVariation.datetimecom(ey, em, ed, eh, emin), pstmt, 3);
                if (d.getText().isEmpty() && h.getText().isEmpty()) {
                    pstmt.setNull(4, java.sql.Types.VARCHAR);
                } else {
                    pstmt.setString(4, String.valueOf(Integer.valueOf(d.getText()) * 24 + Integer.valueOf(h.getText())));
                }
                SQLTools.emptyslotsetnull(cause, pstmt, 5);
                pstmt.execute();
                AuditLog.Audit("主管/HR-修正員工異常工時紀錄", name, lo, StringVariation.datetimecom(sy, sm, sd, sh, smin));
            }
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

        int temp = 0;
        try (Connection conn = SQLTools.MSSQL()) {
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
        StageControll.open(MAbWtimeRController.class, "/View/MAbWtimeR.fxml");
    }
}
