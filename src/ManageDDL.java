import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



public class ManageDDL {
    //统一使用StaticValue的stage。
    public static Stage stage =StaticValue.stage;

    //存放ddl信息
    public static ArrayList<DDL> ddlArrayList=new ArrayList<DDL>();

    //从文件中读取ddl信息
    public static void read() throws IOException, ParseException {
        ddlArrayList.clear();
        FileReader reader = new FileReader("./data/" + StaticValue.userName + "/ddl.txt");
        char[] buf=new char[4096];
        try {
            reader.read(buf);
        }
        catch(Exception e){
            System.out.println(e);
        }
        String row_data= new String(buf);

        //不同ddl的信息之间用三个#分隔。
        String[] manyDDLs=row_data.split("###");

        for(int i=0;i<manyDDLs.length-1;i++){
            //一条ddl内部的不同部分用两个,分隔。
            String[] oneDDL=manyDDLs[i].split(",,");
            DDL newDDL=new DDL(oneDDL[2],oneDDL[1],oneDDL[0]);
            ManageDDL.ddlArrayList.add(newDDL);
        }

        //ddl按照截止日期排序展示
        Collections.sort(ManageDDL.ddlArrayList, new Comparator<DDL>() {
            @Override
            public int compare(DDL o1, DDL o2) {
                if((o1.str).compareTo(o2.str)>0)
                    return 1;
                else
                    return -1;
            }
        });
        reader.close();
    }


    //写回修改过的ddl信息
    public static void write() throws IOException {
        FileWriter writer = new FileWriter("./data/" + StaticValue.userName + "/ddl.txt");
        for(int i=0;i<ManageDDL.ddlArrayList.size();i++){
            if(ManageDDL.ddlArrayList.get(i).delete==1) continue;
            writer.write(ManageDDL.ddlArrayList.get(i).toString());
        }
        writer.flush();
        writer.close();
    }

    //装载场景，每次刷新页面就是用这个函数重新装载场景。
    public static void setScene() throws IOException, ParseException {
        ManageDDL.read();
        BorderPane mainPane=new BorderPane();

        //设置DDL管理页面的顶部标识，统一使用StaticValue的顶部部分。
        mainPane.setTop(new TopVBox().getvBox());

        //设置DDL管理页面的底部按钮
        mainPane.setBottom(new bottomPane().getBottomPane());

        //展示当前DDL
        try {
            mainPane.setCenter(new centerPane().getCenterPane());
        }
        catch(Exception e){
            System.out.println(e);
        }

        Scene ddlManageScene=new Scene(mainPane,StaticValue.stageWidth,StaticValue.stageHeight);
        stage.setScene(ddlManageScene);
        stage.setTitle("学业助理");
        stage.show();
    }

    ManageDDL() throws IOException, ParseException {
        ManageDDL.setScene();
    }

}


class DDLBar{
    HBox hbox=new HBox();
    DDLBar(){
        Label titleLabel=new Label("DDL内容");
        titleLabel.setMinWidth(StaticValue.stageWidth*3/10);
        titleLabel.setMaxWidth(StaticValue.stageWidth*3/10);
        titleLabel.setTextFill(Paint.valueOf("blue"));

        Label hyperlink=new Label("相关链接");
        hyperlink.setMinWidth(StaticValue.stageWidth*2/10);
        hyperlink.setMaxWidth(StaticValue.stageWidth*2/10);
        hyperlink.setTextFill(Paint.valueOf("blue"));

        Label dateLabel=new Label("截止日期");
        dateLabel.setMinWidth(StaticValue.stageWidth/10);
        dateLabel.setMaxWidth(StaticValue.stageWidth/10);
        dateLabel.setTextFill(Paint.valueOf("blue"));


        Label timeLabel=new Label("剩余时间");
        timeLabel.setMinWidth(StaticValue.stageWidth*2/10);
        timeLabel.setTextFill(Paint.valueOf("blue"));

        Label amountLabel=new Label("当前共有"+ ManageDDL.ddlArrayList.size()+"条DDL");
        amountLabel.setMinWidth(StaticValue.stageWidth*1/10);
        amountLabel.setTextFill(Paint.valueOf("blue"));

        hbox.getChildren().addAll(titleLabel,hyperlink,dateLabel,timeLabel,amountLabel);
        hbox.setSpacing(10);
    }
    HBox getHbox(){
        return this.hbox;
    }
}

//DDL类
class DDL{
    HBox hbox=new HBox();

    //删除ddl的逻辑是把delete参数改为1，之后重新写回和加载ddl时便不会写回和加载这条ddl。
    public int delete=0;

    public String str;
    public String title;
    public String url;
    public String date;

    DDL(String title,String url,String date) throws ParseException {
        str=date+",,"+url+",,"+title+"###";
        this.title=title;
        this.url=url;
        this.date=date;

        Label titleLabel=new Label(title);
        titleLabel.setMinWidth(StaticValue.stageWidth*3/10);
        titleLabel.setMaxWidth(StaticValue.stageWidth*3/10);

        Hyperlink hyperlink=new Hyperlink(url);
        hyperlink.setMinWidth(StaticValue.stageWidth*2/10);
        hyperlink.setMaxWidth(StaticValue.stageWidth*2/10);

        Label dateLabel=new Label(date);
        dateLabel.setMinWidth(StaticValue.stageWidth/10);
        dateLabel.setMaxWidth(StaticValue.stageWidth/10);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate=new Date();
        Date ddlDate=sdf.parse(date);
        long start = currentDate.getTime();
        long end=ddlDate.getTime();

        //计算距离截止日期还有多久
        int betweenDays = (int) ((end - start)/(24*3600*1000));
        int betweenHours=(int) ((end - start)/(3600*1000));
        String timeLeft;

        //flag为0代表ddl过期，需要做出提示。计算出未到期的话会把flag改为1。
        int flag=0;
        if(betweenHours>=0){
            int hour=betweenHours%24;
            timeLeft=String.format("还有%d天%d小时",betweenDays,hour);
            flag=1;
        }
        else{
            betweenDays=(int) ((start - end)/(24*3600*1000));
            betweenHours=(int) ((start - end)/(3600*1000));
            int hour=betweenHours%24;
            timeLeft=String.format("请注意！已过期%d天%d小时",betweenDays,hour);
        }
        Label timeLabel=new Label(timeLeft);
        timeLabel.setMinWidth(StaticValue.stageWidth*2/10);

        Button button=new Button("删除~");
        Button editButton=new Button("编辑~");

        if(flag==0){
            hbox.setBackground(new Background(new BackgroundFill(Paint.valueOf("red"),null,null)));
        }

        hbox.getChildren().addAll(titleLabel,hyperlink,dateLabel,timeLabel,editButton,button);
        hbox.setSpacing(10);

        button.setOnAction(event -> {
            new deleteDDL(this);
        });

        editButton.setOnAction(event -> {
            this.delete=1;
            new addDDL(this);
        });

        hyperlink.setOnAction(event->{
            try {
                URI uri=new URI(url);
                java.awt.Desktop.getDesktop().browse(uri);
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        });
    }
    HBox getHBox(){
        return this.hbox;
    }
    @Override
    public String toString(){
        return str;
    }
}


class bottomPane{
    HBox bottomPane=new HBox();
    bottomPane(){
        bottomPane.setAlignment(Pos.CENTER);
        Button createButton =new Button("新建一条DDL");
        bottomPane.getChildren().addAll(createButton);
        bottomPane.setMinHeight(60);
        createButton.setOnAction(e->{
            new addDDL();
        });

    }
    HBox getBottomPane(){
        return this.bottomPane;
    }
}

//中间展示ddl信息
class centerPane{
    ScrollPane centerPane=new ScrollPane();
    centerPane() throws FileNotFoundException {
        VBox DDLs=new VBox();
        DDLs.getChildren().add(new DDLBar().getHbox());
        for(int i=0;i<ManageDDL.ddlArrayList.size();i++){
            if(ManageDDL.ddlArrayList.get(i).delete==1) continue;
            DDLs.getChildren().add(ManageDDL.ddlArrayList.get(i).getHBox());
        }
        DDLs.setSpacing(20);
        DDLs.setAlignment(Pos.TOP_CENTER);
        centerPane.setContent(DDLs);
    }
    ScrollPane getCenterPane(){
        return this.centerPane;
    }
}

//此类用于删除ddl，逻辑是把ddlArrayList中要删除的ddl的delete参数改为1，然后写回ddl和重新加载界面，写回ddl时delete参数为1的ddl不会被写回。
class deleteDDL{
    deleteDDL(DDL from){
        Alert alert = new Alert(Alert.AlertType.NONE);
        ButtonType buttonYes = new ButtonType("确定");
        ButtonType buttonNo = new ButtonType("取消");
        alert.getButtonTypes().setAll(buttonYes, buttonNo);
        alert.setTitle("移除一条DDL");
        alert.setHeaderText("");
        alert.setContentText("已完成？");
        alert.initOwner(ManageDDL.stage);
        alert.show();
        alert.setOnCloseRequest(e -> {
            ButtonType result = alert.getResult();
            if (result != null && result.equals(buttonYes)) {
                try {
                    from.delete=1;
                    ManageDDL.write();
                    ManageDDL.read();
                    ManageDDL.setScene();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                alert.close();
            }
        });
    }
}


//此类负责添加和编辑两个功能。
//编辑的逻辑就是把要修改的ddl的原有信息提前放在文本框里，然后新建ddl。
class addDDL{
    addDDL(){
        Text ddlText=new Text("DDL内容：");
        TextField ddlTextField=new TextField("");
        ddlTextField.setPrefWidth(400);

        Text urlText=new Text("链接地址：");
        String urlTextNotice="选填";
        TextField urlTextField=new TextField(urlTextNotice);
        urlTextField.setPrefWidth(400);

        Label dueTimeLabel=new Label("请输入截止日期：");


        Text yearText=new Text("年：");
        TextField yearTextField=new TextField("");
        yearTextField.setPrefWidth(100);


        Text monthText=new Text(" 月：");
        TextField monthTextField=new TextField("");
        monthTextField.setPrefWidth(100);

        Text dayText=new Text(" 日：");
        TextField dayTextField=new TextField("");
        dayTextField.setPrefWidth(100);


        Button yes=new Button("确定");
        Button no=new Button("取消");

        HBox topPane=new HBox();
        topPane.setAlignment(Pos.CENTER);
        Label topTitleOfAddDDL=new Label("添加新的DDL");
        topTitleOfAddDDL.setFont(Font.font("黑体", FontWeight.BOLD,30));
        topPane.getChildren().add(topTitleOfAddDDL);

        HBox bottomPane=new HBox(yes,no);
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setMinHeight(50);
        bottomPane.setSpacing(30);

        HBox ddlPart=new HBox(ddlText,ddlTextField);
        ddlPart.setAlignment(Pos.CENTER);
        ddlPart.setMinHeight(80);


        HBox urlPart=new HBox(urlText,urlTextField);
        urlPart.setAlignment(Pos.CENTER);
        urlPart.setMinHeight(80);

        HBox noticePart=new HBox(dueTimeLabel);
        noticePart.setAlignment(Pos.CENTER);


        HBox dueTimePart=new HBox(yearText,yearTextField,monthText,monthTextField,dayText,dayTextField);
        dueTimePart.setAlignment(Pos.CENTER);
        dueTimePart.setMinHeight(80);

        VBox centerPane=new VBox(ddlPart,urlPart,noticePart,dueTimePart);
        centerPane.setAlignment(Pos.CENTER);

        BorderPane addDDLPane=new BorderPane();
        addDDLPane.setTop(topPane);
        addDDLPane.setBottom(bottomPane);
        addDDLPane.setCenter(centerPane);


        Scene addDDLScene=new Scene(addDDLPane);
        Stage addDDLStage=new Stage();
        addDDLStage.setScene(addDDLScene);
        addDDLStage.setTitle("又来活儿了是吧");
        addDDLStage.setMinWidth(600);
        addDDLStage.show();

        no.setOnAction(e->{
            addDDLStage.close();
        });
        yes.setOnAction(e->{
            String newDDLTitle=ddlTextField.getText();
            String newUrl=urlTextField.getText();
            String theYear=yearTextField.getText();
            String theMonth=monthTextField.getText();
            String theDay=dayTextField.getText();
            try{

                int year=Integer.parseInt(theYear);
                int month=Integer.parseInt(theMonth);
                int day=Integer.parseInt(theDay);

                theYear=Integer.toString(year);
                if(month<10){
                    theMonth="0"+Integer.toString(month);
                }
                else theMonth=Integer.toString(month);
                if(day<10){
                    theDay="0"+Integer.toString(day);
                }
                else theDay=Integer.toString(day);
                String theDate=theYear+"-"+theMonth+"-"+theDay;

                //这里验证日期合法性
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date testDate=sdf.parse(theDate);
                String testString = sdf.format(testDate);

                if(testDate.compareTo(new Date())<=0){
                    throw new Exception();
                }
                if(!testString.equals(theDate)){
                    throw new Exception();
                }
                //一直验证到这里

                if(newUrl.equals(urlTextNotice)){
                    newUrl="null";
                }
                DDL newDDL=new DDL(newDDLTitle,newUrl,theDate);
                ManageDDL.ddlArrayList.add(newDDL);
                ManageDDL.write();
                ManageDDL.setScene();
                addDDLStage.close();
            }
            catch(Exception exception){
                new PopUpWindow("请输入正确的年月日信息,注意日期不能早于当前");
                addDDLStage.close();
            }
        });
    }


    //重载构造函数，这段代码是直接复制无参构造函数后稍加改动而来的。用于编辑ddl，逻辑是把旧的ddl信息提前放在文本输入框里，然后执行新建ddl。
        addDDL(DDL parentDDL){
            Text ddlText=new Text("DDL内容：");
            TextField ddlTextField=new TextField(parentDDL.title);
            ddlTextField.setPrefWidth(400);

            Text urlText=new Text("链接地址：");
            String urlTextNotice=parentDDL.url;

            TextField urlTextField=new TextField(urlTextNotice);
            urlTextField.setPrefWidth(400);

            Label dueTimeLabel=new Label("请输入截止日期：");

            String [] theOldDate=parentDDL.date.split("-");

            Text yearText=new Text("年：");
            TextField yearTextField=new TextField(theOldDate[0]);
            yearTextField.setPrefWidth(100);


            Text monthText=new Text(" 月：");
            TextField monthTextField=new TextField(theOldDate[1]);
            monthTextField.setPrefWidth(100);

            Text dayText=new Text(" 日：");
            TextField dayTextField=new TextField(theOldDate[2]);
            dayTextField.setPrefWidth(100);


            Button yes=new Button("确定");
            Button no=new Button("取消");

            HBox topPane=new HBox();
            topPane.setAlignment(Pos.CENTER);
            Label topTitleOfAddDDL=new Label("修改一条DDL");
            topTitleOfAddDDL.setFont(Font.font("黑体", FontWeight.BOLD,30));
            topPane.getChildren().add(topTitleOfAddDDL);

            HBox bottomPane=new HBox(yes,no);
            bottomPane.setAlignment(Pos.CENTER);
            bottomPane.setMinHeight(50);
            bottomPane.setSpacing(30);

            HBox ddlPart=new HBox(ddlText,ddlTextField);
            ddlPart.setAlignment(Pos.CENTER);
            ddlPart.setMinHeight(80);


            HBox urlPart=new HBox(urlText,urlTextField);
            urlPart.setAlignment(Pos.CENTER);
            urlPart.setMinHeight(80);

            HBox noticePart=new HBox(dueTimeLabel);
            noticePart.setAlignment(Pos.CENTER);


            HBox dueTimePart=new HBox(yearText,yearTextField,monthText,monthTextField,dayText,dayTextField);
            dueTimePart.setAlignment(Pos.CENTER);
            dueTimePart.setMinHeight(80);

            VBox centerPane=new VBox(ddlPart,urlPart,noticePart,dueTimePart);
            centerPane.setAlignment(Pos.CENTER);

            BorderPane addDDLPane=new BorderPane();
            addDDLPane.setTop(topPane);
            addDDLPane.setBottom(bottomPane);
            addDDLPane.setCenter(centerPane);


            Scene addDDLScene=new Scene(addDDLPane);
            Stage addDDLStage=new Stage();
            addDDLStage.setScene(addDDLScene);
            addDDLStage.setTitle("计划要跟随变化~");
            addDDLStage.setMinWidth(600);
            addDDLStage.show();

            no.setOnAction(e->{
                addDDLStage.close();
            });
            yes.setOnAction(e->{
                String newDDLTitle=ddlTextField.getText();
                String newUrl=urlTextField.getText();
                String theYear=yearTextField.getText();
                String theMonth=monthTextField.getText();
                String theDay=dayTextField.getText();
                try{

                    int year=Integer.parseInt(theYear);
                    int month=Integer.parseInt(theMonth);
                    int day=Integer.parseInt(theDay);

                    theYear=Integer.toString(year);
                    if(month<10){
                        theMonth="0"+Integer.toString(month);
                    }
                    else theMonth=Integer.toString(month);
                    if(day<10){
                        theDay="0"+Integer.toString(day);
                    }
                    else theDay=Integer.toString(day);
                    String theDate=theYear+"-"+theMonth+"-"+theDay;

                    //这里验证日期合法性
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date testDate=sdf.parse(theDate);
                    String testString = sdf.format(testDate);

                    if(testDate.compareTo(new Date())<=0){
                        throw new Exception();
                    }
                    if(!testString.equals(theDate)){
                        throw new Exception();
                    }
                    //一直验证到这里

                    DDL newDDL=new DDL(newDDLTitle,newUrl,theDate);
                    ManageDDL.ddlArrayList.add(newDDL);
                    ManageDDL.write();
                    ManageDDL.setScene();
                    addDDLStage.close();
                }
                catch(Exception exception){
                    new PopUpWindow("请输入正确的年月日信息,且日期不能早于当前",parentDDL);
                    addDDLStage.close();
                }
            });
        }
}


//弹窗类，传入字符串信息后可以显示
class PopUpWindow{
    PopUpWindow(String message) {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示信息");
        alert.setHeaderText("");
        alert.setContentText(message);
        alert.initOwner(ManageDDL.stage);
        alert.show();
        alert.setOnCloseRequest(e->{
           new addDDL();
        });
    }
    //addDDL类负责编辑ddl和新加ddl两个功能，因此弹窗类也要做出对应的改变
    PopUpWindow(String message,DDL oldDDL) {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示信息");
        alert.setHeaderText("");
        alert.setContentText(message);
        alert.initOwner(ManageDDL.stage);
        alert.show();
        alert.setOnCloseRequest(e->{
            new addDDL(oldDDL);
        });
    }
}

//https://github.com/maple826/CourseManagementHelper