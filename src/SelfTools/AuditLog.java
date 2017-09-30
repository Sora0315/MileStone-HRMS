package SelfTools;

import static Controller.LoginController.*;
import static SelfTools.SQLTools.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * @author Casval
 */
public class AuditLog {
    
    static DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    static String datenow = dateformat.format(new Date());
    static Connection conn;
    static PreparedStatement pstmt = null;
    /*
    Methods overloading
    */
    public static void AuditLogin(){
        
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Time, Host)\n "
                    + "values(?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idout);
            pstmt.setString(2, "Succefully Login");
            pstmt.setString(3, datenow);
            pstmt.setString(4, host);
            pstmt.execute();
        }
        catch(Exception e){
         }
    }
    
    public static void AuditInvalidLogin(){
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Time, Host)\n "
                    + "values(?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            if(idout==null){
                pstmt.setString(1, "登入者不明");
            }else{
                pstmt.setString(1, idout);
            }
            pstmt.setString(2, "Invalid Login");
            pstmt.setString(3, datenow);
            pstmt.setString(4, host);
            pstmt.execute();
        }
        catch(Exception e){
        }
    }
    
    public static void Audit(String act, TextField tf){
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Data1, Time, Host)\n "
                    + "values(?,?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idout);
            pstmt.setString(2, act);
            emptyslotsetnull(tf.getText(), pstmt, 3);
            pstmt.setString(4, datenow);
            pstmt.setString(5, host);
            pstmt.execute();
        }
        catch(Exception e){
        }
    }
    
    public static void Audit(String act, TextField tf, String txt){
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Data1, Data2, Time, Host)\n "
                    + "values(?,?,?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idout);
            pstmt.setString(2, act);
            emptyslotsetnull(tf.getText(), pstmt, 3);
            emptyslotsetnull(txt, pstmt, 4);
            pstmt.setString(5, datenow);
            pstmt.setString(6, host);
            pstmt.execute();
        }
        catch(Exception e){
        }
    }
    
    public static void Audit(String act, ComboBox item1, ComboBox items, String txt){
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Data1, Data2, Data3, Time, Host)\n "
                    + "values(?,?,?,?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idout);
            pstmt.setString(2, act);
            emptyslotsetnull(item1.getSelectionModel().getSelectedItem().toString(), pstmt, 3);
            pstmt.setString(4, items.getSelectionModel().getSelectedItem().toString());
            emptyslotsetnull(txt, pstmt, 5);
            pstmt.setString(6, datenow);
            pstmt.setString(7, host);
            pstmt.execute();
        }
        catch(Exception e){
        }
    }
    
        public static void Audit(String act, TextField tf, ComboBox items, String txt){
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Data1, Data2, Data3, Time, Host)\n "
                    + "values(?,?,?,?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idout);
            pstmt.setString(2, act);
            emptyslotsetnull(tf.getText(), pstmt, 3);
            pstmt.setString(4, items.getSelectionModel().getSelectedItem().toString());
            emptyslotsetnull(txt, pstmt, 5);
            pstmt.setString(6, datenow);
            pstmt.setString(7, host);
            pstmt.execute();
        }
        catch(Exception e){
        }
    }
    
    public static void Audit(String act, String txt, ComboBox items, String txt2, String txt3){
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Data1, Data2, Data3, Data4, Time, Host)\n "
                    + "values(?,?,?,?,?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idout);
            pstmt.setString(2, act);
            emptyslotsetnull(txt, pstmt, 3);
            pstmt.setString(4, items.getSelectionModel().getSelectedItem().toString());
            emptyslotsetnull(txt2, pstmt, 5);
            emptyslotsetnull(txt3, pstmt, 6);
            pstmt.setString(7, datenow);
            pstmt.setString(8, host);
            pstmt.execute();
        }
        catch(Exception e){
        }
    }
    
    public static void Audit(String act, TextField tf, TextField tf2){
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Data1, Data2, Time, Host)\n "
                    + "values(?,?,?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idout);
            pstmt.setString(2, act);
            emptyslotsetnull(tf.getText(), pstmt, 3);
            emptyslotsetnull(tf2.getText(), pstmt, 4);
            pstmt.setString(5, datenow);
            pstmt.setString(6, host);
            pstmt.execute();
        }
        catch(Exception e){
        }
    }
    
    public static void Audit(String act, TextField tf, TextField tf2, TextField tf3){
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Data1, Data2, Data3, Time, Host)\n "
                    + "values(?,?,?,?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idout);
            pstmt.setString(2, act);
            emptyslotsetnull(tf.getText(), pstmt, 3);
            emptyslotsetnull(tf2.getText(), pstmt, 4);
            emptyslotsetnull(tf3.getText(), pstmt, 5);
            pstmt.setString(6, datenow);
            pstmt.setString(7, host);
            pstmt.execute();
        }
        catch(Exception e){
        }
    }
    
    public static void Audit(String act, TextField tf, TextField tf2, TextField tf3
                            , TextField tf4){
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Data1, Data2, Data3, Data4, Time, Host)\n "
                    + "values(?,?,?,?,?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idout);
            pstmt.setString(2, act);
            emptyslotsetnull(tf.getText(), pstmt, 3);
            emptyslotsetnull(tf2.getText(), pstmt, 4);
            emptyslotsetnull(tf3.getText(), pstmt, 5);
            emptyslotsetnull(tf4.getText(), pstmt, 6);
            pstmt.setString(7, datenow);
            pstmt.setString(8, host);
            pstmt.execute();
        }
        catch(Exception e){
        }
    }
    
    public static void Audit(String act, TextField tf, TextField tf2, TextField tf3
                            , TextField tf4, TextField tf5){
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Data1, Data2, Data3, Data4, Data5, Time, Host)\n "
                    + "values(?,?,?,?,?,?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idout);
            pstmt.setString(2, act);
            emptyslotsetnull(tf.getText(), pstmt, 3);
            emptyslotsetnull(tf2.getText(), pstmt, 4);
            emptyslotsetnull(tf3.getText(), pstmt, 5);
            emptyslotsetnull(tf4.getText(), pstmt, 6);
            emptyslotsetnull(tf5.getText(), pstmt, 7);
            pstmt.setString(8, datenow);
            pstmt.setString(9, host);
            pstmt.execute();
        }
        catch(Exception e){
        }
    }
    
    public static void Audit(String act, TextField tf, TextField tf2, ComboBox item){
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Data1, Data2, Data3, Time, Host)\n "
                    + "values(?,?,?,?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idout);
            pstmt.setString(2, act);
            emptyslotsetnull(tf, pstmt, 3);
            emptyslotsetnull(tf2, pstmt, 4);
            emptyslotsetnull(item, pstmt, 5);
            pstmt.setString(6, datenow);
            pstmt.setString(7, host);
            pstmt.execute();
        }
        catch(Exception e){
        }
    }
    
    public static void Audit(String act, TextField tf, TextField tf2, String txt){
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Data1, Data2, Data3, Time, Host)\n "
                    + "values(?,?,?,?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idout);
            pstmt.setString(2, act);
            emptyslotsetnull(tf, pstmt, 3);
            emptyslotsetnull(tf2, pstmt, 4);
            emptyslotsetnull(txt, pstmt, 5);
            pstmt.setString(6, datenow);
            pstmt.setString(7, host);
            pstmt.execute();
        }
        catch(Exception e){
        }
    }
       
    public static void Audit(String act, ComboBox item){
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Data1, Time, Host)\n "
                    + "values(?,?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idout);
            pstmt.setString(2, act);
            pstmt.setString(3, item.getSelectionModel().getSelectedItem().toString());
            pstmt.setString(4, datenow);
            pstmt.setString(5, host);
            pstmt.execute();
        }
        catch(Exception e){
        }
    }
    
    public static void Audit(String act, ComboBox item, ComboBox item2){
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Data1, Data2, Time, Host)\n "
                    + "values(?,?,?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idout);
            pstmt.setString(2, act);
            pstmt.setString(3, item.getSelectionModel().getSelectedItem().toString());
            pstmt.setString(4, item2.getSelectionModel().getSelectedItem().toString());
            pstmt.setString(5, datenow);
            pstmt.setString(6, host);
            pstmt.execute();
        }
        catch(Exception e){
        }
    }
    
    public static void Audit(String act, ComboBox item, ComboBox item2, ComboBox item3){
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Data1, Data2, Data3, Time, Host)\n "
                    + "values(?,?,?,?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idout);
            pstmt.setString(2, act);
            pstmt.setString(3, item.getSelectionModel().getSelectedItem().toString());
            pstmt.setString(4, item2.getSelectionModel().getSelectedItem().toString());
            pstmt.setString(5, item3.getSelectionModel().getSelectedItem().toString());
            pstmt.setString(6, datenow);
            pstmt.setString(7, host);
            pstmt.execute();            
        }
        catch(Exception e){
        }
    }
    
    public static void Audit(String act, ComboBox item, TextField tf){
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Data1, Data2, Time, Host)\n "
                    + "values(?,?,?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idout);
            pstmt.setString(2, act);
            pstmt.setString(3, item.getSelectionModel().getSelectedItem().toString());
            emptyslotsetnull(tf, pstmt, 4);
            pstmt.setString(5, datenow);
            pstmt.setString(6, host);
            pstmt.execute();
        }
        catch(Exception e){
        }
    }
    
    public static void Audit(String act, ComboBox item, TextField tf, TextField tf2){
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Data1, Data2, Data3, Time, Host)\n "
                    + "values(?,?,?,?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idout);
            pstmt.setString(2, act);
            pstmt.setString(3, item.getSelectionModel().getSelectedItem().toString());
            emptyslotsetnull(tf, pstmt, 4);
            emptyslotsetnull(tf2, pstmt, 5);
            pstmt.setString(6, datenow);
            pstmt.setString(7, host);
            pstmt.execute();
        }
        catch(Exception e){
        }
    }
    
    public static void Audit(String act, TextField tf, TextField tf2, TextField tf3
            , TextField tf4, ComboBox item){
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Data1, Data2, Data3, Data4, Data5, Time, Host)\n "
                    + "values(?,?,?,?,?,?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idout);
            pstmt.setString(2, act);
            emptyslotsetnull(tf.getText(), pstmt, 3);
            emptyslotsetnull(tf2.getText(), pstmt, 4);
            emptyslotsetnull(tf3.getText(), pstmt, 5);
            emptyslotsetnull(tf4.getText(), pstmt, 6);
            pstmt.setString(7, item.getSelectionModel().getSelectedItem().toString());
            pstmt.setString(8, datenow);
            pstmt.setString(9, host);
            pstmt.execute();
        }
        catch(Exception e){
        }
    }
    
    public static void Audit(String act, String txt, ComboBox item, String txt2){
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Data1, Data2, Data3, Time, Host)\n "
                    + "values(?,?,?,?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idout);
            pstmt.setString(2, act);
            emptyslotsetnull(txt, pstmt, 3);
            emptyslotsetnull(item, pstmt, 4);
            emptyslotsetnull(txt2, pstmt, 5);
            pstmt.setString(6, datenow);
            pstmt.setString(7, host);
            pstmt.execute();
        }
        catch(Exception e){
        }
    }
    
    public static void Audit(String act, TextField tf, ComboBox item, TextField tf2){
        try{
            conn = MSSQL();
            String sql = "use SecurityDB Insert into Audit_HRMS(UserName, Action, Data1, Data2, Data3, Time, Host)\n "
                    + "values(?,?,?,?,?,?,?)" ;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idout);
            pstmt.setString(2, act);
            emptyslotsetnull(tf, pstmt, 3);
            emptyslotsetnull(item, pstmt, 4);
            emptyslotsetnull(tf2, pstmt, 5);
            pstmt.setString(6, datenow);
            pstmt.setString(7, host);
            pstmt.execute();
        }
        catch(Exception e){
        }
    }
}
