import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpMode;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class 数值预报文件处理 {
    public void ftp处理() {
        try {
            try {
                格点预报同步();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                站点预报同步();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                rmaps同步();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void 格点预报同步() {
        try {
            Ftp ftp = new Ftp("172.18.112.10", 21, "hhhtftp", "hhhtftp0606", CharsetUtil.CHARSET_UTF_8, FtpMode.Passive);
            ftp.setBackToPwd(false);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String format1 = df.format(new Date());
            ftp.cd("zdyb_szyb/grid/method");

            String myDirName = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "\\区台数值预报文件\\格点数据\\" + format1 + "\\";
            if (ftp.exist(format1)) {
                if (!FileUtil.exist(myDirName)) {
                    FileUtil.mkdir(myDirName);
                }
                List<String> list = ftp.ls(format1);
                for (String fileName : list
                ) {
                    try {
                        String filePath = myDirName + fileName;
                        if (!FileUtil.exist(filePath)) {
                            ftp.download(format1, fileName, FileUtil.file(filePath));
                            System.out.println(DateUtil.date() + "   " + filePath);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void rmaps同步() {
        try {
            Ftp ftp = new Ftp("172.18.112.10", 21, "hhhtftp", "hhhtftp0606", CharsetUtil.CHARSET_UTF_8, FtpMode.Passive);
            ftp.setBackToPwd(false);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String format1 = df.format(new Date());
            ftp.cd("zdyb_szyb/grid/rmaps");

            String myDirName = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "\\区台数值预报文件\\rmaps数据\\" + format1 + "\\";
            if (ftp.exist(format1)) {
                if (!FileUtil.exist(myDirName)) {
                    FileUtil.mkdir(myDirName);
                }
                List<String> list = ftp.ls(format1);
                for (String fileName : list
                ) {
                    try {
                        String filePath = myDirName + fileName;
                        if (!FileUtil.exist(filePath)) {
                            ftp.download(format1, fileName, FileUtil.file(filePath));
                            System.out.println(DateUtil.date() + "   " + filePath);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

            }

            format1 = DateUtil.format(DateUtil.offsetDay(new Date(), -1), "yyyyMMdd");
            myDirName = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "\\区台数值预报文件\\rmaps数据\\" + format1 + "\\";
            if (ftp.exist(format1)) {
                if (!FileUtil.exist(myDirName)) {
                    FileUtil.mkdir(myDirName);
                }
                List<String> list = ftp.ls(format1);
                for (String fileName : list
                ) {
                    try {
                        String filePath = myDirName + fileName;
                        if (!FileUtil.exist(filePath)) {
                            ftp.download(format1, fileName, FileUtil.file(filePath));
                            System.out.println(DateUtil.date() + "   " + filePath);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    public void 站点预报同步() {
        try {
            Ftp ftp = new Ftp("172.18.112.10", 21, "hhhtftp", "hhhtftp0606", CharsetUtil.CHARSET_UTF_8, FtpMode.Passive);
            ftp.setBackToPwd(true);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String format1 = df.format(new Date());
            ftp.cd("zdyb_szyb/site");
            List<String> dataTypes = ftp.ls("");
            for (String dataType : dataTypes
            ) {
                try {
                    String myDirName = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "\\区台数值预报文件\\站点数据\\" + dataType + "\\" + format1 + "\\";
                    if (ftp.exist(dataType + "/" + format1)) {
                        if (!FileUtil.exist(myDirName)) {
                            FileUtil.mkdir(myDirName);
                        }
                        List<String> list = ftp.ls(dataType + "/" + format1);

                        for (String fileName : list
                        ) {
                            try {
                                String filePath = myDirName + fileName;
                                if (!FileUtil.exist(filePath)) {
                                    List<String> ss12 = ftp.ls("");
                                    ftp.download(dataType + "/" + format1, fileName, FileUtil.file(filePath));
                                    List<String> ss11 = ftp.ls("");
                                    System.out.println(DateUtil.date() + "   " + filePath);
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
