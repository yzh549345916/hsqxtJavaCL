package yzh.数值预报处理.环境气象;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface huanbao {
    List<站点信息> GetStationsByType(@Param("Type") String Type);

    Integer insert_TEM(@Param("dataList") List<高空要素Model> dataList);

    Integer insert_ECheigh(@Param("dataList") List<高空要素Model> dataList, @Param("Type") String Type);

    Integer CountEC(@Param("Type") String Type, @Param("mydatetime") Date mydatetime);

    List<EC高空Model> GetECHeightByDatetime(@Param("mydatetime") Date mydatetime);

    void deleteHistoryEC(@Param("mydatetime") Date mydatetime);
    Integer insert_qtShaChen(@Param("tableNameDate")String tableNameDate,@Param("dataList") List<区台沙尘Model> dataList);

    List<区台沙尘Model> GetqtShaChenSurfaceByStationIDDatetimeValidTimeFcstName(@Param("stationID") String stationID, @Param("Datetime") Date datetime, @Param("validTime") Integer validTime, @Param("fcstName") String fcstName);

    List<区台沙尘Model> GetqtShaChenSurfaceByDatetimeValidTimeFcstName(@Param("Datetime") Date datetime, @Param("validTime") Integer validTime, @Param("fcstName") String fcstName);

    List<区台沙尘Model> GetqtShaChenSurfaceByStationIDDatetimeFcstName(@Param("stationID") String stationID, @Param("Datetime") Date datetime, @Param("fcstName") String fcstName);

    List<区台沙尘Model> GetqtShaChenHeightByStationIDDatetimeValidTimeFcstName(@Param("stationID") String stationID, @Param("Datetime") Date datetime, @Param("validTime") Integer validTime, @Param("fcstName") String fcstName);
}
