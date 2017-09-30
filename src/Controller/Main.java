package Controller;

import SelfTools.StageControll;
import javafx.application.Application;
import javafx.stage.Stage;

/*
 * Version 3.0
 * Update at 2017.09.30 18:00
 * @author Sora
 */
public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        StageControll.open(LoginController.class, "/View/Login.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
