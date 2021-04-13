package yzh.数值预报处理.环境气象;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface huanbao {
    public List<站点信息> GetStationsByType(@Param("Type")String Type);
    public Integer insert_TEM(@Param("dataList")List<高空要素Model> dataList);
    public Integer insert_ECheigh(@Param("dataList")List<高空要素Model> dataList,@Param("Type")String Type);
    public Integer CountEC(@Param("Type")String Type,@Param("mydatetime") Date mydatetime);
    public List<EC高空Model> GetECHeightByDatetime(@Param("mydatetime") Date mydatetime);
    public void deleteHistoryEC(@Param("mydatetime") Date mydatetime);
}
