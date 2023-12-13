package com.atguigu.process.mapper;

import com.atguigu.model.process.OaProcess;
import com.atguigu.vo.process.ProcessQueryVo;
import com.atguigu.vo.process.ProcessVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface OaProcessMapper extends BaseMapper<OaProcess> {
    /**
     * 根据条件进行查询并分页
     *
     * @param pageObj Page对象
     * @param vo      内部封装了查询条件
     * @return IPage，即分页后的数据
     */
    IPage<ProcessVo> selectAndPage(Page<ProcessVo> pageObj, @Param("vo") ProcessQueryVo vo);
}
