package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;
 
public class AttrRealProperty extends PDFAttribute {

	static Pattern twoMoneyRegex = Pattern.compile("(?:^|[\\D&&[^\\.]]+)([0-9]{1,3}(?:,[0-9]{3}){1,4}(?:\\.[0-9]{2})?)[\\s\\$\\D]+([0-9]{1,3}(?:,[0-9]{3}){1,4}(?:\\.[0-9]{2})?).*");
	static String noneRegex = "^N[oO][nN][eE].*";
	String realPropertyValue = null;
	String realPropertySecured = null;


	public AttrRealProperty() {
		super();
		// TODO Auto-generated constructor stub
		name = "Value of Real Property \t Mortgage";
		page = 2;
 	}

	public void computeValue(String src){
		Matcher m = twoMoneyRegex.matcher(src);
		if(m.find()  ){
			if(realPropertyValue == null){
				realPropertyValue = m.group(1); 
				realPropertySecured = m.group(2);
			}
			else{
				realPropertyValue = addTwoString(realPropertyValue, m.group(1).replaceAll(",", ""));
				realPropertySecured = addTwoString(realPropertySecured, m.group(2).replaceAll(",", ""));
			}
 //			System.out.println("Real Property " + realPropertyValue + " , " +realPropertySecured);
		}

		if(src.matches(noneRegex) || src.startsWith("0.00")){
			realPropertyValue = "0.00";
			realPropertySecured = "0.00";
		}
	}
	 

	public String toString(){
		if(realPropertyValue!=null) { 
			this.value = realPropertyValue;
		}
		else
			this.value = "N.A. ";
		if(realPropertySecured!=null){
			this.value += "\t" + realPropertySecured;
		}
		else
			this.value += "\t N.A.";
		return this.value;
	}
}
