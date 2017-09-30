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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 * @author Sora
 */
public class MPsecPwkexp2Controller implements Initializable {

    @FXML
    public AnchorPane PAP;
    @FXML
    public Label i1, i2, i3;
    @FXML
    public ComboBox s1, s2, s3, s4, s5, s6, s7, s8, w1, w2, w3, wp1, wp2, wp3;
    @FXML
    public TextField name, sy1, sy2, sy3, sm1, sm2, sm3, ey1, ey2, ey3, em1, em2, em3;
    @FXML
    public Button back, search, check, submit;

    public String id;
    public int snum, wnum;
    public String[] os = new String[8];
    public String[] ow = new String[3];
    public String[] owp = new String[3];
    public String[] penum = new String[3];

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ComboBox[] c = {s1, s2, s3, s4, s5, s6, s7, s8, w1, w2, w3, wp1, wp2, wp3};
        TextField[] t = {sy1, sy2, sy3, sm1, sm2, sm3, ey1, ey2, ey3, em1, em2, em3};
        for (ComboBox items : c) {
            items.setDisable(true);
            StageControll.ComboBoxCtrl(items);
            items.setEditable(true);
            AutoCompleteComboBox autoCompleteComboBox = new AutoCompleteComboBox(items);
        }
        StageControll.Keydirect(search, s1);
        StageControll.Keydirect(s1, s2);
        StageControll.Keydirect(s2, s3);
        StageControll.Keydirect(s3, s4);
        StageControll.Keydirect(s4, s5);
        StageControll.Keydirect(s5, s6);
        StageControll.Keydirect(s6, s7);
        StageControll.Keydirect(s7, s8);
        StageControll.Keydirect(s1, w1);
        StageControll.Keydirect(w1, wp1);
        StageControll.Keydirect(w2, wp2);
        StageControll.Keydirect(w3, wp3);
        for (TextField items : t) {
            items.setDisable(true);
            StageControll.TextFieldhandle(items);
        }
        StageControll.TextFieldhandle(name);
        submit.setDisable(true);
        String specsql = "use MileStoneHRMS select s.Sp_Name from Speciality as s ";
        String wsql = "use MileStoneHRMS select distinct w.Exp_Name "
                + " from WkExperience as w left outer join ExpPost as e on e.ExpPost_ID = w.ExpPost_ID ";
        try {
            ComboBox[] s = {s1, s2, s3, s4, s5, s6, s7, s8};
            for (ComboBox items : s) {
                SQLTools.SqlGetItem(specsql, items);
            }
            ComboBox[] w = {w1, w2, w3};
            for (ComboBox items : w) {
                SQLTools.SqlGetItem(wsql, items);
            }
        } catch (Exception e) {
        }
        StageControll.ButtonCtrl(back, name);
        StageControll.ButtonCtrl(search, s1);
        StageControll.ButtonCtrl(check, submit);
        StageControll.ButtonCtrl(submit, back);
    }

    @FXML
    public void btn_search(MouseEvent event) throws Exception {
        String specsql = "use MileStoneHRMS select s.Sp_Name from PSpec as p "
                + " left outer join Speciality as s on s.Sp_ID = p.Sp_ID "
                + " where p.P_ID = " + "'" + id + "'";
        ComboBox[] s = {s1, s2, s3, s4, s5, s6, s7, s8};
        search_and_setdata(specsql, s, os);
        String wsql = "use MileStoneHRMS select w.Exp_Name from PWkExp as p "
                + " left outer join WkExperience as w on p.Exp_ID = w.Exp_ID "
                + " where p.P_ID = " + "'" + id + "'"
                + " order by p.Exp_Num Asc ";
        ComboBox[] w = {w1, w2, w3};
        search_and_setdata(wsql, w, ow);
        String wpsql = "use MileStoneHRMS select e.ExpPost_Name from PWkExp as p "
                + " left outer join WkExperience as w on p.Exp_ID = w.Exp_ID "
                + " left outer join ExpPost as e on e.ExpPost_ID = w.ExpPost_ID "
                + " where p.P_ID = " + "'" + id + "'"
                + " order by p.Exp_Num Asc ";
        ComboBox[] wp = {wp1, wp2, wp3};
        search_and_setdata(wpsql, wp, owp);
        String penumsql = "use MileStoneHRMS select p.Exp_Num from PWkExp as p "
                + " left outer join WkExperience as w on p.Exp_ID = w.Exp_ID "
                + " where p.P_ID = " + "'" + id + "'"
                + " order by p.Exp_Num Asc ";
        Label[] lb = {i1, i2, i3};
        search_and_setdata(penumsql, lb, penum);
        search_and_setdata(" year(cast(p.StartD as date)) ", w1, wp1, sy1);
        search_and_setdata(" month(cast(p.StartD as date)) ", w1, wp1, sm1);
        search_and_setdata(" year(cast(p.EndD as date)) ", w1, wp1, ey1);
        search_and_setdata(" month(cast(p.EndD as date)) ", w1, wp1, em1);
        search_and_setdata(" year(cast(p.StartD as date)) ", w2, wp2, sy2);
        search_and_setdata(" month(cast(p.StartD as date)) ", w2, wp2, sm2);
        search_and_setdata(" year(cast(p.EndD as date)) ", w2, wp2, ey2);
        search_and_setdata(" month(cast(p.EndD as date)) ", w2, wp2, em2);
        search_and_setdata(" year(cast(p.StartD as date)) ", w3, wp3, sy3);
        search_and_setdata(" month(cast(p.StartD as date)) ", w3, wp3, sm3);
        search_and_setdata(" year(cast(p.EndD as date)) ", w3, wp3, ey3);
        search_and_setdata(" month(cast(p.EndD as date)) ", w3, wp3, em3);
    }

    public void search_and_setdata(String column, ComboBox w, ComboBox wp, TextField target) throws Exception {
        String sql = "use MileStoneHRMS select " + column + " from PWkExp as p "
                + " left outer join WkExperience as w on w.Exp_ID = p.Exp_ID "
                + " left outer join ExpPost as e on e.ExpPost_ID = w.ExpPost_ID"
                + " where p.P_ID = " + "'" + id + "'"
                + " and w.Exp_Name = " + "'" + w.getSelectionModel().getSelectedItem() + "'"
                + " and e.ExpPost_Name = " + "'" + wp.getSelectionModel().getSelectedItem() + "'";

        try (Connection conn = SQLTools.MSSQL()) {
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                String out = null;
                while (rs.next()) {
                    out = rs.getString(1);
                }
                if (!w.getSelectionModel().isEmpty()) {
                    target.setDisable(false);
                    target.setText(out);
                } else {
                    target.setText(null);
                }
            }
        }
    }

    public void search_and_setdata(String sql, ComboBox[] target, String[] name) throws Exception {
        try (Connection conn = SQLTools.MSSQL()) {
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                int i = 0;
                while (rs.next()) {
                    String out = rs.getString(1);
                    target[i].getSelectionModel().select(out);
                    target[i].setDisable(false);
                    name[i] = out;
                    i = i + 1;
                }
            }
        }
    }

    public void search_and_setdata(String sql, Label[] target, String[] name) throws Exception {
        try (Connection conn = SQLTools.MSSQL()) {
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                int i = 0;
                while (rs.next()) {
                    String out = rs.getString(1);
                    target[i].setText(out);
                    name[i] = out;
                    i = i + 1;
                }
            }
        }
    }

    @FXML
    public void set_id() {
        if (!name.getText().isEmpty()) {
            try {
                id = SQLTools.ValueGetId("use MileStoneHRMS select p.P_ID from Personel as p "
                        + " where p.Name_CH = ", name);
            } catch (Exception e) {
            }
        }
    }

    public void set_wp_enable(ComboBox w, ComboBox wp) throws Exception {
        if (!w.getSelectionModel().isEmpty()) {
            String sql = " use  MileStoneHRMS select distinct e.ExpPost_Name from WkExperience as w "
                    + " left outer join ExpPost as e on e.ExpPost_ID = w.ExpPost_ID "
                    + " where w.Exp_Name = " + "'" + w.getSelectionModel().getSelectedItem().toString() + "'";
            SQLTools.SqlGetItem(sql, wp);
            wp.setDisable(false);
        }
    }

    @FXML
    public void setwp1() {
        try {
            set_wp_enable(w1, wp1);
        } catch (Exception e) {
        }
    }

    @FXML
    public void setwp2() throws Exception {
        try {
            set_wp_enable(w2, wp2);
        } catch (Exception e) {
        }
    }

    @FXML
    public void setwp3() throws Exception {
        try {
            set_wp_enable(w3, wp3);
        } catch (Exception e) {
        }
    }

    public void setenable_c(int i) {
        ComboBox[] c = {s1, s2, s3, s4, s5, s6, s7, s8};
        if (!c[i].getSelectionModel().isEmpty()) {
            c[i + 1].setDisable(false);
        }
    }

    @FXML
    public void sets2() {
        setenable_c(0);
    }

    @FXML
    public void sets3() {
        setenable_c(1);
    }

    @FXML
    public void sets4() {
        setenable_c(2);
    }

    @FXML
    public void sets5() {
        setenable_c(3);
    }

    @FXML
    public void sets6() {
        setenable_c(4);
    }

    @FXML
    public void sets7() {
        setenable_c(5);
    }

    @FXML
    public void sets8() {
        setenable_c(6);
    }

    @FXML
    public void setw2() {
        if (!wp1.getSelectionModel().isEmpty()) {
            w2.setDisable(false);
            TextField[] t = {sy2, sm2, ey2, em2};
            for (TextField items : t) {
                items.setDisable(false);
            }
        }
    }

    @FXML
    public void setw3() {
        if (!wp2.getSelectionModel().isEmpty()) {
            w3.setDisable(false);
            TextField[] t = {sy3, sm3, ey3, em3};
            for (TextField items : t) {
                items.setDisable(false);
            }
        }
    }

    @FXML
    public void btn_check(MouseEvent event) {
        ComboBox[] s = {s1, s2, s3, s4, s5, s6, s7, s8};
        ComboBox[] w = {w1, w2, w3};
        for (int i = 0; i < s.length; i++) {
            if (s[i].getSelectionModel().isEmpty()) {
                snum = i;
                break;
            }
        }
        for (int i = 0; i < w.length; i++) {
            if (!w[i].getSelectionModel().isEmpty() || w[i].getSelectionModel().getSelectedItem() != null) {
                wnum = i + 1;
            } else {
                break;
            }
        }
        if (snum == 0 && wnum == 0) {
        } else {
            submit.setDisable(false);
        }
    }

    @FXML
    public void btn_submit() throws Exception {
        Data_Save();
        StageControll.close(PAP);
        StageControll.open(MPsecPwkexp2Controller.class, "/View/MPsecPwkexp2.fxml");
    }

    public void Data_Save() throws Exception {
        TextField[] date = {sy1, sy2, sy3, sm1, sm2, sm3, ey1, ey2, ey3, em1, em2, em3};
        ComboBox[] s = {s1, s2, s3, s4, s5, s6, s7, s8};
        ComboBox[] w = {w1, w2, w3};
        ComboBox[] wp = {wp1, wp2, wp3};
        if (snum > 0) {
            for (int i = 1; i < (snum + 1); i++) {
                String ask = "use MileStoneHRMS select s.Sp_ID from Speciality as s where s.Sp_Name = ";
                String sqlspec;
                if (os[i - 1] == null) {
                    sqlspec = " use MileStoneHRMS insert into PSpec(P_ID, Sp_ID) "
                            + " values(" + "'" + id + "'" + " ,?)";
                } else {
                    sqlspec = "use MileStoneHRMS update PSpec set Sp_ID = ? "
                            + " where P_ID  = " + "'" + id + "'"
                            + " and Sp_ID = " + "'" + SQLTools.ValueGetId(ask, os[i - 1]) + "'";
                }

                try (Connection conn = SQLTools.MSSQL()) {
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlspec)) {
                        pstmt.setString(1, SQLTools.ValueGetId(ask, s[i - 1]));
                        pstmt.execute();
                    }
                }
            }
            AuditLog.Audit("主管/HR-修正員工專長專業", name);
        }

        if (wnum > 0) {
            for (int i = 1; i < (wnum + 1); i++) {
                String exppostname;
                if (!wp[i - 1].getSelectionModel().isEmpty()) {
                    exppostname = " and e.ExpPost_Name = " + "'"
                            + wp[i - 1].getSelectionModel().getSelectedItem().toString() + "'";
                } else {
                    exppostname = " and e.ExpPost_Name is null ";
                }
                String ask2 = "use MileStoneHRMS select w.Exp_ID from WkExperience as w "
                        + " left outer join ExpPost as e on e.ExpPost_ID = w.ExpPost_ID "
                        + " where w.Exp_Name = " + "'" + w[i - 1].getSelectionModel().getSelectedItem() + "'"
                        + exppostname;
                
                String out = null;
                try (Connection conn = SQLTools.MSSQL()) {
                    try (ResultSet rs = conn.createStatement().executeQuery(ask2)) {
                        while (rs.next()) {
                            out = rs.getString(1);
                        }
                    }
                }
                
                String sqlw;
                if (ow[i - 1] == null) {
                    sqlw = "use MileStoneHRMS insert into PWkExp(P_ID, Exp_ID, Exp_Num, StartD, EndD) "
                            + " values(" + "'" + id + "'" + ",?,?,?,?) ";
                } else {
                    String exppost;
                    if (owp[i - 1] == null) {
                        exppost = " and e.ExpPost_Name is null ";
                    } else {
                        exppost = " and e.ExpPost_Name = " + "'" + owp[i - 1] + "'";
                    }

                    sqlw = "use MileStoneHRMS update p set Exp_ID = ?, Exp_Num = ?, StartD = ?, EndD = ? from PWkExp as p "
                            + " left outer join WkExperience as w on w.Exp_ID = p.Exp_ID "
                            + " left outer join ExpPost as e on e.ExpPost_ID = w.ExpPost_ID "
                            + " where p.P_ID = " + "'" + id + "'"
                            + " and p.Exp_Num = " + "'" + penum[i - 1] + "'"
                            + " and w.Exp_Name = " + "'" + ow[i - 1] + "'"
                            + exppost;
                }

                try (Connection conn = SQLTools.MSSQL()) {
                    try (PreparedStatement pstmt = conn.prepareCall(sqlw)) {
                        pstmt.setString(1, out);
                        pstmt.setInt(2, i);
                        if (date[i - 1].getText().isEmpty() || date[i + 2].getText().isEmpty()) {
                            pstmt.setNull(3, java.sql.Types.DATE);
                        } else {
                            pstmt.setString(3, StringVariation.datecom(date[i - 1].getText(), date[i + 2].getText(), "01"));
                        }
                        if (date[i + 6].getText().isEmpty() || date[i + 9].getText().isEmpty()) {
                            pstmt.setNull(4, java.sql.Types.DATE);
                        } else {
                            pstmt.setString(4, StringVariation.datecom(date[i + 5].getText(), date[i + 8].getText(), "01"));
                        }
                        pstmt.execute();
                    }
                }
            }
            AuditLog.Audit("主管/HR-修正員工工作履歷", name);
        }
    }

    @FXML
    public void btn_back(MouseEvent event) throws Exception {
        StageControll.open(MPsecPwkexpController.class, "/View/MPsecPwkexp.fxml");
        StageControll.close(PAP);
    }
}
