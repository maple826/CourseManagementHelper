import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.io.IOException;

public class StaticValue {
    static double stageWidth = 800;
    static double stageHeight = 450;
    static String userName;
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
    Label title = new Label("学习小帮手");
    Button exit = new Button("退出");

    TopHBox() {
        userName.setFont(Font.font("黑体", FontWeight.BOLD,18));
        title.setFont(Font.font("华文行楷", FontWeight.BOLD,36));
        title.setTextFill(Color.BLUE);
        exit.setFont(Font.font("黑体", FontWeight.BOLD,18));
        exit.setTextFill(Color.RED);
        exit.setOnAction(e -> {
            new CourseAlert("Exit");
        });

        hBox.getChildren().addAll(userName,title,exit);
        hBox.setMargin(userName,new Insets(StaticValue.stageHeight / 25));
        hBox.setMargin(title,new Insets(StaticValue.stageHeight / 40,0,
                StaticValue.stageHeight / 45,StaticValue.stageHeight * 2 / 5));
        hBox.setMargin(exit,new Insets(StaticValue.stageHeight / 25,0,
                StaticValue.stageHeight / 25,StaticValue.stageWidth * 2 / 7));
    }

    HBox gethBox() {
        return this.hBox;
    }
}