package yzh.环境气象;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.junit.Test;
import yzh.天擎.myself.文件下载;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class 沙尘模式下载 {
    @Test
    public void cs(){
        日常下载();
    }
    public static void 日常下载(){
       try{
           JSONArray myFileLists= 文件下载.下载亚洲沙尘模式(new Date());
           if(myFileLists!=null&&myFileLists.size()>0){
               for (Object myList:myFileLists
                    ) {
                   try{
                       JSONObject myfile=(JSONObject)myList;
                       Date date = DateUtil.parse(myfile.get("Datetime").toString(), "yyyyMMddHHmmss");
                       long filesize= Convert.toLong(myfile.get("FILE_SIZE").toString(),999999L);
                       String file_nameILE_NAME=myfile.get("FILE_NAME").toString();
                       if(!判断指定日期沙尘文件是否存在(date,filesize,file_nameILE_NAME)){
                           String file_url=myfile.get("FILE_URL").toString();


                           SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                           String format1 = df.format(date);
                           String myName = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "\\区台数值预报文件\\szyb\\huanbao\\CUACE\\" + format1 + "\\"+file_nameILE_NAME;
                           HttpUtil.downloadFile(file_url, FileUtil.file(myName), new StreamProgress(){
                               double jd=0;
                               @Override
                               public void start() {
                                   Console.log("开始下载  {}",file_nameILE_NAME);
                               }

                               @Override
                               public void progress(long progressSize) {
                                   double jdls=(progressSize/(double)filesize)*100;
                                   if(jdls-jd>3){
                                       Console.log("{}已下载：{}  {}%", file_nameILE_NAME,FileUtil.readableFileSize(progressSize), NumberUtil.round(jdls,1));
                                       jd=jdls;
                                   }

                               }

                               @Override
                               public void finish() {
                                   Console.log("下载完成  {}",file_nameILE_NAME);
                               }
                           });
                       }

                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }

           }
       } catch (Exception e) {
           e.printStackTrace();
       }
    }
    public static boolean 判断指定日期沙尘文件是否存在(Date date,long size,String fileName){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String format1 = df.format(date);
        String myDirName = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "\\区台数值预报文件\\szyb\\huanbao\\CUACE\\" + format1 + "\\";
        if (!FileUtil.exist(myDirName)) {
            FileUtil.mkdir(myDirName);
            return false;
        }
        myDirName += fileName;
        //FileUtil.exist(myDirName,".*"+df2.format(date)+".nc$")
        if(FileUtil.exist(myDirName)){
            File myfile=new File(myDirName);
            return FileUtil.size(myfile)==size;
        }else{
            return false;
        }


    }
}
