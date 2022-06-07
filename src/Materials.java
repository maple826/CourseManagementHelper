import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.*;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;

class Test{
    public static void main(String[] args){

    }
}
//用于获得某路径下文件
class All_materials {
//    传入路径（例：".//data//user1//资源//course1"）,遍历取得所有资源存入ArrayList res中
    public static void get_materials(String path, ArrayList<String> res){
        File file = new File(path);
        int i ;
        File[] temp = file.listFiles();
        for(i = 0;i < temp.length;i++){
            if(temp[i].isFile()){
                res.add(temp[i].getName());
            }
        }
        return;
    }

}
public class Materials{
    public static BorderPane material_pane;
    public static String course;

    public Materials(BorderPane pane,String course){
        material_pane = pane;
        this.course = course;
        create_materials();
    }
    private void create_materials(){
        material_pane.setCenter(new Center_scroller_pane().get_mat_pane());
        material_pane.getScene().setRoot(material_pane);

        Course.stage.show();
    }

}
//中心滚动栏
class Center_scroller_pane{
    private ScrollPane mat_pane = new ScrollPane();
    private VBox mat_vbox = new VBox();
    private VBox bkmark_vbox = new VBox();
    private ArrayList mat_list = new ArrayList<>();
    private ArrayList bkmark_list = new ArrayList();
    private ContextMenu menu;
    Center_scroller_pane(){
        int i;
//        设置右键添加资料/书签功能
        menu = new ContextMenu();
        MenuItem itemAddMat = new MenuItem("添加资料");
        MenuItem itemAddBkm = new MenuItem("添加书签");
        menu.getItems().addAll(itemAddMat,itemAddBkm);
        mat_pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.SECONDARY)) {
                    menu.show(mat_pane, Side.TOP, event.getX(), event.getY());
                }
                else if (event.getButton().equals(MouseButton.PRIMARY)) {
                    menu.hide();
                }
            }
        });
        itemAddMat.setOnAction(e -> {
            new Material_alert("Add",Materials.course,0);
        });
        itemAddBkm.setOnAction(e -> {
            new Material_alert("Add",Materials.course);
        });
        /***
         第一部分——本地资料
         ***/
        try{
            All_materials.get_materials(".//data//"+StaticValue.userName+"//资源//"+Materials.course, mat_list);
        }catch(NullPointerException exc){};
        mat_vbox.setSpacing(StaticValue.stageHeight / 20);
        mat_vbox.setPadding(new Insets(StaticValue.stageHeight/10,0,0,StaticValue.stageWidth/3));
        Text title_mat = new Text("资料");
        title_mat.setFont(Font.font("华文行楷", FontWeight.BOLD, 30));
        title_mat.setFill(Color.rgb(245,202,42));
        mat_vbox.getChildren().add(title_mat);
        Button buttons[] = new Button[mat_list.size()];

        for(i = 0;i < mat_list.size();i++){
//            使用Mat_button类生成本地资料按钮
            buttons[i] = new Mat_button(mat_list.get(i).toString(),Materials.course).getButton();
            VBox.setMargin(buttons[i], new Insets(0, 0, 0, 8)); //为每个节点设置外边距
            mat_vbox.getChildren().add(buttons[i]);
        }
        /***
        第二部分——书签链接
        ***/
        try{
            All_materials.get_materials(".//data//"+StaticValue.userName+"//资源//"+
                    Materials.course+"//书签链接", bkmark_list);
        }catch (NullPointerException exc){}

        Text title_bkmark = new Text("书签链接");
        title_bkmark.setFont(Font.font("华文行楷", FontWeight.BOLD, 30));
        title_bkmark.setFill(Color.rgb(245,202,42));
        mat_vbox.getChildren().add(title_bkmark);

        Hyperlink options[] = new Hyperlink[bkmark_list.size()];
        for(i = 0;i < bkmark_list.size();i++) {
//            使用Bkmark_hplink类创建书签hyperlink
            options[i] = new Bkmark_hplink(bkmark_list.get(i).toString(),Materials.course).getHyperlink();
            VBox.setMargin(options[i], new Insets(0, 0, 0, 8)); //为每个节点设置外边距
            mat_vbox.getChildren().add(options[i]);
        }
        mat_pane.setContent(mat_vbox);
    }
    public ScrollPane get_mat_pane(){
        return mat_pane;
    }

}
//根据传入的本地资料名建立button
class Mat_button{
    private String mat;
    private ContextMenu menu;
    private Button button;
    Mat_button(String material,String course){
        mat = material;
        button = new Button(mat);
//        设置右键菜单
        menu = new ContextMenu();
        MenuItem itemDel = new MenuItem("删除");
        MenuItem itemEdit = new MenuItem("编辑");
        menu.getItems().addAll(itemDel,itemEdit);
        button.setFont(Font.font("宋体",18));
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.SECONDARY)) {
                    menu.show(button, Side.TOP, event.getX(), event.getY());
                }
                else if(event.getButton().equals(MouseButton.PRIMARY)){
                    File mat_f = new File(".//data//"+StaticValue.userName+"//资源//"+Materials.course+"//"
                            +mat);
                    Desktop desktop = Desktop.getDesktop();
                    if(mat_f.exists()) {
                        try{
                            desktop.open(mat_f);
                        }catch (IOException exc){
                            System.out.println("打开文件失败");
                        }
                    }
                }
            }
        });
        itemDel.setOnAction(e -> {
            new Material_alert("Delete",button.getText(),course,0);
        });
        itemEdit.setOnAction(e -> {
            new Material_alert("Edit",button.getText(),course,0);
        });
    }
    public Button getButton(){
        return button;
    }
}
//根据传入的书签名建立hyperlink
class Bkmark_hplink{
    private String bkmark;
    private ContextMenu menu;
    private Hyperlink hyperlink;
    Bkmark_hplink(String bkm,String course){
        bkmark = bkm;
//        删除书签文件名的后缀作为hyperlink的名字
        String[] tmp = bkm.split("\\.");
        int drop_size = 0;
        String hplink_name = bkm;
        try{
            drop_size = tmp[tmp.length-1].length();
            hplink_name = bkm.substring(0,bkm.length()-drop_size-1);
        }catch (IndexOutOfBoundsException excep){};


        hyperlink = new Hyperlink(hplink_name);
//        设置右键菜单
        menu = new ContextMenu();
        MenuItem itemDel = new MenuItem("删除");
        MenuItem itemEdit = new MenuItem("编辑");
        menu.getItems().addAll(itemDel,itemEdit);
        EventHandler<ActionEvent> event =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e)
                    {
                        String bkmark_path = ".//data//"+StaticValue.userName+"//资源//"+
                                Materials.course+"//书签链接//"+bkmark;
                        FileInputStream fis= null;
                        try {
                            fis = new FileInputStream(bkmark_path);
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        InputStreamReader isr= null;
                        try {
                            isr = new InputStreamReader(fis, "UTF-8");
                        } catch (UnsupportedEncodingException ex) {
                            ex.printStackTrace();
                        }
                        BufferedReader br = new BufferedReader(isr);
                        String bkmark_url = "";
                        try {
                            bkmark_url = br.readLine();
                            br.close();
                            isr.close();
                            fis.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        //创建一个url,使用默认浏览器打开
                        try {
                            URI url = new URI(bkmark_url);
                            java.awt.Desktop.getDesktop().browse(url);
                        } catch (IOException | URISyntaxException ex) {
                            ex.printStackTrace();
                        }
                    }

                };
        // 按下链接后
        hyperlink.setOnAction(event);
        hyperlink.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.SECONDARY)) {
                    menu.show(hyperlink, Side.TOP, event.getX(), event.getY());
                }
            }
        });
        itemDel.setOnAction(e -> {
            new Material_alert("Delete",bkm,course,1);
        });
        itemEdit.setOnAction(e -> {
            new Material_alert("Edit",bkm,course,1);
        });
    }
    public Hyperlink getHyperlink(){
        return hyperlink;
    }

}
//    提示信息类，mat_or_bkm用于判定是本地资料还是书签链接,前者为0后者为1
class Material_alert {

    //    此构造函数专为生成新书签使用
    Material_alert(String s,String course){
        if(s.equals("Add")){
            Text text_name = new Text("请输入书签名称：");
            Text text_content = new Text("请输入网址链接：");
            Text hint = new Text("(建议该网址从浏览器复制)");
            text_name.setFont(Font.font("宋体",18));
            text_content.setFont(Font.font("宋体",18));
            TextField textField_name = new TextField();
            TextField textField_content = new TextField();
            Button yes = new Button("确定");
            Button no = new Button("取消");
            yes.setFont(Font.font("Microsoft YaHei", 18));
            no.setFont(Font.font("Microsoft YaHei", 18));
            yes.setPadding(new Insets(10));
            no.setPadding(new Insets(10));
            HBox hBox1 = new HBox(text_name,textField_name);
            HBox hBox2 = new HBox(yes,no);
            HBox hBox3 = new HBox(text_content,textField_content);
            VBox vBox = new VBox(hBox1,hBox3,hint);
            vBox.setSpacing(StaticValue.stageHeight / 30);
            hBox2.setAlignment(Pos.CENTER);
            hBox2.setSpacing(StaticValue.stageHeight / 24);
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(vBox);

            borderPane.setBottom(hBox2);
            borderPane.setMargin(vBox,new Insets(30));
            Stage alert = new Stage();
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setScene(new Scene(borderPane,StaticValue.stageWidth * 2 / 5,StaticValue.stageHeight * 2 / 5));
            alert.setTitle("提示信息");
            alert.show();

            no.setOnAction(e -> {
                alert.close();
            });

            yes.setOnAction(e -> {
//              书签链接文件后缀必须为.txt
                String new_bkm = textField_name.getText() + ".txt";
                File f = new File("./data/" + StaticValue.userName + "/资源/" + course + "/书签链接/" + new_bkm);
                if(f.exists()){
                    alert.close();
                    new Material_alert("Exist",course,1);
                    return;
                }
                try {
                    f.createNewFile();
                } catch (IOException ex) {
                    alert.close();
                    new Material_alert("Exist",course,1);
                    return;
                }
                try (FileOutputStream fileOutputStream = new FileOutputStream("./data/"
                        + StaticValue.userName + "/资源/" + course + "/书签链接/" + new_bkm)) {
                    byte[] bytes = textField_content.getText().getBytes();
                    fileOutputStream.write(bytes);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                alert.close();
                new Material_alert("SuccessAdd",course,1);
            });
        }

    }
//    此构造函数用于生成新资料等功能
    Material_alert(String s,String course,int mat_or_bkm) {

        if(s.equals("Add")) {
            Text text,text1;
            text = new Text("请输入资料名称：");
            text1 = new Text("可将本地文件拖拽至此");
            text.setFont(Font.font("宋体",18));
            text1.setFont(Font.font("宋体",16));
            TextField textField = new TextField();
            Button yes = new Button("确定");
            Button no = new Button("取消");
            yes.setFont(Font.font("Microsoft YaHei", 18));
            no.setFont(Font.font("Microsoft YaHei", 18));
            yes.setPadding(new Insets(10));
            no.setPadding(new Insets(10));
            HBox hBox1 = new HBox(text,textField);
            VBox vBox = new VBox(hBox1,text1);
            vBox.setSpacing(StaticValue.stageHeight / 30);
            HBox hBox2 = new HBox(yes,no);
            hBox2.setSpacing(StaticValue.stageHeight / 24);
            hBox2.setAlignment(Pos.CENTER);
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(vBox);
            borderPane.setBottom(hBox2);
            borderPane.setMargin(vBox,new Insets(30));

            Stage alert = new Stage();
            alert.initModality(Modality.APPLICATION_MODAL);
            Scene mat_scene = new Scene(borderPane,StaticValue.stageWidth * 2 / 5,StaticValue.stageHeight * 2 / 5);
//            实现拖拽文件功能
            mat_scene.setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    Dragboard dragboard = event.getDragboard();
                    if (dragboard.hasFiles()){
                        try {
                            File receive_file = dragboard.getFiles().get(0);
                            File add_file = new File("./data/" + StaticValue.userName + "/资源/"
                                    + course + "/" + receive_file.getName());
                            if(add_file.exists()) {
                                alert.close();
                                new Material_alert("Exist",course,0);
                                return;
                            }
                            try {
                                Files.copy(receive_file.toPath(),add_file.toPath());
                            } catch (IOException ex) {
                                alert.close();
                                new Material_alert("Exist",course,0);
                                return;
                            }
                            alert.close();
                            new Material_alert("SuccessAdd",course,0);
                        }catch (Exception e){
                            e.toString();
                        }
                    }
                }
            });
            mat_scene.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    if (event.getDragboard().hasFiles()) {
                        /* allow for both copying and moving, whatever user chooses */
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                    event.consume();
                }
            });
            alert.setScene(mat_scene);
            alert.setTitle("提示信息");
            alert.show();

            no.setOnAction(e -> {
                alert.close();
            });

            yes.setOnAction(e -> {
                if(mat_or_bkm == 0){
                    String new_mat = textField.getText();
                    File f = new File("./data/" + StaticValue.userName + "/资源/" + course + "/" + new_mat);
                    if(f.exists()) {
                        alert.close();
                        new Material_alert("Exist",course,0);
                        return;
                    }
                    try{
                        f.createNewFile();
                    } catch (IOException ex) {
                        alert.close();
                        new Material_alert("Exist",course,0);
                        return;
                    }
                    alert.close();
                    new Material_alert("SuccessAdd",course,0);
                }
                else{
//                    书签链接文件后缀必须为.txt
                    String new_bkm = textField.getText() + ".txt";
                    File f = new File("./data/" + StaticValue.userName + "/资源/" + course + "/书签链接/" + new_bkm);
                    try {
                        f.createNewFile();
                    } catch (IOException ex) {
                        alert.close();
                        new Material_alert("Exist",course,1);
                        return;
                    }
                    alert.close();
                    new Material_alert("SuccessAdd",course,1);
                }

            });
        }
//        提示信息——编辑成功
        else if(s.equals("SuccessEdit")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示信息");
            alert.setHeaderText("");
            alert.setContentText("更改成功！");
            alert.show();
            new Materials(Materials.material_pane,course);
        }
//        提示信息——添加成功
        else if(s.equals("SuccessAdd")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示信息");
            alert.setHeaderText("");
            alert.setContentText("添加成功！");
            alert.show();
            new Materials(Materials.material_pane,course);
        }
//        提示信息——已存在
        else if(s.equals("Exist")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示信息");
            alert.setHeaderText("");
            if(mat_or_bkm == 0){
                alert.setContentText("该文件已存在！");
            }
            else {
                alert.setContentText("该书签已存在！");
            }
            alert.show();
        }
    }
//    删除和编辑的提示信息
    Material_alert(String s,String name,String course,int mat_or_bkm) {
//        提示信息——删除
        if(s.equals("Delete")) {
            Alert alert = new Alert(Alert.AlertType.NONE);
            ButtonType buttonYes = new ButtonType("确定");
            ButtonType buttonNo = new ButtonType("取消");
            alert.getButtonTypes().setAll(buttonYes, buttonNo);
            alert.setTitle("提示信息");
            alert.setHeaderText("");
            if(mat_or_bkm == 0){
                alert.setContentText("确定要删除资料：" + name + " 吗？");
            }
            else{
                String[] tmp = name.split("\\.");
                int drop_size = 0;
                try{
                    drop_size = tmp[tmp.length-1].length();
                }catch (IndexOutOfBoundsException excep){};
                String hplink_name = name.substring(0,name.length()-drop_size-1);
                alert.setContentText("确定要删除书签：" + hplink_name + " 吗？");
            }

            alert.initOwner(Course.stage);
            alert.show();

            alert.setOnCloseRequest(e -> {
                ButtonType result = alert.getResult();
                if (result != null && result.equals(buttonYes)) {
                    if(mat_or_bkm == 0){
                        try {
                            StaticValue.deleteFile(new File("./data/" + StaticValue.userName + "/资源/" +
                                    course + "/" + name));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        new Materials(Materials.material_pane,course);
                    }
                    else{
                        try {
                            StaticValue.deleteFile(new File("./data/" + StaticValue.userName + "/资源/" +
                                    course + "/书签链接/" + name));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        new Materials(Materials.material_pane,course);
                    }

                } else {
                    alert.close();
                }
            });

        }
//        提示信息——编辑
        else if(s.equals("Edit")) {
            Text text;
            if(mat_or_bkm == 0){
                text = new Text("请输入新文件名称：");
            }
            else{
                text = new Text("请输入新书签名称：");
            }
            text.setFont(Font.font("宋体",18));
            TextField textField = new TextField();
            Button yes = new Button("确定");
            Button no = new Button("取消");
            yes.setFont(Font.font("Microsoft YaHei", 18));
            no.setFont(Font.font("Microsoft YaHei", 18));
            yes.setPadding(new Insets(10));
            no.setPadding(new Insets(10));
            HBox hBox1 = new HBox(text,textField);
            HBox hBox2 = new HBox(yes,no);
            hBox2.setAlignment(Pos.CENTER);
            hBox2.setSpacing(StaticValue.stageHeight / 24);
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
                if(mat_or_bkm == 0){
//                    资料保留原有文件后缀
                    String[] temp = name.split("\\.");
                    String new_mat = textField.getText()+"."+temp[temp.length-1];

                    File f1 = new File("./data/" + StaticValue.userName + "/资源/" + course + "/" + name);
                    File f2 = new File("./data/" + StaticValue.userName + "/资源/" + course + "/" + new_mat);
                    alert.close();
                    f1.renameTo(f2);
                    new Material_alert("SuccessEdit",course,0);
                    new Materials(Materials.material_pane,course);
                }
                else{
//                    规定书签文件名后缀必须为txt
                    String new_bkm = textField.getText()+".txt";
                    File f1 = new File("./data/" + StaticValue.userName + "/资源/" + course + "/书签链接/" + name);
                    File f2 = new File("./data/" + StaticValue.userName + "/资源/" + course + "/书签链接/" + new_bkm);
                    alert.close();
                    f1.renameTo(f2);
                    new Material_alert("SuccessEdit",course,1);
                    new Materials(Materials.material_pane,course);
                }
            });
        }
    }
}


