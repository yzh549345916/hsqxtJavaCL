package yzh.dao;

import model.区台格点数值预报站点Model;
import model.数值预报数据检索Model;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StationDao {
    public int 插入格点数值预报站点数据(List<区台格点数值预报站点Model> records);
    public int 获取数据已入库个数(数值预报数据检索Model data);
    public int insert_Szyb_GD_ZD(@Param("tableName")String tableName, @Param("dataList")List<区台格点数值预报站点Model> dataList);
}
