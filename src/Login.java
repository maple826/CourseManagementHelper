import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodTextRun;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;

public class Login{
    public static Stage stage;
    Label iconLabel = new LoginIconLabel().getLoginIconLabel();
    //BorderPane内部组件按Top，Bottom，Left，Right，Center排布
    BorderPane loginPane = new BorderPane();
    TextField loginName = new TextField();
    PasswordField loginPwd = new PasswordField();
    Button registerButton = new RegisterButton(loginName,loginPwd).getRegisterButton();
    Button loginButton = new LoginButton(loginName,loginPwd).getLoginButton();
    //FlowPane内部组件按行排布
    //两个Pane分别放置用户名及输入框，密码及输入框
    FlowPane loginNamePane = new FlowPane(new LoginNameText().getLoginNameText(),loginName);
    FlowPane loginPwdPane = new FlowPane(new LoginPwdText().getLoginPwdText(),loginPwd);
    //空白占位，使用户名、密码间，密码、按钮间存在一行的空隙
    FlowPane loginBlankPane1 = new FlowPane(new Text(""));
    FlowPane loginBlankPane2 = new FlowPane(new Text(""));
    //放置按钮组件
    FlowPane buttonPane = new FlowPane(registerButton,loginButton);
    //VBox内部按列排布
    VBox loginVBox = new VBox(loginNamePane,loginBlankPane1,loginPwdPane,loginBlankPane2,buttonPane);

    public Login(Stage stage) {
        this.stage = stage;
        setStyle();
        createLogin();
    }

    private void createLogin() {
        //setAlignment定义组件在BorderPane内部五种位置之一的相对位置，例如：BorderPane的Top的CENTER
        //setMargin设置组件周围间距
        loginNamePane.setAlignment(Pos.CENTER);
        loginBlankPane1.setAlignment(Pos.CENTER);
        loginPwdPane.setAlignment(Pos.CENTER);
        loginBlankPane1.setAlignment(Pos.CENTER);
        buttonPane.setAlignment(Pos.CENTER);

        loginPane.setTop(iconLabel);
        loginPane.setAlignment(iconLabel, Pos.CENTER);
        loginPane.setMargin(iconLabel,
                new Insets(StaticValue.stageWidth / 12,0,StaticValue.stageWidth / 12,0));
        buttonPane.setMargin(loginButton,new Insets(0,0,0,StaticValue.stageWidth / 9));

        loginPane.setCenter(loginVBox);

        stage.setScene(new Scene(loginPane,StaticValue.stageWidth,StaticValue.stageHeight));
        stage.setTitle("学业助理");
        stage.show();
    }

    private void setStyle() {
        //loginVBox.setStyle("-fx-background-image: url('"+ "../image/img.png" + "')");
        loginPane.setStyle("-fx-background-color: #203A97");

        loginName.setStyle("-fx-font-size: 14;");
        loginPwd.setStyle("-fx-font-size: 14");

        String buttonStyle1 = StaticValue.buttonStyle1 + "-fx-font-size: 13";
        String buttonStyle2 = StaticValue.buttonStyle2 + "-fx-font-size: 16";
        registerButton.setStyle(buttonStyle1);
        loginButton.setStyle(buttonStyle1);
        registerButton.setOnMouseMoved(e -> {
            registerButton.setStyle(buttonStyle2);
        });
        registerButton.setOnMouseExited(e -> {
            registerButton.setStyle(buttonStyle1);
        });
        loginButton.setOnMouseMoved(e -> {
            loginButton.setStyle(buttonStyle2);
        });
        loginButton.setOnMouseExited(e -> {
            loginButton.setStyle(buttonStyle1);
        });

    }
}

class LoginIconLabel {
    private Label iconLable = new Label("学业助理");
    Label getLoginIconLabel() {
        return this.iconLable;
    }
    LoginIconLabel() {
        iconLable.setFont(Font.font("华文行楷", FontWeight.BOLD,50));
        iconLable.setTextFill(Color.rgb(245,202,42));
    }
}

class LoginNameText {
    private Text loginNameText = new Text("用户名 ");
    Text getLoginNameText() {
        return this.loginNameText;
    }
    LoginNameText() {
        loginNameText.setFont(Font.font("宋体",20));
        loginNameText.setFill(Color.rgb(245,202,42));
    }
}

class LoginPwdText {
    private Text loginPwdText = new Text("密码    ");
    Text getLoginPwdText() {
        return this.loginPwdText;
    }
    LoginPwdText() {
        loginPwdText.setFont(Font.font("宋体",20));
        loginPwdText.setFill(Color.rgb(245,202,42));
    }
}

class RegisterButton {
    private Button registerButton = new Button("注册");
    RegisterButton(TextField na,TextField pw) {
        registerButton.setOnAction(e -> {
            String name = na.getText(),pwd = pw.getText();
            File f = new File("./data/" + name);
            if(f.exists()) {
                new LoginAlert("用户名已存在！");
                return;
            }
            f.mkdir();
            f = new File("./data/" + name + "/pwd.txt");
            try {
                f.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                FileWriter writer = new FileWriter(f);
                writer.write(pwd);
                writer.flush();
                new LoginAlert("注册成功！");
                f = new File("./data/" + name + "/ddl.txt");
                f.createNewFile();
                f = new File("./data/" + name + "/Memorandum.txt");
                f.createNewFile();
                f = new File("./data/" + name + "/资源");
                f.mkdir();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    Button getRegisterButton() {
        return this.registerButton;
    }
}

class LoginButton {
    private Button loginButton = new Button("登录");
    LoginButton(TextField na,TextField pw) {
        loginButton.setOnAction(e -> {
            String name = na.getText(),pwd = pw.getText();
            File f = new File("./data/" + name);
            if(!f.exists()) {
                new LoginAlert("用户名不存在！");
                return;
            }
            try {
                FileReader reader = new FileReader("./data/" + name + "/pwd.txt");
                char[] buf = new char[1024];
                int len = reader.read(buf);
                if(!new String(buf,0,len).equals(pwd)) {
                    new LoginAlert("密码错误！");
                    return;
                }
                StaticValue.userName = name;
                new Home();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    Button getLoginButton() {
        return this.loginButton;
    }
}

class LoginAlert {
    LoginAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示信息");
        alert.setHeaderText("");
        alert.setContentText(s);
        alert.initOwner(Login.stage);
        alert.show();
    }
}
