import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import model.区台格点数值预报站点Model;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import yzh.dao.StationDao;
import yzh.dao.StationDaoImpl;
import yzh.util.SqlSessionFactoryUtil;

import java.util.List;


public class 主程序 {
    public static void main(String[] args) {
        数值预报文件处理 szyb = new 数值预报文件处理();
        szyb.ftp处理();
        区台格点数值预报站点数据定时处理();
        RMAPS数值预报站点数据定时处理();
        RMAPS数值预报站点数据历史处理();
        /*
        考虑到Quartz表达式的兼容性，且存在对于秒级别精度匹配的需求，Hutool可以通过设置使用秒匹配模式来兼容。
        //支持秒级别定时任务  CronUtil.setMatchSecond(true); 此时Hutool可以兼容Quartz表达式（5位表达式、6位表达式都兼容）
        星号（*）：代表所有可能的值，例如month字段如果是星号，则表示在满足其它字段的制约条件后每月都执行该命令操作。
        逗号（,）：可以用逗号隔开的值指定一个列表范围，例如，“1,2,5,7,8,9”
        中杠（-）：可以用整数之间的中杠表示一个整数范围，例如“2-6”表示“2,3,4,5,6”  */
       // */10 * * * * *  十秒
        //正斜线（/）：可以用正斜线指定时间的间隔频率，例如“0-23/2”表示每两小时执行一次。同时正斜线可以和星号一起使用，例如*/10，如果用在minute字段，表示每十分钟执行一次。
        CronUtil.schedule("*/1 * * * *", new Task() {
            @Override
            public void execute() {
                szyb.ftp处理();
            }
        });
        CronUtil.schedule("40,45,50,59 4,5,13,14 * * *", new Task() {
            @Override
            public void execute() {
                区台格点数值预报站点数据定时处理();
            }
        });
        CronUtil.schedule("22 1 * * *", new Task() {
            @Override
            public void execute() {
                区台格点数值预报站点数据历史处理();
                RMAPS数值预报站点数据历史处理();
            }
        });
        CronUtil.schedule("10,20,30,40,50 1,2,12,13 * * *", new Task() {
            @Override
            public void execute() {
                RMAPS数值预报站点数据定时处理();
            }
        });
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }
    public static void 区台格点数值预报站点数据定时处理(){
        DateTime myDate=DateUtil.offsetHour(DateUtil.beginOfDay(DateUtil.date()), 20);
        if(DateUtil.hour(DateUtil.date(),true)<12){
            myDate=DateUtil.offsetHour(DateUtil.beginOfDay(DateUtil.date()), 8);
        }
        Grib2处理 grib2处理=new Grib2处理();
        if(!grib2处理.判断数据是否存在(1,103,"TEM", myDate)){
            List<区台格点数值预报站点Model> lists= grib2处理.区台格点数值预报站点数据Grib2文件处理(myDate);
            if(lists.size()>0){
                SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();
                SqlSession session = sqlSessionFactory.openSession(true);
                StationDao stationDao = session.getMapper(StationDao.class);
                int n=stationDao.insert_Szyb_GD_ZD("Szyb_GD_ZD_"+DateUtil.format(myDate,"yyyyMMdd"), lists);
                if(n>0){
                    System.out.println(DateUtil.date()+"   共计入库"+myDate+"的数据"+n+"条。");
                }
            }
        }
    }
    public static void RMAPS数值预报站点数据定时处理(){
        DateTime myDate=DateUtil.offsetHour(DateUtil.beginOfDay(DateUtil.date()), 8);
        if(DateUtil.hour(DateUtil.date(),true)<=12){
            myDate=DateUtil.offsetHour(DateUtil.beginOfDay(DateUtil.date()), 20-24);
        }
        Grib2处理 grib2处理=new Grib2处理();
        try{
            if(!grib2处理. rmaps格点数据是否存在(myDate)){
                grib2处理.rmaps格点提取(myDate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!grib2处理.判断RMAPS数据是否存在("TEM", myDate)){
            grib2处理. rmaps格点数值预报站点数据Grib2文件处理(myDate);
        }
    }
    @Test
    public static void cs(){
        DateTime myDate=DateUtil.offsetHour(DateUtil.beginOfDay(DateUtil.date()), 20);
        if(DateUtil.hour(DateUtil.date(),true)<12){
            myDate=DateUtil.offsetHour(DateUtil.beginOfDay(DateUtil.date()), 8);
        }
        Grib2处理 grib2处理=new Grib2处理();
        List<区台格点数值预报站点Model> lists= grib2处理.区台格点数值预报站点数据Grib2文件处理(myDate);
        if(lists.size()>0){
            SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();
            SqlSession session = sqlSessionFactory.openSession(true);
            StationDao stationDao = session.getMapper(StationDao.class);
            int n=stationDao.insert_Szyb_GD_ZD("Szyb_GD_ZD_"+DateUtil.format(myDate,"yyyyMMdd"), lists);
            if(n>0){
                System.out.println(DateUtil.date()+"   共计入库"+myDate+"的数据"+n+"条。");
            }
        }
    }
    public static void 区台格点数值预报站点数据历史处理(){
        try{
            DateTime myDate=DateUtil.offsetHour(DateUtil.beginOfDay(DateUtil.date()), 8);
            Grib2处理 grib2处理=new Grib2处理();
            SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();
            SqlSession session = sqlSessionFactory.openSession(true);
            StationDao stationDao = session.getMapper(StationDao.class);

            for(int i=-240;i<0;i=i+12)
            {
                DateTime myTime=DateUtil.offsetHour(myDate,i);
                if(!grib2处理.判断数据是否存在(1,103,"TEM", myTime)){
                    List<区台格点数值预报站点Model> lists= grib2处理.区台格点数值预报站点数据Grib2文件处理(myTime);
                    if(lists.size()>0){
                        int n=stationDao.insert_Szyb_GD_ZD("Szyb_GD_ZD_"+DateUtil.format(myTime,"yyyyMMdd"), lists);
                        if(n>0){
                            System.out.println(DateUtil.date()+"   共计恢复入库"+myTime+"的数据"+n+"条。");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void RMAPS数值预报站点数据历史处理(){
       try{
           DateTime myDate=DateUtil.offsetHour(DateUtil.beginOfDay(DateUtil.date()), 8-24);
           Grib2处理 grib2处理=new Grib2处理();
           SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();
           SqlSession session = sqlSessionFactory.openSession(true);

           for(int i=-240;i<0;i=i+12)
           {
               DateTime myTime=DateUtil.offsetHour(myDate,i);
               if(!grib2处理.判断RMAPS数据是否存在("TEM", myTime)){
                   grib2处理. rmaps格点数值预报站点数据Grib2文件处理(myTime);
               }
               try{
                   if(!grib2处理. rmaps格点数据是否存在(myTime)){
                       grib2处理.rmaps格点提取(myTime);
                   }

               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
    }
    public static void RMAPS数值预报站点数据总历史处理(){
        try{
            DateTime myDate=DateUtil.offsetHour(DateUtil.beginOfDay(DateUtil.date()), 8-24);
            Grib2处理 grib2处理=new Grib2处理();
            SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();
            SqlSession session = sqlSessionFactory.openSession(true);
            DateTime startdateTime = new DateTime("2020-08-07 08:00:00", DatePattern.NORM_DATETIME_FORMAT);
            while(startdateTime.compareTo(myDate)<=0){
                if(!grib2处理.判断RMAPS数据是否存在("TEM", startdateTime)){
                    grib2处理. rmaps格点数值预报站点数据Grib2文件处理(startdateTime);
                }
                startdateTime= DateUtil.offsetHour(startdateTime,12);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
