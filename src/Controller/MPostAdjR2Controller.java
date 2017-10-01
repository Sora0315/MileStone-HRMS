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
public class MPostAdjR2Controller implements Initializable {
    
    @FXML public AnchorPane PRAP;
    @FXML public TextField id, name;
    @FXML public ComboBox post;
    @FXML public TextField ssy, sy, sm, sd, ey, em, ed;
    @FXML public Button back, search, check, submit;
    public static String fpost = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TextField[] list = {id, sy, sm, sd, ey, em, ed};
        for (TextField items : list) {
            items.setDisable(true);
            StageControll.TextFieldhandle(items);
        }
        StageControll.TextFieldhandle(name);
        StageControll.TextFieldhandle(ssy);
        StageControll.ButtonCtrl(back, name);
        StageControll.ButtonCtrl(search, sy);
        StageControll.ButtonCtrl(check, submit);
        StageControll.ButtonCtrl(submit, back);
        post.setEditable(true);
        AutoCompleteComboBox autoCompleteComboBox = new AutoCompleteComboBox(post);
        check.setDisable(true);
        submit.setDisable(true);
        String sql = "use MileStoneHRMS select p.Post_Name from Position as p";
        try {
            SQLTools.comboboxSetItem(sql, post);
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
    
    @FXML
    public void btn_search(MouseEvent event) throws Exception{
        if (!id.getText().isEmpty() || !post.getSelectionModel().isEmpty() || !ssy.getText().isEmpty()) {
            search_and_setdata("year(cast(p.StartD as date))", sy);
            search_and_setdata("month(cast(p.StartD as date))", sm);
            search_and_setdata("day(cast(p.StartD as date))", sd);
            search_and_setdata("year(cast(p.EndD as date))", ey);
            search_and_setdata("month(cast(p.EndD as date))", em);
            search_and_setdata("day(cast(p.EndD as date))", ed);
            if (sy.getText().isEmpty() || sm.getText().isEmpty() || sd.getText().isEmpty()) {
                NoticeController.noticecontent = "找不到資料唷！";
                StageControll.open(NoticeController.class, "/View/Notice.fxml");
            } else {
                TextField[] list = {sy, sm, sd, ey, em, ed};
                for (TextField items : list) {
                    items.setDisable(false);
                }
                fpost = post.getSelectionModel().getSelectedItem().toString();
                check.setDisable(false);
            }
        } else {
            NoticeController.noticecontent = "嘿！找資料也得輸入必要資訊呀！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");
        }
    }
    
    public void search_and_setdata(String column,TextField target) throws Exception{
        String sql = "use MileStoneHRMS select " + column
                + " from PostAdjR as p left outer join Position as ps on p.Post_ID = ps.Post_ID\n"
                + " where p.P_ID = " + "'" + id.getText() + "'"
                + " and ps.Post_Name = " + "'" + post.getSelectionModel().getSelectedItem() + "'"
                + " and year(cast(p.StartD as date)) = " + "'" + ssy.getText() + "'";
 
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
    
    @FXML
    public void btn_checkdata(MouseEvent event) throws Exception{
        if(id.getText().isEmpty() || post.getSelectionModel().getSelectedItem()==null
                || sy.getText().isEmpty() || sm.getText().isEmpty() || sd.getText().isEmpty()){
            NoticeController.noticecontent = "請輸入必要資料！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");     
        }
        else{
            submit.setDisable(false);
        }     
    }
    
    @FXML
    public void btn_submit(MouseEvent event) throws Exception{
        Data_Save();
        StageControll.close(PRAP);
        StageControll.open(MPostAdjR2Controller.class, "/View/MPostAdjR2.fxml");
    }
    
    public void Data_Save() throws Exception{
        String ask  = "use MileStoneHRMS select p.Post_ID from Position as p where p.Post_Name = ";
        System.out.println(fpost);
        String sql = "use MileStoneHRMS update PostAdjR "
                + " set Post_ID = ?, StartD = ?, EndD = ? "
                + " where PostAdjR.P_ID =  " + "'" + id.getText() + "'"
                + " and PostAdjR.Post_ID = " + "'" + SQLTools.ValueGetId(ask, fpost) + "'"
                + " and year(cast(PostAdjR.StartD as date)) = " + "'" + ssy.getText() + "'";

        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, set_postid());
                pstmt.setString(2, StringVariation.datecom(sy, sm, sd));
                if (ey.getText().isEmpty() || em.getText().isEmpty() || ed.getText().isEmpty()) {
                    pstmt.setNull(3, java.sql.Types.NULL);
                } else {
                    pstmt.setString(3, StringVariation.datecom(ey, em, ed));
                }
                pstmt.execute();
                AuditLog.Audit("主管/HR-修正員工職務異動紀錄", name, post, StringVariation.datecom(sy, sm, sd));
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
        StageControll.open(MPostAdjRController.class, "/View/MPostAdjR.fxml");
    }
}
