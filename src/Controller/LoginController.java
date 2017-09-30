package Controller;

import SelfTools.AuditLog;
import SelfTools.ExportTools;
import SelfTools.SQLTools;
import SelfTools.StageControll;
import java.net.InetAddress;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 * @author Sora
 */
public class LoginController implements Initializable {

    @FXML public AnchorPane LogAP;
    @FXML public TextField id;
    @FXML private PasswordField pwd;
    @FXML public ComboBox grp;
    @FXML public Button cancel, reg, login;
    public static String idout, grpout, host, mspid;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Set_Group();
        } catch (Exception e) {
        }
        grp.setEditable(true);
        StageControll.ComboBoxCtrl(grp);
        StageControll.TextFieldhandle(id);
        StageControll.TextFieldhandle((TextField) pwd);
        StageControll.Buttonhandle(cancel);
        StageControll.Buttonhandle(reg);
        StageControll.Buttonhandle(login);
        StageControll.Keydirect(cancel, grp);
        login.setDisable(true);
    }

    @FXML
    public void Set_Group() throws Exception {
        String sql = "use SecurityDB select distinct s.GRP from SC as s";
        SQLTools.SqlGetItem_S(sql, grp);
    }

    @FXML
    public void Set_btn_Login() throws Exception {
        if (id.getText().isEmpty() || pwd.getText().isEmpty()
                || grp.getSelectionModel().isEmpty()) {
        } else {
            login.setDisable(false);
        }
    }

    @FXML
    public void btn_login(MouseEvent event) throws Exception {
        if ((id.getText() + pwd.getText() + grp.getSelectionModel().getSelectedItem()).hashCode() == convert("SC", "ID", "PWD", "GRP", id, pwd, grp)) {
            idout = id.getText();
            grpout = grp.getSelectionModel().getSelectedItem().toString();
            host = InetAddress.getLocalHost().getHostAddress();
            mspidset();
            AuditLog.AuditLogin();
            if (grp.getSelectionModel().getSelectedItem().toString().matches("Staff")) {
                StageControll.open(MenuEController.class, "/View/MenuE.fxml");
            } else {
                StageControll.open(MenuCController.class, "/View/MenuC.fxml");
                ExportTools.exportpath();
            }
            StageControll.close(LogAP);
        } else {
            NoticeController.noticecontent = "登入失敗！";
            NoticeController.noticecontent2 = "請確認帳號名稱，密碼與群組選擇是否正確！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml", true);
            host = InetAddress.getLocalHost().getHostAddress();
            AuditLog.AuditInvalidLogin();
        }
    }

    @FXML
    public void btn_register() throws Exception {
        StageControll.open(RegController.class, "/View/Reg.fxml");
        StageControll.close(LogAP);
    }

    private int convert(String table, String col, String col2, String col3, TextField tf, TextField tf2, ComboBox box) throws Exception {
        int output;
        try (Connection conn_s = SQLTools.MSSQL_S()) {
            String sql = "use SecurityDB select " + table + "." + col
                    + " , " + table + "." + col2 + " , " + table + "." + col3
                    + " from " + table
                    + " where " + table + "." + col + " = '" + tf.getText() + "'"
                    + " and " + table + "." + col2 + " = '" + tf2.getText() + "'"
                    + " and " + table + "." + col3 + " = '" + box.getSelectionModel().getSelectedItem() + "'";
            Statement stmt = conn_s.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            output = 0;
            while (rs.next()) {
                output = (rs.getString(1) + rs.getString(2) + rs.getString(3)).hashCode();
            }
        }
        return output;
    }

    public static void mspidset() throws Exception {
        try (Connection conn_s = SQLTools.MSSQL_S()) {
            String ask = "use SecurityDB select s.P_ID from SC as s "
                    + " where s.ID = " + "'" + idout + "'"
                    + " and s.GRP = " + "'" + grpout + "'";
            ResultSet rs = conn_s.createStatement().executeQuery(ask);
            String out = null;
            while (rs.next()) {
                out = rs.getString(1);
            }
            mspid = out;
        }
    }

    @FXML
    public void btn_cancel(MouseEvent event) throws Exception {
        StageControll.close(LogAP);
    }
}
