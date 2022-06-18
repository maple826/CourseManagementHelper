import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
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
/**
 * @author maple826
 * @author AboveParadise
 * 课程资料类
 * <p>
 *     用户选择 “课程资料” 按钮后的界面 <br>
 *     具有课程添加、删除、编辑功能 <br>
 *     切换课程时随机更换BUAA背景图
 * </p>
 */
public class Course {
    static Stage stage;
    BorderPane coursePane = new BorderPane();
    VBox courseTopVBox = StaticValue.getNewTopVBox();
    ScrollPane courseLeftScrollPane = new CourseLeftScrollPane().getPane();
    /**
     * 构造函数.
     * <p>
     *     创建界面
     * </p>
     */
    public Course() {
        this.stage = StaticValue.stage;
        createCourse();
    }
    /**
     * 创建课程界面.
     * <p>
     *     调整组件位置
     * </p>
     */
    private void createCourse() {

        courseLeftScrollPane.setStyle("-fx-background-color: transparent;");
        coursePane.setTop(courseTopVBox);
        coursePane.setLeft(courseLeftScrollPane);
        StaticValue.set_bkg_pic("./src/img/course_bkg",coursePane);
//        coursePane.setStyle("-fx-background-image: url('/img/course_bkg/8.jpg');"+"-fx-background-size: cover;");
        stage.setScene(new Scene(coursePane,StaticValue.stageWidth,StaticValue.stageHeight));
        stage.setTitle("学习小帮手");
        stage.show();
    }
}
/**
 * 左侧滚动栏类.
 */
class CourseLeftScrollPane {
    private ScrollPane pane = new ScrollPane();
    private VBox vBox = new VBox();
    private ContextMenu menu;
    /**
     * 构造函数.
     * <p>
     *     设置右键添加课程功能 <br>
     *     创建界面
     * </p>
     */
    CourseLeftScrollPane() {

        vBox.setStyle("-fx-background-color: transparent;");
        vBox.setSpacing(StaticValue.stageHeight / 20);
        Text text_course = new Text("课程");
        text_course.setFont(Font.font("华文行楷", FontWeight.BOLD, 30));
        text_course.setFill(Color.rgb(245,202,42));
        vBox.getChildren().add(text_course);
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
//        pane.setFitToHeight(true);
//        pane.setFitToWidth(true);
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
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.getStylesheets().add("bkg.css");

//        pane.setStyle("-fx-background-color: transparent;");
//        pane.setOpacity(0.7);//0为完全透明
    }
    /**
     * 获取该滚动栏
     */
    public ScrollPane getPane() {
        return pane;
    }
}
/**
 * 课程按钮类.
 */
class CourseButton {
    private String course;
    private Button button;
    private ContextMenu menu;
    /**
     * 构造函数.
     * 根据传入的课程名建立button
     * @param course 当前课程
     */
    public CourseButton(String course) {
        this.course = course;
        MenuItem itemDel = new MenuItem("删除");
        MenuItem itemEdit = new MenuItem("编辑");

        button = new Button(course);
        button.setFont(Font.font("宋体",24));
        button.setStyle(StaticValue.buttonStyle2 + "-fx-font-size: 21");
        menu = new ContextMenu();

        menu.getItems().addAll(itemDel,itemEdit);

        //当鼠标进入按钮时添加阴影特效
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(67,173,217));
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            button.setEffect(shadow);
        });
        //当鼠标离开按钮时移除阴影效果
        button.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            button.setEffect(null);
        });
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
/**
 * 提示信息类.
 */
class CourseAlert {
    /**
     * 构造函数.
     * @param s 指令
     */
    CourseAlert(String s) {
        if(s.equals("Exit")) {
            Text text = new Text("您确定要退出吗？");
            text.setFont(Font.font("宋体",18));
            Button yes = new Button("确定");
            Button no = new Button("取消");
            yes.setFont(Font.font("Microsoft YaHei", 18));
            no.setFont(Font.font("Microsoft YaHei", 18));
            yes.setPadding(new Insets(10));
            no.setPadding(new Insets(10));
            String buttonStyle1 = StaticValue.buttonStyle1 + "-fx-font-size: 18";
            yes.setStyle(buttonStyle1);
            no.setStyle(buttonStyle1);
            HBox hBox1 = new HBox(text);
            HBox hBox2 = new HBox(yes,no);
            hBox2.setAlignment(Pos.CENTER);
            hBox2.setSpacing(StaticValue.stageHeight / 24);
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(hBox1);
            borderPane.setBottom(hBox2);
            text.setFont(Font.font("宋体",FontWeight.BOLD,20));
            borderPane.setMargin(hBox1,new Insets(40,0,0,80));
            borderPane.setStyle("-fx-background-image: url('/img/alert.png');" +
                    "-fx-background-size: cover");
            Stage alert = new Stage();
            alert.setResizable(false);
            alert.getIcons().add(new Image("/img/light_bulb.png"));
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setScene(new Scene(borderPane,300,160));
            alert.setTitle("提示信息");
            alert.show();
            yes.setOnMouseMoved(e -> {
                yes.setStyle(StaticValue.buttonStyle2 + "-fx-font-size: 18");
            });
            yes.setOnMouseExited(e -> {
                yes.setStyle(StaticValue.buttonStyle1 + "-fx-font-size: 18");
            });
            no.setOnMouseMoved(e -> {
                no.setStyle(StaticValue.buttonStyle2 + "-fx-font-size: 18");
            });
            no.setOnMouseExited(e -> {
                no.setStyle(StaticValue.buttonStyle1 + "-fx-font-size: 18");
            });
            yes.setOnAction(e -> {
                StaticValue.userName = "";
                alert.close();
                new Login();
            });
            no.setOnAction(e -> {
                alert.close();
            });
        }
        else if(s.equals("Exist")) {
            new LoginAlert("该课程已存在！");
        }
        //        提示信息——添加成功
        else if(s.equals("SuccessAdd")) {
            new Course();
            new LoginAlert("添加成功！");
        }
        //        提示信息——编辑成功
        else if(s.equals("SuccessEdit")) {
            new Course();
            new LoginAlert("更改成功！");
        }
        else if(s.equals("Wrong")){
//            借用Login.java中生成提升窗口的功能
            new LoginAlert("请输入正确的课程名！");
        }
        else if(s.equals("Add")) {
            Text text = new Text("请输入课程名称：");
            text.setFont(Font.font("宋体",18));
            TextField textField = new TextField();
            Button yes = new Button("确定");
            Button no = new Button("取消");
            yes.setFont(Font.font("Microsoft YaHei", 18));
            no.setFont(Font.font("Microsoft YaHei", 18));
            yes.setPadding(new Insets(10));
            no.setPadding(new Insets(10));
            String buttonStyle1 = StaticValue.buttonStyle1 + "-fx-font-size: 18";
            yes.setStyle(buttonStyle1);
            no.setStyle(buttonStyle1);
            HBox hBox1 = new HBox(text,textField);
            HBox hBox2 = new HBox(yes,no);
            hBox2.setAlignment(Pos.CENTER);
            hBox2.setSpacing(StaticValue.stageHeight / 24);
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(hBox1);
            borderPane.setBottom(hBox2);
            borderPane.setStyle("-fx-background-image: url('/img/alert.png');" +
                    "-fx-background-size: cover");
            borderPane.setMargin(hBox1,new Insets(30));
            Stage alert = new Stage();
            alert.getIcons().add(new Image("/img/light_bulb.png"));
            alert.setHeight(170);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setScene(new Scene(borderPane,StaticValue.stageWidth * 2 / 5,StaticValue.stageHeight * 2 / 5));
            alert.setTitle("提示信息");
            alert.show();
            yes.setOnMouseMoved(e -> {
                yes.setStyle(StaticValue.buttonStyle2 + "-fx-font-size: 18");
            });
            yes.setOnMouseExited(e -> {
                yes.setStyle(StaticValue.buttonStyle1 + "-fx-font-size: 18");
            });
            no.setOnMouseMoved(e -> {
                no.setStyle(StaticValue.buttonStyle2 + "-fx-font-size: 18");
            });
            no.setOnMouseExited(e -> {
                no.setStyle(StaticValue.buttonStyle1 + "-fx-font-size: 18");
            });
            no.setOnAction(e -> {
                alert.close();
            });

            yes.setOnAction(e -> {
                String course = textField.getText();
                if(course.equals("")){
                    new LoginAlert("请输入正确的课程名！");
                    return;
                }
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
    /**
     * 构造函数.
     * 实现编辑、删除功能
     * @param course 课程
     * @param s 指令
     */
    CourseAlert(String s,String course) {
        if(s.equals("Delete")) {
            Text text = new Text("确定要删除课程：" + course + " 吗？");
            text.setFont(Font.font("宋体",18));
            Button yes = new Button("确定");
            Button no = new Button("取消");
            yes.setFont(Font.font("Microsoft YaHei", 18));
            no.setFont(Font.font("Microsoft YaHei", 18));
            yes.setPadding(new Insets(10));
            no.setPadding(new Insets(10));
            String buttonStyle1 = StaticValue.buttonStyle1 + "-fx-font-size: 18";
            yes.setStyle(buttonStyle1);
            no.setStyle(buttonStyle1);
            HBox hBox1 = new HBox(text);
            HBox hBox2 = new HBox(yes,no);
            hBox2.setAlignment(Pos.CENTER);
            hBox2.setSpacing(StaticValue.stageHeight / 24);
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(hBox1);
            borderPane.setBottom(hBox2);
            borderPane.setStyle("-fx-background-image: url('/img/alert.png');" +
                    "-fx-background-size: cover");
            borderPane.setMargin(hBox1,new Insets(30));
            Stage alert = new Stage();
            alert.getIcons().add(new Image("/img/light_bulb.png"));
            alert.setHeight(170);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setScene(new Scene(borderPane,StaticValue.stageWidth * 2 / 5,StaticValue.stageHeight * 2 / 5));
            alert.setTitle("提示信息");
            alert.show();
            yes.setOnMouseMoved(e -> {
                yes.setStyle(StaticValue.buttonStyle2 + "-fx-font-size: 18");
            });
            yes.setOnMouseExited(e -> {
                yes.setStyle(StaticValue.buttonStyle1 + "-fx-font-size: 18");
            });
            no.setOnMouseMoved(e -> {
                no.setStyle(StaticValue.buttonStyle2 + "-fx-font-size: 18");
            });
            no.setOnMouseExited(e -> {
                no.setStyle(StaticValue.buttonStyle1 + "-fx-font-size: 18");
            });
            yes.setOnAction(e -> {
                try {
                    StaticValue.deleteFile(new File("./data/" + StaticValue.userName + "/资源/" + course));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                new Course();
                alert.close();
                new LoginAlert("删除成功！");
            });
            no.setOnAction(event -> {
                alert.close();
            });
        }
        else if(s.equals("Edit")) {
            Text text = new Text("请输入新课程名称：");
            text.setFont(Font.font("宋体",18));
            TextField textField = new TextField();
            Button yes = new Button("确定");
            Button no = new Button("取消");
            yes.setFont(Font.font("Microsoft YaHei", 18));
            no.setFont(Font.font("Microsoft YaHei", 18));
            String buttonStyle1 = StaticValue.buttonStyle1 + "-fx-font-size: 18";
            yes.setStyle(buttonStyle1);
            no.setStyle(buttonStyle1);
            yes.setPadding(new Insets(10));
            no.setPadding(new Insets(10));
            HBox hBox1 = new HBox(text,textField);
            HBox hBox2 = new HBox(yes,no);
            hBox2.setSpacing(StaticValue.stageHeight / 24);
            hBox2.setAlignment(Pos.CENTER);
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(hBox1);
            borderPane.setBottom(hBox2);
            borderPane.setMargin(hBox1,new Insets(30));
            borderPane.setStyle("-fx-background-image: url('/img/alert.png');" +
                    "-fx-background-size: cover");
            Stage alert = new Stage();
            alert.getIcons().add(new Image("/img/light_bulb.png"));
            alert.setHeight(2/5 * StaticValue.stageHeight);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setScene(new Scene(borderPane,StaticValue.stageWidth * 2 / 5,StaticValue.stageHeight * 2 / 5));
            alert.setTitle("提示信息");
            alert.setHeight(180);
            alert.show();
            yes.setOnMouseMoved(e -> {
                yes.setStyle(StaticValue.buttonStyle2 + "-fx-font-size: 18");
            });
            yes.setOnMouseExited(e -> {
                yes.setStyle(StaticValue.buttonStyle1 + "-fx-font-size: 18");
            });
            no.setOnMouseMoved(e -> {
                no.setStyle(StaticValue.buttonStyle2 + "-fx-font-size: 18");
            });
            no.setOnMouseExited(e -> {
                no.setStyle(StaticValue.buttonStyle1 + "-fx-font-size: 18");
            });
            no.setOnAction(e -> {
                alert.close();
            });

            yes.setOnAction(e -> {
                String newCourse = textField.getText();
                if(newCourse.equals("")){
                    new CourseAlert("Wrong");
                    return;
                }
                File f1 = new File("./data/" + StaticValue.userName + "/资源/" + course);
                File f2 = new File("./data/" + StaticValue.userName + "/资源/" + newCourse);
                if(f2.exists()) {
                    alert.close();
                    new CourseAlert("Exist");
                    return;
                }
                alert.close();
                f1.renameTo(f2);
//                new CourseAlert("SuccessEdit");
                new Course();
                new LoginAlert("更改成功！");
            });
        }
    }
}