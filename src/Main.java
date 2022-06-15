import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.List;


public class Main extends Application {
    public static void main(String[] args) {
        Application.launch();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        StaticValue.stage = primaryStage;
        primaryStage.setResizable(false);
        new Login(primaryStage);
    }
}
