import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ManageMemorandum {
    public static Stage stage =new Stage();
    public static ArrayList<Memorandum> MemoList=new ArrayList<Memorandum>();
    public static Memorandum currentMemorandum;
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
        String[] splitData=row_data.split("#");

        for(int i=0;i<splitData.length-1;i+=3){
            Memorandum newMemorandum=new Memorandum(splitData[i],splitData[i+1],splitData[i+2]);
            MemoList.add(newMemorandum);
        }
        Collections.sort(MemoList, new Comparator<Memorandum>() {
            @Override
            public int compare(Memorandum o1, Memorandum o2) {
                if(o1.getLastModifiedTime().compareTo(o2.getLastModifiedTime())>=0){
                    return -1;
                }
                else return 1;
            }
        });
        if(currentMemorandum==null) {
            if(MemoList.size()>0) {
                currentMemorandum = MemoList.get(0);
            }
        }
    }
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
    public static void setScene() throws FileNotFoundException, ParseException {
        ManageMemorandum.read();
        BorderPane mainPane=new BorderPane();
        //设置页面的顶部标识
        mainPane.setTop(new topPane1().getTopPane());
        //设置页面的左侧标识
        mainPane.setLeft(new leftPane1().getLeftPane());

        //展示备忘录
        try {
            mainPane.setCenter(new centerPane1().getCenterPane());
        }
        catch(Exception e){
            System.out.println(e);
        }
        Scene MemoManageScene=new Scene(mainPane,900,500);
        stage.setScene(MemoManageScene);
        stage.setTitle("学习小帮手");
        stage.show();
    }
    ManageMemorandum() throws FileNotFoundException, ParseException {
        setScene();
    }
}

class Memorandum{
    private  String name;
    private String content;
    private Date lastModifiedTime;
    public int delete=0;
    HBox hbox=new HBox();
    Memorandum(String name,String content){
        this.name=name;
        this.content=content;
        this.lastModifiedTime=new Date();
    }

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
        HBox innerHBox=new HBox(display,delete,modify);
        VBox innerVBox=new VBox(name,innerHBox);
        hbox.getChildren().add(innerVBox);
        hbox.setAlignment(Pos.CENTER);
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
        return date+"#"+this.name+"#"+this.content+"#";
    }
}

class topPane1{
    HBox topPane=new HBox();
    topPane1(){
        Label Memo=new Label("备忘录");
        Memo.setFont(Font.font("黑体", FontWeight.BOLD,60));
        Memo.setTextFill(Color.BLUE);
        topPane.getChildren().add(Memo);
        topPane.setMinHeight(80);
        topPane.setAlignment(Pos.CENTER);
    }
    HBox getTopPane(){
        return this.topPane;
    }
}
class centerPane1{
    ScrollPane centerPane=new ScrollPane();
    centerPane1(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Text content=new Text(ManageMemorandum.currentMemorandum.getContent());
        Label date=new Label(sdf.format(ManageMemorandum.currentMemorandum.getLastModifiedTime()));
        date.setAlignment(Pos.CENTER);
        Label name=new Label(ManageMemorandum.currentMemorandum.getName());
        name.setAlignment(Pos.CENTER);
        VBox vbox=new VBox(name,date,content);
        centerPane.setContent(vbox);
    }
    ScrollPane getCenterPane(){
        return this.centerPane;
    }
}
class leftPane1{
    ScrollPane leftPane=new ScrollPane();
    leftPane1(){
        VBox vbox=new VBox();
        for(int i=0;i<ManageMemorandum.MemoList.size();i++){
            if(ManageMemorandum.MemoList.get(i).delete==1) continue;
            vbox.getChildren().add(ManageMemorandum.MemoList.get(i).getHBox());
        }

        HBox bottomPane=new HBox();
        bottomPane.setAlignment(Pos.CENTER);
        Button createButton =new Button("新建一条备忘录");
        bottomPane.getChildren().addAll(createButton);
        bottomPane.setMinHeight(60);
        createButton.setOnAction(e->{
            new addMemo(new Memorandum("",""));
        });

        BorderPane borderPane=new BorderPane();
        borderPane.setBottom(bottomPane);
        borderPane.setCenter(vbox);

        leftPane.setContent(borderPane);
    }
    ScrollPane getLeftPane(){
        return this.leftPane;
    }
}

class deleteMemo {
    deleteMemo(Memorandum from) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        ButtonType buttonYes = new ButtonType("确定");
        ButtonType buttonNo = new ButtonType("取消");
        alert.getButtonTypes().setAll(buttonYes, buttonNo);
        alert.setTitle("删除备忘录");
        alert.setHeaderText("");
        alert.setContentText("确定删除？");
        alert.initOwner(ManageMemorandum.stage);
        alert.show();
        alert.setOnCloseRequest(e -> {
            ButtonType result = alert.getResult();
            if (result != null && result.equals(buttonYes)) {
                try {
                    from.delete = 1;
                    ManageMemorandum.currentMemorandum = null;
                    ManageMemorandum.write();
                    ManageMemorandum.setScene();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                alert.close();
            }
        });
    }
}

class addMemo{
    addMemo(Memorandum origin){
        Text nameText=new Text("备忘录名称：");
        TextField nameTextField=new TextField(origin.getName());
        Text contentText=new Text("备忘录内容：");
        TextField contentField=new TextField(origin.getContent());
        Button yes=new Button("确定");
        Button no=new Button("取消");

        HBox topPane=new HBox();
        topPane.setAlignment(Pos.CENTER);
        Label topTitleOfAddDDL=new Label("编辑备忘录");
        topTitleOfAddDDL.setFont(Font.font("黑体", FontWeight.BOLD,30));
        topPane.getChildren().add(topTitleOfAddDDL);

        HBox bottomPane=new HBox(yes,no);
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setMinHeight(50);
        bottomPane.setSpacing(30);


        HBox namePart=new HBox(nameText,nameTextField);
        namePart.setAlignment(Pos.CENTER);
        namePart.setMinHeight(80);

        HBox contentTextPart=new HBox(contentText);
        contentTextPart.setAlignment(Pos.CENTER);

        HBox contentPart=new HBox(contentField);
        contentPart.setAlignment(Pos.CENTER);
        contentPart.setMinHeight(80);


        VBox centerPane=new VBox(namePart,contentTextPart,contentPart);
        centerPane.setAlignment(Pos.CENTER);

        BorderPane addMemoPane=new BorderPane();
        addMemoPane.setTop(topPane);
        addMemoPane.setBottom(bottomPane);
        addMemoPane.setCenter(centerPane);


        Scene addMemoScene=new Scene(addMemoPane);
        Stage addMemoStage=new Stage();
        addMemoStage.setScene(addMemoScene);
        addMemoStage.setTitle("编辑备忘录");
        addMemoStage.setMinWidth(600);
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