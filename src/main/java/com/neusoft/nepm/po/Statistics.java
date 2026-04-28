package com.neusoft.nepm.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("statistics")
public class Statistics {
	
	@TableId(value="id",type=IdType.AUTO)
	private Integer id;
	private Integer provinceId;
	private Integer cityId;
	private String address;
	private Integer so2Value;
	private Integer so2Level;
	private Integer coValue;
	private Integer coLevel;
	private Integer spmValue;
	private Integer spmLevel;
	private Integer aqiId;
	private String confirmDate;
	private String confirmTime;
	private Integer gmId;
	private String fdId;
	private String information;
	private String remarks;
	
	//多对一
    @TableField(exist=false)
    private Supervisor supervisor;
    @TableField(exist=false)
    private GridProvince gridProvince;
    @TableField(exist=false)
    private GridCity gridCity;
    @TableField(exist=false)
    private Aqi aqi;
    @TableField(exist=false)
    private GridMember gridMember;
    
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public Integer getSo2Value() {
		return so2Value;
	}
	public void setSo2Value(Integer so2Value) {
		this.so2Value = so2Value;
	}
	public Integer getSo2Level() {
		return so2Level;
	}
	public void setSo2Level(Integer so2Level) {
		this.so2Level = so2Level;
	}
	public Integer getCoValue() {
		return coValue;
	}
	public void setCoValue(Integer coValue) {
		this.coValue = coValue;
	}
	public Integer getCoLevel() {
		return coLevel;
	}
	public void setCoLevel(Integer coLevel) {
		this.coLevel = coLevel;
	}
	public Integer getSpmValue() {
		return spmValue;
	}
	public void setSpmValue(Integer spmValue) {
		this.spmValue = spmValue;
	}
	public Integer getSpmLevel() {
		return spmLevel;
	}
	public void setSpmLevel(Integer spmLevel) {
		this.spmLevel = spmLevel;
	}
	public Integer getAqiId() {
		return aqiId;
	}
	public void setAqiId(Integer aqiId) {
		this.aqiId = aqiId;
	}
	public String getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}
	public String getConfirmTime() {
		return confirmTime;
	}
	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}
	public Integer getGmId() {
		return gmId;
	}
	public void setGmId(Integer gmId) {
		this.gmId = gmId;
	}
	public String getFdId() {
		return fdId;
	}
	public void setFdId(String fdId) {
		this.fdId = fdId;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
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
