package SelfTools;

import Controller.NoticeController;
import static SelfTools.StageControll.open;
import java.io.BufferedReader;
import org.apache.poi.xssf.usermodel.*;
import org.apache.commons.io.IOUtils; 
import org.apache.commons.net.ftp.FTPClient; 
import java.io.IOException; 
import java.io.FileOutputStream; 
import java.io.FileReader;

/**
 * @author Casval
 */
public class ExportTools {
    
    public static String path1, path2;
    
    public static XSSFSheet sheet(XSSFWorkbook book, String txt){
        return book.getSheet(txt);
    }
    
    public static XSSFRow row(XSSFSheet sheet, int i){
        return sheet.getRow(i);
    }
    
    public static XSSFCell cell(XSSFRow row, int i){
        return row.getCell(i);
    }
    
    public static void filedowonload(String ftpfilename, String localpath, String localname) throws Exception {
        //注意：ftp檔案名稱不能以中文命名，否則無法下載。
        FTPClient ftpClient = new FTPClient();
        FileOutputStream out = null;
        try {
            ftpClient.connect("ftp.drivehq.com");
            ftpClient.login("boistorche@gmail.com", "54685057");
            String remoteFileName = ("/download/" + ftpfilename);
            out = new FileOutputStream(localpath + "\\" + localname);
            ftpClient.setBufferSize(1024);
            //設置檔案類型（二進位）
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.retrieveFile(remoteFileName, out);
        } catch (IOException e) {
            NoticeController.noticecontent = "FTP用戶端出錯！";
            NoticeController.noticecontent2 = "請聯絡程式設計師或系統管理員。";
            open(NoticeController.class, "Notice.fxml", true);
            throw new RuntimeException("FTP用戶端出錯！", e);
        } finally {
            IOUtils.closeQuietly(out);
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                throw new RuntimeException("關閉FTP連接發生異常！", e);
            }
        }
    }
    
    public static void exportpath() throws Exception{
        try{
            FileReader fr = new FileReader("C:/MileStone_HRMS/settings.txt");
            BufferedReader bfr = new BufferedReader(fr);
            path1 = bfr.readLine();
            path2 = bfr.readLine();
            fr.close();
        }catch(Exception e){
            NoticeController.noticecontent = "由於匯出範本未下載，";
            NoticeController.noticecontent2 = "請先前往\"匯出下載與設定\"下載並設置位置。";
            open(NoticeController.class, "Notice.fxml", true);
        }
    }
}

