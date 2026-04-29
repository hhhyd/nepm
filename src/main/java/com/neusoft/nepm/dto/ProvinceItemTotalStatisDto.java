package com.neusoft.nepm.dto;

//省分组分项统计
public class ProvinceItemTotalStatisDto {

	private Integer provinceId;
	private String provinceAbbr;
	private String provinceName;
	private Integer so2Total;
	private Integer totalCount;
	private Integer goodCount;
	private Integer coTotal;
	private Integer spmTotal;
	private Integer aqiTotal;
	
	public Integer getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}
	public String getProvinceAbbr() {
		return provinceAbbr;
	}
	public void setProvinceAbbr(String provinceAbbr) {
		this.provinceAbbr = provinceAbbr;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getGoodCount() {
		return goodCount;
	}
	public void setGoodCount(Integer goodCount) {
		this.goodCount = goodCount;
	}

	public Integer getSo2Total() {
		return so2Total;
	}
	public void setSo2Total(Integer so2Total) {
		this.so2Total = so2Total;
	}
	public Integer getCoTotal() {
		return coTotal;
	}
	public void setCoTotal(Integer coTotal) {
		this.coTotal = coTotal;
	}
	public Integer getSpmTotal() {
		return spmTotal;
	}
	public void setSpmTotal(Integer spmTotal) {
		this.spmTotal = spmTotal;
	}
	public Integer getAqiTotal() {
		return aqiTotal;
	}
	public void setAqiTotal(Integer aqiTotal) {
		this.aqiTotal = aqiTotal;
	}
}
