package Controller;

import static SelfTools.StringVariation.*;
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
import static SelfTools.AuditLog.Audit;
import SelfTools.AutoCompleteComboBox;
import SelfTools.SQLTools;
import SelfTools.StageControll;

/**
 * FXML Controller class
 * @author Sora
 */
public class PsecPwkexpController implements Initializable {
    
    @FXML public AnchorPane PAP;
    @FXML public ComboBox s1, s2, s3, s4, s5, s6, s7, s8, w1, w2, w3, wp1, wp2, wp3;
    @FXML public TextField name, sy1, sy2, sy3, sm1, sm2, sm3, ey1, ey2, ey3, em1, em2, em3;
    @FXML public Button back, confirm, submit;
    public String id;
    public int snum, wnum;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ComboBox [] c = {s2, s3, s4, s5, s6, s7, s8, w2, w3, wp1, wp2, wp3};
        TextField [] t = {sy2, sy3, sm2, sm3, ey2, ey3, em2, em3};
        for(ComboBox items : c){
            items.setDisable(true);
        }
        for(TextField items : t){
            items.setDisable(true);
        }
        submit.setDisable(true);
        String specsql = "use MileStoneHRMS select s.Sp_Name from Speciality as s ";
        String wsql = "use MileStoneHRMS select distinct w.Exp_Name " +
                " from WkExperience as w left outer join ExpPost as e on e.ExpPost_ID = w.ExpPost_ID ";
        try{
            ComboBox [] s = {s1, s2, s3, s4, s5, s6, s7, s8};
            for(ComboBox items : s){
                SQLTools.SqlGetItem(specsql, items);
            }
            ComboBox [] w = {w1, w2, w3};
            for(ComboBox items : w){
                SQLTools.SqlGetItem(wsql, items);
            }
        }
        catch(Exception e){
        }
        ComboBox []  cball = {s1, s2, s3, s4, s5, s6, s7, s8, w1, w2, w3, wp1, wp2, wp3};
        TextField [] tfall = {name, sy1, sy2, sy3, sm1, sm2, sm3, ey1, ey2, ey3, em1, em2, em3};
        for(ComboBox items : cball){
            StageControll.ComboBoxCtrl(items);
            items.setEditable(true);
            AutoCompleteComboBox autoCompleteComboBox = new AutoCompleteComboBox(items);
        }
        StageControll.Keydirect(name, s1);
        StageControll.Keydirect(s1, s2);
        StageControll.Keydirect(s2, s3);
        StageControll.Keydirect(s3, s4);
        StageControll.Keydirect(s4, s5);
        StageControll.Keydirect(s5, s6);
        StageControll.Keydirect(s6, s7);
        StageControll.Keydirect(s7, s8);
        StageControll.Keydirect(s1, w1);
        StageControll.Keydirect(em1, w2);
        StageControll.Keydirect(em2, w3);
        StageControll.Keydirect(w1, wp1);
        StageControll.Keydirect(w2, wp2);
        StageControll.Keydirect(w3, wp3);
        for(TextField items : tfall){
            StageControll.TextFieldhandle(items); 
        }
        StageControll.ButtonCtrl(back, name);
        StageControll.ButtonCtrl(confirm, submit);
        StageControll.ButtonCtrl(submit, back);
    }
    
    @FXML
    public void set_id(){
        if(!name.getText().isEmpty()){
            try{
                id = SQLTools.ValueGetId("use MileStoneHRMS select p.P_ID from Personel as p "
                        + " where p.Name_CH = ", name);
            }
            catch(Exception e){
            }
        }
    }
    
    public void set_wp_enable(ComboBox w, ComboBox wp) throws Exception{
        if(!w.getSelectionModel().isEmpty()){
            String sql = " use  MileStoneHRMS select distinct e.ExpPost_Name from WkExperience as w "
                    + " left outer join ExpPost as e on e.ExpPost_ID = w.ExpPost_ID "
                    + " where w.Exp_Name = " + "'" + w.getSelectionModel().getSelectedItem().toString() + "'";
            SQLTools.SqlGetItem(sql, wp);
            wp.setDisable(false);
        }
    }
    
    @FXML
    public void setwp1(){
        try {
            set_wp_enable(w1, wp1);
        } catch (Exception e) {
        }
    }
    
    @FXML
    public void setwp2() throws Exception{
        try{
            set_wp_enable(w2, wp2);
        }catch(Exception e){
        }
    }
    
    @FXML
    public void setwp3() throws Exception{
        try{
            set_wp_enable(w3, wp3);
        }catch(Exception e){
        }
    }
    
    public void setenable_c(int i){
        ComboBox [] c = {s1, s2, s3, s4, s5, s6, s7, s8};
        if(!c[i].getSelectionModel().isEmpty()){
            c[i+1].setDisable(false);            
        }
    }
    
    @FXML
    public void sets2(){
        setenable_c(0);
    }
    
    @FXML
    public void sets3(){
        setenable_c(1);
    }
    
    @FXML
    public void sets4(){
        setenable_c(2);
    }
    
    @FXML
    public void sets5(){
        setenable_c(3);
    }
    
    @FXML
    public void sets6(){
        setenable_c(4);
    }
    
    @FXML
    public void sets7(){
        setenable_c(5);
    }
    
    @FXML
    public void sets8(){
        setenable_c(6);
    }
    
    @FXML
    public void setw2(){
        if(!wp1.getSelectionModel().isEmpty()){
            w2.setDisable(false);
            TextField [] t = {sy2, sm2, ey2, em2};
            for(TextField items : t){
                items.setDisable(false);
            }
        } 
    }
    
    @FXML
    public void setw3(){
        if(!wp2.getSelectionModel().isEmpty()){
            w3.setDisable(false);
            TextField [] t = {sy3, sm3, ey3, em3};
            for(TextField items : t){
                items.setDisable(false);
            }
        }
    }
    
    @FXML
    public void btn_check(MouseEvent event){
        ComboBox [] s = {s1, s2, s3, s4, s5, s6, s7, s8};
        ComboBox [] w = {w1, w2, w3};
        for(int i=0;i<s.length;i++){
            if(s[i].getSelectionModel().isEmpty()){
                snum = i;
                break;
            }
        }
        for(int i=0;i<w.length;i++){
            if(w[i].getSelectionModel().isEmpty()){
                wnum = i;
                break;
            }
        }
        if(snum==0 && wnum==0){
        }
        else{
            submit.setDisable(false);
            NoticeController.noticecontent = "請填寫必要資料。";
            try{
                StageControll.open(NoticeController.class, "/View/Notice.fxml");
            }catch(Exception e){
            }
        }
    }
    
    @FXML
    public void btn_submit() throws Exception{
        Data_Save();
        StageControll.close(PAP);
        StageControll.open(PsecPwkexpController.class, "/View/PsecPwkexp.fxml");
    }
    
    public void Data_Save() throws Exception{
        TextField [] date = {sy1, sy2, sy3, sm1, sm2, sm3, ey1, ey2, ey3, em1, em2, em3};
        ComboBox [] s = {s1, s2, s3, s4, s5, s6, s7, s8};
        ComboBox [] w = {w1, w2, w3};
        ComboBox [] wp = {wp1, wp2, wp3};
        if (snum > 0) {
            for (int i = 1; i < (snum + 1); i++) {
                String ask = "use MileStoneHRMS select s.Sp_ID from Speciality as s where s.Sp_Name = ";
                String sqlspec = "use MileStoneHRMS insert into PSpec(P_ID, Sp_ID) "
                        + " values(?,?) ";

                try (Connection conn = SQLTools.MSSQL()) {
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlspec)) {
                        pstmt.setString(1, id);
                        pstmt.setString(2, SQLTools.ValueGetId(ask, s[i - 1]));
                        pstmt.execute();
                    }
                }
            }
            Audit("主管/HR-員工專長專業登錄", name);
        }
        
        if(wnum>0){
            for(int i = 1; i<(wnum+1);i++){
                String exppostname;
                if(!wp[i-1].getSelectionModel().isEmpty()){
                    exppostname = " and e.ExpPost_Name = " + "'"
                            + wp[i-1].getSelectionModel().getSelectedItem().toString() + "'";    
                }
                else{
                    exppostname = " and e.ExpPost_Name is null ";
                }
                String ask2 = "use MileStoneHRMS select w.Exp_ID from WkExperience as w "
                        + " left outer join ExpPost as e on e.ExpPost_ID = w.ExpPost_ID "
                        + " where w.Exp_Name = " + "'" + w[i-1].getSelectionModel().getSelectedItem() + "'"
                        + exppostname;
                String out = "";
                try (Connection conn = SQLTools.MSSQL()) {
                    try (ResultSet rs = conn.createStatement().executeQuery(ask2)) {
                        while (rs.next()) {
                            out = rs.getString(1);
                        }
                    }
                }
                 
                String ask3 = "use MileStoneHRMS\n" +
                        "select top 1 cast(p.Exp_Num as int) from PWkExp as p\n" +
                        "left outer join Personel as pl on pl.P_ID = p.P_ID\n" +
                        "where p.P_ID = " + "'" + id + "'" +
                        "order by p.Exp_Num Desc ";
                
                //此段迴圈必須重新檢視 2017.09.28
                int num = 0;
                try (Connection conn = SQLTools.MSSQL()) {
                    try (ResultSet rs = conn.createStatement().executeQuery(ask3)) {
                        while (rs.next()) {
                            num = rs.getInt(1);
                        }
                    }
                }//End
               
                String sqlw = "use MileStoneHRMS insert into PWkExp(P_ID, Exp_ID, Exp_Num, StartD, EndD) "
                        + " values(?,?,?,?,?) ";

                try (Connection conn = SQLTools.MSSQL()) {
                    try (PreparedStatement pstmt = conn.prepareCall(sqlw)) {
                        pstmt.setString(1, id);
                        pstmt.setString(2, out);
                        pstmt.setInt(3, i);
                        if (date[i].getText().isEmpty() || date[i + 3].getText().isEmpty()) {
                            pstmt.setNull(4, java.sql.Types.DATE);
                        } else {
                            pstmt.setString(4, datecom(date[i].getText(), date[i + 3].getText(), "01"));
                        }
                        if (date[i + 6].getText().isEmpty() || date[i + 9].getText().isEmpty()) {
                            pstmt.setNull(5, java.sql.Types.DATE);
                        } else {
                            pstmt.setString(5, datecom(date[i + 6].getText(), date[i + 9].getText(), "01"));
                        }
                        pstmt.execute();
                    }
                }
            }
            Audit("主管/HR-員工工作履歷登錄", name);
        }
    }
    
    @FXML
    public void btn_back(MouseEvent event) throws Exception{
        StageControll.open(MenuSController.class, "/View/MenuS.fxml");
        StageControll.close(PAP);
    } 
}
