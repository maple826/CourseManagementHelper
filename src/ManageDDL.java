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
    public static Stage stage =new Stage();

    public static ArrayList<DDL> ddlArrayList=new ArrayList<DDL>();

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
        String[] manyDDLs=row_data.split("#");

        for(int i=0;i<manyDDLs.length-1;i++){
            String[] oneDDL=manyDDLs[i].split(",,");
            DDL newDDL=new DDL(oneDDL[2],oneDDL[1],oneDDL[0]);
            ManageDDL.ddlArrayList.add(newDDL);
        }
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

    public static void write() throws IOException {
        FileWriter writer = new FileWriter("./data/" + StaticValue.userName + "/ddl.txt");
        for(int i=0;i<ManageDDL.ddlArrayList.size();i++){
            if(ManageDDL.ddlArrayList.get(i).delete==1) continue;
            writer.write(ManageDDL.ddlArrayList.get(i).toString());
        }
        writer.flush();
        writer.close();
    }

    public static void setScene() throws IOException, ParseException {
        ManageDDL.read();
        BorderPane mainPane=new BorderPane();
        //设置DDL管理页面的顶部标识
        mainPane.setTop(new topPane().getTopPane());

        //设置DDL管理页面的底部按钮
        mainPane.setBottom(new bottomPane().getBottomPane());

        //展示当前DDL
        try {
            mainPane.setCenter(new centerPane().getCenterPane());
        }
        catch(Exception e){
            System.out.println(e);
        }
        Scene ddlManageScene=new Scene(mainPane,900,500);
        stage.setScene(ddlManageScene);
        stage.setTitle("学习小帮手");
        stage.show();
    }

    ManageDDL() throws IOException, ParseException {
        ManageDDL.setScene();
    }

}

class DDL{
    HBox hbox=new HBox();
    public int delete=0;
    public String str;
    DDL(String title,String url,String date) throws ParseException {
        str=date+",,"+url+",,"+title+"#";

        Label titleLabel=new Label(title);
        titleLabel.setMinWidth(200);


        Hyperlink hyperlink=new Hyperlink(url);
        hyperlink.setMinWidth(200);

        Label dateLabel=new Label(date);
        dateLabel.setMinWidth(100);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate=new Date();
        Date ddlDate=sdf.parse(date);
        long start = currentDate.getTime();
        long end=ddlDate.getTime();
        int betweenDays = (int) ((end - start)/(24*3600*1000));
        int betweenHours=(int) ((end - start)/(3600*1000));
        String timeLeft;
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
        timeLabel.setMinWidth(200);

        Button button=new Button("删除~");

        if(flag==0){
            hbox.setBackground(new Background(new BackgroundFill(Paint.valueOf("red"),null,null)));
        }

        hbox.getChildren().addAll(titleLabel,hyperlink,dateLabel,timeLabel,button);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setSpacing(30);

        button.setOnAction(event -> {
            new deleteDDL(this);
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



class topPane{
    HBox topPane=new HBox();
    topPane(){
        Label ddlManage=new Label("DDL管理");
        ddlManage.setFont(Font.font("黑体", FontWeight.BOLD,60));
        ddlManage.setTextFill(Color.BLUE);
        topPane.getChildren().add(ddlManage);
        topPane.setMinHeight(80);
        topPane.setAlignment(Pos.CENTER);
    }
    HBox getTopPane(){
        return this.topPane;
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


class centerPane{
    ScrollPane centerPane=new ScrollPane();
    centerPane() throws FileNotFoundException {
        VBox DDLs=new VBox();
        for(int i=0;i<ManageDDL.ddlArrayList.size();i++){
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

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date testDate=sdf.parse(theDate);
                    String testString = sdf.format(testDate);

                    if(testDate.compareTo(new Date())<=0){
                        throw new Exception();
                    }
                    if(!testString.equals(theDate)){
                        throw new Exception();
                    }

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
                    new PopUpWindow("请输入正确的年月日信息,且日期不能早于当前");
                    addDDLStage.close();
                }
            });
        }
}

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
}

//https://github.com/maple826/CourseManagementHelper