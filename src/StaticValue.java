import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

public class StaticValue {
    static Stage stage;
    static double stageWidth = 800;
    static double stageHeight = 450;
    static String userName = "zhangyichi";
    static String course;
    static HBox topHbox;

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

    public static HBox getNewTopHBox() {
        topHbox = new TopHBox().gethBox();
        return topHbox;
    }
}

class TopHBox {
    private HBox hBox = new HBox();
    Label userName = new Label(StaticValue.userName);
    Button info = new Button("个人信息");
    Button exit = new Button("退出");
    HBox leftBottom = new HBox(info,exit);
    VBox left = new VBox(userName,leftBottom);
    Label title = new Label("学习小帮手");
    Button resource = new Button("课程资料");
    Button ddl = new Button("我的DDL");
    Button memo = new Button("备忘录");
    HBox right = new HBox(resource,ddl,memo);

    TopHBox() {
        userName.setFont(Font.font("黑体", FontWeight.BOLD,22));
        title.setFont(Font.font("华文行楷", FontWeight.BOLD,36));
        title.setTextFill(Color.BLUE);
        exit.setTextFill(Color.RED);

        leftBottom.setSpacing(StaticValue.stageHeight / 25);
        left.setSpacing(StaticValue.stageHeight / 20);
        right.setSpacing(StaticValue.stageWidth / 60);

        hBox.getChildren().addAll(left,title,right);
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

    HBox gethBox() {
        return this.hBox;
    }
}