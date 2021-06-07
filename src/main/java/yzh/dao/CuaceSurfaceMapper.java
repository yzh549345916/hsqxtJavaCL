package yzh.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import yzh.model.CuaceSurface;
import yzh.数值预报处理.环境气象.站点信息;
import yzh.数值预报处理.环境气象.高空要素Model;

@Mapper
public interface CuaceSurfaceMapper {
    /**
     * delete by primary key
     * @param stationid primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(@Param("stationid") String stationid, @Param("datetime") Date datetime, @Param("validtime") Integer validtime);

    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(CuaceSurface record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(CuaceSurface record);

    /**
     * select by primary key
     * @param stationid primary key
     * @return object by primary key
     */
    CuaceSurface selectByPrimaryKey(@Param("stationid") String stationid, @Param("datetime") Date datetime, @Param("validtime") Integer validtime);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(CuaceSurface record);
    List<站点信息> GetStationsByType(@Param("Type") String Type);
    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(CuaceSurface record);

    int insert_CuaceSurface(@Param("dataList") List<高空要素Model> dataList, @Param("Type") String Type);
    int insert_CuaceHeigh(@Param("dataList") List<高空要素Model> dataList, @Param("Type") String Type);
}