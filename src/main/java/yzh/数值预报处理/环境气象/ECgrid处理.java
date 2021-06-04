package yzh.数值预报处理.环境气象;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import org.meteoinfo.data.GridData;
import org.meteoinfo.data.meteodata.MeteoDataInfo;
import org.meteoinfo.data.meteodata.Variable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ECgrid处理 {

    public static List<高空要素Model> EC地面grid处理(Date sdate,String resourPath, List<站点信息> stations){
        List<高空要素Model> dataLists = new ArrayList<>();
        MeteoDataInfo aDataInfo = new MeteoDataInfo();
        aDataInfo.openNetCDFData(resourPath);
        List<LocalDateTime> tDimension = aDataInfo.getDataInfo().getTimes();
        aDataInfo.setLevelIndex(0);
        LocalDateTime sLocalDate= LocalDateTimeUtil.of(DateUtil.offsetHour(sdate,-8));
        for (int i = 0; i < tDimension.size(); i++) {
            aDataInfo.setTimeIndex(i);
            List<Variable> variables = aDataInfo.getDataInfo().getVariables();
            for(Variable variable:variables){
              if(variable.isPlottable()){
                  aDataInfo.setVariableName(variable.getName());
                  GridData myGridData = aDataInfo.getGridData();
                  for (int k = 0; k < stations.size(); k++) {
                      站点信息 station = stations.get(k);
                      dataLists.add(new 高空要素Model((int)LocalDateTimeUtil.between(sLocalDate,tDimension.get(i)).toHours(), 0, myGridData.getValue(station.getLon(), station.getLat()), station.getID(), sdate));
                  }
              }
            }
        }
        return dataLists;
    }
}
