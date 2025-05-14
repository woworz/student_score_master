package com.example.score.config;

import com.example.score.utils.PageRowBounds;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Description MyBatis分页拦截器
 **/
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
@Component
public class PageInterceptor implements Interceptor {

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
        
        // 分离代理对象链
        while (metaObject.hasGetter("h")) {
            Object obj = metaObject.getValue("h");
            metaObject = MetaObject.forObject(obj, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
        }
        
        // 分离最后一个代理对象的目标类
        while (metaObject.hasGetter("target")) {
            Object obj = metaObject.getValue("target");
            metaObject = MetaObject.forObject(obj, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
        }
        
        Configuration configuration = (Configuration) metaObject.getValue("delegate.configuration");
        
        // 获取分页参数
        RowBounds rowBounds = (RowBounds) metaObject.getValue("delegate.rowBounds");
        
        // 如果不是自定义的分页参数，则直接返回
        if (!(rowBounds instanceof PageRowBounds)) {
            return invocation.proceed();
        }
        
        // 获取分页参数
        PageRowBounds pageRowBounds = (PageRowBounds) rowBounds;
        
        // 获取原始SQL语句
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        String originalSql = boundSql.getSql();
        
        // 获取总记录数
        Connection connection = (Connection) invocation.getArgs()[0];
        long total = getTotal(connection, originalSql, boundSql, configuration);
        pageRowBounds.setTotal(total);
        
        // 构建分页SQL
        String pageSql = buildPageSql(originalSql, pageRowBounds);
        metaObject.setValue("delegate.boundSql.sql", pageSql);
        
        // 采用物理分页后，就不需要MyBatis的内存分页了，所以重置RowBounds为默认值
        metaObject.setValue("delegate.rowBounds", RowBounds.DEFAULT);
        
        return invocation.proceed();
    }

    /**
     * 获取总记录数
     */
    private long getTotal(Connection connection, String originalSql, BoundSql boundSql, Configuration configuration) throws SQLException {
        String countSql = "SELECT COUNT(*) FROM (" + originalSql + ") AS temp";
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            countStmt = connection.prepareStatement(countSql);
            
            // 设置参数
            ParameterHandler parameterHandler = configuration.newParameterHandler(
                    (MappedStatement) boundSql.getAdditionalParameter("mappedStatement"),
                    boundSql.getParameterObject(),
                    boundSql);
            parameterHandler.setParameters(countStmt);
            
            rs = countStmt.executeQuery();
            long total = 0;
            if (rs.next()) {
                total = rs.getLong(1);
            }
            return total;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // 忽略
                }
            }
            if (countStmt != null) {
                try {
                    countStmt.close();
                } catch (SQLException e) {
                    // 忽略
                }
            }
        }
    }

    /**
     * 构建分页SQL
     */
    private String buildPageSql(String originalSql, PageRowBounds pageRowBounds) {
        StringBuilder pageSql = new StringBuilder(originalSql);
        pageSql.append(" LIMIT ").append(pageRowBounds.getOffset()).append(",").append(pageRowBounds.getLimit());
        return pageSql.toString();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 可以在这里设置一些属性
    }
}
