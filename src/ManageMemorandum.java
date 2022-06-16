import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ShawnZXC
 * 管理备忘录的类
 * <p>
 *     展示和管理备忘录
 * </p>
 */
public class ManageMemorandum {
    /**
     * <p>设置被放入scene的界面</p>
     */
    public static Stage stage =StaticValue.stage;
    /**
     * <p>这个List存放备忘录数据</p>
     */
    public static ArrayList<Memorandum> MemoList=new ArrayList<Memorandum>();
    /**
     * <p>当前备忘录，用于确定哪一条在centerPane中显示</p>
     */
    public static Memorandum currentMemorandum;

    /**
     * <p>从文件中读取备忘录信息</p>
     * @throws FileNotFoundException
     * @throws ParseException
     */
    public static void read() throws FileNotFoundException, ParseException {
        MemoList.clear();
        FileReader reader = new FileReader("./data/" + StaticValue.userName + "/Memorandum.txt");
        char[] buf=new char[1024*1024];
        try {
            reader.read(buf);
        }
        catch(Exception e){
            System.out.println(e);
        }
        String row_data=new String(buf);

        /**
         * <p>
         *     不同信息是用“###”分隔的，不仅不同备忘录是用###分隔的，一条备忘录中的不同部分也是###分隔
         * </p>
         */
        String[] splitData=row_data.split("###");

        for(int i=0;i<splitData.length-1;i+=3){
            Memorandum newMemorandum=new Memorandum(splitData[i],splitData[i+1],splitData[i+2]);
            MemoList.add(newMemorandum);
        }

        /**
         * <p>
         *     按照最近修改时间排序，最晚修改的在第一条
         * </p>
         */
        Collections.sort(MemoList, new Comparator<Memorandum>() {
            @Override
            public int compare(Memorandum o1, Memorandum o2) {
                if(o1.getLastModifiedTime().compareTo(o2.getLastModifiedTime())>=0){
                    return -1;
                }
                else return 1;
            }
        });

        /**
         * <p>
         *     每次重新加载页面时显示最近一次编辑的备忘录
         * </p>
         */
        if(currentMemorandum==null) {
            if(MemoList.size()>0) {
                currentMemorandum = MemoList.get(0);
            }
        }
    }

    /**
     * <p>把修改过的备忘录信息写回文件</p>
     * @throws IOException
     */
    public static void write() throws IOException {
        ManageMemorandum.currentMemorandum=null;
        FileWriter writer = new FileWriter("./data/" + StaticValue.userName + "/Memorandum.txt");
        for(int i=0;i<MemoList.size();i++){
            if(MemoList.get(i).delete==1) continue;
            writer.write(MemoList.get(i).toString());
        }
        writer.flush();
        writer.close();
    }


    /**
     * <p>设置场景</p>
     * @throws FileNotFoundException
     * @throws ParseException
     */
    public static void setScene() throws FileNotFoundException, ParseException {
        ManageMemorandum.read();
        BorderPane mainPane=new BorderPane();
        /**
         * 点击更换背景图
         */
        StaticValue.set_bkg_pic("./src/img/ddl_memo_bkg",mainPane);
//        Date date = new Date();
//        Calendar calendar = GregorianCalendar.getInstance();
//        calendar.setTime(date);
//        int second = calendar.get(Calendar.SECOND);
//        if(second%2==1)
//            mainPane.setStyle("-fx-background-image: url('/img/duskBUAA.jpg');"+"-fx-background-size: cover;");
//        else
//            mainPane.setStyle("-fx-background-image: url('/img/daylightBUAA.jpg');"+"-fx-background-size: cover;");
        mainPane.setTop(new TopVBox().getvBox());

        mainPane.setLeft(new leftPane1().getLeftPane());
        VBox placeHolder=new VBox();
        placeHolder.setMinHeight(60);
        mainPane.setBottom(placeHolder);

        /**
         * <p>展示备忘录</p>
         */
        try {
            mainPane.setCenter(new centerPane1().getCenterPane());
        }
        catch(Exception e){
            System.out.println(e);
        }
        Scene MemoManageScene=new Scene(mainPane,StaticValue.stageWidth,StaticValue.stageHeight);
        stage.setScene(MemoManageScene);
        stage.setTitle("学业助理");
        stage.show();
    }
    ManageMemorandum() throws FileNotFoundException, ParseException {
        setScene();
    }
}


/**
 * 备忘录类
 * <p>
 *     存放备忘录信息
 * </p>
 */
class Memorandum{
    private  String name;
    private String content;
    private Date lastModifiedTime;

    /**
     * <p>
     *     删除备忘录的逻辑和删除ddl一样，delete参数设为1的话不显示不写回。
     * </p>
     */
    public int delete=0;
    HBox hbox=new HBox();

    /**
     * <p>新建和修改备忘录用这个构造函数，时间是系统当前时间</p>
     * @param name
     * @param content
     */
    Memorandum(String name,String content){
        this.name=name;
        this.content=content;
        this.lastModifiedTime=new Date();
    }

    /**
     * <p>读入备忘录用这个构造函数，时间是记录的时间</p>
     * @param date
     * @param name
     * @param content
     * @throws ParseException
     */
    Memorandum(String date,String name,String content ) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.lastModifiedTime=sdf.parse(date);
        this.name= name;
        this.content=content;
    }
    public Date getLastModifiedTime(){
        return this.lastModifiedTime;
    }
    public String getContent(){
        return this.content;
    }
    public String getName(){
        return this.name;
    }

    public HBox getHBox() {
        Label name=new Label(this.name);
        Button display =new Button("查看");
        Button delete=new Button("删除");
        Button modify=new Button("编辑");
        display.setStyle("-fx-background-color:lightskyblue;"+"-fx-font-size: 14");
        delete.setStyle("-fx-background-color:lightskyblue;"+"-fx-font-size: 14");
        modify.setStyle("-fx-background-color:lightskyblue;"+"-fx-font-size: 14");


        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(67,173,217));
        display.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            display.setEffect(shadow);
        });
        //当鼠标离开按钮时移除阴影效果
        display.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            display.setEffect(null);
        });
        delete.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            delete.setEffect(shadow);
        });
        //当鼠标离开按钮时移除阴影效果
        delete.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            delete.setEffect(null);
        });
        modify.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            modify.setEffect(shadow);
        });
        //当鼠标离开按钮时移除阴影效果
        modify.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            modify.setEffect(null);
        });




        HBox innerHBox=new HBox(display,delete,modify);
        innerHBox.setStyle("-fx-background-color: transparent;"+"-fx-font-size: 18");
        VBox innerVBox=new VBox(name,innerHBox);
        innerVBox.setStyle("-fx-background-color: transparent;"+"-fx-font-size: 18");
        hbox.getChildren().add(innerVBox);
        hbox.setAlignment(Pos.CENTER);
        hbox.setBackground(new Background(new BackgroundFill(Paint.valueOf("white"),null,null)));
        hbox.setOpacity(0.7);
        display.setOnAction(e->{
            ManageMemorandum.currentMemorandum=this;
            try {
                ManageMemorandum.setScene();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        delete.setOnAction(e->{
            new deleteMemo(this);
        });
        modify.setOnAction(e->{
            new addMemo(this);
        });
        return hbox;
    }

    @Override
    public String toString(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date=sdf.format(lastModifiedTime);
        return date+"###"+this.name+"###"+this.content+"###";
    }
}


/**
 * <p>界面中间是当前展示的备忘录</p>
 */
class centerPane1{
    ScrollPane centerPane=new ScrollPane();
    centerPane1(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Text content=new Text(ManageMemorandum.currentMemorandum.getContent());
        content.setFill(Paint.valueOf("yellow"));
        Label date=new Label(sdf.format(ManageMemorandum.currentMemorandum.getLastModifiedTime()));
        date.setAlignment(Pos.CENTER);
        date.setTextFill(Paint.valueOf("yellow"));
        Label name=new Label(ManageMemorandum.currentMemorandum.getName());
        name.setAlignment(Pos.CENTER);
        name.setTextFill(Paint.valueOf("yellow"));
        VBox vbox=new VBox(name,date,content);
        vbox.setStyle("-fx-background-color: transparent;"+"-fx-font-size: 18");
        centerPane.setContent(vbox);
//        centerPane.setOpacity(0.8);
        centerPane.getStylesheets().add("bkg.css");
    }
    ScrollPane getCenterPane(){
        return this.centerPane;
    }
}

/**
 * <p>界面左边是备忘录目录</p>
 */
class leftPane1{
    ScrollPane leftPane=new ScrollPane();
    leftPane1(){
        VBox vbox=new VBox();
        vbox.setStyle("-fx-background-color: transparent;"+"-fx-font-size: 18");
        vbox.setSpacing(20);
        for(int i=0;i<ManageMemorandum.MemoList.size();i++){
            if(ManageMemorandum.MemoList.get(i).delete==1) continue;
            vbox.getChildren().add(ManageMemorandum.MemoList.get(i).getHBox());
        }

        HBox bottomPane=new HBox();
        bottomPane.setStyle("-fx-background-color: transparent;"+"-fx-font-size: 18");
        bottomPane.setAlignment(Pos.CENTER);
        Button createButton =new Button("新建一条备忘录");
        String buttonStyle1 = "-fx-background-color:darkturquoise;"+
                "-fx-background-radius:20;";
        String buttonStyle2 = "-fx-background-color:lightskyblue;"+
                "-fx-background-radius:20;";
        createButton.setStyle(buttonStyle1 + "-fx-font-size: 13");
        createButton.setOnMouseMoved(e -> {
            createButton.setStyle(buttonStyle2 + "-fx-font-size: 16");
        });
        createButton.setOnMouseExited(e -> {
            createButton.setStyle(buttonStyle1 + "-fx-font-size: 13");
        });

        createButton.setOpacity(0.9);
        bottomPane.getChildren().addAll(createButton);
        bottomPane.setMinHeight(60);
        createButton.setOnAction(e->{
            new addMemo(new Memorandum("",""));
        });

        BorderPane borderPane=new BorderPane();
        borderPane.setStyle("-fx-background-color: transparent;"+"-fx-font-size: 18");
        borderPane.setBottom(bottomPane);
        borderPane.setCenter(vbox);

        leftPane.setContent(borderPane);
//        leftPane.setOpacity(0.5);
        leftPane.getStylesheets().add("bkg.css");

    }
    ScrollPane getLeftPane(){
        return this.leftPane;
    }
}

/**
 * <p>此类用于完成备忘录的删除</p>
 */
class deleteMemo{
    deleteMemo(Memorandum from){
        Stage alert = new Stage();
        alert.setResizable(false);
        alert.getIcons().add(new Image("/img/light_bulb.png"));
        alert.setHeight(200);
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-image: url('/img/alert.png');" +
                "-fx-background-size: cover");
        Text text = new Text("确定删除吗？");
        text.setFont(Font.font("宋体",FontWeight.BOLD,20));
        //text.setFill(Color.rgb(245,202,42));
        Button yes = new Button("确定");
        Button no =new Button("取消");
        no.setStyle(StaticValue.buttonStyle1 + "-fx-font-size: 16");
        yes.setStyle(StaticValue.buttonStyle1 + "-fx-font-size: 16");
        no.setOnAction(e -> {
            alert.close();
        });
        yes.setOnAction(e->{
            try {
                from.delete = 1;
                ManageMemorandum.currentMemorandum = null;
                ManageMemorandum.write();
                ManageMemorandum.setScene();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        no.setOnMouseMoved(e -> {
            no.setStyle(StaticValue.buttonStyle2 + "-fx-font-size: 16");
        });
        no.setOnMouseExited(e -> {
            no.setStyle(StaticValue.buttonStyle1 + "-fx-font-size: 16");
        });
        yes.setOnMouseMoved(e -> {
            yes.setStyle(StaticValue.buttonStyle2 + "-fx-font-size: 16");
        });
        yes.setOnMouseExited(e -> {
            yes.setStyle(StaticValue.buttonStyle1 + "-fx-font-size: 16");
        });
        pane.setCenter(text);
        HBox bottomBox=new HBox(yes,no);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setSpacing(30);
        pane.setBottom(bottomBox);
        pane.setMargin(yes,new Insets(0,0,20,130));
        pane.setMargin(no,new Insets(0,0,20,130));
        alert.setTitle("删除一条备忘录");
        alert.setScene(new Scene(pane,300,160));
        alert.show();
    }
}

//class deleteMemo {
//    deleteMemo(Memorandum from) {
//        Alert alert = new Alert(Alert.AlertType.NONE);
//        ButtonType buttonYes = new ButtonType("确定");
//        ButtonType buttonNo = new ButtonType("取消");
//        alert.getButtonTypes().setAll(buttonYes, buttonNo);
//        alert.setTitle("删除备忘录");
//        alert.setHeaderText("");
//        alert.setContentText("确定删除吗？");
//        alert.initOwner(ManageMemorandum.stage);
//        alert.show();
//        alert.setOnCloseRequest(e -> {
//            ButtonType result = alert.getResult();
//            if (result != null && result.equals(buttonYes)) {
//                try {
//                    from.delete = 1;
//                    ManageMemorandum.currentMemorandum = null;
//                    ManageMemorandum.write();
//                    ManageMemorandum.setScene();
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            } else {
//                alert.close();
//            }
//        });
//    }
//}


/**
 * <p>新建和修改备忘录都用这个类，修改的逻辑是把原有信息提前放在文本框中然后新建</p>
 */
class addMemo{
    addMemo(Memorandum origin){
        Text nameText=new Text("备忘录名称：");
        TextField nameTextField=new TextField(origin.getName());
        nameText.setStyle("-fx-background-color: transparent;"+"-fx-font-size: 18");
        Text contentText=new Text("备忘录内容：");
        TextField contentField=new TextField(origin.getContent());
        contentText.setStyle("-fx-background-color: transparent;"+"-fx-font-size: 18");
        Button yes=new Button("确定");
        Button no=new Button("取消");
        String buttonStyle1 = "-fx-background-color:darkturquoise;"+
                "-fx-background-radius:20;";
        String buttonStyle2 = "-fx-background-color:lightskyblue;"+
                "-fx-background-radius:20;";
        no.setStyle(buttonStyle1 + "-fx-font-size: 13");
        no.setOnMouseMoved(e -> {
            no.setStyle(buttonStyle2 + "-fx-font-size: 16");
        });
        no.setOnMouseExited(e -> {
            no.setStyle(buttonStyle1 + "-fx-font-size: 13");
        });
        yes.setStyle(buttonStyle1 + "-fx-font-size: 13");
        yes.setOnMouseMoved(e -> {
            yes.setStyle(buttonStyle2 + "-fx-font-size: 16");
        });
        yes.setOnMouseExited(e -> {
            yes.setStyle(buttonStyle1 + "-fx-font-size: 13");
        });


        HBox topPane=new HBox();
        topPane.setAlignment(Pos.CENTER);
        topPane.setStyle("-fx-background-color: transparent;"+"-fx-font-size: 18");
        Label topTitleOfAddDDL=new Label("编辑备忘录");
        topTitleOfAddDDL.setFont(Font.font("黑体", FontWeight.BOLD,30));
        topPane.getChildren().add(topTitleOfAddDDL);

        HBox bottomPane=new HBox(yes,no);
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setMinHeight(50);
        bottomPane.setSpacing(30);
        bottomPane.setStyle("-fx-background-color: transparent;"+"-fx-font-size: 18");

        HBox namePart=new HBox(nameText,nameTextField);
        namePart.setAlignment(Pos.CENTER);
        namePart.setMinHeight(80);
        namePart.setStyle("-fx-background-color: transparent;"+"-fx-font-size: 18");

//        HBox contentTextPart=new HBox(contentText);
//        contentTextPart.setAlignment(Pos.CENTER);
//        contentTextPart.setStyle("-fx-background-color: transparent;"+"-fx-font-size: 18");

        HBox contentPart=new HBox(contentText,contentField);
        contentPart.setAlignment(Pos.CENTER);
        contentPart.setMinHeight(80);
        contentPart.setStyle("-fx-background-color: transparent;"+"-fx-font-size: 18");

        VBox centerPane=new VBox(namePart,contentPart);
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setStyle("-fx-background-color: transparent;"+"-fx-font-size: 18");

        BorderPane addMemoPane=new BorderPane();
        addMemoPane.setTop(topPane);
        addMemoPane.setBottom(bottomPane);
        addMemoPane.setCenter(centerPane);
        addMemoPane.setStyle("-fx-background-color: transparent;"+"-fx-font-size: 18");
        addMemoPane.setStyle("-fx-background-image: url('/img/alert.png');"+"-fx-background-size: cover;");


        Scene addMemoScene=new Scene(addMemoPane);
        Stage addMemoStage=new Stage();
        addMemoStage.setScene(addMemoScene);
        addMemoStage.setTitle("编辑备忘录");
        addMemoStage.getIcons().add(new Image("/img/light_bulb.png"));
        addMemoStage.setMinWidth(700);
        addMemoStage.show();

        no.setOnAction(e->{
            addMemoStage.close();
        });

        yes.setOnAction(e->{
            String newName=nameTextField.getText();
            String newContent=contentField.getText();
            origin.delete=1;
            Memorandum newMemo=new Memorandum(newName,newContent);
            ManageMemorandum.MemoList.add(newMemo);
            try {
                ManageMemorandum.write();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                ManageMemorandum.setScene();
            } catch (FileNotFoundException | ParseException ex) {
                ex.printStackTrace();
            }
            addMemoStage.close();
        });
    }
}