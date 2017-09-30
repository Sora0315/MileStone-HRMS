package Controller;

import SelfTools.AuditLog;
import SelfTools.SQLTools;
import SelfTools.StageControll;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 * @author Sora
 */
public class RegController implements Initializable {
    
    @FXML public AnchorPane RAP;
    @FXML public Button r, c;
    @FXML private TextField pid, id;
    @FXML private PasswordField pwd;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        r.setDisable(true);
        StageControll.TextFieldhandle(pid);
        StageControll.TextFieldhandle(id);
        StageControll.TextFieldhandle((TextField)pwd);
        StageControll.ButtonCtrl(r, c);
        StageControll.ButtonCtrl(c, pid);
    }
    
    @FXML public void set_r_enable() throws Exception{
        if(!pid.getText().isEmpty() && !id.getText().isEmpty() && !pwd.getText().isEmpty()){
            r.setDisable(false);
        }
    }
    
    @FXML
    public void btn_r(MouseEvent event) throws Exception {
        String sql = "use SecurityDB insert into SC(ID, PWD, GRP, P_ID) "
                + " values(?,?,?,?)";

        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id.getText());
                pstmt.setString(2, pwd.getText());
                pstmt.setString(3, "Staff");
                pstmt.setString(4, pid.getText());
                pstmt.execute();
                AuditLog.Audit("Register", id, pwd, pid);
                StageControll.close(RAP);
                StageControll.open(LoginController.class, "/View/Login.fxml");
                NoticeController.noticecontent = "註冊成功！";
                NoticeController.noticecontent2 = "請以申請帳號登入。";
                StageControll.open(NoticeController.class, "/View/Notcie.fxml", true);
            }
        }
    }
    
    @FXML public void btn_cancel(MouseEvent event) throws Exception{
        StageControll.close(RAP);
        StageControll.open(LoginController.class, "/View/Login.fxml");
    }
}
