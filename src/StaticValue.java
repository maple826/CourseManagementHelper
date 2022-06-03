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
    static double stageWidth = 900;
    static double stageHeight = 500;
    static String userName = "";
    static String course;
    static HBox topHbox;
    static String buttonStyle1 = "-fx-background-color:darkturquoise;"+
            "-fx-background-radius:20;";
    static String buttonStyle2 = "-fx-background-color:lightskyblue;"+
            "-fx-background-radius:20;";

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
    Label title = new Label("学不可以已");
    Button resource = new Button("课程资料");
    Button ddl = new Button("我的DDL");
    Button memo = new Button("备忘录");
    HBox right = new HBox(resource,ddl,memo);

    TopHBox() {
        setStyle();

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

    private void setStyle() {
        Button[] button = {info,resource,ddl,memo};

        hBox.setStyle("-fx-background-color: #203A97");

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

    HBox gethBox() {
        return this.hBox;
    }
}