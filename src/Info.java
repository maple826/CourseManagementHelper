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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.*;

public class Info {
    Info() {
        createInfo();
    }

    private void createInfo() {
        BorderPane pane = new BorderPane();
        pane.setTop(StaticValue.getNewTopHBox());
        VBox vBox = new CenterChangePwd().getvBox();
        pane.setCenter(vBox);
        StaticValue.stage.setScene(new Scene(pane,StaticValue.stageWidth,StaticValue.stageHeight));
        StaticValue.stage.setTitle("学习小帮手");
        StaticValue.stage.show();
    }
}
class CenterChangePwd {
    private VBox vBox = new VBox();
    CenterChangePwd() {
        Label title = new Label("修改密码");
        title.setFont(Font.font("方正姚体", 22));
        Label oldPwdLabel = new Label("旧密码");
        Label newPwdLabel = new Label("新密码");
        Label newPwdLabel2 = new Label("再次输入新密码");
        PasswordField oldPwd = new PasswordField();
        PasswordField newPwd = new PasswordField();
        PasswordField newPwd2 = new PasswordField();
        Button button = new Button("修改密码");
        VBox pwdVBox = new VBox(oldPwdLabel,oldPwd,newPwdLabel,newPwd,newPwdLabel2,newPwd2);
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
                FileReader reader = new FileReader("./data/" + StaticValue.userName + "/pwd.txt");
                char[] buf = new char[1024];
                int len = reader.read(buf);
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
                writer = new FileWriter(new File("./data/" + StaticValue.userName + "/pwd.txt"));
                writer.write(new1);
                writer.flush();
                new InfoAlert("修改成功！");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    VBox getvBox() {
        return this.vBox;
    }
}

class InfoAlert {
    InfoAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示信息");
        alert.setHeaderText("");
        alert.setContentText(s);
        alert.show();
    }
}