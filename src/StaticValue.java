import java.io.File;
import java.io.IOException;

public class StaticValue {
    static double stageWidth = 800;
    static double stageHeight = 450;
    static String userName;
    static String course;

    public static void deleteFile(File file) throws IOException {
        File[] files = file.listFiles();
        if (files!=null){
            for (int i = 0; i < files.length ; i++) {
                if (files[i].isFile()){
                    files[i].delete();
                }else if (files[i].isDirectory()){
                    deleteFile(files[i]);
                }
                files[i].delete();//删除子目录
            }
        }
        file.delete();
    }
}