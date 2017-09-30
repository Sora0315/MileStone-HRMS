package SelfTools;

import java.sql.Connection;
import java.sql.ResultSet;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * @author Sora
 */
public class TableTools {
  
    public static void DataSet(TableView tableview, int size, int cellwidth,String[] col_name, String sql ) throws Exception{
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        data.clear();
        data.removeAll(data);
        for(int i=0;i<size;i++){
            tableview.getColumns().clear();    
        }
        try(Connection conn = SQLTools.MSSQL()){
            //cellvaluefactory
            for (int i = 1; i <= size; i++) { //careful with indexes
                TableColumn<ObservableList<String>, String> cols = new TableColumn<>();
                cols.setPrefWidth(cellwidth);
                int I = i;
                cols.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(I - 1)));
                cols.setText(col_name[i - 1]);
                tableview.getColumns().add(cols);
            }
            //Set Result
            try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
                while (rs.next()) {
                    ObservableList<String> row = FXCollections.observableArrayList();
                    row.clear();
                    row.removeAll(row);
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        row.add(rs.getString(i));
                    }
                    data.add(row);
                }
                tableview.setItems(data);
            }
        }      
    }
}


