import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * 主界面类
 */
public class Home {
    /**
     * 动态壁纸切换常量
     */
    private static int WALLPAPER_NUM = 1;

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
        pane.setTop(StaticValue.getNewTopVBox());
        new Thread(() -> {
            while (true){
                String image = "/img/login" + String.valueOf(WALLPAPER_NUM) + ".jpg";
                pane.setStyle("-fx-background-image: url("+ image +");" +
                        "-fx-background-size: cover");
                WALLPAPER_NUM = (WALLPAPER_NUM + 1) % 3;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        pane.setStyle("‐fx‐background‐color: transparent;");
        StaticValue.stage.setScene(new Scene(pane,StaticValue.stageWidth,StaticValue.stageHeight));
        StaticValue.stage.setTitle("学业助理");
        StaticValue.stage.show();
    }
}
