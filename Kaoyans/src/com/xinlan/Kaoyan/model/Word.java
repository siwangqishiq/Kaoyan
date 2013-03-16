package com.xinlan.Kaoyan.model;

public class Word
{
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getCh() {
		return ch;
	}
	public void setCh(String ch) {
		this.ch = ch;
	}
	public String getExtern() {
		return extern;
	}
	public void setExtern(String extern) {
		this.extern = extern;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	private Integer id;
	private String word;
	private String ch;
	private String extern;
	private Integer flag;
}//end class
