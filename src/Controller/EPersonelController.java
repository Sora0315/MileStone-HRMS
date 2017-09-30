package Controller;

import SelfTools.SQLTools;
import SelfTools.StageControll;
import SelfTools.UserInfo;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 * @author Sora
 */
public class EPersonelController implements Initializable {

    @FXML
    public AnchorPane PersonAP;
    @FXML
    public Label pid, namech, nameen, nid, by, bm, bd, blood, cell, tel, address, school, dep, ername, ercell, ertel, iy, im, id, status, sy, sm, sd, ey, em, ed;
    @FXML
    public Button back;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pid.setText(UserInfo.emp_pid);
        try {
            set_data_p();
            set_data_s();
        } catch (Exception e) {
        }
        StageControll.Buttonhandle(back);
    }

    public void set_data_p() throws Exception {
        String sql = "use MileStoneHRMS select p.Name_CH, p.Name_EN, p.NID,"
                + " year(cast(p.Birth as date)), month(cast(p.Birth as date)), day(cast(p.Birth as date)),"
                + " b.Type_Name, p.Cell, p.Tel, p.Address, gs.School_Name, gd.Dep_Name,"
                + " year(cast(p.Inaugu_Day as date)), month(cast(p.Inaugu_Day as date)), day(cast(p.Inaugu_Day as date)),"
                + " p.ERCon_Name, p.ERCon_Cell, p.ERCon_Tel "
                + " from Personel as p "
                + " left outer join BloodType as b on p.Type_ID = b.Type_ID"
                + " left outer join GSch as gs on gs.School_ID = p.School_ID"
                + " left outer join GDep as gd on gd.Dep_ID = p.Dep_ID "
                + " where p.P_ID = " + "'" + UserInfo.emp_pid + "'";
        
        try (Connection conn = SQLTools.MSSQL()) {
            try (ResultSet rs = conn.createStatement().executeQuery(sql);) {
                while (rs.next()) {
                    namech.setText(rs.getString(1));
                    nameen.setText(rs.getString(2));
                    nid.setText(rs.getString(3));
                    by.setText(rs.getString(4));
                    bm.setText(rs.getString(5));
                    bd.setText(rs.getString(6));
                    blood.setText(rs.getString(7));
                    cell.setText(rs.getString(8));
                    tel.setText(rs.getString(9));
                    address.setText(rs.getString(10));
                    school.setText(rs.getString(11));
                    dep.setText(rs.getString(12));
                    iy.setText(rs.getString(13));
                    im.setText(rs.getString(14));
                    id.setText(rs.getString(15));
                    ername.setText(rs.getString(16));
                    ercell.setText(rs.getString(17));
                    ertel.setText(rs.getString(18));
                }
            }
        }
    }

    public void set_data_s() throws Exception {
        String sql = "use MileStoneHRMS select top 1 a.Act_Name, year(cast(s.StartD  as date)),"
                + " month(cast(s.StartD  as date)), day(cast(s.StartD  as date)), year(cast(s.EndD  as date)),"
                + " month(cast(s.EndD  as date)), day(cast(s.EndD  as date)) "
                + " from PStatus as s left outer join Activity as a on a.Act_ID = s.Act_ID "
                + " where s.P_ID = " + "'" + UserInfo.emp_pid + "'"
                + " order by s.StartD Desc ";
        
        try (Connection conn = SQLTools.MSSQL()) {
            try (ResultSet rs = conn.createStatement().executeQuery(sql);) {
                while (rs.next()) {
                    status.setText(rs.getString(1));
                    sy.setText(rs.getString(2));
                    sm.setText(rs.getString(3));
                    sd.setText(rs.getString(4));
                    ey.setText(rs.getString(5));
                    em.setText(rs.getString(6));
                    ed.setText(rs.getString(7));
                }
            }
        }
    }

    @FXML
    public void btn_back() throws Exception {
        StageControll.open(MenuEController.class, "/View/MenuE.fxml");
        StageControll.close(PersonAP);
    }
}
