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
 * @author Sora
 */
public class SalaryRController implements Initializable {
    
    @FXML public AnchorPane SAP;
    @FXML public TextField id, py, pm, pd, sy, sm, sd, ey, em, ed;
    @FXML public TextField basepay, spay1, spay2, spay3, bpay1, bpay2, lh, total;
    @FXML public ComboBox name, btype1, btype2, sname1, sname2, sname3;
    @FXML public Button back, confirm, export, submit;
    public int rounds, brounds;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setDisable(true);
        sname2.setDisable(true);
        spay2.setDisable(true);
        sname3.setDisable(true);
        spay3.setDisable(true);
        btype2.setDisable(true);
        bpay2.setDisable(true);
        total.setDisable(true);
        export.setDisable(true);
        submit.setDisable(true);
        ComboBox [] list = {sname1, sname2, sname3};
        String sql_name = "use MileStoneHRMS select distinct p.Name_CH from Personel as p "
                + " left join PStatus as ps on ps.P_ID = p.P_ID left join Activity as a on a.Act_ID = ps.Act_ID\n"
                + " where a.Act_Name like '在職' and ps.EndD is null ";
        String sql ="use MileStoneHRMS select t.Type_Name from BonusType as t ";
        String sql2 = "use MileStoneHRMS select s.Type_Name from SdyType as s ";
        try{
            SQLTools.SqlGetItem(sql_name, name);
            SQLTools.SqlGetItem(sql, btype1);
            SQLTools.SqlGetItem(sql, btype2);            
            for(ComboBox items : list){
                SQLTools.SqlGetItem(sql2,items);
            }   
        }
        catch(Exception e){
        }
        ComboBox [] cball = {name, btype1, btype2, sname1, sname2, sname3};
        for(ComboBox items : cball){
            items.setEditable(true);
            StageControll.ComboBoxCtrl(items);
            AutoCompleteComboBox autoCompleteComboBox = new AutoCompleteComboBox(items);
        }
        StageControll.Keydirect(back, name);
        StageControll.Keydirect(spay1, btype1);
        StageControll.Keydirect(bpay1, btype2);
        StageControll.Keydirect(basepay, sname1);
        StageControll.Keydirect(spay1, sname2);
        StageControll.Keydirect(spay2, sname3);
        TextField [] tfall ={id, py, pm, pd, sy, sm, sd, ey, em, ed
                , basepay, spay1, spay2, spay3, bpay1, bpay2, lh, total};
        for(TextField items : tfall){
            StageControll.TextFieldhandle(items);
        }
        StageControll.ButtonCtrl(back, name);
        StageControll.ButtonCtrl(confirm, export);
        StageControll.ButtonCtrl(export, submit);
        StageControll.ButtonCtrl(submit, back);
        StageControll.Keydirect(submit, back);
        StageControll.Keydirect(confirm, export);
        StageControll.Keydirect(export, submit);
        name.setOnAction(e -> set_id());
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
    public void setsname2(){
        if(!spay1.getText().isEmpty()){
            sname2.setDisable(false);
            spay2.setDisable(false);
        }
    }
    
    @FXML
    public void setsname3(){
        if(!spay2.getText().isEmpty()){
            sname3.setDisable(false);
            spay3.setDisable(false);
        }
    }
    
    @FXML
    public void setbtype2(){
        if(!bpay1.getText().isEmpty()){
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
            String sql = "use MileStoneHRMS select s.P_ID from SalaryR as s where s.P_ID = "
                    + "'" + id.getText() + "'" + " and s.PayDay = "
                    + "'" + StringVariation.datecom(py, pm, pd) + "'";
            try (Connection conn = SQLTools.MSSQL()) {
                try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                    if (rs.next()) {
                        NoticeController.noticecontent = "資料已存在！";
                        StageControll.open(NoticeController.class, "/View/Notice.fxml");
                    } else {
                        submit.setDisable(false);
                        export.setDisable(false);
                    }
                }
            }
        }       
    }
    
    @FXML
    public void btn_submit(MouseEvent event) throws Exception{
        Data_Save();
        StageControll.close(SAP);
        StageControll.open(SalaryRController.class, "/View/SalaryR.fxml");
    }
    
    public void Data_Save() throws Exception{
        String sql = "use MileStoneHRMS insert into SalaryR(P_ID, PayDay, Cal_StartD, Cal_EndD, BasePay, LH_Ins, NetTotal)"
                + " values(?,?,?,?,?,?,?)";

        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id.getText());
                pstmt.setString(2, StringVariation.datecom(py, pm, pd));
                if (!sy.getText().isEmpty() || !sm.getText().isEmpty() || !sd.getText().isEmpty()) {
                    pstmt.setString(3, StringVariation.datecom(sy, sm, sd));
                } else {
                    pstmt.setNull(3, java.sql.Types.VARCHAR);
                }
                if (!ey.getText().isEmpty() || !em.getText().isEmpty() || !ed.getText().isEmpty()) {
                    pstmt.setString(4, StringVariation.datecom(ey, em, ed));
                } else {
                    pstmt.setNull(4, java.sql.Types.VARCHAR);
                }
                SQLTools.emptyslotsetnull(basepay, pstmt, 5);
                SQLTools.emptyslotsetnull(lh, pstmt, 6);
                SQLTools.emptyslotsetnull(total, pstmt, 7);
                pstmt.execute();
                AuditLog.Audit("主管/HR-員工薪資紀錄登錄", name);
            }
            
            if (rounds > 0) {
                for (int i = 0; i < rounds; i++) {
                    ComboBox[] clist = {sname1, sname2, sname3};
                    TextField[] tlist = {spay1, spay2, spay3};
                    String ask = "use MileStoneHRMS select s.Type_ID from SdyType as s where s.Type_Name = ";
                    String sql2 = "use MileStoneHRMS insert into Sdy(P_ID, PayDay, Type_ID, Subsidy)"
                            + " values(?,?,?,?)";

                    try (PreparedStatement pstmt = conn.prepareStatement(sql2)) {
                        pstmt.setString(1, id.getText());
                        pstmt.setString(2, StringVariation.datecom(py, pm, pd));
                        pstmt.setString(3, SQLTools.ValueGetId(ask, clist[i]));
                        pstmt.setString(4, tlist[i].getText());
                        pstmt.execute();
                    }
                }
                if (sname1 != null) {
                    AuditLog.Audit("主管/HR-員工薪資紀錄登錄(津貼)", name);
                }
            }
            System.out.println(brounds);
        
            if (brounds > 0) {
                for (int i = 0; i < brounds; i++) {
                    ComboBox[] b_clist = {btype1, btype2};
                    TextField[] b_tlist = {bpay1, bpay2};
                    String b_ask = "use MileStoneHRMS select b.Type_ID from BonusType as b where b.Type_Name = ";
                    String sql3 = "use MileStoneHRMS insert into Bonus(P_ID, PayDay, Type_ID, Bonus)"
                            + " values(?,?,?,?)";

                    try (PreparedStatement pstmt = conn.prepareStatement(sql3)) {
                        pstmt.setString(1, id.getText());
                        pstmt.setString(2, StringVariation.datecom(py, pm, pd));
                        pstmt.setString(3, SQLTools.ValueGetId(b_ask, b_clist[i]));
                        pstmt.setString(4, b_tlist[i].getText());
                        pstmt.execute();
                    }
                }
                if (sname1 != null) {
                    AuditLog.Audit("主管/HR-員工薪資紀錄登錄(獎金)", name);
                }
            }    
        }
    }
    
    public void total(){
        int a, b, c, d, e, f, g;
        a = SQLTools.txtnotemptytoint(basepay);
        b = SQLTools.txtnotemptytoint(spay1);
        c = SQLTools.txtnotemptytoint(spay2);
        d = SQLTools.txtnotemptytoint(spay3);
        e = SQLTools.txtnotemptytoint(bpay1);
        g = SQLTools.txtnotemptytoint(bpay2);
        f = SQLTools.txtnotemptytoint(lh);
        total.setText(String.valueOf((a+b+c+d+e+g-f)));
    }
    
    public int set_rounds(){
        ComboBox [] list = {sname1, sname2, sname3};
        if(sname1.getSelectionModel().isEmpty()){
            rounds = 0;
        }
        else{
            for(int i=0;i<list.length;i++){
                if(list[i].getSelectionModel().getSelectedItem()==null){
                    rounds = i;
                    break;
                }
            }
        }
        return rounds;
    }
    
    public int set_brounds() {
        if (btype1.getSelectionModel().isEmpty()) {
            brounds = 0;
        } else if (btype2.getSelectionModel().isEmpty()) {
            brounds = 1;
        } else {
            brounds = 2;
        }
        return brounds;
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
            filename = id.getText() + " (" + yy + mm + "薪資明細).xlsx";
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
            ExportTools.row(sheet, 5).getCell(2).setCellValue(name.getSelectionModel().getSelectedItem().toString());
            ExportTools.row(sheet, 9).getCell(6).setCellValue(basepay.getText());
            if (!sname1.getSelectionModel().isEmpty()) {
                ExportTools.row(sheet, 10).getCell(1).setCellValue(sname1.getSelectionModel().getSelectedItem().toString());
                ExportTools.row(sheet, 10).getCell(6).setCellValue(spay1.getText());
            }
            if (!sname2.getSelectionModel().isEmpty()) {
                ExportTools.row(sheet, 11).getCell(1).setCellValue(sname2.getSelectionModel().getSelectedItem().toString());
                ExportTools.row(sheet, 11).getCell(6).setCellValue(spay2.getText());
            }
            if (!sname3.getSelectionModel().isEmpty()) {
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
    public void btn_back() throws Exception{
        StageControll.close(SAP);
        StageControll.open(MenuSController.class, "/View/MenuS.fxml");
    }
}