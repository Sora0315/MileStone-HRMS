package Controller;

import SelfTools.AuditLog;
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
import SelfTools.AutoCompleteComboBox;
import SelfTools.ExportTools;
import SelfTools.SQLTools;
import SelfTools.StageControll;
import SelfTools.StringVariation;

/**
 * FXML Controller class
 *
 * @author Sora
 */
public class MSalary2Controller implements Initializable {

    @FXML
    public AnchorPane SAP;
    @FXML
    public TextField id, name;
    @FXML
    public TextField syy, smm, py, pm, pd, sy, sm, sd, ey, em, ed;
    @FXML
    public TextField basepay;
    @FXML
    public TextField spay1, spay2, spay3;
    @FXML
    public ComboBox sname1, sname2, sname3, btype1, btype2;
    @FXML
    public TextField bpay1, bpay2, lh, total;
    @FXML
    public Button back, search, check, export, submit;
    public String[] sname = new String[3];
    public String[] spay = new String[3];
    public String[] bname = new String[2];
    public String[] bpay = new String[2];
    public String odate;
    public int oround, round, obround, bround;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TextField[] dislist = {id, py, pm, pd, sy, sm, sd, ey, em, ed, basepay, spay1, spay2, spay3, bpay1, bpay2, lh, total};
        ComboBox[] clist = {sname1, sname2, sname3, btype1, btype2};
        for (TextField items : dislist) {
            items.setDisable(true);
            StageControll.TextFieldhandle(items);
        }
        StageControll.TextFieldhandle(name);
        StageControll.TextFieldhandle(syy);
        StageControll.TextFieldhandle(smm);
        for (ComboBox items : clist) {
            items.setEditable(true);
            items.setDisable(true);
            StageControll.ComboBoxCtrl(items);
            AutoCompleteComboBox autoCompleteComboBox = new AutoCompleteComboBox(items);
        }
        StageControll.Keydirect(basepay, sname1);
        StageControll.Keydirect(spay1, sname2);
        StageControll.Keydirect(spay2, sname3);
        StageControll.Keydirect(spay3, btype1);
        StageControll.Keydirect(bpay1, btype2);
        Button[] btn = {check, export, submit};
        for (Button items : btn) {
            items.setDisable(true);
            StageControll.Buttonhandle(items);
        }
        StageControll.Buttonhandle(back);
        StageControll.Buttonhandle(search);
        String sql = "use MileStoneHRMS select t.Type_Name from BonusType as t";
        try {
            SQLTools.comboboxSetItem(sql, btype1);
            SQLTools.comboboxSetItem(sql, btype2);
        } catch (Exception e) {
        }
    }

    public void btn_search(MouseEvent event) throws Exception {
        if (!id.getText().isEmpty() || !syy.getText().isEmpty() || !smm.getText().isEmpty()) {
            search_and_setdata("year(cast(s.PayDay as date))", py);
            search_and_setdata("month(cast(s.PayDay as date))", pm);
            search_and_setdata("day(cast(s.PayDay as date))", pd);
            odate = StringVariation.datecom(py, pm, pd);
            search_and_setdata("year(cast(s.Cal_StartD as date))", sy);
            search_and_setdata("month(cast(s.Cal_StartD as date))", sm);
            search_and_setdata("day(cast(s.Cal_StartD as date))", sd);
            search_and_setdata("year(cast(s.Cal_EndD as date))", ey);
            search_and_setdata("month(cast(s.Cal_EndD as date))", em);
            search_and_setdata("day(cast(s.Cal_EndD as date))", ed);
            search_and_setdata("cast(s.BasePay as int)", basepay);
            ComboBox[] clist = {sname1, sname2, sname3};
            search_and_setdata("st.Type_Name", clist, sname);
            for (ComboBox items : clist) {
                SQLTools.comboboxSetItem("use MileStoneHRMS select s.Type_Name from SdyType as s ", items);
            }
            TextField[] tlist = {spay1, spay2, spay3};
            search_and_setdata("cast(sd.Subsidy as int)", tlist, spay);

            ComboBox[] blist = {btype1, btype2};
            search_and_setdata_b("bt.Type_Name", blist, bname);

            TextField[] btlist = {bpay1, bpay2};
            search_and_setdata_b("cast(b.Bonus as int)", btlist, bpay);

            search_and_setdata("cast(s.LH_Ins as int)", lh);
            search_and_setdata("cast(s.NetTotal as int)", total);
            if (py.getText().isEmpty() || pm.getText().isEmpty() || pd.getText().isEmpty()) {
                NoticeController.noticecontent = "找不到資料唷！";
                StageControll.open(NoticeController.class, "/View/Notice.fxml");
            } else {
                TextField[] list = {py, pm, pd, sy, sm, sd, ey, em, ed, basepay, bpay1, bpay2, lh};
                for (TextField items : list) {
                    items.setDisable(false);
                }
                btype1.setDisable(false);
                btype2.setDisable(false);
                check.setDisable(false);
            }
        } else {
            NoticeController.noticecontent = "嘿！找資料也得輸入必要資訊呀！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");
        }
    }

    public void search_and_setdata(String column, TextField target) throws Exception {
        String sql = "use MileStoneHRMS select " + column
                + " from SalaryR as s \t"
                + " where s.P_ID = " + "'" + id.getText() + "' \t"
                + " and year(cast(s.Cal_StartD as date)) = " + "'" + syy.getText() + "' \t"
                + " and month(cast(s.Cal_StartD as date)) = " + "'" + smm.getText() + "' \t";

        try (Connection conn = SQLTools.MSSQL()) {
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                String output = null;
                while (rs.next()) {
                    output = rs.getString(1);
                }
                if (output != null) {
                    target.setText(output);
                } else {
                    target.clear();
                }
            }
        }
    }

    public void search_and_setdata(String column, ComboBox[] list, String[] list2) throws Exception {
        String sql = "use MileStoneHRMS select " + column + " from Sdy as sd "
                + " left outer join SdyType as st on st.Type_ID = sd.Type_ID "
                + " left outer join SalaryR as s on s.P_ID = sd.P_ID and s.PayDay = sd.PayDay "
                + " where s.P_ID = " + "'" + id.getText() + "' \t"
                + " and year(cast(s.Cal_StartD as date)) = " + "'" + syy.getText() + "' \t"
                + " and month(cast(s.Cal_StartD as date)) = " + "'" + smm.getText() + "' \t";

        try (Connection conn = SQLTools.MSSQL()) {
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                int i = 0;
                while (rs.next()) {
                    list[i].getSelectionModel().select(rs.getString(1));
                    list[i].setDisable(false);
                    list2[i] = rs.getString(1);
                    i = i + 1;
                }
                oround = i;
                if (i < list.length) {
                    list[i].setDisable(false);
                }
            }
        }
    }

    public void search_and_setdata_b(String column, ComboBox[] list, String[] list2) throws Exception {
        String sql = "use MileStoneHRMS select " + column + " from Bonus as b "
                + " left outer join BonusType as bt on bt.Type_ID = b.Type_ID "
                + " left outer join SalaryR as s on s.P_ID = b.P_ID and s.PayDay = b.PayDay "
                + " where s.P_ID = " + "'" + id.getText() + "' \t"
                + " and year(cast(s.Cal_StartD as date)) = " + "'" + syy.getText() + "' \t"
                + " and month(cast(s.Cal_StartD as date)) = " + "'" + smm.getText() + "' \t";

        try (Connection conn = SQLTools.MSSQL()) {
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                int i = 0;
                while (rs.next()) {
                    list[i].getSelectionModel().select(rs.getString(1));
                    list[i].setDisable(false);
                    list2[i] = rs.getString(1);
                    i = i + 1;
                }
                obround = i;
                if (i < list.length) {
                    list[i].setDisable(false);
                }
            }
        }
    }

    public void search_and_setdata_b(String column, TextField[] list, String[] list2) throws Exception {
        String sql = "use MileStoneHRMS select " + column + " from Bonus as b "
                + " left outer join BonusType as bt on bt.Type_ID = b.Type_ID "
                + " left outer join SalaryR as s on s.P_ID = b.P_ID and s.PayDay = b.PayDay "
                + " where s.P_ID = " + "'" + id.getText() + "' \t"
                + " and year(cast(s.Cal_StartD as date)) = " + "'" + syy.getText() + "' \t"
                + " and month(cast(s.Cal_StartD as date)) = " + "'" + smm.getText() + "' \t";

        try (Connection conn = SQLTools.MSSQL()) {
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                int i = 0;
                while (rs.next()) {
                    list[i].setText(rs.getString(1));
                    list[i].setDisable(false);
                    list2[i] = rs.getString(1);
                    i = i + 1;
                }
                if (i < list.length) {
                    list[i].setDisable(false);
                }
            }
        }
    }

    public void search_and_setdata(String column, TextField[] list, String[] list2) throws Exception {
        String sql = "use MileStoneHRMS select " + column + " from Sdy as sd "
                + " left outer join SdyType as st on st.Type_ID = sd.Type_ID "
                + " left outer join SalaryR as s on s.P_ID = sd.P_ID and s.PayDay = sd.PayDay "
                + " where s.P_ID = " + "'" + id.getText() + "' \t"
                + " and year(cast(s.Cal_StartD as date)) = " + "'" + syy.getText() + "' \t"
                + " and month(cast(s.Cal_StartD as date)) = " + "'" + smm.getText() + "' \t";

        try (Connection conn = SQLTools.MSSQL()) {
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                int i = 0;
                while (rs.next()) {
                    list[i].setText(rs.getString(1));
                    list[i].setDisable(false);
                    list2[i] = rs.getString(1);
                    i = i + 1;
                }
                if (i < list.length) {
                    list[i].setDisable(false);
                }
            }
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
    public void setsname2() throws Exception {
        if (!spay1.getText().isEmpty()) {
            sname2.setDisable(false);
            spay2.setDisable(false);
        }
    }

    @FXML
    public void setsname3() throws Exception {
        if (!spay2.getText().isEmpty()) {
            sname3.setDisable(false);
            spay3.setDisable(false);
        }
    }

    @FXML
    public void setbtype2() throws Exception {
        if (!bpay1.getText().isEmpty()) {
            btype2.setDisable(false);
            bpay2.setDisable(false);
        }
    }

    @FXML
    public void btn_checkdata(MouseEvent event) throws Exception {
        if (id.getText().isEmpty()
                || py.getText().isEmpty() || pm.getText().isEmpty() || pd.getText().isEmpty()) {
            NoticeController.noticecontent = "請輸入必要資料！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");
        } else {
            set_rounds();
            set_brounds();
            total();
            submit.setDisable(false);
            export.setDisable(false);
        }
    }

    public int set_rounds() {
        ComboBox[] list = {sname1, sname2, sname3};
        if (sname1.getSelectionModel().isEmpty()) {
            round = 0;
        } else {
            for (int i = 0; i < list.length; i++) {
                if (list[i].getSelectionModel().getSelectedItem() == null) {
                    round = i;
                    break;
                }
            }
        }
        return round;
    }

    public int set_brounds() {
        if (btype1.getSelectionModel().isEmpty()) {
            bround = 0;
        } else if (btype2.getSelectionModel().isEmpty()) {
            bround = 1;
        } else {
            bround = 2;
        }
        return bround;
    }

    @FXML
    public void btn_submit(MouseEvent event) throws Exception {
        Data_Save();
        StageControll.close(SAP);
        StageControll.open(MSalary2Controller.class, "/View/MSalary2.fxml");
    }

    public void Data_Save() throws Exception {
        String sql = "use MileStoneHRMS update SalaryR "
                + " set PayDay = ?, Cal_StartD = ?, Cal_EndD = ?, BasePay = ? "
                + " , LH_Ins = ?, NetTotal = ? "
                + " where SalaryR.P_ID = " + "'" + id.getText() + "'\n"
                + " and PayDay = " + "'" + odate + "'";

        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, StringVariation.datecom(py, pm, pd));
                if (!sy.getText().isEmpty() || !sm.getText().isEmpty() || !sd.getText().isEmpty()) {
                    pstmt.setString(2, StringVariation.datecom(sy, sm, sd));
                } else {
                    pstmt.setNull(2, java.sql.Types.VARCHAR);
                }
                if (!ey.getText().isEmpty() || !em.getText().isEmpty() || !ed.getText().isEmpty()) {
                    pstmt.setString(3, StringVariation.datecom(ey, em, ed));
                } else {
                    pstmt.setNull(3, java.sql.Types.VARCHAR);
                }
                SQLTools.emptyUnitSetNull(basepay, pstmt, 4);
                SQLTools.emptyUnitSetNull(lh, pstmt, 5);
                SQLTools.emptyUnitSetNull(total, pstmt, 6);
                pstmt.execute();
                AuditLog.Audit("主管/HR-修正員工薪資紀錄", name, StringVariation.datecom(py, pm, pd));
            }

            ComboBox[] clist = {sname1, sname2, sname3};
            TextField[] tlist = {spay1, spay2, spay3};
            String ask = "use MileStoneHRMS select st.Type_ID from SdyType as st where st.Type_Name = ";
            if (oround == 0) {
                for (int i = 0; i < round; i++) {
                    String sql0 = "use MileStoneHRMS insert into Sdy(P_ID, PayDay, Type_ID, Subsidy) "
                            + " values(?,?,?,?) ";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql0)) {
                        pstmt.setString(1, id.getText());
                        pstmt.setString(2, StringVariation.datecom(py, pm, pd));
                        pstmt.setString(3, SQLTools.ValueGetId(ask, clist[i]));
                        SQLTools.emptyUnitSetNull(tlist[i].getText(), pstmt, 4);
                        pstmt.execute();
                        AuditLog.Audit("主管/HR-修正員工薪資紀錄(津貼)", name, StringVariation.datecom(py, pm, pd));
                    }
                }
            } else {
                for (int i = 0; i < round; i++) {
                    if (sname[i] == null) {
                        if (round > 0) {
                            String sql0 = "use MileStoneHRMS insert into Sdy(P_ID, PayDay, Type_ID, Subsidy) "
                                    + " values(?,?,?,?) ";
                            try (PreparedStatement pstmt = conn.prepareStatement(sql0)) {
                                pstmt.setString(1, id.getText());
                                pstmt.setString(2, StringVariation.datecom(py, pm, pd));
                                pstmt.setString(3, SQLTools.ValueGetId(ask, clist[i]));
                                SQLTools.emptyUnitSetNull(tlist[i].getText(), pstmt, 4);
                                pstmt.execute();
                            }
                        }
                    } else if (round > 0) {
                        String sql2 = "use MileStoneHRMS update Sdy set Type_ID = ?, Subsidy = ?\n"
                                + " from Sdy as sd left outer join SalaryR as s on s.P_ID = sd.P_ID\n"
                                + " left outer join SdyType as st on st.Type_ID = sd.Type_ID "
                                + " where sd.P_ID = " + "'" + id.getText() + "'"
                                + " and sd.PayDay = " + "'" + odate + "'"
                                + " and st.Type_Name = " + "'" + sname[i] + "'"
                                + " and sd.Subsidy = " + "'" + spay[i] + "'";
                        try (PreparedStatement pstmt = conn.prepareStatement(sql2)) {
                            pstmt.setString(1, SQLTools.ValueGetId(ask, clist[i]));
                            SQLTools.emptyUnitSetNull(tlist[i].getText(), pstmt, 2);
                            pstmt.execute();
                            AuditLog.Audit("主管/HR-修正員工薪資紀錄(津貼)", name, StringVariation.datecom(py, pm, pd));
                        }
                    } else {
                        NoticeController.noticecontent = "如果要刪除津貼資料，洽系統管理員。";
                        StageControll.open(NoticeController.class, "/View/Notice.fxml");
                    }
                }
            }

            ComboBox[] blist = {btype1, btype2};
            TextField[] btlist = {bpay1, bpay2};
            String ask_b = "use MileStoneHRMS select bt.Type_ID from BonusType as bt where bt.Type_Name = ";
            if (obround == 0) {
                for (int i = 0; i < bround; i++) {
                    String sql0 = "use MileStoneHRMS insert into Bonus(P_ID, PayDay, Type_ID, Bonus) "
                            + " values(?,?,?,?) ";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql0)) {
                        pstmt.setString(1, id.getText());
                        pstmt.setString(2, StringVariation.datecom(py, pm, pd));
                        pstmt.setString(3, SQLTools.ValueGetId(ask_b, blist[i]));
                        SQLTools.emptyUnitSetNull(btlist[i].getText(), pstmt, 4);
                        pstmt.execute();
                        AuditLog.Audit("主管/HR-修正員工薪資紀錄(獎金)", name, StringVariation.datecom(py, pm, pd));
                    }
                }
            } else {
                for (int i = 0; i < bround; i++) {
                    if (bname[i] == null) {
                        if (bround > 0) {
                            String sql0 = "use MileStoneHRMS insert into Bonus(P_ID, PayDay, Type_ID, Bonus) "
                                    + " values(?,?,?,?) ";
                            try (PreparedStatement pstmt = conn.prepareStatement(sql0)) {
                                pstmt.setString(1, id.getText());
                                pstmt.setString(2, StringVariation.datecom(py, pm, pd));
                                pstmt.setString(3, SQLTools.ValueGetId(ask_b, blist[i]));
                                SQLTools.emptyUnitSetNull(btlist[i].getText(), pstmt, 4);
                                pstmt.execute();
                            }
                        }
                    } else if (bround > 0) {
                        String sql2 = "use MileStoneHRMS update Bonus set Type_ID = ?, Bonus = ?\n"
                                + " from Bonus as b left outer join SalaryR as s on s.P_ID = b.P_ID\n"
                                + " left outer join BonusType as bt on bt.Type_ID = b.Type_ID "
                                + " where b.P_ID = " + "'" + id.getText() + "'"
                                + " and b.PayDay = " + "'" + odate + "'"
                                + " and bt.Type_Name = " + "'" + bname[i] + "'"
                                + " and b.Bonus = " + "'" + bpay[i] + "'";

                        try (PreparedStatement pstmt = conn.prepareStatement(sql2)) {
                            pstmt.setString(1, SQLTools.ValueGetId(ask_b, blist[i]));
                            SQLTools.emptyUnitSetNull(btlist[i].getText(), pstmt, 2);
                            pstmt.execute();
                            AuditLog.Audit("主管/HR-修正員工薪資紀錄(獎金)", name, StringVariation.datecom(py, pm, pd));
                        }
                    } else {
                        NoticeController.noticecontent = "如果要刪除津貼資料，洽系統管理員。";
                        StageControll.open(NoticeController.class, "/View/Notice.fxml");
                    }
                }
            }
        }
    }

    public void total() {
        int a, b, c, d, e, f, g;
        a = SQLTools.textfieldConvertInt(basepay);
        b = SQLTools.textfieldConvertInt(spay1);
        c = SQLTools.textfieldConvertInt(spay2);
        d = SQLTools.textfieldConvertInt(spay3);
        e = SQLTools.textfieldConvertInt(bpay1);
        g = SQLTools.textfieldConvertInt(bpay2);
        f = SQLTools.textfieldConvertInt(lh);
        total.setText(String.valueOf((a + b + c + d + e + g - f)));
    }

    @FXML
    public void exportexcel(MouseEvent event) throws Exception {
        try {
            String filename, yy, mm;
            if (pm.getText().equals("1") || pm.getText().equals("01")) {
                mm = "12";
                yy = String.valueOf(Integer.parseInt(py.getText()) - 1);
            } else {
                mm = StringVariation.right(("00" + String.valueOf((Integer.parseInt(pm.getText()) - 1))), 2);
                yy = py.getText();
            }
            filename = id.getText() + " (" + yy + mm + "薪資明細-修正).xlsx";
            File outputfile = new File(ExportTools.path2, filename);
            XSSFWorkbook workbook = new XSSFWorkbook(ExportTools.path1 + "/薪資明細空白範本.xlsx");
            XSSFSheet sheet = workbook.getSheet("薪資明細");
            ExportTools.row(sheet, 2).getCell(2).setCellValue(py.getText());
            ExportTools.row(sheet, 2).getCell(5).setCellValue(pm.getText());
            ExportTools.row(sheet, 2).getCell(7).setCellValue(pd.getText());
            ExportTools.row(sheet, 3).getCell(3).setCellValue(sy.getText());
            ExportTools.row(sheet, 3).getCell(5).setCellValue(sm.getText());
            ExportTools.row(sheet, 3).getCell(7).setCellValue(sd.getText());
            ExportTools.row(sheet, 3).getCell(10).setCellValue(ey.getText());
            ExportTools.row(sheet, 3).getCell(12).setCellValue(em.getText());
            ExportTools.row(sheet, 3).getCell(14).setCellValue(ed.getText());
            ExportTools.row(sheet, 4).getCell(2).setCellValue(id.getText());
            ExportTools.row(sheet, 5).getCell(2).setCellValue(name.getText());
            ExportTools.row(sheet, 9).getCell(6).setCellValue(basepay.getText());
            if (sname1.getSelectionModel().getSelectedItem() != null) {
                ExportTools.row(sheet, 10).getCell(1).setCellValue(sname1.getSelectionModel().getSelectedItem().toString());
                ExportTools.row(sheet, 10).getCell(6).setCellValue(spay1.getText());
            }
            if (sname2.getSelectionModel().getSelectedItem() != null) {
                ExportTools.row(sheet, 11).getCell(1).setCellValue(sname2.getSelectionModel().getSelectedItem().toString());
                ExportTools.row(sheet, 11).getCell(6).setCellValue(spay2.getText());
            }
            if (sname3.getSelectionModel().getSelectedItem() != null) {
                ExportTools.row(sheet, 12).getCell(1).setCellValue(sname3.getSelectionModel().getSelectedItem().toString());
                ExportTools.row(sheet, 12).getCell(6).setCellValue(spay3.getText());
            }
            if (btype1.getSelectionModel().getSelectedItem() != null) {
                ExportTools.row(sheet, 13).getCell(1).setCellValue(btype1.getSelectionModel().getSelectedItem().toString());
                ExportTools.row(sheet, 13).getCell(6).setCellValue(bpay1.getText());
            }
            if (btype2.getSelectionModel().getSelectedItem() != null) {
                ExportTools.row(sheet, 14).getCell(1).setCellValue(btype2.getSelectionModel().getSelectedItem().toString());
                ExportTools.row(sheet, 14).getCell(6).setCellValue(bpay2.getText());
            }
            ExportTools.row(sheet, 15).getCell(1).setCellValue("勞健保個人負擔額");
            ExportTools.row(sheet, 15).getCell(6).setCellValue("-" + lh.getText());
            ExportTools.row(sheet, 17).getCell(6).setCellValue(total.getText());
            //Save file
            try (FileOutputStream fileout = new FileOutputStream(outputfile)) {
                workbook.write(fileout);
            }
            NoticeController.noticecontent = "您的匯出已完成！";
            NoticeController.noticecontent2 = "請至匯出資料夾確認。";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");
        } catch (Exception e) {
            NoticeController.noticecontent = "範本或匯出資料夾不存在！";
            NoticeController.noticecontent2 = "請先至\"匯出與下載設定\"進行設置。";
            StageControll.open(NoticeController.class, "/View/Notice.fxml", true);
        }
    }

    @FXML
    public void btn_back() throws Exception {
        StageControll.close(SAP);
        StageControll.open(MSalaryController.class, "/View/MSalary.fxml");
    }
}
