package SelfTools;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/*
* @author Sora
*/
public class AutoCompleteComboBox <T> implements EventHandler<KeyEvent> {
    public ComboBox comboBox;
    public StringBuilder sb;
    public ObservableList<T> data;
    public boolean moveCaretToPos = false;
    public int caretPos;
    
    public AutoCompleteComboBox(final ComboBox comboBox) {
        this.comboBox = comboBox;
        sb = new StringBuilder();
        data = comboBox.getItems();
        this.comboBox.setEditable(true);
        this.comboBox.setOnKeyPressed( e -> {
            comboBox.hide();
        });
        this.comboBox.setOnKeyReleased(AutoCompleteComboBox.this);
    }
    
    @Override
    public void handle(KeyEvent event) {
        if(null != event.getCode()) switch (event.getCode()) {
            case UP:
                caretPos = -1;
                moveCaret(comboBox.getEditor().getText().length());
                return;
            case DOWN:
                caretPos = -1;
                moveCaret(comboBox.getEditor().getText().length());
                return;
            case BACK_SPACE:
                moveCaretToPos = true;
                caretPos = comboBox.getEditor().getCaretPosition();
                break;
            case DELETE:
                moveCaretToPos = true;
                caretPos = comboBox.getEditor().getCaretPosition();
                break;    
            default:
                break;
        }
        if (event.isControlDown() || event.getCode() == KeyCode.HOME || event.getCode() == KeyCode.LEFT
                || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
            return;
        }
        ObservableList list = FXCollections.observableArrayList();
        for (int i=0; i<data.size(); i++) {
            if(data.get(i).toString().toLowerCase().startsWith(
                    AutoCompleteComboBox.this.comboBox
                            .getEditor().getText().toLowerCase())) {
                list.add(data.get(i));
            }
        }
        String t = comboBox.getEditor().getText();
        comboBox.setVisibleRowCount(5);
        comboBox.setItems(list);
        comboBox.getEditor().setText(t);
        if(!moveCaretToPos) {
            caretPos = -1;
        }
        moveCaret(t.length());
        if(!list.isEmpty()) {
            if(event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.ESCAPE ){
                comboBox.hide();
            }
            else if(event.getCode() == KeyCode.ENTER){
                comboBox.getValue();
            }
            else if(event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.CONTROL
                    || event.getCode() == KeyCode.SHIFT){
            }
            else{
                comboBox.show();
            }
        }
    }
    
    public void moveCaret(int textLength) {
        if(caretPos == -1) {
            comboBox.getEditor().positionCaret(textLength);
        } else {
            comboBox.getEditor().positionCaret(caretPos);
        }
        moveCaretToPos = false;
    }
}
