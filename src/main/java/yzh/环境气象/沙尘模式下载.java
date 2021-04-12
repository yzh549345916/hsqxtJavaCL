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
import yzh.数值预报处理.nc处理;

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
                           String myName = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/huanbao/CUACE/" + format1 + "/"+file_nameILE_NAME;
                           String comMyName = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/huanbao/compressCUACE/" + format1 + "/";
                           if(!FileUtil.exist(comMyName)){
                               FileUtil.mkdir(comMyName);
                           }
                           comMyName+=file_nameILE_NAME;
                           HttpUtil.downloadFile(file_url, FileUtil.file(myName), new StreamProgress(){
                               double jd=0;
                               @Override
                               public void start() {
                                   Console.log("{}  开始下载  {}",new Date(),file_nameILE_NAME);
                               }

                               @Override
                               public void progress(long progressSize) {
                                   double jdls=(progressSize/(double)filesize)*100;
                                   if(jdls-jd>3){
                                       Console.log("{}  {}已下载：{}  {}%", new Date(),file_nameILE_NAME,FileUtil.readableFileSize(progressSize), NumberUtil.round(jdls,1));
                                       jd=jdls;
                                   }

                               }

                               @Override
                               public void finish() {
                                   Console.log("{}下载完成  {}",new Date(),file_nameILE_NAME);

                               }
                           });
                           if(FileUtil.exist(myName)){
                               //String pathLS= FileUtil.file(comMyName).getPath();
                               if(nc处理.compressCUACE(myName,comMyName)){
                                   Console.log("{} {}压缩成功",new Date(),file_nameILE_NAME);
                               }else{
                                   Console.log("{} {}压缩失败",new Date(),file_nameILE_NAME);
                               }
                           }

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
        String myDirName = FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/huanbao/CUACE/" + format1 + "/";
        if (!FileUtil.exist(myDirName)) {
            FileUtil.mkdir(myDirName);
            return false;
        }
        myDirName += fileName;
        //FileUtil.exist(myDirName,".*"+df2.format(date)+".nc$")
        if(FileUtil.exist(myDirName)){
            File myfile= FileUtil.file(myDirName);
            return FileUtil.size(myfile)==size;
        }else{
            return false;
        }


    }

    public static void 压缩近7天的数据(){
        try{
            Date myDate=new Date();
            for(int i=-7;i<=0;i++){
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String format1 = df.format(myDate);
                String sPath=FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/huanbao/CUACE/" + format1 + "/";
                if(FileUtil.exist(sPath)){
                    File[] files=FileUtil.ls(sPath);
                    if(files.length>0){
                        String dPath=FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/huanbao/compressCUACE/" + format1 + "/";
                        if(!FileUtil.exist(dPath)){
                            FileUtil.mkdir(dPath);
                        }
                        for (File file:files
                        ) {
                            String mydPath=dPath+file.getName();
                            if(!FileUtil.exist(mydPath)){
                                nc处理.compressCUACE(file.getPath(),mydPath);
                                Console.log("压缩{}",mydPath);
                            }
                        }
                    }
                }

                myDate=DateUtil.offsetDay(myDate,-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void 删除7天前的原始的数据(){
        try{
            Date myDate=DateUtil.offsetDay(new Date(),-8);
            String sPath=FileUtil.getParent(new ClassPathResource("config").getAbsolutePath(), 2) + "/区台数值预报文件/szyb/huanbao/CUACE/" ;
            for(int i=-30;i<=0;i++){
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String format1 = df.format(myDate);
                String myPath=sPath+format1 + "/";
                if(FileUtil.exist(myPath)){
                    FileUtil.del(myPath);
                }

                myDate=DateUtil.offsetDay(myDate,-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
