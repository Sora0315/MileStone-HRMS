package Controller;

import SelfTools.AuditLog;
import SelfTools.AutoCompleteComboBox;
import SelfTools.ExportTools;
import SelfTools.SQLTools;
import SelfTools.StageControll;
import SelfTools.StringVariation;
import java.io.File;
import java.io.FileOutputStream;
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
import org.apache.poi.xssf.usermodel.*;

/**
 * FXML Controller class
 * @author Sora
 */
public class MSalaryAdj2Controller implements Initializable {

    @FXML public AnchorPane SAP;
    @FXML public TextField id, name;
    @FXML public TextField sy, sm;
    @FXML public TextField ay, am, ad, fbasepay, abasepay;
    @FXML public ComboBox fsname1, fsname2, fsname3, asname1, asname2, asname3;
    @FXML public TextField fspay1, fspay2, fspay3, aspay1, aspay2, aspay3;
    @FXML public TextField ftotal, atotal, diff;
    @FXML public Button back, search, check, export, submit;
    public String[] ofsname = new String[3], oasname = new String[3], ofspay = new String[3], oaspay = new String[3];
    public String otime;
    public int ofround, oaround, fround, around;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String sql = "use MileStoneHRMS select sd.Type_Name from SdyType as sd ";
        TextField[] list = {id, ay, am, ad, fbasepay, abasepay, fspay1, aspay1, fspay2, aspay2, fspay3, aspay3, ftotal, atotal, diff};
        ComboBox[] clist = {fsname1, fsname2, fsname3, asname1, asname2, asname3};
        for (TextField items : list) {
            items.setDisable(true);
            StageControll.TextFieldhandle(items);
        }
        StageControll.TextFieldhandle(name);
        StageControll.TextFieldhandle(sy);
        StageControll.TextFieldhandle(sm);
        for (ComboBox items : clist) {
            try {
                SQLTools.comboboxSetItem(sql, items);
                StageControll.ComboBoxCtrl(items);
                items.setEditable(true);
            } catch (Exception e) {
            }
            AutoCompleteComboBox autoCompleteComboBox = new AutoCompleteComboBox(items);
            items.setDisable(true);
        }
        StageControll.Keydirect(abasepay, fsname1);
        StageControll.Keydirect(aspay1, fsname2);
        StageControll.Keydirect(aspay2, fsname3);
        StageControll.Keydirect(fspay1, asname1);
        StageControll.Keydirect(fspay2, asname2);
        StageControll.Keydirect(fspay3, asname3);
        StageControll.ButtonCtrl(back, name);
        StageControll.ButtonCtrl(search, ay);
        StageControll.ButtonCtrl(check, export);
        StageControll.ButtonCtrl(export, submit);
        StageControll.ButtonCtrl(submit, back);
        check.setDisable(true);
        export.setDisable(true);
        submit.setDisable(true);
    }

    public void set_id() {
        try {
            String sql = "use MileStoneHRMS select p.P_ID from Personel as p where p.Name_CH = ";
            id.setText(SQLTools.ValueGetId(sql, name));
        } catch (Exception e) {
        }
    }

    @FXML
    public void btn_search(MouseEvent event) throws Exception {
        if (!id.getText().isEmpty() || !sy.getText().isEmpty() || !sm.getText().isEmpty()) {
            search_and_setdata("year(cast(s.StartD as date))", ay);
            search_and_setdata("month(cast(s.StartD as date))", am);
            search_and_setdata("day(cast(s.StartD as date))", ad);
            otime = StringVariation.datecom(ay, am, ad);
            search_and_setdata("cast(s.Bef_BasePay as int)", fbasepay);
            search_and_setdata("cast(s.Aft_BasePay as int)", abasepay);
            String sql_fs = "use MileStoneHRMS select  sd.Type_Name  from BefSdy as bf \n"
                    + " left outer join SdyType as sd on bf.Type_ID = sd.Type_ID\n"
                    + " where bf.P_ID = " + "'" + id.getText() + "'"
                    + " and year(bf.StartD) = " + "'" + ay.getText() + "'" + " and month(bf.StartD) = " + "'" + am.getText() + "'";
            String sql_as = "use MileStoneHRMS select  sd.Type_Name  from AftSdy as af \n"
                    + " left outer join SdyType as sd on af.Type_ID = sd.Type_ID\n"
                    + " where af.P_ID = " + "'" + id.getText() + "'"
                    + " and year(af.StartD) = " + "'" + ay.getText() + "'" + " and month(af.StartD) = " + "'" + am.getText() + "'";
            ComboBox[] fs = {fsname1, fsname2, fsname3};
            ComboBox[] as = {asname1, asname2, asname3};
            ofround = search_and_setdata(sql_fs, fs, ofsname);
            oaround = search_and_setdata(sql_as, as, oasname);
            String sql_fspay = "use MileStoneHRMS select  cast(bf.Bef_Subsidy as int)  from BefSdy as bf \n"
                    + " left outer join SdyType as sd on bf.Type_ID = sd.Type_ID\n"
                    + " where bf.P_ID = " + "'" + id.getText() + "'"
                    + " and year(bf.StartD) = " + "'" + ay.getText() + "'" + " and month(bf.StartD) = " + "'" + am.getText() + "'";
            String sql_aspay = "use MileStoneHRMS select  cast(af.Aft_Subsidy as int)  from AftSdy as af \n"
                    + " left outer join SdyType as sd on af.Type_ID = sd.Type_ID\n"
                    + " where af.P_ID = " + "'" + id.getText() + "'"
                    + " and year(af.StartD) = " + "'" + ay.getText() + "'" + " and month(af.StartD) = " + "'" + am.getText() + "'";
            TextField[] fspay = {fspay1, fspay2, fspay3};
            TextField[] aspay = {aspay1, aspay2, aspay3};
            search_and_setdata(sql_fspay, fspay, ofspay);
            search_and_setdata(sql_aspay, aspay, oaspay);
            ftotal();
            atotal();
            search_and_setdata("cast(s.Difference as int)", diff);
            if (ay.getText().isEmpty() || am.getText().isEmpty() || ad.getText().isEmpty()) {
                NoticeController.noticecontent = "找不到資料唷！";
                StageControll.open(NoticeController.class, "/View/Notice.fxml");
            } else {
                TextField[] list = {ay, am, ad, fbasepay, abasepay};
                for (TextField items : list) {
                    items.setDisable(false);
                }
                check.setDisable(false);
            }
        } else {
            NoticeController.noticecontent = "嘿！找資料也得輸入必要資訊呀！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");
        }
    }

    public void search_and_setdata(String column, TextField target) throws Exception {
        String sql = "use MileStoneHRMS select " + column
                + " from SalaryAdjR as s where s.P_ID = " + "'" + id.getText() + "'"
                + " and year(cast(s.StartD as date)) = " + "'" + sy.getText() + "'"
                + " and month(cast(s.StartD as date)) = " + "'" + sm.getText() + "'";

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

    public int search_and_setdata(String sql, ComboBox[] list, String[] olist) throws Exception {
        try (Connection conn = SQLTools.MSSQL()) {
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                int i = 0;
                while (rs.next()) {
                    list[i].getSelectionModel().select(rs.getString(1));
                    list[i].setDisable(false);
                    olist[i] = rs.getString(1);
                    i = i + 1;
                }
                list[i].setDisable(false);
                return i;
            }
        }
    }

    public void search_and_setdata(String sql, TextField[] list, String[] olist) throws Exception {
        try (Connection conn = SQLTools.MSSQL()) {
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                int i = 0;
                while (rs.next()) {
                    list[i].setText(rs.getString(1));
                    list[i].setDisable(false);
                    olist[i] = rs.getString(1);
                    i = i + 1;
                }
                list[i].setDisable(false);
            }
        }
    }

    @FXML
    public void setfsname2() {
        if (!fspay1.getText().isEmpty()) {
            fsname2.setDisable(false);
            fspay2.setDisable(false);
        }
    }

    @FXML
    public void setfsname3() {
        if (!fspay2.getText().isEmpty()) {
            fsname3.setDisable(false);
            fspay3.setDisable(false);
        }
    }

    @FXML
    public void setasname2() {
        if (!aspay1.getText().isEmpty()) {
            asname2.setDisable(false);
            aspay2.setDisable(false);
        }
    }

    @FXML
    public void setasname3() {
        if (!aspay2.getText().isEmpty()) {
            asname3.setDisable(false);
            aspay3.setDisable(false);
        }
    }

    @FXML
    public void btn_checkdata(MouseEvent event) throws Exception {
        if (id.getText().isEmpty()
                || ay.getText().isEmpty() || am.getText().isEmpty() || ad.getText().isEmpty()) {
            NoticeController.noticecontent = "請輸入必要資料！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");
        } else {
            ComboBox[] fs = {fsname1, fsname2, fsname3};
            ComboBox[] as = {asname1, asname2, asname3};
            fround = set_rounds(fs);
            around = set_rounds(as);
            ftotal();
            atotal();
            diff();
            submit.setDisable(false);
            export.setDisable(false);
        }
    }

    public int set_rounds(ComboBox[] list) {
        int out = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i].getSelectionModel().getSelectedItem() == null) {
                out = i;
            }
        }
        return out;
    }

    @FXML
    public void btn_submit(MouseEvent event) throws Exception {
        Data_Save();
        StageControll.close(SAP);
        StageControll.open(MSalaryAdj2Controller.class, "/View/MSalaryAdj2.fxml");
    }

    public void Data_Save() throws Exception {
        String sql = "use MileStoneHRMS update SalaryAdjR set StartD = ? "
                + ", Bef_BasePay = ?, Aft_BasePay = ?, Difference = ? "
                + " where SalaryAdjR.P_ID = " + "'" + id.getText() + "'\n"
                + " and SalaryAdjR.StartD = " + "'" + otime + "'\n";

        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, StringVariation.datecom(ay, am, ad));
                SQLTools.emptyUnitSetNull(fbasepay, pstmt, 2);
                SQLTools.emptyUnitSetNull(abasepay, pstmt, 3);
                SQLTools.emptyUnitSetNull(diff, pstmt, 4);
                pstmt.execute();
                AuditLog.Audit("主管/HR-修正員工薪資調整紀錄", name, StringVariation.datecom(ay, am, ad));
            }

            ComboBox[] fsname = {fsname1, fsname2, fsname3};
            ComboBox[] asname = {asname1, asname2, asname3};
            TextField[] fspay = {fspay1, fspay2, fspay3};
            TextField[] aspay = {aspay1, aspay2, aspay3};
            String ask = "use MileStoneHRMS select sd.Type_ID from SdyType as sd where sd.Type_Name = ";

            if (ofround == 0) {
                for (int i = 0; i < fround; i++) {
                    String bef_0 = "use MileStoneHRMS insert into BefSdy(P_ID, StartD, Type_ID, Bef_Subsidy) "
                            + " values(?,?,?,?)";

                    try (PreparedStatement pstmt = conn.prepareStatement(bef_0)) {
                        pstmt.setString(1, id.getText());
                        pstmt.setString(2, StringVariation.datecom(ay, am, ad));
                        pstmt.setString(3, SQLTools.ValueGetId(ask, fsname[i].getSelectionModel().getSelectedItem().toString()));
                        SQLTools.emptyUnitSetNull(fspay[i].getText(), pstmt, 4);
                        pstmt.execute();
                    }
                }
            } else {
                for (int i = 0; i < fround; i++) {
                    if (ofsname[i] == null) {
                        String bef_0 = "use MileStoneHRMS insert into BefSdy(P_ID, StartD, Type_ID, Bef_Subsidy) "
                                + " values(?,?,?,?)";

                        try (PreparedStatement pstmt = conn.prepareStatement(bef_0)) {
                            pstmt.setString(1, id.getText());
                            pstmt.setString(2, StringVariation.datecom(ay, am, ad));
                            pstmt.setString(3, SQLTools.ValueGetId(ask, fsname[i].getSelectionModel().getSelectedItem().toString()));
                            SQLTools.emptyUnitSetNull(fspay[i].getText(), pstmt, 4);
                            pstmt.execute();
                        }
                    } else {
                        String bef = "use MileStoneHRMS update BefSdy set StartD = ?, Type_ID = ?, Bef_Subsidy = ? "
                                + " where P_ID = " + "'" + id.getText() + "' and StartD = " + "'" + otime + "'"
                                + " and Type_ID = " + "'" + SQLTools.ValueGetId(ask, ofsname[i]) + "'";

                        try (PreparedStatement pstmt = conn.prepareStatement(bef)) {
                            pstmt.setString(1, StringVariation.datecom(ay, am, ad));
                            pstmt.setString(2, SQLTools.ValueGetId(ask, fsname[i].getSelectionModel().getSelectedItem().toString()));
                            SQLTools.emptyUnitSetNull(fspay[i].getText(), pstmt, 3);
                            pstmt.execute();
                        }
                    }
                }
            }

            if (oaround == 0) {
                for (int i = 0; i < around; i++) {
                    String aft_0 = "use MileStoneHRMS insert into AftSdy(P_ID, StartD, Type_ID, Aft_Subsidy) "
                            + " values(?,?,?,?)";

                    try (PreparedStatement pstmt = conn.prepareStatement(aft_0)) {
                        pstmt.setString(1, id.getText());
                        pstmt.setString(2, StringVariation.datecom(ay, am, ad));
                        pstmt.setString(3, SQLTools.ValueGetId(ask, asname[i].getSelectionModel().getSelectedItem().toString()));
                        SQLTools.emptyUnitSetNull(aspay[i].getText(), pstmt, 4);
                        pstmt.execute();
                    }
                }
                AuditLog.Audit("主管/HR-修正員工薪資調整紀錄(津貼)", name, StringVariation.datecom(ay, am, ad));
            } else {
                for (int i = 0; i < around; i++) {
                    if (oasname[i] == null) {
                        String aft_0 = "use MileStoneHRMS insert into AftSdy(P_ID, StartD, Type_ID, Aft_Subsidy) "
                                + " values(?,?,?,?)";

                        try (PreparedStatement pstmt = conn.prepareStatement(aft_0)) {
                            pstmt.setString(1, id.getText());
                            pstmt.setString(2, StringVariation.datecom(ay, am, ad));
                            pstmt.setString(3, SQLTools.ValueGetId(ask, asname[i].getSelectionModel().getSelectedItem().toString()));
                            SQLTools.emptyUnitSetNull(aspay[i].getText(), pstmt, 4);
                            pstmt.execute();
                        }
                    } else {
                        String aft = "use MileStoneHRMS update AftSdy set StartD = ?, Type_ID = ?, Aft_Subsidy = ? "
                                + " where P_ID = " + "'" + id.getText() + "' and StartD = " + "'" + otime + "'"
                                + " and Type_ID = " + "'" + SQLTools.ValueGetId(ask, oasname[i]) + "'";

                        try (PreparedStatement pstmt = conn.prepareStatement(aft)) {
                            pstmt.setString(1, StringVariation.datecom(ay, am, ad));
                            pstmt.setString(2, SQLTools.ValueGetId(ask, asname[i].getSelectionModel().getSelectedItem().toString()));
                            SQLTools.emptyUnitSetNull(aspay[i].getText(), pstmt, 3);
                            pstmt.execute();
                        }
                    }
                }
                AuditLog.Audit("主管/HR-修正員工薪資調整紀錄(津貼)", name, StringVariation.datecom(ay, am, ad));
            }
        }
    }

    public void ftotal() {
        int a, b, c, d;
        a = SQLTools.textfieldConvertInt(fbasepay);
        b = SQLTools.textfieldConvertInt(fspay1);
        c = SQLTools.textfieldConvertInt(fspay2);
        d = SQLTools.textfieldConvertInt(fspay3);
        String tal = String.valueOf((a + b + c + d));
        ftotal.setText(tal);
    }

    public void atotal() {
        int a, b, c, d;
        a = SQLTools.textfieldConvertInt(abasepay);
        b = SQLTools.textfieldConvertInt(aspay1);
        c = SQLTools.textfieldConvertInt(aspay2);
        d = SQLTools.textfieldConvertInt(aspay3);
        String tal = String.valueOf((a + b + c + d));
        atotal.setText(tal);
    }

    public void diff() {
        diff.setText(String.valueOf((Integer.parseInt(atotal.getText()) - Integer.parseInt(ftotal.getText()))));
    }

    @FXML
    public void exportexcel(MouseEvent event) throws Exception {
        String filename = id.getText() + " " + ay.getText() + StringVariation.right(("00" + am.getText()), 2) + "薪資調整通知書-修正.xlsx";
        File outputfile = new File(ExportTools.path2, filename);
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(ExportTools.path1 + "/薪資調整通知書範本.xlsx");
            XSSFSheet sheet = workbook.getSheet("薪資調整通知書");

            ExportTools.row(sheet, 3).getCell(2).setCellValue(id.getText());
            ExportTools.row(sheet, 3).getCell(7).setCellValue(name.getText());
            ExportTools.row(sheet, 6).getCell(1).setCellValue(ay.getText());
            ExportTools.row(sheet, 6).getCell(3).setCellValue(am.getText());
            ExportTools.row(sheet, 6).getCell(5).setCellValue(ad.getText());
            if (!fbasepay.getText().isEmpty()) {
                ExportTools.row(sheet, 11).getCell(2).setCellValue(fbasepay.getText());
            }
            if (!abasepay.getText().isEmpty()) {
                ExportTools.row(sheet, 11).getCell(7).setCellValue(abasepay.getText());
            }
            if (fsname1.getSelectionModel().getSelectedItem() != null) {
                ExportTools.row(sheet, 12).getCell(0).setCellValue(fsname1.getSelectionModel().getSelectedItem().toString());
                ExportTools.row(sheet, 12).getCell(2).setCellValue(fspay1.getText());
            }
            if (asname1.getSelectionModel().getSelectedItem() != null) {
                ExportTools.row(sheet, 12).getCell(5).setCellValue(asname1.getSelectionModel().getSelectedItem().toString());
                ExportTools.row(sheet, 12).getCell(7).setCellValue(aspay1.getText());
            }
            if (fsname2.getSelectionModel().getSelectedItem() != null) {
                ExportTools.row(sheet, 13).getCell(0).setCellValue(fsname2.getSelectionModel().getSelectedItem().toString());
                ExportTools.row(sheet, 13).getCell(2).setCellValue(fspay2.getText());
            }
            if (asname2.getSelectionModel().getSelectedItem() != null) {
                ExportTools.row(sheet, 13).getCell(5).setCellValue(asname2.getSelectionModel().getSelectedItem().toString());
                ExportTools.row(sheet, 13).getCell(7).setCellValue(aspay2.getText());
            }
            if (fsname3.getSelectionModel().getSelectedItem() != null) {
                ExportTools.row(sheet, 14).getCell(0).setCellValue(fsname3.getSelectionModel().getSelectedItem().toString());
                ExportTools.row(sheet, 14).getCell(2).setCellValue(fspay3.getText());
            }
            if (asname3.getSelectionModel().getSelectedItem() != null) {
                ExportTools.row(sheet, 14).getCell(5).setCellValue(asname3.getSelectionModel().getSelectedItem().toString());
                ExportTools.row(sheet, 14).getCell(7).setCellValue(aspay3.getText());
            }
            if (!ftotal.getText().isEmpty()) {
                ExportTools.row(sheet, 17).getCell(2).setCellValue(ftotal.getText());
            }
            if (!atotal.getText().isEmpty()) {
                ExportTools.row(sheet, 17).getCell(7).setCellValue(atotal.getText());
            }
            ExportTools.row(sheet, 20).getCell(3).setCellValue(diff.getText());
            //Save file
            try (FileOutputStream fileout = new FileOutputStream(outputfile)) {
                workbook.write(fileout);
                fileout.close();
                NoticeController.noticecontent = "您的匯出已完成！";
                NoticeController.noticecontent2 = "請至匯出資料夾確認。";
                StageControll.open(NoticeController.class, "/View/Notice.fxml", true);
            } catch (Exception e) {
            }
        } catch (Exception e) {
            NoticeController.noticecontent = "範本或匯出資料夾不存在！";
            NoticeController.noticecontent2 = "請先至\"匯出與下載設定\"進行設置。";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");
        }
    }

    @FXML
    public void btn_back() throws Exception {
        StageControll.close(SAP);
        StageControll.open(MSalaryAdjController.class, "/View/MSalaryAdj.fxml");
    }
}
