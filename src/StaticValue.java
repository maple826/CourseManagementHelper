import com.sun.javafx.geom.Shape;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

/**
 * 静态值类.
 * 包括需要多次使用的值，组件或方法
 */
public class StaticValue {
    static Stage stage;
    /**
     * 场景宽度
     */
    static double stageWidth = 900;
    /**
     * 场景长度
     */
    static double stageHeight = 500;
    /**
     * 当前用户名，初始为空
     */
    static String userName = "";
    /**
     * 除登录界面外其他界面的顶层组件
     */
    static VBox topVbox;
    static String buttonStyle1 = "-fx-background-color:darkturquoise;"+
            "-fx-background-radius:20;";
    static String buttonStyle2 = "-fx-background-color:lightskyblue;"+
            "-fx-background-radius:20;";

    /**
     * 删除file文件及其子文件
     * @param file
     * @throws IOException
     */
    public static void deleteFile(File file) throws IOException {
        File[] files = file.listFiles();
        if (files!=null){
            for (int i = 0; i < files.length ; i++) {
                if (files[i].isFile()){
                    files[i].delete();
                }else if (files[i].isDirectory()){
                    deleteFile(files[i]);
                }
                files[i].delete();//删除子目录
            }
        }
        file.delete();
    }

    public static VBox getNewTopVBox() {
        topVbox = new TopVBox().getvBox();
        return topVbox;
    }
}

/**
 * 顶层界面.
 * <p>
 *     包括用户名，图标，及个人信息、退出、课程资料，我的DDL，备忘录按钮
 * </p>
 */
class TopVBox {
    private VBox vBox = new VBox();
    private HBox hBox = new HBox();
    Label userName = new Label(StaticValue.userName);
    Button info = new Button("个人信息");
    Button exit = new Button("退出");
    /**
     * 左侧组件 <br>
     * 包括个人信息，退出两个按钮
     */
    HBox leftBottom = new HBox(info,exit);
    VBox left = new VBox(userName,leftBottom);
    Label title = new Label("学不可以已");
    Button resource = new Button("课程资料");
    Button ddl = new Button("我的DDL");
    Button memo = new Button("备忘录");
    Separator separator = new Separator();
    /**
     * 右侧组件 <br>
     * 包括课程资料，我的DDL，备忘录三个按钮
     */
    HBox right = new HBox(resource,ddl,memo);

    /**
     * 构造函数.
     * <p>
     *     设置样式 <br>
     *     调整内部组件位置 <br>
     *     设置按钮触发效果
     * </p>
     */
    TopVBox() {
        setStyle();
        leftBottom.setSpacing(StaticValue.stageHeight / 25);
        left.setSpacing(StaticValue.stageHeight / 20);
        right.setSpacing(StaticValue.stageWidth / 60);

        hBox.getChildren().addAll(left,title,right);
        vBox.getChildren().addAll(hBox,separator);
        hBox.setMargin(left,new Insets(StaticValue.stageHeight / 30,0,
                0,StaticValue.stageWidth / 100));
        hBox.setMargin(title,new Insets(StaticValue.stageHeight / 15,0,
                0,StaticValue.stageWidth * 2 / 9));
        hBox.setMargin(right,new Insets(StaticValue.stageHeight / 7,0,
                0,StaticValue.stageWidth / 8));

        info.setOnAction(e -> {
            new Info();
        });

        exit.setOnAction(e -> {
            new CourseAlert("Exit");
        });

        resource.setOnAction(e -> {
            new Course();
        });

        ddl.setOnAction(e -> {
            try {
                new ManageDDL();
            } catch (IOException | ParseException ex) {
                ex.printStackTrace();
            }
        });

        memo.setOnAction(e -> {
            try {
                new ManageMemorandum();
            } catch (FileNotFoundException | ParseException ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * 设置UI样式
     * <p>
     *     背景颜色：#203A97 <br>
     *     文字颜色：#F5CA2A <br>
     *     用户名字体：黑体 <br>
     *     图标字体：华文行楷 <br>
     *     按钮样式：{@link StaticValue#buttonStyle1}，{@link StaticValue#buttonStyle2} 其中退出按钮字体为红色
     * </p>

     */
    private void setStyle() {
        Button[] button = {info,resource,ddl,memo};

        hBox.setStyle("-fx-background-color: #203A97");
        separator.setStyle("-fx-background-color: #23b3e7;" +
                "-fx-background-width: 10;");
        separator.setManaged(true);

        userName.setFont(Font.font("黑体", FontWeight.BOLD,22));
        userName.setTextFill(Color.rgb(245,202,42));
        title.setFont(Font.font("华文行楷", FontWeight.BOLD,40));
        title.setTextFill(Color.rgb(245,202,42));

        String buttonStyle1 = StaticValue.buttonStyle1;
        String buttonStyle2 = StaticValue.buttonStyle2;
        for(Button i : button) {
            i.setStyle(buttonStyle1 + "-fx-font-size: 13");
            i.setOnMouseMoved(e -> {
                i.setStyle(buttonStyle2 + "-fx-font-size: 16");
            });
            i.setOnMouseExited(e -> {
                i.setStyle(buttonStyle1 + "-fx-font-size: 13");
            });
        }
        exit.setStyle(buttonStyle1 + ";-fx-text-fill: red;" + "-fx-font-size: 13");
        exit.setOnMouseMoved(e -> {
            exit.setStyle(buttonStyle2 + ";-fx-text-fill: red;" + "-fx-font-size: 16");
        });
        exit.setOnMouseExited(e -> {
            exit.setStyle(buttonStyle1 + ";-fx-text-fill: red;" + "-fx-font-size: 13");
        });
    }

    VBox getvBox() {
        return this.vBox;
    }
}