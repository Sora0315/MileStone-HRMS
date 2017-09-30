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
public class SalaryAdjController implements Initializable {
    @FXML public AnchorPane SAP;
    @FXML public TextField id, ay, am, ad;
    @FXML public TextField fbasepay, abasepay;
    @FXML public ComboBox name, fsname1, fsname2, fsname3, asname1, asname2, asname3;
    @FXML public TextField fspay1, fspay2, fspay3, aspay1, aspay2, aspay3;
    @FXML public TextField ftotal, atotal, diff;
    @FXML public Button back, confirm, export, submit;
    public int fround, around;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TextField [] tlist = {id, fspay2, aspay2, fspay3, aspay3, ftotal, atotal, diff};
        ComboBox [] clist = {fsname2, fsname3, asname2, asname3};
        for(TextField items : tlist){
            items.setDisable(true);
        }
        for(ComboBox items : clist){
            items.setDisable(true);
        }
        String sql_name = "use MileStoneHRMS select distinct p.Name_CH from Personel as p "
                + " left join PStatus as ps on ps.P_ID = p.P_ID left join Activity as a on a.Act_ID = ps.Act_ID\n"
                + " where a.Act_Name like '在職' and ps.EndD is null ";
        String sql = "use MileStoneHRMS select s.Type_Name from SdyType as s ";
        ComboBox [] allclist = {fsname1, fsname2, fsname3, asname1, asname2, asname3};
        try{
            SQLTools.SqlGetItem(sql_name, name);
            StageControll.ComboBoxCtrl(name);
            name.setEditable(true);
            AutoCompleteComboBox acc = new AutoCompleteComboBox(name);
            for(ComboBox items : allclist){
                SQLTools.SqlGetItem(sql, items);
                StageControll.ComboBoxCtrl(items);
                items.setEditable(true);
                AutoCompleteComboBox autoCompleteComboBox = new AutoCompleteComboBox(items);
            }
        }catch(Exception e){
        }
        StageControll.Keydirect(back, name);
        StageControll.Keydirect(abasepay, fsname1);
        StageControll.Keydirect(aspay1, fsname2);
        StageControll.Keydirect(aspay2, fsname3);
        StageControll.Keydirect(fspay1, asname1);
        StageControll.Keydirect(fspay2, asname2);
        StageControll.Keydirect(fspay3, asname3);
        TextField [] tfall = {id, ay, am, ad, fbasepay, abasepay,
            fspay1, fspay2, fspay3, aspay1, aspay2, aspay3, ftotal, atotal, diff};
        for(TextField itmes : tfall){
            StageControll.TextFieldhandle(itmes);
        }
        StageControll.ButtonCtrl(confirm, export);
        StageControll.ButtonCtrl(export, submit);
        StageControll.ButtonCtrl(submit, back);
        name.setOnAction(e -> set_id());
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
    public void btn_checkdata(MouseEvent event) throws Exception{
        if (id.getText().isEmpty()
                || ay.getText().isEmpty() || am.getText().isEmpty() || ad.getText().isEmpty()) {
            NoticeController.noticecontent = "請輸入必要資料！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");
        } else {
            ftotal();
            atotal();
            diff();
            set_rounds();
            String sql = "use MileStoneHRMS select s.P_ID from SalaryAdjR as s where s.P_ID = "
                    + "'" + id.getText() + "'" + " and s.StartD = "
                    + "'" + StringVariation.datecom(ay, am, ad) + "'";

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
    
    public void set_rounds(){
        ComboBox[] flist = {fsname1, fsname2, fsname3};
        ComboBox[] alist = {asname1, asname2, asname3};
        for (int i = 0; i < flist.length; i++) {
            if (!flist[i].getSelectionModel().isEmpty()) {
                fround = i + 1;
            } else {
                break;
            }
        }
        for (int i = 0; i < alist.length; i++) {
            if (!alist[i].getSelectionModel().isEmpty()) {
                around = i + 1;
            } else {
                break;
            }
        }
//        System.out.println(fround + "\t" + around);
    }
    
    @FXML
    public void btn_submit(MouseEvent event) throws Exception{
        Data_Save();
        StageControll.close(SAP);
        StageControll.open(SalaryAdjController.class, "/View/SalaryAdj.fxml");
    }
    
    public void Data_Save() throws Exception{
        int  a, b, c, d, e, f;
        a = SQLTools.txtnotemptytoint(fspay1);
        b = SQLTools.txtnotemptytoint(fspay2);
        c = SQLTools.txtnotemptytoint(fspay3);
        d = SQLTools.txtnotemptytoint(aspay1);
        e = SQLTools.txtnotemptytoint(aspay2);
        f = SQLTools.txtnotemptytoint(aspay3);
        String sql = "use MileStoneHRMS insert into SalaryAdjR(P_ID, StartD, Bef_BasePay, Aft_BasePay, Difference)"
                + " values(?,?,?,?,?)";
        
        try (Connection conn = SQLTools.MSSQL()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id.getText());
                pstmt.setString(2, StringVariation.datecom(ay, am, ad));
                SQLTools.emptyslotsetnull(fbasepay, pstmt, 3);
                SQLTools.emptyslotsetnull(abasepay, pstmt, 4);
                SQLTools.emptyslotsetnull(diff, pstmt, 5);
                pstmt.execute();
                AuditLog.Audit("主管/HR-員工薪資調整紀錄登錄", name);
            }
            
            String ask = "use MileStoneHRMS select s.Type_ID from SdyType as s  where s.Type_Name =  ";
            for (int i = 0; i < fround; i++) {
                String sql2 = "use MileStoneHRMS insert into BefSdy(P_ID, StartD, Type_ID, Bef_Subsidy) "
                        + " values(?,?,?,?) ";
                ComboBox[] flist = {fsname1, fsname2, fsname3};
                TextField[] ftlist = {fspay1, fspay2, fspay3};

                try (PreparedStatement pstmt = conn.prepareStatement(sql2)) {
                    pstmt.setString(1, id.getText());
                    pstmt.setString(2, StringVariation.datecom(ay, am, ad));
                    pstmt.setString(3, SQLTools.ValueGetId(ask, flist[i]));
                    pstmt.setString(4, ftlist[i].getText());
                    pstmt.execute();
                }
            }
            
            for (int i = 0; i < around; i++) {
                String sql3 = "use MileStoneHRMS insert into AftSdy(P_ID, StartD, Type_ID, Aft_Subsidy) "
                        + " values(?,?,?,?) ";
                ComboBox[] alist = {asname1, asname2, asname3};
                TextField[] atlist = {aspay1, aspay2, aspay3};

                try (PreparedStatement pstmt = conn.prepareStatement(sql3)) {
                    pstmt.setString(1, id.getText());
                    pstmt.setString(2, StringVariation.datecom(ay, am, ad));
                    pstmt.setString(3, SQLTools.ValueGetId(ask, alist[i]));
                    pstmt.setString(4, atlist[i].getText());
                    pstmt.execute();
                }
            }
        }
        
        if(fsname1!=null || asname1!=null){
            AuditLog.Audit("主管/HR-員工薪資調整紀錄登錄(津貼)", name);
        }
    }
    
    public void ftotal(){
        int a, b, c, d;
        a = SQLTools.txtnotemptytoint(fbasepay);
        b = SQLTools.txtnotemptytoint(fspay1);
        c = SQLTools.txtnotemptytoint(fspay2);
        d = SQLTools.txtnotemptytoint(fspay3);
        String tal = String.valueOf((a+b+c+d));
        ftotal.setText(tal);
    }
    
    public void atotal(){
        int a, b, c, d;
        a = SQLTools.txtnotemptytoint(abasepay);
        b = SQLTools.txtnotemptytoint(aspay1);
        c = SQLTools.txtnotemptytoint(aspay2);
        d = SQLTools.txtnotemptytoint(aspay3);
        String tal = String.valueOf((a+b+c+d));
        atotal.setText(tal);
    }
    
    public void diff(){
        diff.setText(String.valueOf((Integer.parseInt(atotal.getText()) - Integer.parseInt(ftotal.getText()))));
    }
    
    @FXML
    public void exportexcel(MouseEvent event) throws Exception{
        String filename = id.getText() + " " + ay.getText() + StringVariation.right(("00" + am.getText()), 2) + "薪資調整通知書.xlsx";
        File outputfile = new File(ExportTools.path2, filename);
        try{
            XSSFWorkbook workbook = new XSSFWorkbook(ExportTools.path1 + "/薪資調整通知書範本.xlsx");
            XSSFSheet sheet = workbook.getSheet("薪資調整通知書");
            
            ExportTools.row(sheet, 3).getCell(2).setCellValue(id.getText());
            ExportTools.row(sheet, 3).getCell(7).setCellValue(name.getSelectionModel().getSelectedItem().toString());
            ExportTools.row(sheet, 6).getCell(1).setCellValue(ay.getText());
            ExportTools.row(sheet, 6).getCell(3).setCellValue(am.getText());
            ExportTools.row(sheet, 6).getCell(5).setCellValue(ad.getText());
            if(!fbasepay.getText().isEmpty()){
                ExportTools.row(sheet, 11).getCell(2).setCellValue(fbasepay.getText());
            }
            if(!abasepay.getText().isEmpty()){
                ExportTools.row(sheet, 11).getCell(7).setCellValue(abasepay.getText());
            }
            if(!fsname1.getSelectionModel().isEmpty()){
                ExportTools.row(sheet, 12).getCell(0).setCellValue(fsname1.getSelectionModel().getSelectedItem().toString());
                ExportTools.row(sheet, 12).getCell(2).setCellValue(fspay1.getText());
            }
            if(!asname1.getSelectionModel().isEmpty()){
                ExportTools.row(sheet, 12).getCell(5).setCellValue(asname1.getSelectionModel().getSelectedItem().toString());
                ExportTools.row(sheet, 12).getCell(7).setCellValue(aspay1.getText());
            }
            if(!fsname2.getSelectionModel().isEmpty()){
                ExportTools.row(sheet, 13).getCell(0).setCellValue(fsname2.getSelectionModel().getSelectedItem().toString());
                ExportTools.row(sheet, 13).getCell(2).setCellValue(fspay2.getText());
            }
            if(!asname2.getSelectionModel().isEmpty()){
                ExportTools.row(sheet, 13).getCell(5).setCellValue(asname2.getSelectionModel().getSelectedItem().toString());
                ExportTools.row(sheet, 13).getCell(7).setCellValue(aspay2.getText());
            }
            if(!fsname3.getSelectionModel().isEmpty()){
                ExportTools.row(sheet, 14).getCell(0).setCellValue(fsname3.getSelectionModel().getSelectedItem().toString());
                ExportTools.row(sheet, 14).getCell(2).setCellValue(fspay3.getText());
            }
            if(!asname3.getSelectionModel().isEmpty()){
                ExportTools.row(sheet, 14).getCell(5).setCellValue(asname3.getSelectionModel().getSelectedItem().toString());
                ExportTools.row(sheet, 14).getCell(7).setCellValue(aspay3.getText());
            }
            if(!ftotal.getText().isEmpty()){
                ExportTools.row(sheet, 17).getCell(2).setCellValue(ftotal.getText());
            }
            if(!atotal.getText().isEmpty()){
                ExportTools.row(sheet, 17).getCell(7).setCellValue(atotal.getText());
            }
            ExportTools.row(sheet, 20).getCell(3).setCellValue(diff.getText());
            //Save file
            try(FileOutputStream fileout = new FileOutputStream(outputfile)) {
                workbook.write(fileout);
                fileout.close();
                NoticeController.noticecontent = "您的匯出已完成！";
                NoticeController.noticecontent2 = "請至匯出資料夾確認。";
                StageControll.open(NoticeController.class, "/View/Notice.fxml");
            }
            catch(Exception e){
            }
        }
        catch(Exception e){
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