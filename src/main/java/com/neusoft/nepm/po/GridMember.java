package com.neusoft.nepm.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("grid_member")
public class GridMember {

	@TableId(value="gm_id",type=IdType.AUTO)
    private Integer gmId;
    private String gmName;
    private String gmCode;
    private String password;
    private Integer provinceId;
    private Integer cityId;
    private String tel;
    private Integer state;
    private String remarks;
    
	public Integer getGmId() {
		return gmId;
	}
	public void setGmId(Integer gmId) {
		this.gmId = gmId;
	}
	public String getGmName() {
		return gmName;
	}
	public void setGmName(String gmName) {
		this.gmName = gmName;
	}
	public String getGmCode() {
		return gmCode;
	}
	public void setGmCode(String gmCode) {
		this.gmCode = gmCode;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
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
}
