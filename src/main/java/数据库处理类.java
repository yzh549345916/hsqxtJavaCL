import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.setting.Setting;
import model.区台数值预报数据Model;
import model.区台格点数值预报站点Model;

import yzh.dao.StationDaoImpl;


import java.util.List;

public class 数据库处理类 {

    public 数据库处理类() {
       /* try {
            dBConfigPath = "设置文件/数据库设置.setting";
            Setting setting = new Setting(dBConfigPath);
            String driver = setting.getByGroup("driver", "yzh");
            url = setting.getByGroup("区台数值预报url", "yzh");
            user = setting.getByGroup("user", "yzh");
            pass = setting.getByGroup("pass", "yzh");
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
    }


    public void cs() {

        Grib2处理 grib2处理=new Grib2处理();
        List<区台格点数值预报站点Model>  lists= grib2处理.区台格点数值预报站点数据Grib2文件处理(DateUtil.offsetHour(DateUtil.beginOfDay(DateUtil.date()), 8));
        StationDaoImpl stationDao = new StationDaoImpl();
       int n= stationDao.插入格点数值预报站点数据(lists);
        String dateStr = "2020-07-22 16:12:00";
        DateTime myDateTime = DateUtil.parse(dateStr);

    }


    /*public boolean 判断数据是否存在(String dataType, DateTime myDateTime){
        StationDaoImpl stationDao=new StationDaoImpl();
        List<站点信息> stations =stationDao.获取站点信息();
        if(stations.size()>0)
        {
            try {
                Connection connection = null;
                Statement stmt = null;
                connection = DriverManager.getConnection(url, user, pass);
                if (connection != null) {
                    stmt = connection.createStatement();
                    StringBuilder sb=new StringBuilder();
                    sb.append("SELECT COUNT(*) mycount FROM `区台数值预报`.`数值预报` WHERE ").append("MyDate = '").append(DateUtil.formatDateTime(myDateTime)).append("' AND ").append(dataType).append(" IS NOT NULL");
                    String sql = sb.toString();
                    ResultSet rs = stmt.executeQuery(sql);
                    while (rs.next()) {
                        try {
                            //如果查询的结果数大于站点总数的90%（每个站点未来72小时逐三小时共计24个点）
                            if(rs.getInt("mycount")>= (stations.size()*0.9*24)){
                                return true;
                            }

                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


        return false;
    }*/

    /*public List<站点信息> 获取站点信息() {
        List<站点信息> stations = new ArrayList<站点信息>();
        try {
            Connection connection = null;
            Statement stmt = null;
            connection = DriverManager.getConnection(url, user, pass);
            if (connection != null) {
                stmt = connection.createStatement();
                String sql = "SELECT * FROM `区台数值预报`.`Stations`";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    try {
                        stations.add(new 站点信息(rs.getString("ID"), rs.getString("Name"), rs.getString("Admin_Code"), rs.getString("Province"), rs.getString("City"), rs.getString("Cnty"), rs.getInt("Station_levl"), rs.getDouble("Lat"), rs.getDouble("Lon"), rs.getDouble("High")));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return stations;
    }*/


    public void 数值预报入库(List<区台数值预报数据Model> dataLists) {

    }

}

