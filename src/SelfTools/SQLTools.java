package SelfTools;

import Controller.NoticeController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * @author Sora
 */
public class SQLTools {

    //JDBC
    public static Connection MSSQL() {
        String URL = "jdbc:sqlserver://localhost:1433;database=MileStoneHRMS;user=poweruser;password=54685057";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException | SQLException e) {
            NoticeController.noticecontent = "連線失敗！";
            NoticeController.noticecontent2 = "請檢查網路。";
            try {
                StageControll.open(NoticeController.class, "/View/Notice.fxml", true);
            } catch (Exception ex) {
            }
            return null;
        }
    }

    public static Connection MSSQL_S() {
        String URL = "jdbc:sqlserver://localhost:1433;database=SecurityDB;user=poweruser;password=54685057";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException | SQLException e) {
            NoticeController.noticecontent = "連線失敗！";
            NoticeController.noticecontent2 = "請檢查網路。";
            try {
                StageControll.open(NoticeController.class, "/View/Notice.fxml", true);
            } catch (Exception ex) {
            }
            return null;
        }
    }

    //By using this method, get the value which match row of value column from DB's table, and return matched ID.
    public static String ValueGetId(String txt, TextField field) throws Exception {
        try (Connection conn = MSSQL()) {
            String sql = txt + "'" + field.getText() + "'";
            String output;
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                output = "";
                while (rs.next()) {
                    output = rs.getString(1);
                }
            }
            return output;
        }
    }

    public static String ValueGetId(String txt, String source) throws Exception {
        try (Connection conn = MSSQL()) {
            String sql = txt + "'" + source + "'";
            String output;
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                output = "";
                while (rs.next()) {
                    output = rs.getString(1);
                }
            }
            return output;
        }
    }

    public static String ValueGetId(String txt, String txt2, TextField field, TextField field2) throws Exception {
        try (Connection conn = MSSQL()) {
            String sql = txt + "'" + field.getText() + "'"
                    + txt2 + "'" + field2.getText() + "'";
            String output;
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                output = "";
                while (rs.next()) {
                    output = rs.getString(1);
                }
            }
            return output;
        }
    }

    public static String ValueGetId(String txt, String txt2, TextField field) throws Exception {
        try (Connection conn = MSSQL()) {
            String sql = txt + "'" + field.getText() + "'" + txt2;
            String output;
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                output = "";
                while (rs.next()) {
                    output = rs.getString(1);
                }
            }
            return output;
        }
    }

    public static String ValueGetId(String txt, ComboBox item) throws Exception {
        try (Connection conn = MSSQL()) {
            String sql = txt + "'" + item.getSelectionModel().getSelectedItem() + "'";
            String output;
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                output = "";
                while (rs.next()) {
                    output = rs.getString(1);
                }
            }
            return output;
        }
    }

    //Sql_Get_ID, by using method will  return a int value for column of Table which would be as the ID.
    public static int Sql_Get_ID(String query) throws Exception {
        try (Connection conn = MSSQL()) {
            int output;
            try (ResultSet rs = conn.createStatement().executeQuery(query)) {
                output = 0;
                while (rs.next()) {
                    output = rs.getInt(1);
                }
            }
            return output;
        }
    }

    //By using this method can draw combobox selected value, and the value will convert as the id put in textfield.
    public static void Cmb_Tfd_putID(String txt, ComboBox cmb, TextField target) throws Exception {
        if (cmb.getSelectionModel().getSelectedItem().toString() != null) {
            target.setText(ValueGetId(txt, cmb));
        }
    }

    //By using the method can draw value from database and put into combobox as selectable item.
    public static void SqlGetItem(String txt, ComboBox cmb) throws Exception {
        try (Connection conn = MSSQL()) {
            ObservableList<String> list;
            try (ResultSet rs = conn.createStatement().executeQuery(txt)) {
                list = FXCollections.observableArrayList();
                while (rs.next()) {
                    list.add(rs.getString(1));
                }
            }
            cmb.setItems(list);
        }
    }

    public static void SqlGetItem_S(String txt, ComboBox cmb) throws Exception {
        try (Connection conn = MSSQL_S()) {
            ObservableList<String> list;
            try (ResultSet rs = conn.createStatement().executeQuery(txt)) {
                list = FXCollections.observableArrayList();
                while (rs.next()) {
                    list.add(rs.getString(1));
                }
            }
            cmb.setItems(list);
        }
    }

    public static int id_incre(int i) {
        int out = i+1;
        return out;
    }

    public static int txtnotemptytoint(TextField txt) {
        int i;
        if (txt.getText() == null || txt.getText().equalsIgnoreCase("") || txt.getText().isEmpty()) {
            i = 0;
        } else {
            i = Integer.parseInt(txt.getText());
        }
        return i;
    }

    public static void emptyslotsetnull(TextField txt, PreparedStatement st, int location) throws Exception {
        if (txt.getText() == null || txt.getText().equalsIgnoreCase("") || txt.getText().isEmpty()) {
            st.setNull(location, java.sql.Types.VARCHAR);
        } else {
            st.setString(location, txt.getText());
        }
    }

    public static void emptyslotsetnull(String txt, PreparedStatement st, int location) throws Exception {
        if (txt == null || txt.equalsIgnoreCase("") || txt.isEmpty()) {
            st.setNull(location, java.sql.Types.VARCHAR);
        } else {
            st.setString(location, txt);
        }
    }

    public static void emptyslotsetnull(ComboBox box, PreparedStatement st, int location) throws Exception {
        if (!box.getSelectionModel().isEmpty()) {
            st.setString(location, box.getSelectionModel().getSelectedItem().toString());
        } else {
            st.setNull(location, java.sql.Types.VARCHAR);
        }
    }

    public static void emptyslotsetnull(ComboBox box, String txt, PreparedStatement st, int location) throws Exception {
        if (!box.getSelectionModel().isEmpty()) {
            st.setString(location, txt);
        } else {
            st.setNull(location, java.sql.Types.VARCHAR);
        }
    }
}
