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
import java.util.Calendar;
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
public class MPersonelController implements Initializable {
    
    @FXML public AnchorPane PersonAP;
    @FXML public TextField name, pid, namech, nameen, nid, by, bm, bd;
    @FXML public ComboBox blood, status;
    @FXML public TextField cell, tel, address, school, dep, ername, ercell
            , ertel, iy, im, id, sy, sm, sd, ey, em, ed;
    @FXML public Button back, search, modify, submit;
    public String oripid, ostatus, osy, osm, osd, oey, oem, oed;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TextField [] tlist = {pid, namech, nameen, nid, by, bm, bd, cell, tel
                , address, school, dep, ername, ercell, ertel, iy, im, id, sy, sm, sd
                , ey, em, ed};
        for(TextField items : tlist){
            items.setDisable(true);
            StageControll.TextFieldhandle(items);
        }
        StageControll.ButtonCtrl(back, name);
        StageControll.ButtonCtrl(search, modify);
        StageControll.ButtonCtrl(modify, namech);
        StageControll.ButtonCtrl(submit, back);
        ComboBox [] clist = {blood, status};
        for(ComboBox items : clist){
            AutoCompleteComboBox autoCompleteComboBox = new AutoCompleteComboBox(items);
            items.setEditable(true);
            items.setDisable(true);
        }
        modify.setDisable(true);
        submit.setDisable(true);
        try {
            get_blood();
            get_activity();
        }
        catch (Exception e) {
        }
    }
    
    @FXML
    public void btn_search(MouseEvent event) throws Exception{
        if(!name.getText().isEmpty()){
            search_and_setdata(" p.P_ID ", pid);
            search_and_setdata(" p.Name_CH ", namech);
            search_and_setdata(" p.Name_EN ", nameen);
            search_and_setdata(" p.NID ", nid);
            search_and_setdata(" year(cast(p.Birth as date)) ", by);
            search_and_setdata(" month(cast(p.Birth as date)) ", bm);
            search_and_setdata(" day(cast(p.Birth as date)) ", bd);
            search_and_setdata(" b.Type_Name ", blood);
            search_and_setdata(" p.Cell ", cell);
            search_and_setdata(" p.Tel ", tel);
            search_and_setdata(" p.Address ", address);
            search_and_setdata(" gs.School_Name ", school);
            search_and_setdata(" gd.Dep_Name ", dep);
            search_and_setdata(" year(cast(p.Inaugu_Day as date)) ", iy);
            search_and_setdata(" month(cast(p.Inaugu_Day as date)) ", im);
            search_and_setdata(" day(cast(p.Inaugu_Day as date)) ", id);
            search_and_setdata(" p.ERCon_Name ", ername);
            search_and_setdata(" p.ERCon_Cell ", ercell);
            search_and_setdata(" p.ERCon_Tel ", ertel);
            if(!pid.getText().isEmpty()){
                oripid = pid.getText();
                search_and_setdata2(" a.Act_Name ", status);
                search_and_setdata2(" year(cast(s.StartD as date)) ", sy);
                search_and_setdata2(" month(cast(s.StartD as date)) ", sm);
                search_and_setdata2(" day(cast(s.StartD as date)) ", sd);
                search_and_setdata2(" year(cast(s.EndD as date)) ", ey);
                search_and_setdata2(" month(cast(s.EndD as date)) ", em);
                search_and_setdata2(" day(cast(s.EndD as date)) ", ed);
                if(!status.getSelectionModel().isEmpty()){
                    ostatus = status.getSelectionModel().getSelectedItem().toString();
                    osy = sy.getText();
                    osm = sm.getText();
                    osd = sd.getText();
                    oey = ey.getText();
                    oem = em.getText();
                    oed = ed.getText();
                }
            }
            if(!pid.getText().isEmpty()){
                modify.setDisable(false);
            }
        }
    }
    
    public void search_and_setdata(String column, TextField target) throws Exception{
        String sql = "use MileStoneHRMS select " + column
                + " from Personel as p left outer join BloodType as b on b.Type_ID = p.Type_ID"
                + " left outer join GSch as gs on gs.School_ID = p.School_ID"
                + " left outer join GDep as gd on gd.Dep_ID = p.Dep_ID "
                + " where p.Name_CH = " + "'" + name.getText() + "'";

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
                + " from Personel as p left outer join BloodType as b on b.Type_ID = p.Type_ID"
                + " left outer join GSch as gs on gs.School_ID = p.School_ID"
                + " left outer join GDep as gd on gd.Dep_ID = p.Dep_ID "
                + " where p.Name_CH = " + "'" + name.getText() + "'";

        try (Connection conn = SQLTools.MSSQL()) {
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                String out = null;
                while (rs.next()) {
                    out = rs.getString(1);
                }
                if (out != null) {
                    target.getSelectionModel().select(out);
                } else {
                }
            }
        }
    }
    
    public void search_and_setdata2(String column, TextField target) throws Exception {
        String sql = "use MileStoneHRMS select top 1 " + column
                + " from PStatus as s left outer join Activity as a on a.Act_ID = s.Act_ID "
                + " where s.P_ID = " + "'" + pid.getText() + "'"
                + " order by cast(s.StartD as date) desc ";

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
    
    public void search_and_setdata2(String column, ComboBox target) throws Exception{
        String sql = "use MileStoneHRMS select top 1 " + column
                + " from PStatus as s left outer join Activity as a on a.Act_ID = s.Act_ID "
                + " where s.P_ID = " + "'" + pid.getText() + "'"
                + " order by cast(s.StartD as date) desc ";
        
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
    public void btn_modify() throws Exception{
        if(!pid.getText().isEmpty()){
            TextField [] tlist = {pid, namech, nameen, nid, by, bm, bd, cell, tel
                    , address, school, dep, ername, ercell, ertel, iy, im, id, sy, sm, sd
                    , ey, em, ed};
            for(TextField items : tlist){
                items.setDisable(false);
            }
            blood.setDisable(false);
            status.setDisable(false);
            submit.setDisable(false);
        }
    }
    
    @FXML
    public void btn_submit(MouseEvent event) throws Exception{
        if(pid.getText().isEmpty() || namech.getText().isEmpty()){
            NoticeController.noticecontent = "請至少輸入員工編號及中文姓名！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");
        }
        else{
            Data_Save();
            StageControll.close(PersonAP);
            StageControll.open(MPersonelController.class, "/View/MPersonel.fxml");
        }
    }
    
    public void Data_Save() throws Exception{
        //PartA : Status
        if(!status.getSelectionModel().isEmpty()
                && !status.getSelectionModel().getSelectedItem().toString().equals(ostatus)
                || !StringVariation.datecom(osy, osm, osd).equals(StringVariation.datecom(sy, sm, sd))
                || !StringVariation.datecom(oey, oem, oed).equals(StringVariation.datecom(ey, em, ed))){
            String act = "use MileStoneHRMS select a.Act_ID from Activity as a where a.Act_Name = ";
            if (ostatus == null
                    || !ostatus.equals(status.getSelectionModel().getSelectedItem().toString())) {

                try (Connection conn = SQLTools.MSSQL()) {
                    try (PreparedStatement pstmt = conn.prepareStatement("use MileStoneHRMS insert "
                            + "PStatus(P_ID, Act_ID, StartD, EndD) values(?,?,?,?) ")) {
                        pstmt.setString(1, pid.getText());
                        pstmt.setString(2, SQLTools.ValueGetId(act, status));
                        pstmt.setString(3, StringVariation.datecom(sy, sm, sd));
                        SQLTools.emptyslotsetnull(StringVariation.datecom(ey, em, ed), pstmt, 4);
                        pstmt.execute();
                        AuditLog.Audit("主管/HR-修改員工服務狀態", name, status, StringVariation.datecom(sy, sm, sd));
                    }
                }
            }
            else{
                String indate = " = '" + StringVariation.datecom(osy, osm, osd) + "'";
                if(StringVariation.datecom(osy, osm, osd)==null){
                    indate = " is null ";
                }
                String actname = " = '" + SQLTools.ValueGetId(act, ostatus) + "'";
                String sql = "use MileStoneHRMS update PStatus "
                        + " set P_ID = ?, Act_ID = ?, StartD = ?, EndD = ? "
                        + " where P_ID = " + "'" + oripid + "'"
                        + " and Act_ID " + actname
                        + " and StartD " + indate ;
                
                try (Connection conn = SQLTools.MSSQL()) {
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, pid.getText());
                        pstmt.setString(2, SQLTools.ValueGetId(act, status.getSelectionModel().getSelectedItem().toString()));
                        pstmt.setString(3, StringVariation.datecom(sy, sm, sd));
                        SQLTools.emptyslotsetnull(StringVariation.datecom(ey, em, ed), pstmt, 4);
                        pstmt.execute();
                        AuditLog.Audit("主管/HR-修改員工服務狀態", name, status, StringVariation.datecom(sy, sm, sd));
                    }
                }
            }
        }
        //PartB : Basic Data
        String getbldsql = "use MileStoneHRMS select b.Type_ID from BloodType as b "
                + "where b.Type_Name = ";
        String getschsql = "use MileStoneHRMS select s.School_ID from GSch as s "
                + "where s.School_Name = ";
        String getdepsql = "use MileStoneHRMS select d.Dep_ID from GDep as d "
                + "where d.Dep_Name = ";
        String sql2 = "use MileStoneHRMS update Personel"
                + " set P_ID = ?, Name_CH = ?, Name_EN = ?, NID = ?, Birth = ? "
                + " , Type_ID = ?, Cell = ?, Tel = ?, Address = ?, School_ID = ? "
                + " , Dep_ID = ?, Inaugu_Day = ?, ERCon_Name = ?, ERCon_Cell = ?, ERCon_Tel = ? "
                + " where P_ID  = " + "'" + oripid + "'";
        
        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql2)) {
                pstmt.setString(1, pid.getText());
                SQLTools.emptyslotsetnull(namech.getText(), pstmt, 2);
                SQLTools.emptyslotsetnull(nameen.getText(), pstmt, 3);
                SQLTools.emptyslotsetnull(nid.getText(), pstmt, 4);
                SQLTools.emptyslotsetnull(StringVariation.datecom(by, bm, bd), pstmt, 5);
                SQLTools.emptyslotsetnull(SQLTools.ValueGetId(getbldsql, blood), pstmt, 6);
                SQLTools.emptyslotsetnull(cell.getText(), pstmt, 7);
                SQLTools.emptyslotsetnull(tel.getText(), pstmt, 8);
                SQLTools.emptyslotsetnull(address.getText(), pstmt, 9);
                SQLTools.emptyslotsetnull(SQLTools.ValueGetId(getschsql, school), pstmt, 10);
                SQLTools.emptyslotsetnull(SQLTools.ValueGetId(getdepsql, dep), pstmt, 11);
                SQLTools.emptyslotsetnull(StringVariation.datecom(iy, im, id), pstmt, 12);
                SQLTools.emptyslotsetnull(ername.getText(), pstmt, 13);
                SQLTools.emptyslotsetnull(ercell.getText(), pstmt, 14);
                SQLTools.emptyslotsetnull(ertel.getText(), pstmt, 15);
                pstmt.execute();
                AuditLog.Audit("主管/HR-修改員工基本資料", pid);
            }
        }
    }
    
    public void get_blood() throws Exception{
        String sql = "use MileStoneHRMS select b.Type_Name from BloodType as b";
        SQLTools.SqlGetItem(sql,blood);
    }
    
    public void get_activity() throws Exception{
        String sql = "use MileStoneHRMS select a.Act_Name from Activity as a";
        SQLTools.SqlGetItem(sql, status);
    }
    
    public String set_pid() throws Exception{
        Calendar date = Calendar.getInstance();
        int year  = date.get(Calendar.YEAR);
        String y = StringVariation.right(Integer.toString(year), 2);
        String sql = "select cast(max(substring(p.P_ID, 6 ,8)) as int) from Personel as p";
        int count = SQLTools.id_incre(SQLTools.Sql_Get_ID(sql));
        String r = Integer.toString(count);
        String newid = "MS" + y + "-" + StringVariation.right(("00"+r),3);
        return newid;
    }
    
    @FXML
    public void btn_back() throws Exception{
        StageControll.open(MenuS2Controller.class, "/View/MenuS2.fxml");
        StageControll.close(PersonAP);
    }
}
