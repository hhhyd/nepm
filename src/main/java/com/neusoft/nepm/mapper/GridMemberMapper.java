package com.neusoft.nepm.mapper;
import org.apache.ibatis.annotations.Param;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nepm.po.GridMember;

public interface GridMemberMapper extends BaseMapper<GridMember>{

	/************************ nepg工程 *********************/
	@Select("select * from grid_member where gm_code=#{gmCode} and password=#{password}")
	public GridMember getGridMemberByCodeByPass(@Param("gmCode") String gmCode, @Param("password") String password);


	/************************ nepg工程 *********************/
}
