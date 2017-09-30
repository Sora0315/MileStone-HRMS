package SelfTools;

import javafx.scene.control.TextField;

/**
 *
 * @author Casval
 */
public class StringVariation {
    
    public static String right(String value, int length){
        return value.substring(value.length()-length);
    }
    
    public static String left(String value, int length){
        return value.substring(1, length);
    }
    
    public static String datecom(String y, String m, String d){
        String date=null;
        if(y == null || m ==null || d == null){
        }
        else{
            date = y + "/" + m + "/" + d;
        }
        return date;
    }
    
    public static String datecom(TextField y, TextField m, TextField d){
        String date=null;
        if(y.getText().isEmpty() || m.getText().isEmpty() || d.getText().isEmpty()){
        }
        else{
            date = y.getText()+ "/" + m.getText() + "/" + d.getText();
        }
        return date;
    }
    
    public static String datecom(TextField y, TextField m, String d){
        String date=null;
        if(y.getText()!=null || m.getText()!=null ){
            date = y.getText()+ "/" + m.getText() + "/" + d;
        }
        else{
        }
        return date;
    }
    
    public static String datetimecom(TextField y, TextField m, TextField d, TextField h, TextField min){
        String datetime=null;
        if(y.getText().isEmpty() || m.getText().isEmpty() || d.getText().isEmpty()
                || h.getText().isEmpty() || min.getText().isEmpty()){
        }
        else{
            datetime = y.getText()+ "/" + m.getText() + "/" + d.getText() + "  " + h.getText() + ":" + min.getText();
        }
        return datetime;
    }
    
}
