package Controller;

import SelfTools.ExportTools;
import SelfTools.StageControll;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 * @author Sora
 */
public class ExportController implements Initializable {
    
    @FXML public AnchorPane AP;
    @FXML public ComboBox drive1, drive2;
    @FXML public TextField dirname1, dirname2;
    @FXML public Button back, set, download; 
    File [] path;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setdriveitem(drive1);
        setdriveitem(drive2);
        set.setDisable(true);
        download.setDisable(true);
        drive1.setEditable(true);
        drive2.setEditable(true);
        StageControll.AnchorPaneKeyCtrl(AP);
        StageControll.Buttonhandle(back);
        StageControll.Buttonhandle(set);
        StageControll.Buttonhandle(download);
        StageControll.TextFieldhandle(dirname1);
        StageControll.TextFieldhandle(dirname2);
        StageControll.Keydirect(back, drive1);
        StageControll.Keydirect(dirname1, drive2);
    }
    
    public void setdriveitem(ComboBox drive){
        path = File.listRoots();
        ObservableList<String>  list = FXCollections.observableArrayList();
        for(File items : path){
            list.add(items.toPath().toString());
        }
        drive.setItems(list);
    }
    
    @FXML
    public void btn_setting() throws Exception{
        try (FileWriter fw = new FileWriter(setdir("C:/", "MileStone_HRMS") + "/settings.txt")) {
            BufferedWriter bfw = new BufferedWriter(fw);
            bfw.write(setdir(drive1, dirname1));
            bfw.newLine();
            bfw.write(setdir(drive2, dirname2));
            bfw.flush();
        }
        download.setDisable(false);
        NoticeController.noticecontent = "您的設置已完成！";
        StageControll.open(NoticeController.class, "/View/Notice.fxml");
    }
    
    @FXML
    public void btn_download() throws FileNotFoundException, IOException, Exception{
        String pathset1;
        String pathset2;
        try (FileReader fr = new FileReader("C:/MileStone_HRMS/settings.txt")) {
            BufferedReader bfr = new BufferedReader(fr);
            pathset1 = bfr.readLine();
            pathset2 = bfr.readLine();
        }
        ExportTools.path1 = pathset1;
        ExportTools.path2 = pathset2;
        ExportTools.filedowonload("Salary.xlsx", pathset1, "薪資明細空白範本.xlsx");
        ExportTools.filedowonload("SalaryAdj.xlsx", pathset1, "薪資調整通知書範本.xlsx");
        NoticeController.noticecontent = "您的下載已完成！";
        StageControll.open(NoticeController.class, "/View/Notice.fxml");        
    }
    
    public String setdir(String drive, String dir) throws IOException{
        Path newdir = Paths.get(drive + dir);
        Files.createDirectories(newdir);
        return newdir.toString();
    }
    
    public String setdir(ComboBox cmb, TextField dir) throws IOException{
        Path newdir = Paths.get(cmb.getSelectionModel().getSelectedItem().toString()
        + "/" + dir.getText());
        Files.createDirectories(newdir);
        return newdir.toString();
    }
    
    @FXML
    public void enable_setting(){
        if(drive1.getSelectionModel() == null || drive2.getSelectionModel() == null
                || !dirname1.getText().isEmpty()){
            set.setDisable(false);
        }
    }
    
    public void btn_back() throws Exception{
        StageControll.open(MenuCController.class, "/View/MenuC.fxml");
        StageControll.close(AP);
    }    
}
