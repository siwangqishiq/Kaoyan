package com.xinlan.Kaoyan.model;

/**
 * 仅含有一个意义的单词Model
 * @author Administrator
 *
 */
public class AtomWord
{
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
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
	private int number;
	private String word; 
	private String ch;
}//end class
