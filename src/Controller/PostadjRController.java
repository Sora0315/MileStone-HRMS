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

/**
 * FXML Controller class
 * @author Sora
 */
public class PostadjRController implements Initializable {
    
    @FXML public AnchorPane PRAP;
    @FXML public TextField id;
    @FXML public ComboBox name, post;
    @FXML public TextField sy, sm, sd, ey, em, ed;
    @FXML public Button back, confirm, submit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setDisable(true);
        submit.setDisable(true);
        String sql_name = "use MileStoneHRMS select distinct p.Name_CH from Personel as p "
                + " left join PStatus as ps on ps.P_ID = p.P_ID left join Activity as a on a.Act_ID = ps.Act_ID\n"
                + " where a.Act_Name like '在職' and ps.EndD is null ";
        String sql ="use MileStoneHRMS select p.Post_Name from Position as p";
        try{
            SQLTools.SqlGetItem(sql_name, name);
            SQLTools.SqlGetItem(sql, post);
        }
        catch(Exception e){
        }
        TextField [] tfall = {id, sy, sm, sd, ey, em, ed};
        for(TextField items : tfall){
            StageControll.TextFieldhandle(items);
        }
        name.setEditable(true);
        post.setEditable(true);
        StageControll.ComboBoxCtrl(name);
        StageControll.ComboBoxCtrl(post);
        AutoCompleteComboBox autoCompleteComboBox = new AutoCompleteComboBox(name);
        AutoCompleteComboBox autoCompleteComboBox1 = new AutoCompleteComboBox(post);
        StageControll.Keydirect(back, name);
        StageControll.Keydirect(name, post);
        StageControll.ButtonCtrl(back, name);
        StageControll.ButtonCtrl(confirm, submit);
        StageControll.ButtonCtrl(submit, back);
        name.setOnAction(e-> set_id());
    }

    public void set_id(){
        try{
            String sql = "use MileStoneHRMS select p.P_ID from Personel as p where p.Name_CH = ";
            id.setText(SQLTools.ValueGetId(sql, name));
        }
        catch(Exception e){
        }
    }
    
    @FXML
    public void btn_checkdata(MouseEvent event) throws Exception{
        if (id.getText().isEmpty() || post.getSelectionModel().getSelectedItem() == null
                || sy.getText().isEmpty() || sm.getText().isEmpty() || sd.getText().isEmpty()) {
            NoticeController.noticecontent = "請輸入必要資料！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");
        } else {
            String sql = "use MileStoneHRMS select p.P_ID from PostAdjR as p where p.P_ID = "
                    + "'" + id.getText() + "'" + " and p.Post_ID = "
                    + "'" + set_postid() + "'" + " and p.StartD = "
                    + "'" + StringVariation.datecom(sy, sm, sd) + "'";
            try (Connection conn = SQLTools.MSSQL()) {
                try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                    if (rs.next()) {
                        NoticeController.noticecontent = "資料已存在！";
                        StageControll.open(NoticeController.class, "/View/Notice.fxml");
                    } else {
                        submit.setDisable(false);
                    }
                }
            }
        }
    }
    
    @FXML
    public void btn_submit(MouseEvent event) throws Exception{
        Data_Save();
        StageControll.close(PRAP);
        StageControll.open(PostadjRController.class, "/View/PostadjR.fxml");
    }
    
    public void Data_Save() throws Exception {
        String sql = "use MileStoneHRMS insert into PostAdjR(P_ID, Post_ID, StartD, EndD)"
                + " values(?,?,?,?)";
        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id.getText());
                pstmt.setString(2, set_postid());
                pstmt.setString(3, StringVariation.datecom(sy, sm, sd));
                SQLTools.emptyslotsetnull(StringVariation.datecom(ey, em, ed), pstmt, 4);
                pstmt.execute();
                AuditLog.Audit("主管/HR-員工職務異動登錄", name, post, StringVariation.datecom(sy, sm, sd));
            }
        }
    }
    
    public String  set_postid() throws Exception{
        String sql = "use MileStoneHRMS select p.Post_ID from Position as p where p.Post_Name = ";
        return(SQLTools.ValueGetId(sql, post));
    }
    
    @FXML
    public void btn_back() throws Exception{
        StageControll.close(PRAP);
        StageControll.open(MenuSController.class, "/View/MenuS.fxml");
    }
}