import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String[] args) {
        Application.launch();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        StaticValue.stage = primaryStage;
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e-> {
            System.exit(0);
        });
        new Login(primaryStage);
    }
}
