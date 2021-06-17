package yzh.dao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import yzh.model.ECSurface;
import yzh.数值预报处理.环境气象.站点信息;
import yzh.数值预报处理.环境气象.高空要素Model;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Mapper
public interface ECSurfaceMapper {
    Integer countByDatetimeAndTmaxIsNotNull(@Param("datetime") Date datetime);
    Integer countByDatetimeAndTminIsNotNull(@Param("datetime") Date datetime);
    Integer countByDatetimeAndTEMIsNotNull(@Param("datetime")Date datetime);
    Integer countByDatetimeAndWIU10IsNotNull(@Param("datetime")Date datetime);
    Integer countByDatetimeAndWIV10IsNotNull(@Param("datetime")Date datetime);
    Integer countByDatetimeAndPRSIsNotNull(@Param("datetime")Date datetime);
    Integer countByDatetimeAndDPTIsNotNull(@Param("datetime")Date datetime);
    Integer countByDatetimeAndRainIsNotNull(@Param("datetime") Date datetime);
    List<站点信息> GetStationsByType(@Param("Type") String Type);
    int insert_ECSurface(@Param("dataList") List<高空要素Model> dataList, @Param("Type") String Type);
    int deleteByDatetimeBefore(@Param("maxDatetime")Date maxDatetime);
    List<ECSurface> findAllByDatetimeAndValidtimeGreaterThanAndValidtimeLessThanOrEqualTo(@Param("datetime")Date datetime,@Param("minValidtime")Integer minValidtime,@Param("maxValidtime")Integer maxValidtime);
    List<ECSurface> findAllByDatetimeAndValidtimeInOrderByValidtime(@Param("datetime")Date datetime,@Param("validtimeCollection")Collection<Integer> validtimeCollection);
    List<ECSurface> findAllByDatetimeAndValidtime(@Param("datetime")Date datetime,@Param("validtime")Integer validtime);





}