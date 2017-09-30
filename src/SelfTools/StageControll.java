package SelfTools;

import com.sun.javafx.scene.control.skin.ButtonSkin;
import com.sun.javafx.scene.control.skin.ComboBoxBaseSkin;
import com.sun.javafx.scene.control.skin.TableViewSkin;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import com.sun.javafx.scene.control.skin.TextFieldSkin;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/*
* @author Sora
*/

public class StageControll {
    
    public static void close(AnchorPane panel)throws Exception{
        Stage stg = (Stage) panel.getScene().getWindow();
        stg.close();
    }
    
    public static void open(Class cs,String name)throws Exception{
        Stage stg;
        FXMLLoader fxmlLoader = new FXMLLoader(cs.newInstance().getClass().getResource(name));
        Parent root = (Parent)fxmlLoader.load();
        Scene scene = new Scene(root);
        stg = new Stage();
        Image img = new Image("file:D:\\Java NetBean\\MileStoneHRMS\\src\\package\\Icon.png ");
        stg.getIcons().add(img);
        stg.setScene(scene);
        stg.setTitle("MileStone, Ltd. HRMS");
        stg.setResizable(false);
        stg.show();
    }
    
    public static void open(Class cs,String name, Boolean bl)throws Exception{
        Stage stg;
        FXMLLoader fxmlLoader = new FXMLLoader(cs.newInstance().getClass().getResource(name));
        Parent root = (Parent)fxmlLoader.load();
        Scene scene = new Scene(root);
        stg = new Stage();
        Image img = new Image("file:D:\\Java NetBean\\MileStoneHRMS\\src\\package\\Icon.png ");
        stg.getIcons().add(img);
        stg.setScene(scene);
        stg.setTitle("MileStone, Ltd. HRMS");
        stg.setResizable(false);
        if(bl==true){
            stg.initModality(Modality.APPLICATION_MODAL);
        }
        stg.show();
    }
    
    public static void Keydirect(ComboBox cmb0, ComboBox cmb1){
        cmb1.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.UP){
                cmb0.requestFocus();
                cmb0.getCursor();
            }
        });
    }
    
    public static void Keydirect(Button btn0, Button btn1){
        btn1.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.UP){
                btn0.requestFocus();
                btn0.getCursor();
            }
        });
    }
    
    public static void Keydirect(ComboBox cmb0, Button btn1){
        btn1.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.UP){
                cmb0.requestFocus();
                cmb0.getCursor();
            }
        });
    }
    
    public static void Keydirect(Button btn0, ComboBox cmb1){
        cmb1.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.UP){
                btn0.requestFocus();
                btn0.getCursor();
            }
        });
    }
    
    public static void Keydirect(TextField tf0, Button btn1){
        btn1.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.UP){
                tf0.requestFocus();
                tf0.getCursor();
            }
        });
    }
    
    public static void Keydirect(ComboBox cmb0, TextField tf1){
        tf1.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.UP){
                cmb0.requestFocus();
                cmb0.getCursor();
            }
        });
    }
    
    public static void Keydirect(TextField tf0, TextField tf1, Button btn2){
        tf1.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.UP){
                tf0.requestFocus();
                tf0.getCursor();
            }
            if(e.getCode() == KeyCode.DOWN){
                btn2.requestFocus();
                btn2.getCursor();
            }
        });
    }
    
    public static void Keydirect(TextField tf0, ComboBox cmb1){
        cmb1.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.UP){
                tf0.requestFocus();
                tf0.getCursor();
            }
        });
    }
    
    public static void Keydirect(Button btn0, TextField tf1, ComboBox cmb2){
        tf1.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.UP){
                btn0.requestFocus();
                btn0.getCursor();
            }
            if(e.getCode() == KeyCode.DOWN){
                cmb2.requestFocus();
                cmb2.getCursor();
            }
        });
    }
    
    public static void Keydirect(TextField tf0, Button btn1, Button btn2){
        btn1.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.UP){
                tf0.requestFocus();
                tf0.getCursor();
            }
            if(e.getCode() == KeyCode.DOWN){
                btn2.requestFocus();
                btn2.getCursor();
            }
        });
    }
    
    public static void Keydirect(Button btn0, Button btn1, Button btn2){
        btn1.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.UP){
                btn0.requestFocus();
                btn0.getCursor();
            }
            if(e.getCode() == KeyCode.DOWN){
                btn2.requestFocus();
                btn2.getCursor();
            }
        });
    }
    
    public static void Keydirect(Button btn0, Button btn1, TextField tf2){
        btn1.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.UP){
                btn0.requestFocus();
                btn0.getCursor();
            }
            if(e.getCode() == KeyCode.DOWN){
                tf2.requestFocus();
                tf2.getCursor();
            }
        });
    }
    
    
    
    public static void AnchorPaneKeyCtrl(AnchorPane ap){
        ap.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.DOWN){
                KeyEvent kv = new KeyEvent(KeyEvent.KEY_PRESSED, null, null,
                        KeyCode.TAB, e.isShiftDown(), e.isAltDown(), e.isControlDown(), e.isMetaDown());
                Event.fireEvent(e.getTarget(), kv);
                e.consume();
            }
            
            if(e.getCode() == KeyCode.RIGHT){
                Object ob = e.getTarget();
                if(ob instanceof ComboBox){
                    ((ComboBox) ob).show();
                }
            }
            
            if(e.getCode() == KeyCode.LEFT){
                Object ob = e.getTarget();
                if(ob instanceof ComboBox){
                    ((ComboBox) ob).hide();
                }
            }
            
            if(e.getCode() == KeyCode.ENTER){
                Event.fireEvent(e.getTarget(), new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0
                        , MouseButton.PRIMARY, 1,  true, true, true, true, true, true, true, true, true, true, null));
            }
        });
    }
    
    public static void ComboBoxCtrl(ComboBox b){
        b.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.DOWN){
                KeyEvent kv = new KeyEvent(KeyEvent.KEY_PRESSED, null, null,
                        KeyCode.TAB, e.isShiftDown(), e.isAltDown(), e.isControlDown(), e.isMetaDown());
                Event.fireEvent(e.getTarget(), kv);
                e.consume();
            }
            
            if(e.getCode() == KeyCode.RIGHT){
                Object ob = e.getTarget();
                if(ob instanceof ComboBox){
                    ((ComboBox) ob).show();
                }
            }
            
            if(e.getCode() == KeyCode.LEFT){
                Object ob = e.getTarget();
                if(ob instanceof ComboBox){
                    ((ComboBox) ob).hide();
                }
            }
            
            if(e.getCode() == KeyCode.ENTER){
                Event.fireEvent(e.getTarget(), new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0
                        , MouseButton.PRIMARY, 1,  true, true, true, true, true, true, true, true, true, true, null));
            }
        });
    }
    
    public static void ButtonCtrl(Button fbtn, Button abtn){
        fbtn.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.DOWN){
                abtn.requestFocus();
                e.consume();
            }
            if(e.getCode() == KeyCode.ENTER){
                Event.fireEvent(fbtn, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0
                        , MouseButton.PRIMARY, 1,  true, true, true, true, true, true, true, true, true, true, null));
            }
        });
    }
    
    public static void ButtonCtrl(Button fbtn, TextField tf){
        fbtn.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.DOWN){
                tf.requestFocus();
                e.consume();
            }
            if(e.getCode() == KeyCode.ENTER){
                Event.fireEvent(fbtn, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0
                        , MouseButton.PRIMARY, 1,  true, true, true, true, true, true, true, true, true, true, null));
            }
        });
    }
    
    public static void ButtonCtrl(Button fbtn, ComboBox cb){
        fbtn.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.DOWN){
                cb.requestFocus();
                e.consume();
            }
            if(e.getCode() == KeyCode.ENTER){
                Event.fireEvent(fbtn, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0
                        , MouseButton.PRIMARY, 1,  true, true, true, true, true, true, true, true, true, true, null));
            }
        });
    }
    
    public static void TextAreahandle(TextArea ta){
        ta.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            TextAreaSkin skin = (TextAreaSkin)ta.getSkin();
            if(e.getCode().equals(KeyCode.UP)){
                skin.getBehavior().traversePrevious();
            }
            if(e.getCode().equals(KeyCode.DOWN) | e.getCode().equals(KeyCode.TAB)){
                skin.getBehavior().traverseNext();
            }
        });
    }
    
    public static void TextFieldhandle(TextField tf){
        tf.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            TextFieldSkin skin = (TextFieldSkin)tf.getSkin();
            if(e.getCode().equals(KeyCode.UP)){
                skin.getBehavior().traversePrevious();
            }
            if(e.getCode().equals(KeyCode.DOWN) || e.getCode().equals(KeyCode.TAB)){
                skin.getBehavior().traverseNext();
            }
        });
    }
    
    public static void TableViewhandle(TableView tv){
        tv.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            TableViewSkin skin = (TableViewSkin)tv.getSkin();
            if(e.getCode().equals(KeyCode.UP)){
                skin.getBehavior().traversePrevious();
            }
            if(e.getCode().equals(KeyCode.DOWN) || e.getCode().equals(KeyCode.TAB)){
                skin.getBehavior().traverseNext();
            }
        });
    }
    
    public static void Buttonhandle(Button btn){
        btn.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            ButtonSkin skin = (ButtonSkin)btn.getSkin();
            if(e.getCode().equals(KeyCode.UP)){
                skin.getBehavior().traversePrevious();
            }
            if(e.getCode().equals(KeyCode.DOWN) || e.getCode().equals(KeyCode.TAB)){
                skin.getBehavior().traverseNext();
            }
            if(e.getCode() == KeyCode.ENTER){
                Event.fireEvent(btn, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0
                        , MouseButton.PRIMARY, 1,  true, true, true, true, true, true, true, true, true, true, null));
            }
        });
    }
    
    public static void ComboBoxhandle(ComboBox cb){
        cb.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ENTER){
                Event.fireEvent(e.getTarget(), new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0
                        , MouseButton.PRIMARY, 1,  true, true, true, true, true, true, true, true, true, true, null));
            }
            ComboBoxBaseSkin skin = (ComboBoxBaseSkin) cb.getSkin();
            if(e.getCode().equals(KeyCode.UP)){
                skin.getBehavior().traversePrevious();
            }
            if(e.getCode().equals(KeyCode.DOWN) || e.getCode().equals(KeyCode.TAB)){
                skin.getBehavior().traverseNext();
            }
            if(e.getCode().equals(KeyCode.RIGHT)){
                skin.show();
            }
            if(e.getCode().equals(KeyCode.LEFT)){
                skin.hide();
            }
            
        });
    }
    
    public static void seq(AnchorPane ap){
        ap.addEventFilter(KeyEvent.KEY_PRESSED, (event) ->
        {
            if(event.getCode() == KeyCode.DOWN){
                KeyEvent newevent = new KeyEvent(null, null, KeyEvent.KEY_PRESSED
                        , "", "\t", KeyCode.TAB, event.isShiftDown(), event.isControlDown()
                        , event.isAltDown(), event.isMetaDown());
                Event.fireEvent(event.getTarget(), newevent);
                event.consume();
            }
        });
    }
}