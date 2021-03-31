package yzh.dao;

import model.区台数值预报数据Model;
import model.区台格点数值预报站点Model;
import model.数值预报数据检索Model;
import model.站点信息;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.ArrayList;
import java.util.List;

public class StationDaoImpl extends BaseDaoImpl implements StationDao{
    public List<站点信息> 获取站点信息() {
        SqlSession sqlSession = getSqlSession();
        List<站点信息> stations = null;
        try {
            stations = sqlSession.selectList("yzh.dao.StationDao.获取站点信息");
        } finally {
            sqlSession.close();
        }
        return stations;
    }

    public int 插入站点(站点信息 upmsUser) {
        SqlSession sqlSession = getSqlSession();
        int n = 0;
        try {
            n = sqlSession.insert("yzh.dao.StationDao.插入站点", upmsUser);
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
        return n;
    }
    @Override
    public int 插入格点数值预报站点数据(List<区台格点数值预报站点Model> records) {

        SqlSession sqlSession = getSqlSession();
        int n = 0;
        try {
            n = sqlSession.insert("yzh.dao.StationDao.insert_Szyb_GD_ZD", records);
            //sqlSession.insertList(@Param("records") List<GwServerConfDetail> records);
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
        return n;
    }
    @Override
    public int insert_Szyb_GD_ZD(@Param("tableName")String tableName, @Param("dataList")List<区台格点数值预报站点Model> dataList){
        return 0;
    }
    @Override
    public int 获取数据已入库个数(数值预报数据检索Model data) {
        SqlSession sqlSession = getSqlSession();
        int count =0;
        try {
            count = sqlSession.selectOne("yzh.dao.StationDao.count_Szyb_GD_ZD",data);
        } finally {
            sqlSession.close();
        }
        return count;
    }
}
