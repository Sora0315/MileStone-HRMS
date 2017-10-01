package Controller;

import SelfTools.AuditLog;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import SelfTools.SQLTools;
import SelfTools.StageControll;
import SelfTools.StringVariation;

/**
 * FXML Controller class
 *
 * @author Sora
 */
public class InvPropertyController implements Initializable {

    @FXML
    public AnchorPane IAP;
    @FXML
    public TextField id;
    @FXML
    public TextField name;
    @FXML
    public ComboBox p1, p2, p3, p4, p5, p6, p7, p8, p9, p10;
    @FXML
    public TextField a1, a2, a3, a4, a5, a6, a7, a8, a9, a10;
    @FXML
    public TextField sy1, sy2, sy3, sy4, sy5, sy6, sy7, sy8, sy9, sy10;
    @FXML
    public TextField sm1, sm2, sm3, sm4, sm5, sm6, sm7, sm8, sm9, sm10;
    @FXML
    public TextField sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10;
    @FXML
    public TextField ey1, ey2, ey3, ey4, ey5, ey6, ey7, ey8, ey9, ey10;
    @FXML
    public TextField em1, em2, em3, em4, em5, em6, em7, em8, em9, em10;
    @FXML
    public TextField ed1, ed2, ed3, ed4, ed5, ed6, ed7, ed8, ed9, ed10;
    @FXML
    public Button back, check, submit;
    public int loop;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            setid();
        } catch (Exception e) {
        }
        id.setDisable(true);
        ComboBox[] p = {p1, p2, p3, p4, p5, p6, p7, p8, p9, p10};
        TextField[] a = {a2, a3, a4, a5, a6, a7, a8, a9, a10};
        TextField[] syr = {sy2, sy3, sy4, sy5, sy6, sy7, sy8, sy9, sy10};
        TextField[] smh = {sm2, sm3, sm4, sm5, sm6, sm7, sm8, sm9, sm10};
        TextField[] sdy = {sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10};
        TextField[] eyr = {ey2, ey3, ey4, ey5, ey6, ey7, ey8, ey9, ey10};
        TextField[] emh = {em2, em3, em4, em5, em6, em7, em8, em9, em10};
        TextField[] edy = {ed2, ed3, ed4, ed5, ed6, ed7, ed8, ed9, ed10};
        TextField[][] explist = {a, syr, smh, sdy, eyr, emh, edy};
        TextField[] tfall = {name, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, sy1, sy2, sy3, sy4, sy5, sy6, sy7, sy8, sy9, sy10, sm1, sm2, sm3, sm4, sm5, sm6, sm7, sm8, sm9, sm10, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, ey1, ey2, ey3, ey4, ey5, ey6, ey7, ey8, ey9, ey10, em1, em2, em3, em4, em5, em6, em7, em8, em9, em10, ed1, ed2, ed3, ed4, ed5, ed6, ed7, ed8, ed9, ed10};

        String sql = "use MileStoneHRMS select p.Pr_Name from Property as p";
        for (int i = 1; i < p.length; i++) {
            p[i].setDisable(true);
        }
        for (TextField[] item1 : explist) {
            for (TextField item2 : item1) {
                item2.setDisable(true);
            }
        }
        for (ComboBox box : p) {
            try {
                SQLTools.comboboxSetItem(sql, box);
                box.setEditable(true);
                StageControll.ComboBoxCtrl(box);
            } catch (Exception e) {
            }
        }
        for (TextField items : tfall) {
            StageControll.TextFieldhandle(items);
        }
//        Keydirect(name, p1);
//        Keydirect(ed1, p2);
//        Keydirect(ed2, p3);
//        Keydirect(ed3, p4);
//        Keydirect(ed4, p5);
//        Keydirect(ed5, p6);
//        Keydirect(ed6, p7);
//        Keydirect(ed7, p8);
//        Keydirect(ed8, p9);
//        Keydirect(ed9, p10);
//        Buttonhandle(back);
//        Buttonhandle(check);
//        Buttonhandle(submit);
        submit.setDisable(true);
    }

    public void setenable(int i) {
        ComboBox[] product = {p1, p2, p3, p4, p5, p6, p7, p8, p9, p10};
        TextField[] amount = {a2, a3, a4, a5, a6, a7, a8, a9, a10};
        TextField[] syear = {sy2, sy3, sy4, sy5, sy6, sy7, sy8, sy9, sy10};
        TextField[] smonth = {sm2, sm3, sm4, sm5, sm6, sm7, sm8, sm9, sm10};
        TextField[] sday = {sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10};
        TextField[] eyear = {ey2, ey3, ey4, ey5, ey6, ey7, ey8, ey9, ey10};
        TextField[] emonth = {em2, em3, em4, em5, em6, em7, em8, em9, em10};
        TextField[] eday = {ed2, ed3, ed4, ed5, ed6, ed7, ed8, ed9, ed10};
        TextField[] set = {amount[i], syear[i], smonth[i], sday[i], eyear[i], emonth[i], eday[i]};
        product[i + 1].setDisable(false);
        for (TextField item : set) {
            item.setDisable(false);
        }
    }

    @FXML
    public void set2() {
        setenable(0);
    }

    @FXML
    public void set3() {
        setenable(1);
    }

    @FXML
    public void set4() {
        setenable(2);
    }

    @FXML
    public void set5() {
        setenable(3);
    }

    @FXML
    public void set6() {
        setenable(4);
    }

    @FXML
    public void set7() {
        setenable(5);
    }

    @FXML
    public void set8() {
        setenable(6);
    }

    @FXML
    public void set9() {
        setenable(7);
    }

    @FXML
    public void set10() {
        setenable(8);
    }

    @FXML
    public void btn_checkdata(MouseEvent event) throws Exception {
        if (!name.getText().isEmpty() && !p1.getSelectionModel().isEmpty()) {
            submit.setDisable(false);
            loop = loopnum();
        } else {
            NoticeController.noticecontent = "請輸入員工姓名與至少一組財產名稱！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");
        }
    }

    public int loopnum() {
        int j = 0;
        ComboBox[] p = {p1, p2, p3, p4, p5, p6, p7, p8, p9, p10};
        for (int k = 0; k < p.length; k++) {
            if (p[k].getSelectionModel().isEmpty()) {
                j = k;
                break;
            }
        }
        return j;
    }

    @FXML
    public void btn_back(MouseEvent event) throws Exception {
        StageControll.close(IAP);
        StageControll.open(MenuSController.class, "/View/MenuS.fxml");
    }

    @FXML
    public void submit(MouseEvent event) throws Exception {
        ComboBox[] p = {p1, p2, p3, p4, p5, p6, p7, p8, p9, p10};
        TextField[] a = {a1, a2, a3, a4, a5, a6, a7, a8, a9, a10};
        TextField[] sy = {sy1, sy2, sy3, sy4, sy5, sy6, sy7, sy8, sy9, sy10};
        TextField[] sm = {sm1, sm2, sm3, sm4, sm5, sm6, sm7, sm8, sm9, sm10};
        TextField[] sd = {sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10};
        TextField[] ey = {ey1, ey2, ey3, ey4, ey5, ey6, ey7, ey8, ey9, ey10};
        TextField[] em = {em1, em2, em3, em4, em5, em6, em7, em8, em9, em10};
        TextField[] ed = {ed1, ed2, ed3, ed4, ed5, ed6, ed7, ed8, ed9, ed10};
        for (int i = 0; i < loop; i++) {
            Data_Save(p[i], a[i], sy[i], sm[i], sd[i], ey[i], em[i], ed[i]);
        }
        StageControll.close(IAP);
        StageControll.open(InvPropertyController.class, "/View/InvProperty.fxml");
    }

    public void setid() throws Exception {
        String sql = "use MileStoneHRMS select cast(substring(i.InvP_ID, 2, 6) as int) from InvProperty as i ";
        String num = Integer.toString(SQLTools.idAutoIncrease(((int) SQLTools.sqlQuerySetId(sql))));
        String iid = "I" + StringVariation.right(("00000" + num), 5);
        id.setText(iid);
    }

    public void Data_Save(ComboBox b, TextField amt, TextField sy, TextField sm, TextField sd, TextField ey, TextField em, TextField ed) throws Exception {
        String ask = "use MileStoneHRMS select p.P_ID from Personel as p where p.Name_CH = ";
        String askcombo = "use MileStoneHRMS select p.Pr_ID from Property as p where p.Pr_Name = ";
        String sql = "use MileStoneHRMS insert into InvProperty(InvP_ID, P_ID, Pr_ID, Amount, StartD, EndD)"
                + " values(?,?,?,?,?,?)";

        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id.getText());
                pstmt.setString(2, SQLTools.ValueGetId(ask, name));
                pstmt.setString(3, SQLTools.ValueGetId(askcombo, b));
                pstmt.setString(4, amt.getText());
                if (!sy.getText().isEmpty() || !sm.getText().isEmpty() || !sd.getText().isEmpty()) {
                    pstmt.setString(5, StringVariation.datecom(sy, sm, sd));
                } else {
                    pstmt.setNull(5, java.sql.Types.VARCHAR);
                }
                if (!ey.getText().isEmpty() || !em.getText().isEmpty() || !ed.getText().isEmpty()) {
                    pstmt.setString(6, StringVariation.datecom(ey, em, ed));
                } else {
                    pstmt.setNull(6, java.sql.Types.VARCHAR);
                }
                pstmt.execute();
                AuditLog.Audit("主管/HR", id, name, b);
            }
        }
    }        
}
