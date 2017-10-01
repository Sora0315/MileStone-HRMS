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
 * @author Sora
 */
public class MEvalR2Controller implements Initializable {

    @FXML public AnchorPane EVAP;
    @FXML public TextField name, comment, ssy, ssm, evy, evm, evd, sy, sm, sd, ey, em, ed;
    @FXML public ComboBox rating;
    @FXML public Button back, search, check, submit;
    public String id, evdate, sdate;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TextField[] list = {comment, evy, evm, evd, sy, sm, sd, ey, em, ed};
        for (TextField items : list) {
            items.setDisable(true);
        }
        TextField[] tfall = {name, comment, ssy, ssm, evy, evm, evd, sy, sm, sd, ey, em, ed};
        for (TextField items : tfall) {
            StageControll.TextFieldhandle(items);
        }
        StageControll.Buttonhandle(back);
        StageControll.Buttonhandle(search);
        StageControll.Buttonhandle(check);
        StageControll.Buttonhandle(submit);
        rating.setEditable(true);
        StageControll.ComboBoxCtrl(rating);
        StageControll.Keydirect(search, rating);
        AutoCompleteComboBox autoCompleteComboBox = new AutoCompleteComboBox(rating);
        rating.setDisable(true);
        submit.setDisable(true);
        check.setDisable(true);
        String sql = "use MileStoneHRMS select r.Rating_Name from Rating as r";
        try {
            SQLTools.comboboxSetItem(sql, rating);
        } catch (Exception e) {
        }
    }

    public void set_id() {
        try {
            String sql = "use MileStoneHRMS select p.P_ID from Personel as p where p.Name_CH = ";
            id = SQLTools.ValueGetId(sql, name);
        } catch (Exception e) {
        }
    }

    @FXML
    public void btn_search(MouseEvent event) throws Exception {
        if (!name.getText().isEmpty() || !ssy.getText().isEmpty() || !ssm.getText().isEmpty()) {
            search_and_setdata("r.Rating_Name", rating);
            search_and_setdata("e.Comment", comment);
            search_and_setdata("year(cast(e.EvalDay as date))", evy);
            search_and_setdata("month(cast(e.EvalDay as date))", evm);
            search_and_setdata("day(cast(e.EvalDay as date))", evd);
            search_and_setdata("year(cast(e.StartD as date))", sy);
            search_and_setdata("month(cast(e.StartD as date))", sm);
            search_and_setdata("day(cast(e.StartD as date))", sd);
            search_and_setdata("year(cast(e.EndD as date))", ey);
            search_and_setdata("month(cast(e.EndD as date))", em);
            search_and_setdata("day(cast(e.EndD as date))", ed);
            evdate = StringVariation.datecom(evy, evm, evd);
            sdate = StringVariation.datecom(sy, sm, sd);
            if (evy.getText().isEmpty() || evm.getText().isEmpty() || evd.getText().isEmpty()) {
                NoticeController.noticecontent = "找不到資料唷！";
                StageControll.open(NoticeController.class, "/View/Notice.fxml");
            } else {
                TextField[] list = {evy, evm, evd, sy, sm, sd, ey, em, ed, comment};
                for (TextField items : list) {
                    items.setDisable(false);
                }
                rating.setDisable(false);
                check.setDisable(false);
            }
        } else {
            NoticeController.noticecontent = "嘿！找資料也得輸入必要資訊呀";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");
        }
    }

    public void search_and_setdata(String column, TextField target) throws Exception {
        String sql = "use MileStoneHRMS select " + column
                + " from Eval as e left outer join Rating as r on e.Ra_ID = r.Ra_ID "
                + " where e.P_ID = " + "'" + id + "'"
                + " and year(cast(e.StartD as date)) = " + "'" + ssy.getText() + "'"
                + " and month(cast(e.StartD as date)) = " + "'" + ssm.getText() + "'";

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
                + " from Eval as e left outer join Rating as r on e.Ra_ID = r.Ra_ID "
                + " where e.P_ID = " + "'" + id + "'"
                + " and year(cast(e.StartD as date)) = " + "'" + ssy.getText() + "'"
                + " and month(cast(e.StartD as date)) = " + "'" + ssm.getText() + "'";

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
        if (name.getText().isEmpty() || evy.getText().isEmpty() || evm.getText().isEmpty() || evd.getText().isEmpty()
                || sy.getText().isEmpty() || sm.getText().isEmpty() || sd.getText().isEmpty()) {
            NoticeController.noticecontent = "請輸入必要資料！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");
        } else {
            submit.setDisable(false);
        }
    }

    @FXML
    public void btn_submit(MouseEvent event) throws Exception {
        Data_Save();
        StageControll.close(EVAP);
        StageControll.open(MEvalR2Controller.class, "/View/MEvalR2.fxml");
    }

    public void Data_Save() throws Exception {
        String sql = "use MileStoneHRMS update Eval\n"
                + " set EvalDay = ?, StartD = ?, EndD = ?, Ra_ID = ?, Comment = ?\n"
                + " where Eval.P_ID = " + "'" + id + "'" + " and Eval.EvalDay = " + "'" + evdate + "'"
                + " and Eval.StartD = " + "'" + sdate + "'";

        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, StringVariation.datecom(evy, evm, evd));
                SQLTools.emptyUnitSetNull(StringVariation.datecom(sy, sm, sd), pstmt, 2);
                SQLTools.emptyUnitSetNull(StringVariation.datecom(ey, em, ed), pstmt, 3);
                SQLTools.emptyUnitSetNull(rating, pstmt, 4);
                SQLTools.emptyUnitSetNull(comment, pstmt, 5);
                pstmt.execute();
                AuditLog.Audit("主管/HR-修正員工考核紀錄", name, rating, StringVariation.datecom(sy, sm, sd));
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
        StageControll.open(MEvalRController.class, "/View/MEvalR.fxml");
    }
}
