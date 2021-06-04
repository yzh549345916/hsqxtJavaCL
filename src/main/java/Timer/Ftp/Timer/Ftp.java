package Timer.Ftp.Timer;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Ftp implements Runnable{
    @Override
    public void run() {
        cn.hutool.extra.ftp.Ftp ftp = new cn.hutool.extra.ftp.Ftp("172.18.112.10", 21, "hhhtftp", "hhhtftp0606", CharsetUtil.CHARSET_UTF_8);
        ftp.setBackToPwd(false);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String format1 = df.format(new Date());
        ftp.cd("zdyb_szyb");
        String myDirName = System.getProperty("user.dir") + "\\java测试文件夹\\" + format1 + "\\";
        if (ftp.exist(format1)) {
            if (!FileUtil.exist(myDirName)) {
                FileUtil.mkdir(myDirName);
            }
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            System.out.println(df2.format(new Date())+"：");
            //boolean b1 = ftp.cd(format1);
            List<String> list = ftp.ls(format1);
            for (String fileName : list
            ) {
                try {
                    String filePath = myDirName + fileName;
                    if (!FileUtil.exist(filePath)) {
                        ftp.download(format1, fileName, FileUtil.file(filePath));
                        System.out.println(fileName);
                    } else {
                        System.out.println(fileName + "已存在");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        /*System.out.println("\n是否退出程序：Y/N");
        Scanner input = new Scanner(System.in);
        String Y_N = input.nextLine();
        if (Y_N.equals('Y') || Y_N.equals('y')) {
            System.exit(0);
        }*/
    }
}
