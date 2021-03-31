package yzh.dao;

import model.区台格点数值预报站点Model;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import yzh.util.SqlSessionFactoryUtil;

import java.util.List;

public class BaseDaoImpl implements BaseDao {


    public SqlSession getSqlSession() {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();
        //sqlSessionFactory.openSession(true)自动提交事务
        return sqlSessionFactory.openSession();
    }


}
