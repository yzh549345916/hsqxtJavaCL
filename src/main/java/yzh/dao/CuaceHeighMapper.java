package yzh.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import yzh.model.CuaceHeigh;
import yzh.数值预报处理.环境气象.站点信息;

@Mapper
public interface CuaceHeighMapper {
    /**
     * delete by primary key
     * @param stationid primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(@Param("stationid") String stationid, @Param("datetime") Date datetime, @Param("validtime") Integer validtime, @Param("fcstlevel") Integer fcstlevel);
    List<站点信息> GetStationsByType(@Param("Type") String Type);
    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(CuaceHeigh record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(CuaceHeigh record);

    /**
     * select by primary key
     * @param stationid primary key
     * @return object by primary key
     */
    CuaceHeigh selectByPrimaryKey(@Param("stationid") String stationid, @Param("datetime") Date datetime, @Param("validtime") Integer validtime, @Param("fcstlevel") Integer fcstlevel);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(CuaceHeigh record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(CuaceHeigh record);
}