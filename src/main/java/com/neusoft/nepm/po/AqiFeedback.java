package com.neusoft.nepm.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("aqi_feedback")
public class AqiFeedback {
	
	@TableId(value="af_id",type=IdType.AUTO)
	private Integer afId;
    private String telId;
    private Integer provinceId;
    private Integer cityId;
    private String address;
    private String information;
    private Integer estimatedGrade;
    private String afDate;
    private String afTime;
    private Integer gmId;
    private String assignDate;
    private String assignTime;
    private Integer state;
    private String remarks;
    
    //多对一
    @TableField(exist=false) //表示这部署数据库表中的字段，自动输出映射时忽略此字段
    private Supervisor supervisor;
    @TableField(exist=false)
    private GridProvince gridProvince;
    @TableField(exist=false)
    private GridCity gridCity;
    @TableField(exist=false)
    private Aqi aqi;
    @TableField(exist=false)
    private GridMember gridMember;
    
	public Integer getAfId() {
		return afId;
	}
	public void setAfId(Integer afId) {
		this.afId = afId;
	}
	public String getTelId() {
		return telId;
	}
	public void setTelId(String telId) {
		this.telId = telId;
	}
	public Integer getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}
	public Integer getEstimatedGrade() {
		return estimatedGrade;
	}
	public void setEstimatedGrade(Integer estimatedGrade) {
		this.estimatedGrade = estimatedGrade;
	}
	public String getAfDate() {
		return afDate;
	}
	public void setAfDate(String afDate) {
		this.afDate = afDate;
	}
	public String getAfTime() {
		return afTime;
	}
	public void setAfTime(String afTime) {
		this.afTime = afTime;
	}
	public Integer getGmId() {
		return gmId;
	}
	public void setGmId(Integer gmId) {
		this.gmId = gmId;
	}
	public String getAssignDate() {
		return assignDate;
	}
	public void setAssignDate(String assignDate) {
		this.assignDate = assignDate;
	}
	public String getAssignTime() {
		return assignTime;
	}
	public void setAssignTime(String assignTime) {
		this.assignTime = assignTime;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Supervisor getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(Supervisor supervisor) {
		this.supervisor = supervisor;
	}
	public GridProvince getGridProvince() {
		return gridProvince;
	}
	public void setGridProvince(GridProvince gridProvince) {
		this.gridProvince = gridProvince;
	}
	public GridCity getGridCity() {
		return gridCity;
	}
	public void setGridCity(GridCity gridCity) {
		this.gridCity = gridCity;
	}
	public Aqi getAqi() {
		return aqi;
	}
	public void setAqi(Aqi aqi) {
		this.aqi = aqi;
	}
	public GridMember getGridMember() {
		return gridMember;
	}
	public void setGridMember(GridMember gridMember) {
		this.gridMember = gridMember;
	}
}
