package yzh.dao;

import model.Rmaps数值预报站点Model;
import model.区台格点数值预报站点Model;
import model.站点信息;
import java.util.Date;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RMAPSDao {
    public List<站点信息> getAllStations();
    public int insert_rmaps_GD_ZD(@Param("tableName")String tableName, @Param("dataList")List<Rmaps数值预报站点Model> dataList);
    public int count_Rmaps_GD_ZD(@Param("TableName")String tableName, @Param("DataType")String DataType, @Param("Time")Date Time);
}
