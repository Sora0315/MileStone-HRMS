package Controller;

import SelfTools.AuditLog;
import SelfTools.SQLTools;
import SelfTools.StageControll;
import SelfTools.StringVariation;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
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
public class MInvProperty2Controller implements Initializable {
    @FXML public AnchorPane IAP;
    @FXML public TextField name, id, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10
            , sy1, sy2, sy3, sy4, sy5, sy6, sy7, sy8, sy9, sy10
            , sm1, sm2, sm3, sm4, sm5, sm6, sm7, sm8, sm9, sm10
            , sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10
            , ey1, ey2, ey3, ey4, ey5, ey6, ey7, ey8, ey9, ey10
            , em1, em2, em3, em4, em5, em6, em7, em8, em9, em10
            , ed1, ed2, ed3, ed4, ed5, ed6, ed7, ed8, ed9, ed10;
    @FXML public ComboBox p1, p2, p3, p4, p5, p6, p7, p8, p9, p10;    
    @FXML public Button back, search, check, submit;
    
    public int loop;
    public List<String> oproduct = new ArrayList<>();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ComboBox [] p = {p1, p2, p3, p4, p5, p6, p7, p8, p9, p10};
        TextField [] tlist = {a1, a2, a3, a4, a5, a6, a7, a8, a9, a10
                , sy1, sy2, sy3, sy4, sy5, sy6, sy7, sy8, sy9, sy10
                , sm1, sm2, sm3, sm4, sm5, sm6, sm7, sm8, sm9, sm10
                , sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10
                , ey1, ey2, ey3, ey4, ey5, ey6, ey7, ey8, ey9, ey10
                , em1, em2, em3, em4, em5, em6, em7, em8, em9, em10
                , ed1, ed2, ed3, ed4, ed5, ed6, ed7, ed8, ed9, ed10};

        String sql = "use MileStoneHRMS select p.Pr_Name from Property as p";
        for(TextField items : tlist){
            items.setDisable(true);
            StageControll.TextFieldhandle(items);
        }
        for(ComboBox items : p){
            try {
                items.setEditable(true);
                items.setDisable(true);
                StageControll.ComboBoxCtrl(items);
                SQLTools.SqlGetItem(sql, items);
            } catch (Exception e) {
            }
        }
        StageControll.Keydirect(search, p1);
        StageControll.Keydirect(ed1, p2);
        StageControll.Keydirect(ed2, p3);
        StageControll.Keydirect(ed3, p4);
        StageControll.Keydirect(ed4, p5);
        StageControll.Keydirect(ed5, p6);
        StageControll.Keydirect(ed6, p7);
        StageControll.Keydirect(ed7, p8);
        StageControll.Keydirect(ed8, p9);
        StageControll.Keydirect(ed9, p10);
        StageControll.TextFieldhandle(id);
        StageControll.TextFieldhandle(name);
        StageControll.Buttonhandle(back);
        StageControll.Buttonhandle(search);
        StageControll.Buttonhandle(check);
        StageControll.Buttonhandle(submit);
        check.setDisable(true);
        submit.setDisable(true);
    }
    
    @FXML
    public void btn_search(MouseEvent event) throws Exception{
        if(!name.getText().isEmpty() || !id.getText().isEmpty()){
            loop = pre_loopnum();
            ComboBox [] product = {p1, p2, p3, p4, p5, p6, p7, p8, p9, p10};
            TextField [] amount = {a1, a2, a3, a4, a5, a6, a7, a8, a9, a10};
            TextField [] syear = {sy1, sy2, sy3, sy4, sy5, sy6, sy7, sy8, sy9, sy10};
            TextField [] smonth = {sm1, sm2, sm3, sm4, sm5, sm6, sm7, sm8, sm9, sm10};
            TextField [] sday = {sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10};
            TextField [] eyear = {ey1, ey2, ey3, ey4, ey5, ey6, ey7, ey8, ey9, ey10};
            TextField [] emonth = {em1, em2, em3, em4, em5, em6, em7, em8, em9, em10};
            TextField [] eday = {ed1, ed2, ed3, ed4, ed5, ed6, ed7, ed8, ed9, ed10};
            search_and_setdata(" pt.Pr_Name ", product);
            search_and_setdata(" i.Amount ", amount);
            search_and_setdata(" year(cast(i.StartD as date)) ", syear);
            search_and_setdata(" month(cast(i.StartD as date)) ", smonth);
            search_and_setdata(" day(cast(i.StartD as date)) ", sday);
            search_and_setdata(" year(cast(i.EndD as date)) ", eyear);
            search_and_setdata(" month(cast(i.EndD as date)) ", emonth);
            search_and_setdata(" day(cast(i.EndD as date)) ", eday);
            if(!product[1].getSelectionModel().isEmpty()){
                for(int i=0; i<loop;i++){
                    setenable(i);
                    oproduct.add(product[i].getSelectionModel().getSelectedItem().toString()); 
                }
                setenable(loop);
                check.setDisable(false);
            }
            else{
                NoticeController.noticecontent = "找不到資料唷！";
                StageControll.open(NoticeController.class, "/View/Notice.fxml");  
            }
        }
        else{
            NoticeController.noticecontent = "嘿！找資料也得輸入必要資訊呀！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");  
        }
    }
    
    public void search_and_setdata(String column,TextField [] target) throws Exception{
        String sql = "use MileStoneHRMS select " + column
                + " from InvProperty as i left outer join Property as pt on pt.Pr_ID = i.Pr_ID\n"
                + " left outer join Personel as p on p.P_ID = i.P_ID "
                + " where p.Name_CH = " + "'" + name.getText() + "'"
                + " and cast(substring(i.InvP_ID, 2, 6) as int) = " + "'" + id.getText() + "'";
        
        try (Connection conn = SQLTools.MSSQL()) {
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                String out = null;
                int i = 0;
                while (rs.next()) {
                    out = rs.getString(1);
                    if (out != null) {
                        target[i].setText(out);
                    } else {
                        target[i].clear();
                    }
                    i = i + 1;
                }
            }
        }
    }
    
    public void search_and_setdata(String column,ComboBox [] target) throws Exception{
        String sql = "use MileStoneHRMS select " + column
                + " from InvProperty as i left outer join Property as pt on pt.Pr_ID = i.Pr_ID\n"
                + " left outer join Personel as p on p.P_ID = i.P_ID "
                + " where p.Name_CH = " + "'" + name.getText() + "'"
                + " and cast(substring(i.InvP_ID, 2, 6) as int) = " + "'" + id.getText() + "'";
        
        try (Connection conn = SQLTools.MSSQL()) {
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                String out = null;
                int i = 0;
                while (rs.next()) {
                    out = rs.getString(1);
                    if (out != null) {
                        target[i].getSelectionModel().select(out);
                    } 
                    i = i + 1;
                }
            }
        }  
    }
    
    public int pre_loopnum() throws Exception{
        String sql = "use MileStoneHRMS select i.InvP_ID "
                + " from InvProperty as i left outer join Property as pt on pt.Pr_ID = i.Pr_ID\n"
                + " left outer join Personel as p on p.P_ID = i.P_ID"
                + " where p.Name_CH = " + "'" + name.getText() + "'"
                + " and cast(substring(i.InvP_ID, 2, 6) as int) = " + "'" + id.getText() + "'";
        
        try (Connection conn = SQLTools.MSSQL()) {
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                while (rs.next()) {
                    loop = loop + 1;
                }
                return loop;
            }
        }
    }

    public void setenable(int i){
        ComboBox [] product = {p1, p2, p3, p4, p5, p6, p7, p8, p9, p10};
        TextField [] amount = {a1, a2, a3, a4, a5, a6, a7, a8, a9, a10};
        TextField [] syear = {sy1,sy2, sy3, sy4, sy5, sy6, sy7, sy8, sy9, sy10};
        TextField [] smonth = {sm1, sm2, sm3, sm4, sm5, sm6, sm7, sm8, sm9, sm10};
        TextField [] sday = {sd1,sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10};
        TextField [] eyear = {ey1,ey2, ey3, ey4, ey5, ey6, ey7, ey8, ey9, ey10};
        TextField [] emonth = {em1, em2, em3, em4, em5, em6, em7, em8, em9, em10};
        TextField [] eday = {ed1, ed2, ed3, ed4, ed5, ed6, ed7, ed8, ed9, ed10};
        TextField [] set = {amount[i], syear[i], smonth[i], sday[i]
                            , eyear[i], emonth[i], eday[i]};
        product[i].setDisable(false);
        for(TextField item: set){
            item.setDisable(false);
        }        
    }
    
    @FXML public void set2(){
        setenable(0);
    }
    @FXML public void set3(){
        setenable(1);
    }
    @FXML public void set4(){
        setenable(2);
    }
    @FXML public void set5(){
        setenable(3);
    }
    @FXML public void set6(){
        setenable(4);
    }
    @FXML public void set7(){
        setenable(5);
    }
    @FXML public void set8(){
        setenable(6);
    }
    @FXML public void set9(){
        setenable(7);
    }
    @FXML public void set10(){
        setenable(8);
    }
    
    @FXML
    public void btn_checkdata(MouseEvent event){
        submit.setDisable(false);
        loop = post_loopnum();
    }
    
    public int post_loopnum(){
        int j = 0;
        ComboBox [] p = {p1, p2, p3, p4, p5, p6, p7, p8, p9, p10};
        for(int k=0; k<p.length; k++){
            if(p[k].getSelectionModel().isEmpty()){
                j=k;
                break;
            }
        }
        return j;
    }
    
    @FXML
    public void btn_back(MouseEvent event) throws Exception{
        StageControll.close(IAP);
        StageControll.open(MInvPropertyController.class, "/View/MInvProperty.fxml");
    }
    
    @FXML
    public void submit(MouseEvent event) throws Exception{
        ComboBox [] p = {p1, p2, p3, p4, p5, p6, p7, p8, p9, p10};
        TextField [] a = {a1, a2, a3, a4, a5, a6, a7, a8, a9, a10};
        TextField [] sy = {sy1, sy2, sy3, sy4, sy5, sy6, sy7, sy8, sy9, sy10};
        TextField [] sm = {sm1, sm2, sm3, sm4, sm5, sm6, sm7, sm8, sm9, sm10};
        TextField [] sd = {sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10};
        TextField [] ey = {ey1, ey2, ey3, ey4, ey5, ey6, ey7, ey8, ey9, ey10};
        TextField [] em = {em1, em2, em3, em4, em5, em6, em7, em8, em9, em10};
        TextField [] ed = {ed1, ed2, ed3, ed4, ed5, ed6, ed7, ed8, ed9, ed10};
        for(int i=0; i<loop; i++){
            Data_Save(i, p[i], a[i], sy[i], sm[i], sd[i], ey[i], em[i], ed[i]);
        }
        StageControll.close(IAP);
        StageControll.open(MInvProperty2Controller.class, "/View/MInvProperty2.fxml");
    }
       
    public void Data_Save(int num, ComboBox b, TextField amt
            , TextField sy, TextField sm, TextField sd
            , TextField ey, TextField em, TextField ed) throws Exception{
        String ask = "use MileStoneHRMS select p.P_ID from Personel as p where p.Name_CH = ";
        String askcombo = "use MileStoneHRMS select p.Pr_ID from Property as p where p.Pr_Name = ";
        String sql = "use MileStoneHRMS update InvProperty\n"
                + " set Pr_ID = ?, Amount = ?, StartD = ?, EndD = ?\n"
                + " where cast(substring(InvP_ID, 2, 6) as int) = " + "'" + id.getText() + "'"
                + " and P_ID = " + "'" + SQLTools.ValueGetId(ask, name) + "'"
                + " and Pr_ID = " + "'" + SQLTools.ValueGetId(askcombo, oproduct.get(num)) + "'";
        
        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, SQLTools.ValueGetId(askcombo, b));
                pstmt.setString(2, amt.getText());
                SQLTools.emptyslotsetnull(StringVariation.datecom(sy, sm, sd), pstmt, 3);
                SQLTools.emptyslotsetnull(StringVariation.datecom(ey, em, ed), pstmt, 4);
                pstmt.execute();
                AuditLog.Audit("主管/HR-修正員工持有財產清冊", name, b, amt);
            }
        }  
    }
}
