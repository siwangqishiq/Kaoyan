package com.xinlan.Kaoyan.model;

public class Item
{
	private Integer id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getEnglish() {
		return english;
	}
	public void setEnglish(String english) {
		this.english = english;
	}
	public String getTranslate() {
		return translate;
	}
	public void setTranslate(String translate) {
		this.translate = translate;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public String getExtern() {
		return extern;
	}
	public void setExtern(String extern) {
		this.extern = extern;
	}
	private Integer number;
	private String english;
	private String translate;
	private Integer flag;
	private String extern;
}
 