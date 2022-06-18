import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * 主界面类
 */
public class Home {
    static boolean changeFlag = true;

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
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (changeFlag){
                        String image = "/img/login" + String.valueOf(StaticValue.WALLPAPER_NUM) + ".jpg";
                        try {
                            pane.setStyle("-fx-background-image: url("+ image +");" +
                                    "-fx-background-size: cover;");
                        }
                        catch (Exception e) {
                            System.out.println(e);
                        }
                        StaticValue.WALLPAPER_NUM = (StaticValue.WALLPAPER_NUM + 1) % 3;
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        pane.setStyle("‐fx‐background‐color: transparent;");
        StaticValue.stage.setScene(new Scene(pane,StaticValue.stageWidth,StaticValue.stageHeight));
        StaticValue.stage.setTitle("学业助理");
        StaticValue.stage.show();
    }
}
