package Controller;

import SelfTools.AuditLog;
import SelfTools.AutoCompleteComboBox;
import SelfTools.LeaveTools;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import SelfTools.SQLTools;
import SelfTools.StageControll;
import SelfTools.StringVariation;
import SelfTools.TableTools;
import SelfTools.UserInfo;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * FXML Controller class
 * @author Sora
 */
public class ELeaveController implements Initializable {
    
    @FXML public AnchorPane EAP;
    @FXML public TableView lo;
    @FXML public Label l, lh;
    @FXML public TextField cause, sy, sm, sd, sh, smin, ey, em, ed, eh, emin, days, hours;
    @FXML public ComboBox choose;
    @FXML public Button back, cal, submit;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("here we are");
        TextField [] list = {sy, sm, sd, sh, smin, ey, em, ed, eh, emin, days, hours};
        for(TextField items : list){
            items.setDisable(true);
        }
        submit.setDisable(true);
        try{
            set_label();
            set_table();
            lo_item_set();
        }
        catch(Exception e){
        }
        choose.setEditable(true);
        AutoCompleteComboBox autoCompleteComboBox = new AutoCompleteComboBox(choose);
        StageControll.ComboBoxCtrl(choose);
        StageControll.Keydirect(back, choose);
        StageControll.TextFieldhandle(cause);
        StageControll.TextFieldhandle(sy);
        StageControll.TextFieldhandle(sm);
        StageControll.TextFieldhandle(sd);
        StageControll.TextFieldhandle(sh);
        StageControll.TextFieldhandle(smin);
        StageControll.TextFieldhandle(ey);
        StageControll.TextFieldhandle(em);
        StageControll.TextFieldhandle(ed);
        StageControll.TextFieldhandle(eh);
        StageControll.TextFieldhandle(emin);
        StageControll.TextFieldhandle(days);
        StageControll.TextFieldhandle(hours);
        StageControll.TableViewhandle(lo);
        StageControll.Buttonhandle(back);
        StageControll.Buttonhandle(cal);
        StageControll.Buttonhandle(submit);
    }
    
    public void set_label() throws Exception{
        LeaveTools leaveTool =  new LeaveTools();
        double label = leaveTool.set_label();
        DecimalFormat df = new DecimalFormat("##.#");
        l.setText(String.valueOf(((int)label)/24));
        lh.setText(String.valueOf(df.format(label%24.0)));
    }
    
    public void set_table() throws Exception{
        String [] col_name = {"請假起始時日","請假終止時日","假別","小計"};
        String sql = "use MileStoneHRMS select cast(a.StartD as smalldatetime), cast(a.EndD as smalldatetime)"
                + ", l.LO_Name,(cast(a.Interval/24 as varchar) + ' 日 ' + cast(a.Interval%24 as varchar) + ' 時')"
                + " from AbWorktimeR as a\n"
                + " left outer join LO as l on l.LO_ID = a.LO_ID\n"
                + " where a.P_ID = " + "'" + UserInfo.emp_pid + "'"
                + " and year(a.StartD) = year(getdate())" 
                + " and l.LO_Name like '%假%' and l.LO_Name not like '%停止%' "
                + " order by a.StartD Asc ";
        TableTools.DataSet(lo, 4, 268, col_name, sql);
    }
    
    public void lo_item_set() throws Exception{
        String sql = "use MileStoneHRMS select l.LO_Name from LO as l "
                + " where l.LO_Name like '%假%' and l.LO_Name not like '%停止%' ";
        SQLTools.SqlGetItem(sql, choose);
    }
    
    @FXML public void set_field_enable(){
        if(!choose.getSelectionModel().isEmpty()){
            TextField [] li = {sy, sm, sd, sh, smin, days, hours};
            for(TextField items : li){
                items.setDisable(false);
            }
        }
    }
    
    @FXML    public void setEndTime() throws Exception{
        TextField [] clearArry = {ey, em, ed, eh, emin};
        for(TextField o : clearArry){
            o.clear();
        }
        System.out.println("設定結束時間。");
        if(choose.getSelectionModel().isEmpty() && smin.getText().isEmpty()){
            NoticeController.noticecontent = "請依規定選擇請假類別！";
            NoticeController.noticecontent2 = "或請依規定填寫請假必要資訊！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");
            return;
        }
        
        if(!smin.getText().isEmpty()
                && !days.getText().isEmpty() && !hours.getText().isEmpty()){
            //日期檢核
            if(Integer.valueOf(sm.getText())>12 || Integer.valueOf(sm.getText())<=0
                || Integer.valueOf(sd.getText()) > 31 || Integer.valueOf(sd.getText())<=0
                || Integer.valueOf(sh.getText()) > 24 || Integer.valueOf(sh.getText())<=0
                || Integer.valueOf(smin.getText()) > 59 || Integer.valueOf(smin.getText())<0){
                NoticeController.noticecontent = "請依西元日期格式填寫！";
                StageControll.open(NoticeController.class, "/View/Notice.fxml");
                return;
            }
            //公司成立日期檢核
            if(Integer.valueOf(sy.getText()) < 2014 
                    || (Integer.valueOf(sy.getText()) == 2014 && Integer.valueOf(sm.getText())< 3 && Integer.valueOf(sd.getText())< 1)){
                NoticeController.noticecontent = "請勿填寫公司創立前日期！";
                StageControll.open(NoticeController.class, "/View/Notice.fxml");
                return;
            }
            
            Calendar date = Calendar.getInstance();
            date.set(Integer.valueOf(sy.getText()), Integer.valueOf(sm.getText())-1
                    , Integer.valueOf(sd.getText()), Integer.valueOf(sh.getText())
                    , Integer.valueOf(smin.getText()));
            System.out.println("請假前：" + date.getTime());
            
            //如果選擇特休假且特休剩餘為完整日數 或 非特休假 的狀況時
            if (Integer.valueOf(lh.getText()) == 0 && choose.getValue().equals("特休假") || !choose.getValue().equals("特休假")) {
                //超過8小時自動修正
                if (Integer.valueOf(hours.getText()) >= 8) {
                    int tempHours = Integer.valueOf(hours.getText());
                    int outputDays = Integer.valueOf(days.getText());
                    int daysCorrect = tempHours / 8;
                    int hoursCorrect = tempHours % 8;
                    outputDays = outputDays + daysCorrect;
                    days.setText(String.valueOf(outputDays));
                    hours.setText(String.valueOf(hoursCorrect));
                    NoticeController.noticecontent = "依規定，每日工作時數為8小時；";
                    NoticeController.noticecontent2 = "系統已自動修正。";
                    StageControll.open(NoticeController.class, "/View/Notice.fxml");
                }
            }       

            //特休假剩餘範圍檢核
            if(choose.getValue().equals("特休假") && (Integer.valueOf(days.getText())*24 + Integer.valueOf(hours.getText()))
                > (Integer.valueOf(l.getText())*24 + Integer.valueOf(lh.getText()))   ){
                days.clear();
                hours.clear();
                NoticeController.noticecontent = "超過特休假剩餘時數！";
                NoticeController.noticecontent2 = "請在剩餘時數內請假！";
                StageControll.open(NoticeController.class, "/View/Notice.fxml");
                return;
            }

            date.add(Calendar.DATE, Integer.valueOf(days.getText()));
            date.add(Calendar.HOUR, Integer.valueOf(hours.getText()));
            System.out.println("請假後：" + date.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String out = sdf.format(date.getTime());
            ey.setText(out.substring(0, 4));
            em.setText(out.substring(5, 7));
            ed.setText(out.substring(8, 10));
            eh.setText(out.substring(11, 13));
            emin.setText(out.substring(14));
            submit.setDisable(false);
        }else{
            NoticeController.noticecontent = "請依規定選擇請假類別！";
            NoticeController.noticecontent2 = "或請依規定填寫請假必要資訊！";
            StageControll.open(NoticeController.class, "/View/Notice.fxml");
            System.out.println("?!!!");
        }
    }
    
    @FXML public void btn_submit(MouseEvent event) throws Exception{
        Data_Save();
        StageControll.open(MenuEController.class, "/View/MenuE.fxml");
        StageControll.close(EAP);
        NoticeController.noticecontent = "請假資料已送出。";
        StageControll.open(NoticeController.class, "/View/Notice.fxml", true);
    }
    
    public void Data_Save() throws Exception{
        String ask = " use MileStoneHRMS select l.LO_ID from LO as l where l.LO_Name = ";
        String sql = "use MileStoneHRMS insert into AbWorktimeR(P_ID, LO_ID, StartD, EndD, Interval, Cause)"
                + " values(?,?,?,?,?,?)";
        try (Connection conn = SQLTools.MSSQL()) {
            try(PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1, UserInfo.emp_pid);
                pstmt.setString(2, SQLTools.ValueGetId(ask, choose));
                pstmt.setString(3, StringVariation.datetimecom(sy, sm, sd, sh, smin));
                SQLTools.emptyslotsetnull(StringVariation.datetimecom(ey, em, ed, eh, emin), pstmt, 4);
                if (days.getText().isEmpty() && hours.getText().isEmpty()) {
                    pstmt.setNull(5, java.sql.Types.VARCHAR);
                } else {
                    pstmt.setString(5, String.valueOf(Integer.valueOf(days.getText()) * 24 + Integer.valueOf(hours.getText())));
                }
                SQLTools.emptyslotsetnull(cause, pstmt, 6);
                pstmt.execute();
                AuditLog.Audit("員工申請-請假", UserInfo.emp_pid, choose, StringVariation.datetimecom(sy, sm, sd, sh, smin));
            }           
        }
    }
    
    @FXML public void btn_back(MouseEvent event) throws Exception{
        StageControll.open(MenuEController.class, "/View/MenuE.fxml");
        StageControll.close(EAP);
    }
}
