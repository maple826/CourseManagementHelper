import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * 主界面类
 */
public class Home {
    /**
     * 构造函数.
     * 创建主界面
     */
    Home() {
        createHome();
    }

    /**
     * 创建主界面.
     * 设置标题，组件并显示
     */
    private void createHome() {
        BorderPane pane = new BorderPane();
        pane.setTop(StaticValue.getNewTopHBox());
        pane.setStyle("-fx-background-color: #203A97");
        StaticValue.stage.setScene(new Scene(pane,StaticValue.stageWidth,StaticValue.stageHeight));
        StaticValue.stage.setTitle("学业助理");
        StaticValue.stage.show();
    }
}
