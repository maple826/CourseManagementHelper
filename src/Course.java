import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;

public class Course {
    static Stage stage;
    BorderPane coursePane = new BorderPane();
    Label courseIconLabel = new CourseIconLabel().getIconLabel();
    Label courseUserName = new CourseUserName().getUserName();
    Button courseExitButton = new CourseExitButton().getExit();
    Button courseAddButton = new CourseAddButton().getAdd();
    HBox courseTopHBox = new CourseTopHBox(courseUserName,courseIconLabel,courseExitButton).gethBox();
    Line topLine = new Line(0,StaticValue.stageHeight / 7,
            StaticValue.stageWidth,StaticValue.stageHeight / 7);
    VBox courseLeftVBox = new CourseLeftVBox().getvBox();

    public Course(Stage stage) {
        this.stage = stage;
        createCourse();
    }
    private void createCourse() {
        coursePane.setTop(courseTopHBox);
        coursePane.setBottom(courseAddButton);
        coursePane.setLeft(new ScrollPane(courseLeftVBox));
        coursePane.getChildren().add(topLine);
        stage.setScene(new Scene(coursePane,StaticValue.stageWidth,StaticValue.stageHeight));
        stage.setTitle("学习小帮手");
        stage.show();
    }
}

class CourseIconLabel {
    private Label iconLable = new Label("学习小帮手");
    Label getIconLabel() {
        return this.iconLable;
    }
    CourseIconLabel() {
        iconLable.setFont(Font.font("华文行楷", FontWeight.BOLD,36));
        iconLable.setTextFill(Color.BLUE);
    }
}

class CourseUserName {
    private Label userName = new Label(StaticValue.userName);
    Label getUserName() {
        return this.userName;
    }
    CourseUserName() {
        userName.setFont(Font.font("黑体", FontWeight.BOLD,18));
    }
}

class CourseExitButton {
    private Button exit = new Button("退出");
    Button getExit() {
        return this.exit;
    }
    CourseExitButton() {
        exit.setFont(Font.font("黑体", FontWeight.BOLD,18));
        exit.setTextFill(Color.RED);
        exit.setOnAction(e -> {
            new CourseAlert("Exit");
        });
    }
}

class CourseTopHBox {
    private HBox hBox = new HBox();
    HBox gethBox() {
        return this.hBox;
    }
    CourseTopHBox(Label userName,Label icon,Button exit) {
        hBox.getChildren().addAll(userName,icon,exit);
        hBox.setMargin(userName,new Insets(StaticValue.stageHeight / 25));
        hBox.setMargin(icon,new Insets(StaticValue.stageHeight / 40,0,
                StaticValue.stageHeight / 45,StaticValue.stageHeight * 2 / 5));
        hBox.setMargin(exit,new Insets(StaticValue.stageHeight / 25,0,
                StaticValue.stageHeight / 25,StaticValue.stageWidth * 2 / 7));
    }
}

class CourseLeftVBox {
    private VBox vBox = new VBox();
    VBox getvBox() {
        return this.vBox;
    }
    CourseLeftVBox() {
        vBox.setSpacing(StaticValue.stageHeight / 20);
        File f = new File("./data/" + StaticValue.userName);
        File[] tmp = f.listFiles();
        String[] course = new String[tmp.length - 1];
        for(int i = 0,j = 0;i < tmp.length;i++) {
            if (tmp[i].getName().equals("pwd.txt")) continue;
            course[j++] = tmp[i].getName();
        }
        Arrays.sort(course);
        Button[] courseButton = new Button[course.length];
        for(int i = 0;i < course.length;i++) {
            courseButton[i] = new Button(course[i]);
            courseButton[i].setFont(Font.font("宋体",24));
            vBox.getChildren().add(courseButton[i]);
        }
    }
}

class CourseAddButton {
    private Button add = new Button("添加");
    Button getAdd() {
        return this.add;
    }
    CourseAddButton() {
        add.setFont(Font.font("黑体", FontWeight.BOLD,18));
        add.setOnAction(e -> {
            new CourseAlert("Add");
        });
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
            alert.initOwner(Course.stage);
            alert.show();
            alert.setOnCloseRequest(e -> {
                ButtonType result = alert.getResult();
                if (result != null && result.equals(buttonYes)) {
                    StaticValue.userName = "";
                    new Login(Course.stage);
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
        else if(s.equals("Success")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示信息");
            alert.setHeaderText("");
            alert.setContentText("添加成功！");
            alert.show();
            new Course(Course.stage);
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
                File f = new File("./data/" + StaticValue.userName + "/" + course);
                if(f.exists()) {
                    alert.close();
                    new CourseAlert("Exist");
                    System.out.println("Exist");
                    return;
                }
                f.mkdir();
                new File("./data/" + StaticValue.userName + "/" + course + "/ddl").mkdir();
                new File("./data/" + StaticValue.userName + "/" + course + "/资源").mkdir();
                alert.close();
                new CourseAlert("Success");
            });
        }
    }
}