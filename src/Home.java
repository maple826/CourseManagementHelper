import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Home {

    Home() {
        createHome();
    }

    private void createHome() {
        BorderPane pane = new BorderPane();
        pane.setTop(StaticValue.getNewTopHBox());
        pane.setStyle("-fx-background-color: #203A97");
        StaticValue.stage.setScene(new Scene(pane,StaticValue.stageWidth,StaticValue.stageHeight));
        StaticValue.stage.setTitle("学业助理");
        StaticValue.stage.show();
    }
}
