import javafx.application.Application;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        new StaticValue();
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StaticValue.stage = primaryStage;
        new Login(primaryStage);
    }
}
