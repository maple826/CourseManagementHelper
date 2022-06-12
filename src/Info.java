import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;

/**
 * 个人信息类.
 * 包括修改密码功能
 */
public class Info {
    Info() {
        createInfo();
    }

    /**
     * 使用{@link StaticValue#topHbox}作为顶端部件 <br>
     * 使用{@link CenterChangePwd#getvBox()}作为中心部件 <br>
     * 设置标题、大小并显示
     */
    private void createInfo() {
        BorderPane pane = new BorderPane();
        pane.setTop(StaticValue.getNewTopHBox());
        VBox vBox = new CenterChangePwd().getvBox();
        pane.setCenter(vBox);
        StaticValue.stage.setScene(new Scene(pane,StaticValue.stageWidth,StaticValue.stageHeight));
        StaticValue.stage.setTitle("学业助理");
        StaticValue.stage.show();
    }
}
/**
 * 修改密码类.
 */
class CenterChangePwd {
    VBox vBox = new VBox();
    Label title = new Label("修改密码");
    Label oldPwdLabel = new Label("旧密码");
    Label newPwdLabel = new Label("新密码");
    Label newPwdLabel2 = new Label("再次输入新密码");
    PasswordField oldPwd = new PasswordField();
    PasswordField newPwd = new PasswordField();
    PasswordField newPwd2 = new PasswordField();
    Button button = new Button("修改密码");
    VBox pwdVBox = new VBox(oldPwdLabel,oldPwd,newPwdLabel,newPwd,newPwdLabel2,newPwd2);

    CenterChangePwd() {
        setStyle();

        vBox.getChildren().addAll(title,pwdVBox,button);

        pwdVBox.setSpacing(StaticValue.stageHeight / 50);
        vBox.setSpacing(StaticValue.stageHeight / 25);
        vBox.setMargin(title,new Insets(StaticValue.stageHeight / 10,0,0,0));
        vBox.setMargin(pwdVBox,new Insets(0,StaticValue.stageWidth / 3,
                0,StaticValue.stageWidth / 3));
        vBox.setMargin(button,new Insets(0,0,0,StaticValue.stageWidth * 4 / 9));

        button.setOnAction(e -> {
            String old = oldPwd.getText();
            String new1 = newPwd.getText();
            String new2 = newPwd2.getText();
            String pwd = "";
            try {
                File f = new File("./data/" + StaticValue.userName + "/pwd.txt");
                f.setReadable(true);
                FileReader reader = new FileReader("./data/" + StaticValue.userName + "/pwd.txt");
                char[] buf = new char[1024];
                int len = reader.read(buf);
                f.setReadable(false);
                pwd = new String(buf,0,len);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if(!pwd.equals(old)) {
                new InfoAlert("原密码错误！");
                return;
            }
            if(!new1.equals(new2)) {
                new InfoAlert("两次输入密码不一致！");
                return;
            }
            if(new1.equals(old)) {
                new InfoAlert("请勿设置相同的密码！");
                return;
            }
            FileWriter writer = null;
            try {
                File f = new File("./data/" + StaticValue.userName + "/pwd.txt");
                f.setWritable(true);
                writer = new FileWriter(f);
                writer.write(new1);
                writer.flush();
                f.setWritable(false);
                new InfoAlert("修改成功！");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void setStyle() {
        title.setFont(Font.font("方正姚体", 26));
        title.setTextFill(Color.rgb(245,202,42));

        oldPwdLabel.setFont(Font.font("宋体",18));
        oldPwdLabel.setTextFill(Color.rgb(245,202,42));
        newPwdLabel.setFont(Font.font("宋体",18));
        newPwdLabel.setTextFill(Color.rgb(245,202,42));
        newPwdLabel2.setFont(Font.font("宋体",18));
        newPwdLabel2.setTextFill(Color.rgb(245,202,42));

        vBox.setStyle("-fx-background-color: #203A97");

        button.setStyle(StaticValue.buttonStyle1 + "-fx-font-size: 13");
        button.setOnMouseMoved(e -> {
            button.setStyle(StaticValue.buttonStyle2 + "-fx-font-size: 16");
        });
        button.setOnMouseExited(e -> {
            button.setStyle(StaticValue.buttonStyle1 + "-fx-font-size: 13");
        });
    }

    VBox getvBox() {
        return this.vBox;
    }
}

/**
 * 个人信息提示类.
 */
class InfoAlert {
    /**
     * 显示s作为提示内容
     * @param s
     */
    InfoAlert(String s) {
        Stage alert = new Stage();
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: rgb(32,34,151)");
        Text text = new Text(s);
        text.setFont(Font.font("宋体",20));
        text.setFill(Color.rgb(245,202,42));
        Button button = new Button("确定");
        button.setStyle(StaticValue.buttonStyle1 + "-fx-font-size: 16");
        button.setOnAction(e -> {
            alert.close();
        });
        button.setOnMouseMoved(e -> {
            button.setStyle(StaticValue.buttonStyle2 + "-fx-font-size: 16");
        });
        button.setOnMouseExited(e -> {
            button.setStyle(StaticValue.buttonStyle1 + "-fx-font-size: 16");
        });
        pane.setCenter(text);
        pane.setBottom(button);
        pane.setMargin(button,new Insets(0,0,20,130));
        alert.setTitle("提示信息");
        alert.setScene(new Scene(pane,300,160));
        alert.show();
    }
}