package Controller;

import SelfTools.AuditLog;
import SelfTools.SQLTools;
import SelfTools.StageControll;
import SelfTools.StringVariation;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 * @author Sora
 */
public class AddWkExpController implements Initializable {
    
    @FXML public AnchorPane EAP;
    @FXML public TextField eid, ename, pid, pname, content;
    @FXML public Button back, set, submit;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        eid.setDisable(true);
        pid.setDisable(true);
        submit.setDisable(true);
        StageControll.TextFieldhandle(ename);
        StageControll.TextFieldhandle(pname);
        StageControll.TextFieldhandle(content);
        StageControll.Buttonhandle(back);
        StageControll.Buttonhandle(set);
        StageControll.Buttonhandle(submit);
    }
    
    @FXML
    public void set_eid(MouseEvent event) throws Exception{
        if(ename.getText().isEmpty() && eid.getText().isEmpty()){
            NoticeController.noticecontent = "請輸入必要資料！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml"); 
        }
        else{
            if(pname.getText().isEmpty()){
                if(!ename.getText().isEmpty()){
                    String askpre = "use MileStoneHRMS\n" +
                            "select we.Exp_ID from WkExperience as we where we.Exp_Name = ";
                    String askpost = " and we.ExpPost_ID is NULL ";
                    if(SQLTools.ValueGetId(askpre, askpost, ename).isEmpty()){
                        String sql = "use MileStoneHRMS select cast(substring(ex.Exp_ID, 4, 8) as int) from WkExperience as ex ";
                        String num = Integer.toString(SQLTools.id_incre(((int)SQLTools.Sql_Get_ID(sql))));
                        String id = "EXP" + StringVariation.right(("00000" + num), 5);
                        eid.setText(id);
                        submit.setDisable(false);
                    }
                    else{
                        NoticeController.noticecontent = "資料已存在！";
                        StageControll.open(NoticeController.class, "/View/Notice.fxml"); 
                    }
                }
            }
            else{
                //先判斷pname是否已有pid，未有pid先建立。因為pid未建立，因此eid必然必須set new。
                if(pid.getText().isEmpty()){
                    String idsql = "use MileStoneHRMS\n" +
                            "select max(cast((substring((ex.ExpPost_ID), 5, 9)) as int))\n" +
                            "from ExpPost as ex";
                    String idnum = Integer.toString(SQLTools.id_incre(SQLTools.Sql_Get_ID(idsql)));
                    String pidset = "EXPO" + StringVariation.right(("00000" + idnum), 5);
                    pid.setText(pidset);
                    Data_Save("ExpPost", "ExpPost_ID, ExpPost_Name", pidset, pname);
                    String idsql2 = "use MileStoneHRMS\n" +
                            "select max(cast((substring((we.Exp_ID), 4, 8)) as int))\n" +
                            "from WkExperience as we ";
                    String id2num = Integer.toString(SQLTools.id_incre(SQLTools.Sql_Get_ID(idsql2)));
                    String eidset = "EXP" + StringVariation.right(("00000" + id2num), 5);
                    eid.setText(eidset); 
                    submit.setDisable(false);
                }
                //如果已有pid，則開始判斷ename與pid是否已同時具有資料，即eid的存在。
                else{
                    if(!ename.getText().isEmpty()){
                        String askpre = "use MileStoneHRMS\n" +
                                "select we.Exp_ID from WkExperience as we where we.Exp_Name = ";
                        String askpost = "  and we.ExpPost_ID = ";
                        if(SQLTools.ValueGetId(askpre, askpost, ename, pid).isEmpty()){
                            String sql = "use MileStoneHRMS select cast(substring(ex.Exp_ID, 4, 8) as int) from WkExperience as ex ";
                            String num = Integer.toString(SQLTools.id_incre(((int)SQLTools.Sql_Get_ID(sql))));
                            String id = "EXP" + StringVariation.right(("00000" + num), 5);
                            eid.setText(id);
                            submit.setDisable(false);
                        }
                        else{
                            NoticeController.noticecontent = "資料已存在！";
                            StageControll.open(NoticeController.class, "/View/Notice.fxml"); 
                        }
                    }
                }
            }
        }
    }
    
    public void set_pid() throws Exception{        
        String sql = "use MileStoneHRMS select e.ExpPost_ID from ExpPost as e where ExpPost_Name = ";
        pid.setText(SQLTools.ValueGetId(sql, pname));
    }
    
     @FXML
    public void btn_submit(MouseEvent event) throws Exception{
        if(!eid.getText().isEmpty()){
            Data_Save("WkExperience", "Exp_ID, Exp_Name, ExpPost_ID, Exp_Content ", eid, ename, pid, content);
            AuditLog.Audit("準備程序-工作履歷登錄", ename, pname);
        }
        StageControll.close(EAP);
        StageControll.open(AddWkExpController.class, "/View/AddWkExp.fxml");
    }
    
    public void Data_Save(String table, String itemnum, TextField id, TextField name) throws Exception{
        String sql = "use MileStoneHRMS insert into " + table + "( " + itemnum + " ) values (?,?)" ;
        try(Connection conn = SQLTools.MSSQL()){
            try(PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1, id.getText());
                pstmt.setString(2, name.getText());
                pstmt.execute();        
            }
        }
    }
    
    public void Data_Save(String table, String itemnum, String id, TextField name) throws Exception{
        String sql = "use MileStoneHRMS insert into " + table + "( " + itemnum + " ) values (?,?)" ;
        try(Connection conn = SQLTools.MSSQL()){
            try(PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1, id);
                pstmt.setString(2, name.getText());
                pstmt.execute();        
            }
        }
    }
    
    public void Data_Save(String table, String itemnum, TextField id, TextField name, TextField name2, TextField name3) throws Exception{
        String sql = "use MileStoneHRMS insert into " + table + "( " + itemnum + " ) values (?,?,?,?)" ;
        try(Connection conn = SQLTools.MSSQL()){
            try(PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1, id.getText());
                pstmt.setString(2, name.getText());
                SQLTools.emptyslotsetnull(name2, pstmt, 3);
                SQLTools.emptyslotsetnull(name3, pstmt, 4);
                pstmt.execute();                
            }
        }
    }
    
    @FXML
    public void btn_back(MouseEvent event) throws Exception{
        StageControll.close(EAP);
        StageControll.open(PreparationController.class, "/View/Preparation.fxml");
    }
}