package com.neusoft.nepm.dto;

import java.util.List;

/**
 * 共通分页查询返回对象
 */
public class PageResponseDto<T> {

	private Integer totalRow;       //总行数
	private Integer totalPageNum;   //总页数
	private Integer preNum;         //上一页
	private Integer nextNum;        //下一页
	private Integer pageNum;        //当前页
	private Integer maxPageNum;     //每页显示最大行数
	private Integer beginNum;       //开始查询记录数
	private List<T> list;           //业务数据
	
	public Integer getTotalRow() {
		return totalRow;
	}
	public void setTotalRow(Integer totalRow) {
		this.totalRow = totalRow;
	}
	public Integer getTotalPageNum() {
		return totalPageNum;
	}
	public void setTotalPageNum(Integer totalPageNum) {
		this.totalPageNum = totalPageNum;
	}
	public Integer getPreNum() {
		return preNum;
	}
	public void setPreNum(Integer preNum) {
		this.preNum = preNum;
	}
	public Integer getNextNum() {
		return nextNum;
	}
	public void setNextNum(Integer nextNum) {
		this.nextNum = nextNum;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getMaxPageNum() {
		return maxPageNum;
	}
	public void setMaxPageNum(Integer maxPageNum) {
		this.maxPageNum = maxPageNum;
	}
	public Integer getBeginNum() {
		return beginNum;
	}
	public void setBeginNum(Integer beginNum) {
		this.beginNum = beginNum;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
}
