import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.InputMethodTextRun;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;

/**
 * @author maple826
 * 登陆类.
 * <p>
 *     创建登录页面 <br>
 *     具有注册、登录功能
 * </p>
 */
public class Login{
    public static Stage stage;
    Label iconLabel = new LoginIconLabel().getLoginIconLabel();
    /**
     * BorderPane内部分为上下左右中五个区域
     */
    BorderPane loginPane = new BorderPane();
    TextField loginName = new TextField();
    PasswordField loginPwd = new PasswordField();
    Button registerButton = new RegisterButton(loginName,loginPwd).getRegisterButton();
    Button loginButton = new LoginButton(loginName,loginPwd).getLoginButton();
    /**
     * FlowPane内部水平排布
     */
    FlowPane loginNamePane = new FlowPane(new LoginNameText().getLoginNameText(),loginName);
    FlowPane loginPwdPane = new FlowPane(new LoginPwdText().getLoginPwdText(),loginPwd);
    FlowPane loginBlankPane1 = new FlowPane(new Text(""));
    FlowPane loginBlankPane2 = new FlowPane(new Text(""));
    FlowPane buttonPane = new FlowPane(registerButton,loginButton);
    /**
     * VBox内部竖直排布
     */
    VBox loginVBox = new VBox(loginNamePane,loginBlankPane1,loginPwdPane,loginBlankPane2,buttonPane);
    /**
     * 动态壁纸切换常量
     */
    private static int WALLPAPER_NUM = 1;

    /**
     * 构造函数.
     * <p>
     *     设置UI样式 <br>
     *     创建登陆界面
     * </p>
     * @param stage
     */
    public Login(Stage stage) {
        this.stage = stage;
        setStyle();
        createLogin();
    }

    /**
     * 创建登陆界面.
     * <p>
     *     调整组件位置 <br>
     *     设置标题，展示界面
     * </p>
     */
    private void createLogin() {
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

    /**
     * 设置UI样式.
     * <p>
     *     背景颜色为 #203A97 <br>
     *     字体颜色为 #F5CA2A <br>
     *     鼠标移动到登录、注册按钮⑩，相应按钮颜色、大小将会改变
     * </p>
     */
    private void setStyle() {
        stage.getIcons().add(new Image("img/icon.png"));
        new Thread(() -> {
            while (true){
                String image = "/img/login" + String.valueOf(WALLPAPER_NUM) + ".jpg";
                loginPane.setStyle("-fx-background-image: url("+ image +");" +
                        "-fx-background-size: cover");
                WALLPAPER_NUM = (WALLPAPER_NUM + 1) % 3;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        loginNamePane.setStyle("‐fx‐background‐color: transparent;");
        loginNamePane.setOpacity(0.7);
        loginPwdPane.setStyle("‐fx‐background‐color: transparent;");
        loginPwdPane.setOpacity(0.7);
        loginBlankPane1.setStyle("‐fx‐background‐color: transparent;");
        loginBlankPane1.setOpacity(0.7);
        loginBlankPane2.setStyle("‐fx‐background‐color: transparent;");
        loginBlankPane2.setOpacity(0.7);
        buttonPane.setStyle("‐fx‐background‐color: transparent;");
        buttonPane.setOpacity(0.7);

        loginName.setStyle("-fx-font-size: 14;");
        loginPwd.setStyle("-fx-font-size: 14");

        String buttonStyle1 = StaticValue.buttonStyle1 + "-fx-font-size: 16";
        String buttonStyle2 = StaticValue.buttonStyle2 + "-fx-font-size: 20";
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

/**
 * 学业助理图标.
 */
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

/**
 * 用户名.
 */
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

/**
 * 密码.
 */
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

/**
 * 注册按钮.
 * <p>
 *     点击后首先判断用户名是否存在，存在则提示，否则在data目录下新建该用户
 * </p>
 */
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
                f.setReadable(false);
                f.setWritable(false);
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

/**
 * 登录按钮.
 * <p>
 *     点击后会验证用户名是否存在及密码是否正确，正确则进入Home界面，否则提示用户名不存在或密码错误
 * </p>
 */
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
                File f1 = new File("./data/" + name + "/pwd.txt");
                f1.setReadable(true);
                FileReader reader = new FileReader("./data/" + name + "/pwd.txt");
                char[] buf = new char[1024];
                int len = reader.read(buf);
                f1.setReadable(false);
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
/**
 * 提示信息.
 */
class LoginAlert {
    /**
     * 构造函数.
     * @param s 提示信息内容
     */
    LoginAlert(String s) {
        Stage alert = new Stage();
        alert.setResizable(false);
        alert.getIcons().add(new Image("/img/light_bulb.png"));
        alert.setHeight(200);
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-image: url('/img/dontforget.jpg');" +
                "-fx-background-size: cover");
        Text text = new Text(s);
        text.setFont(Font.font("宋体",FontWeight.BOLD,20));
        text.setFill(Color.rgb(32,34,151));
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
