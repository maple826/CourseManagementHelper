import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Course {
    static Stage stage;
    BorderPane coursePane = new BorderPane();
    HBox courseTopHBox = StaticValue.getNewTopHBox();
    Line topLine = new Line(0,StaticValue.stageHeight / 7,
            StaticValue.stageWidth,StaticValue.stageHeight / 7);
    ScrollPane courseLeftScrollPane = new CourseLeftScrollPane().getPane();

    public Course() {
        this.stage = StaticValue.stage;
        createCourse();
    }
    private void createCourse() {
        coursePane.setTop(courseTopHBox);
        coursePane.setLeft(courseLeftScrollPane);
        coursePane.getChildren().add(topLine);
        stage.setScene(new Scene(coursePane,StaticValue.stageWidth,StaticValue.stageHeight));
        stage.setTitle("学习小帮手");
        stage.show();
    }
}

class CourseLeftScrollPane {
    private ScrollPane pane = new ScrollPane();
    private VBox vBox = new VBox();
    private ContextMenu menu;
    CourseLeftScrollPane() {
        vBox.setSpacing(StaticValue.stageHeight / 20);
        File f = new File("./data/" + StaticValue.userName + "/资源");
        File[] tmp = f.listFiles();
        String[] course = new String[tmp.length];
        for(int i = 0,j = 0;i < tmp.length;i++) {
            course[j++] = tmp[i].getName();
        }
        Arrays.sort(course);
        Button[] courseButton = new Button[course.length];
        for(int i = 0;i < course.length;i++) {
            courseButton[i] = new CourseButton(course[i]).getButton();
            vBox.getChildren().add(courseButton[i]);
        }

        pane.setContent(vBox);

        menu = new ContextMenu();
        MenuItem itemAdd = new MenuItem("添加");
        menu.getItems().add(itemAdd);
        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.SECONDARY)) {
                    menu.show(vBox, Side.TOP, event.getX(), event.getY());
                }
                else if (event.getButton().equals(MouseButton.PRIMARY)) {
                    menu.hide();
                }
            }
        });
        itemAdd.setOnAction(e -> {
            new CourseAlert("Add");
        });
    }

    public ScrollPane getPane() {
        return pane;
    }
}

class CourseButton {
    private String course;
    private Button button;
    private ContextMenu menu;
    public CourseButton(String course) {
        this.course = course;
        button = new Button(course);
        button.setFont(Font.font("宋体",24));
        menu = new ContextMenu();
        MenuItem itemDel = new MenuItem("删除");
        MenuItem itemEdit = new MenuItem("编辑");
        menu.getItems().addAll(itemDel,itemEdit);
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.SECONDARY)) {
                    menu.show(button, Side.TOP, event.getX(), event.getY());
                }
                else if(event.getButton().equals(MouseButton.PRIMARY)){
                    new Materials(new Course().coursePane,button.getText());
                }
            }
        });
        itemDel.setOnAction(e -> {
            new CourseAlert("Delete",this.course);
        });
        itemEdit.setOnAction(e -> {
            new CourseAlert("Edit",this.course);
        });
    }

    public Button getButton() {
        return button;
    }
}

class CourseAlert {
    CourseAlert(String s) {
        if(s.equals("Exit")) {
            Alert alert = new Alert(Alert.AlertType.NONE);
            ButtonType buttonYes = new ButtonType("确定");
            ButtonType buttonNo = new ButtonType("取消");
            alert.getButtonTypes().setAll(buttonYes,buttonNo);
            alert.setTitle("提示信息");
            alert.setHeaderText("");
            alert.setContentText("您确定要退出当前帐号吗");
            alert.initOwner(StaticValue.stage);
            alert.show();
            alert.setOnCloseRequest(e -> {
                ButtonType result = alert.getResult();
                if (result != null && result.equals(buttonYes)) {
                    StaticValue.userName = "";
                    new Login(StaticValue.stage);
                } else {
                    alert.close();
                }
            });
        }
        else if(s.equals("Exist")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示信息");
            alert.setHeaderText("");
            alert.setContentText("该课程已存在！");
            alert.show();
        }
        else if(s.equals("SuccessAdd")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示信息");
            alert.setHeaderText("");
            alert.setContentText("添加成功！");
            alert.show();
            new Course();
        }
        else if(s.equals("SuccessEdit")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示信息");
            alert.setHeaderText("");
            alert.setContentText("更改成功！");
            alert.show();
            new Course();
        }
        else if(s.equals("Add")) {
            Text text = new Text("请输入课程名称：");
            text.setFont(Font.font("宋体",18));
            TextField textField = new TextField();
            Button yes = new Button("确定");
            Button no = new Button("取消");
            HBox hBox1 = new HBox(text,textField);
            HBox hBox2 = new HBox(yes,no);
            hBox2.setAlignment(Pos.CENTER);
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(hBox1);
            borderPane.setBottom(hBox2);
            borderPane.setMargin(hBox1,new Insets(30));
            Stage alert = new Stage();
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setScene(new Scene(borderPane,StaticValue.stageWidth * 2 / 5,StaticValue.stageHeight * 2 / 5));
            alert.setTitle("提示信息");
            alert.show();

            no.setOnAction(e -> {
                alert.close();
            });

            yes.setOnAction(e -> {
                String course = textField.getText();
                File f = new File("./data/" + StaticValue.userName + "/资源/" + course);
                if(f.exists()) {
                    alert.close();
                    new CourseAlert("Exist");
                    return;
                }
                f.mkdir();
                f = new File("./data/" + StaticValue.userName + "/资源/" + course + "/书签链接");
                f.mkdir();
                new File("./data/" + StaticValue.userName + "/资源/" + course).mkdir();
                alert.close();
                new CourseAlert("SuccessAdd");
            });
        }
    }
    CourseAlert(String s,String course) {
        if(s.equals("Delete")) {
            Alert alert = new Alert(Alert.AlertType.NONE);
            ButtonType buttonYes = new ButtonType("确定");
            ButtonType buttonNo = new ButtonType("取消");
            alert.getButtonTypes().setAll(buttonYes, buttonNo);
            alert.setTitle("提示信息");
            alert.setHeaderText("");
            alert.setContentText("确定要删除课程：" + course + " 吗？");
            alert.initOwner(StaticValue.stage);
            alert.show();
            alert.setOnCloseRequest(e -> {
                ButtonType result = alert.getResult();
                if (result != null && result.equals(buttonYes)) {
                    try {
                        StaticValue.deleteFile(new File("./data/" + StaticValue.userName + "/资源/" + course));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    new Course();
                } else {
                    alert.close();
                }
            });
        }
        else if(s.equals("Edit")) {
            Text text = new Text("请输入新课程名称：");
            text.setFont(Font.font("宋体",18));
            TextField textField = new TextField();
            Button yes = new Button("确定");
            Button no = new Button("取消");
            HBox hBox1 = new HBox(text,textField);
            HBox hBox2 = new HBox(yes,no);
            hBox2.setAlignment(Pos.CENTER);
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(hBox1);
            borderPane.setBottom(hBox2);
            borderPane.setMargin(hBox1,new Insets(30));
            Stage alert = new Stage();
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setScene(new Scene(borderPane,StaticValue.stageWidth * 2 / 5,StaticValue.stageHeight * 2 / 5));
            alert.setTitle("提示信息");
            alert.show();

            no.setOnAction(e -> {
                alert.close();
            });

            yes.setOnAction(e -> {
                String newCourse = textField.getText();
                File f1 = new File("./data/" + StaticValue.userName + "/资源/" + course);
                File f2 = new File("./data/" + StaticValue.userName + "/资源/" + newCourse);
                alert.close();
                f1.renameTo(f2);
                new CourseAlert("SuccessEdit");
                new Course();
            });
        }
    }
}