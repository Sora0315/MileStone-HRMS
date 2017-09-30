package Controller;

import SelfTools.AuditLog;
import SelfTools.AutoCompleteComboBox;
import SelfTools.SQLTools;
import SelfTools.StageControll;
import SelfTools.StringVariation;
import SelfTools.TableTools;
import SelfTools.UserInfo;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 * @author Sora
 */
public class EOvertimeController implements Initializable {

    @FXML public AnchorPane EAP;
    @FXML public TableView lo;
    @FXML public TextField cause, sy, sm, sd, sh, smin, ey, em, ed, eh, emin, hrs;
    @FXML public ComboBox choose;
    @FXML public Button back, cal, submit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TextField[] list = {sy, sm, sd, sh, smin, ey, em, ed, eh, emin, hrs};
        for (TextField items : list) {
            items.setDisable(true);
        }
        submit.setDisable(true);
        try {
            set_table();
            lo_item_set();
        } catch (Exception e) {
            e.printStackTrace();
        }
        StageControll.ComboBoxCtrl(choose);
        choose.setEditable(true);
        AutoCompleteComboBox autoCompleteComboBox = new AutoCompleteComboBox(choose);
        StageControll.Keydirect(back, choose);
        StageControll.TableViewhandle(lo);
        StageControll.TextFieldhandle(cause);
        StageControll.TextFieldhandle(sy);
        StageControll.TextFieldhandle(sm);
        StageControll.TextFieldhandle(sd);
        StageControll.TextFieldhandle(sh);
        StageControll.TextFieldhandle(smin);
        StageControll.TextFieldhandle(ey);
        StageControll.TextFieldhandle(em);
        StageControll.TextFieldhandle(ed);
        StageControll.TextFieldhandle(eh);
        StageControll.TextFieldhandle(emin);
        StageControll.TextFieldhandle(hrs);
        StageControll.Buttonhandle(back);
        StageControll.Buttonhandle(cal);
        StageControll.Buttonhandle(submit);
    }

    public void set_table() throws Exception {
        String[] col_name = {"起始時日", "終止時日", "種類", "時數"};
        String sql = "use MileStoneHRMS select cast(a.StartD as smalldatetime), cast(a.EndD as smalldatetime)"
                + ", l.LO_Name, (cast(a.Interval/24 as varchar) + ' 日 ' + cast(a.Interval%24 as varchar) + ' 時')"
                + " from AbWorktimeR as a\n"
                + " left outer join LO as l on l.LO_ID = a.LO_ID\n"
                + " where a.P_ID = " + "'" + UserInfo.emp_pid + "'"
                + " and year(a.StartD) = year(getdate())"
                + " and l.LO_Name not like '%假%' or l.LO_Name like '%停止%' "
                + " order by a.StartD Asc ";
        TableTools.DataSet(lo, 4, 268, col_name, sql);
    }

    public void lo_item_set() throws Exception {
        String sql = "use MileStoneHRMS select l.LO_Name from LO as l "
                + " where l.LO_Name not like '%假%'  or l.LO_Name like '%停止%' ";
        SQLTools.SqlGetItem(sql, choose);
    }

    @FXML
    public void set_field_enable() {
        if (!choose.getSelectionModel().isEmpty()) {
            TextField[] li = {sy, sm, sd, sh, smin, ey, em, ed, eh, emin};
            for (TextField items : li) {
                items.setDisable(false);
            }
        }
    }

    @FXML
    public void set_hrs() throws Exception {
        if(!choose.getSelectionModel().isEmpty() && !smin.getText().isEmpty()){
            submit.setDisable(false); 
        }
        else{
            NoticeController.noticecontent = "加班有很多種，請選擇！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml"); 
        }
        if (!smin.getText().isEmpty() && !emin.getText().isEmpty()) {
            String sql = " select datediff( minute, " + "'" + StringVariation.datetimecom(sy, sm, sd, sh, smin) + "', '"
                    + StringVariation.datetimecom(ey, em, ed, eh, emin) + "' ) ";
             int temp = 0;
            try (Connection conn = SQLTools.MSSQL()) {
                try (ResultSet rs = conn.createStatement().executeQuery(sql);) {
                    while (rs.next()) {
                        temp = rs.getInt(1);
                    }
                    double out = temp / 60.0;
                    DecimalFormat df = new DecimalFormat("##.#");
                    hrs.setText(df.format(out));
                }
            }  
        }
        else{
            NoticeController.noticecontent = "請填寫完整的起迄時間！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml"); 
        }
        submit.setDisable(false);
    }

    @FXML
    public void btn_submit(MouseEvent event) throws Exception {
        Data_Save();
        StageControll.open(MenuEController.class, "/View/MenuE.fxml");
        StageControll.close(EAP);
        NoticeController.noticecontent = "加班資料已送出。";
        StageControll.open(NoticeController.class, "/View/Notice.fxml", true);
    }

    public void Data_Save() throws Exception {
        String ask = " use MileStoneHRMS select l.LO_ID from LO as l where l.LO_Name = ";
        String sql = "use MileStoneHRMS insert into AbWorktimeR(P_ID, LO_ID, StartD, EndD, Interval, Cause, ApprP_ID)"
                + " values(?,?,?,?,?,?,?)";
        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, UserInfo.emp_pid);
                pstmt.setString(2, SQLTools.ValueGetId(ask, choose));
                pstmt.setString(3, StringVariation.datetimecom(sy, sm, sd, sh, smin));
                SQLTools.emptyslotsetnull(StringVariation.datetimecom(ey, em, ed, eh, emin), pstmt, 4);
                if (hrs.getText().isEmpty()) {
                    pstmt.setNull(5, java.sql.Types.VARCHAR);
                } else {
                    pstmt.setString(5, hrs.getText());
                }
                SQLTools.emptyslotsetnull(cause, pstmt, 6);
                pstmt.setString(7, EPreOvertimeController.ApprP_ID);
                pstmt.execute();
                AuditLog.Audit("員工申請-加班", UserInfo.emp_pid, choose, StringVariation.datetimecom(sy, sm, sd, sh, smin), EPreOvertimeController.ApprP_ID);
            }
        }
    }

    @FXML
    public void btn_back(MouseEvent event) throws Exception {
        StageControll.open(MenuEController.class, "/View/MenuE.fxml");
        StageControll.close(EAP);
    }
}
