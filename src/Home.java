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
        StaticValue.stage.setScene(new Scene(pane,StaticValue.stageWidth,StaticValue.stageHeight));
        StaticValue.stage.setTitle("学习小帮手");
        StaticValue.stage.show();
    }
}
