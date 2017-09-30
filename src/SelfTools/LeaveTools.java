package SelfTools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Casy
 */
public class LeaveTools {
    
    private String emp_id;
    private String leaveDate;
    private String leaveHour;

    public LeaveTools(){
        this.emp_id = UserInfo.emp_pid;
    }

    public String getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(String leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getLeaveHour() {
        return leaveHour;
    }

    public void setLeaveHour(String leaveHour) {
        this.leaveHour = leaveHour;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }
    
    public double set_label() throws SQLException{
        try(Connection conn = SQLTools.MSSQL()){
            //Special Leave Spent of Anniverseriy
            String sql = "use MileStoneHRMS select ab.Interval from AbWorktimeR as ab "
                + " left join Personel as p on p.P_ID = ab.P_ID left join LO as l on l.LO_ID = ab.LO_ID "
                + " where l.LO_Name like '%特休假%' and year(ab.StartD) = year(getdate()) "
                + " and month(ab.StartD) >= month(p.Inaugu_Day) and day(ab.StartD) >= day(p.Inaugu_Day) "
                + "and ab.P_ID = " + "'" + emp_id + "'";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            double specialLeaveSpent = 0d;
            while(rs.next()){
                specialLeaveSpent = specialLeaveSpent + rs.getDouble(1);
            }
            
            //Get Seniority
            sql = "use MileStoneHRMS\n"
                + " select cast(cast(datediff(dd, cast(p.Inaugu_Day as date), GETDATE()) as decimal(6,2))/365 as decimal(5,3))\n"
                + " from Personel as p where p.P_ID = " + "'" + emp_id + "'";
            rs = conn.createStatement().executeQuery(sql);
            double seniority = 0;
            while(rs.next()){
                seniority = rs.getDouble(1);
            }
            
            //Set Special Leave
            double approvedSpecialLeave = 0d;
            
            if(0.5 <= seniority  && seniority < 1){
                approvedSpecialLeave = 3;
            }else if(1 <= seniority && seniority < 2){
                approvedSpecialLeave = 7;
            }else if(2 <= seniority && seniority < 3){
                approvedSpecialLeave = 10;
            }else if(3 <= seniority &&  seniority < 5){
                approvedSpecialLeave = 14;
            }else if(5 <= seniority && seniority < 10){
                approvedSpecialLeave = 15;
            }else if(10 <= seniority){
                approvedSpecialLeave = 15 + (seniority-10);
                if(approvedSpecialLeave > 30){
                    approvedSpecialLeave = 30;
                }
            }
            
            System.out.println("年資：" + seniority + "年");
            System.out.println("核准特休日數：" + approvedSpecialLeave + "日");    
            System.out.println("年度已休的特休：" + specialLeaveSpent + "時");
            System.out.println("剩餘特休時數：" + (approvedSpecialLeave*24 - specialLeaveSpent) + "時");
            
            //Calculating Remaining Special Leave
            double result = (approvedSpecialLeave*24 - specialLeaveSpent);
            System.out.println(result);
            
            return result;
            
        }
    }
    
}
