package com.example.score.utils;

import org.apache.ibatis.session.RowBounds;

/**
 * Description 自定义分页参数类，继承自MyBatis的RowBounds
 **/
public class PageRowBounds extends RowBounds {
    
    /**
     * 总记录数
     */
    private long total;

    /**
     * 构造函数
     * @param offset 偏移量
     * @param limit 每页记录数
     */
    public PageRowBounds(int offset, int limit) {
        super(offset, limit);
    }

    /**
     * 获取总记录数
     * @return 总记录数
     */
    public long getTotal() {
        return total;
    }

    /**
     * 设置总记录数
     * @param total 总记录数
     */
    public void setTotal(long total) {
        this.total = total;
    }
}
