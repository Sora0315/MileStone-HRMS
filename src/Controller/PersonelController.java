package Controller;

import SelfTools.AuditLog;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import SelfTools.AutoCompleteComboBox;
import SelfTools.SQLTools;
import SelfTools.StageControll;
import SelfTools.StringVariation;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 * @author Sora
 */
public class PersonelController implements Initializable {
    
    @FXML public AnchorPane PersonAP;  
    @FXML public TextField pid, namech, nameen, nid, by, bm, bd;
    @FXML public ComboBox blood, status;
    @FXML public TextField cell, tel, address, school, dep, ername, ercell
            , ertel, iy, im, id, sy, sm, sd, ey, em, ed;
    @FXML public Button back, submit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            get_blood();
            get_activity();
            pid.setText(set_pid());
        }
        catch (Exception e) {
        }
        blood.setEditable(true);
        status.setEditable(true);
        AutoCompleteComboBox autoCompleteComboBox = new AutoCompleteComboBox(blood);
        AutoCompleteComboBox autoCompleteComboBox1 = new AutoCompleteComboBox(status);
        StageControll.ComboBoxCtrl(blood);
        StageControll.ComboBoxCtrl(status);
        StageControll.Keydirect(nid, blood);
        StageControll.Keydirect(id, status);
        StageControll.ButtonCtrl(submit, back);
        StageControll.ButtonCtrl(back, pid);
        TextField []  tfall = {pid, namech, nameen, nid, by, bm, bd, cell
                , tel, address, school, dep, ername, ercell, ertel, iy, im, id
                , sy, sm, sd, ey, em, ed};
        for(TextField items : tfall){
            StageControll.TextFieldhandle(items);
        }
    }
    
    @FXML
    public void btn_submit() throws Exception{
        if(pid.getText().isEmpty() || namech.getText().isEmpty()){
                NoticeController.noticecontent = "請至少輸入中文姓名！";
                StageControll.open(NoticeController.class, "/View/Notice.fxml");  
        }
        else{
            Data_Save();
            StageControll.close(PersonAP);
            StageControll.open(PersonelController.class, "/View/Personel.fxml");
        }
    }
        
    public void Data_Save() throws Exception{
        String getbldsql = "use MileStoneHRMS select b.Type_ID from BloodType as b "
                + "where b.Type_Name = ";
        String getschsql = "use MileStoneHRMS select s.School_ID from GSch as s "
                + "where s.School_Name = ";
        String getdepsql = "use MileStoneHRMS select d.Dep_ID from GDep as d "
                + "where d.Dep_Name = ";
        String sql = "use MileStoneHRMS insert into Personel(P_ID, Name_CH, Name_EN, NID,"
                + " Birth, Type_ID, Cell, Tel, Address, School_ID, Dep_ID, Inaugu_Day,"
                + " ERCon_Name, ERCon_Cell, ERCon_Tel)\n"
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        System.out.println(SQLTools.ValueGetId(getbldsql, blood));

        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, pid.getText());
                SQLTools.emptyUnitSetNull(namech.getText(), pstmt, 2);
                SQLTools.emptyUnitSetNull(nameen.getText(), pstmt, 3);
                SQLTools.emptyUnitSetNull(nid.getText(), pstmt, 4);
                SQLTools.emptyUnitSetNull(StringVariation.datecom(by, bm, bd), pstmt, 5);
                SQLTools.emptyUnitSetNull(SQLTools.ValueGetId(getbldsql, blood), pstmt, 6);
                SQLTools.emptyUnitSetNull(cell.getText(), pstmt, 7);
                SQLTools.emptyUnitSetNull(tel.getText(), pstmt, 8);
                SQLTools.emptyUnitSetNull(address.getText(), pstmt, 9);
                SQLTools.emptyUnitSetNull(SQLTools.ValueGetId(getschsql, school), pstmt, 10);
                SQLTools.emptyUnitSetNull(SQLTools.ValueGetId(getdepsql, dep), pstmt, 11);
                SQLTools.emptyUnitSetNull(StringVariation.datecom(iy, im, id), pstmt, 12);
                SQLTools.emptyUnitSetNull(ername.getText(), pstmt, 13);
                SQLTools.emptyUnitSetNull(ercell.getText(), pstmt, 14);
                SQLTools.emptyUnitSetNull(ertel.getText(), pstmt, 15);
                pstmt.execute();
                AuditLog.Audit("主管/HR-員工基本資料登錄", namech, pid);
            }

            if (!status.getSelectionModel().isEmpty()
                    || !sy.getText().isEmpty() || !sm.getText().isEmpty() || !sd.getText().isEmpty()) {
                String act = "use MileStoneHRMS select a.Act_ID from Activity as a where a.Act_Name = ";
                String sql2 = "use MileStoneHRMS insert into PStatus(P_ID, Act_ID, StartD, EndD)"
                        + " values(?,?,?,?)";

                try (PreparedStatement pstmt = conn.prepareStatement(sql2)) {
                    pstmt.setString(1, pid.getText());
                    pstmt.setString(2, SQLTools.ValueGetId(act, status));
                    pstmt.setString(3, StringVariation.datecom(sy, sm, sd));
                    SQLTools.emptyUnitSetNull(StringVariation.datecom(ey, em, ed), pstmt, 4);
                    pstmt.execute();
                    AuditLog.Audit("主管/HR-員工服務狀態登錄", pid, status, StringVariation.datecom(sy, sm, sd));
                }
            }
        }
    }
    
    public void get_blood() throws Exception{
        String sql = "use MileStoneHRMS select b.Type_Name from BloodType as b";
        SQLTools.comboboxSetItem(sql,blood);
    }
    
    public void get_activity() throws Exception{
        String sql = "use MileStoneHRMS select a.Act_Name from Activity as a";
        SQLTools.comboboxSetItem(sql, status);
    }
    
    public String set_pid() throws Exception{
        Calendar date = Calendar.getInstance();
        int year  = date.get(Calendar.YEAR);
        String y = StringVariation.right(Integer.toString(year), 2);
        String sql = "select cast(max(substring(p.P_ID, 6 ,8)) as int) from Personel as p"
                + " where substring(p.P_ID, 3, 2) = substring(cast(year(getdate()) as varchar), 3,2)";
        int count = SQLTools.idAutoIncrease(SQLTools.sqlQuerySetId(sql));
        String r = Integer.toString(count);
        String newid = "MS" + y + "-" + StringVariation.right(("00"+r),3);
        return newid;
    }
    
    @FXML
    public void btn_back() throws Exception{
        StageControll.open(MenuSController.class, "/View/MenuS.fxml");
        StageControll.close(PersonAP);        
    }
}
