import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ssh.Sftp;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.Test;
import yzh.数值预报处理.nc处理meteo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class 数值预报文件处理 {
    @Test
    public void cs(){
        //沙尘模式删除历史数据();
        DateTime myDate = new DateTime("2021-05-12 20:00:00", DatePattern.NORM_DATETIME_FORMAT);
        沙尘模式同步(myDate);
    }
    public void ftp处理() {

        try {
            try {
                rmaps同步();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void 格点预报同步() {
        try {
            Ftp ftp = new Ftp("172.18.112.10", 21, "hhhtftp", "hhhtftp0606", CharsetUtil.CHARSET_UTF_8);
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
            Ftp ftp = new Ftp("172.18.112.10", 21, "hhhtftp", "hhhtftp0606", CharsetUtil.CHARSET_UTF_8);
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
    public static void 沙尘模式同步(Date date) {
        try {
            Sftp ftp = new Sftp("172.18.142.212", 22, "qxt", "qxt4348050");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String format1 = df.format(date);
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM");
            String format2 = df2.format(date);
            String myDirName = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/huanbao/shachen/" ;
            File fileCon=new File(myDirName+format2+"_log.json");
            JSONArray array=new JSONArray();
            try{
                if(fileCon.exists()){
                    String myConf=FileUtil.readUtf8String(fileCon);
                    if(!myConf.isBlank()){
                        array=JSONUtil.parseArray(myConf);
                        for(int i=0;i<array.size();i++){
                            String datetimeStrLs =(String)((JSONObject)array.get(i)).get("datetime");
                            if(datetimeStrLs.equals(DateUtil.formatDateTime(date))){
                                return;
                            }
                        }
                    }

                }else{
                    FileUtil.touch(fileCon);
                }
            } catch (IORuntimeException e) {
                e.printStackTrace();
            }
            if(ftp.toParent()){
               if(ftp.cd("vsftpd/shachen/product/data_beifen")){
                   List<String> myfiles=ftp.ls(".", lsEntry -> lsEntry.getFilename().contains(format1));
                   if(myfiles.size()>0){
                       String myFileName=myDirName+"qtshachen_"+format1+".nc";
                       File myfile=FileUtil.file(myFileName);
                       if(!myfile.exists()){
                           ftp.download(myfiles.get(0),myfile);
                       }
                       nc处理meteo.区台沙尘模式数据处理(date,myFileName);
                       int count=1;
                       try{
                           JSONObject json1 = JSONUtil.createObj()
                                   .putOnce("count", count)
                                   .putOnce("datetime", DateUtil.formatDateTime(date));
                           array.put(json1);
                           FileUtil.writeUtf8String(array.toString(),fileCon);
                           System.out.println(DateUtil.date() + "处理"+format1+"区台沙尘模式数据");
                       } catch (IORuntimeException e) {
                           e.printStackTrace();
                       }

                   }
               }
           }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        沙尘模式删除历史数据();
    }

    public static void 沙尘模式删除历史数据(){
        Sftp ftp = new Sftp("172.18.142.212", 22, "qxt", "qxt4348050");
        if(ftp.toParent()){
            if(ftp.cd("vsftpd/shachen/product/data_beifen")){
                for(int i=-45;i<-30;i++){
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String format1 = df.format(DateUtil.offsetDay(new Date(),i));
                    List<String> myfiles=ftp.ls(".", lsEntry -> lsEntry.getFilename().contains(format1));
                    if(myfiles.size()>0){
                        ftp.delFile(myfiles.get(0));
                    }
                }
            }

        }
        String myDirName = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/huanbao/shachen/" ;
        for(int i=-15;i<=-3;i++){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String format1 = df.format(DateUtil.offsetDay(new Date(),i));
            String myFileName=myDirName+"qtshachen_"+format1+".nc";
            if(FileUtil.exist(myFileName)){
                FileUtil.del(myFileName);
            }
        }
    }

    public void 站点预报同步() {
        try {
            Ftp ftp = new Ftp("172.18.112.10", 21, "hhhtftp", "hhhtftp0606", CharsetUtil.CHARSET_UTF_8);
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
