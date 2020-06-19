package com.zq.xijue.mapper;

import com.zq.xijue.entity.SysOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysOrderMapper {
    int insert(SysOrder record);

    int insertSelective(SysOrder record);

    SysOrder queryByOrderCode(String orderCode);

    void updatePaymentState(@Param("orderCode") String orderCode, @Param("traceCode") String traceCode);
}