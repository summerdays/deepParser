package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;

//@author Nan Wang

public class AttrBusinessRealProperty extends PDFAttribute {
	
	static Pattern twoMoneyRegex = Pattern.compile("(?:^|[\\D&&[^\\.]]+)([0-9]{1,3}(?:,[0-9]{3}){1,4}(?:\\.[0-9]{2})?)[\\s\\$\\D]+([0-9]{1,3}(?:,[0-9]{3}){1,4}(?:\\.[0-9]{2})?).*");
    static String noneRegex = "^N[oO][nN][eE].*";

	String businessRealPropertyValue = null;
	String businessRealPropertySecured = null;

	public AttrBusinessRealProperty() {
		super();
		// TODO Auto-generated constructor stub
		name = "Value of Real Property (Business) \t Secured Claim of Real Property (Business)";
		page = 2;
	}
	
	public void computeValue(String src){
		if(src.contains("usiness")){
			Matcher m = twoMoneyRegex.matcher(src);
			if(m.find()){
				if(businessRealPropertyValue == null){
					businessRealPropertyValue = m.group(1); 
					businessRealPropertySecured = m.group(2);
				}
				else{
					double v1 = Double.valueOf(businessRealPropertyValue
							.replaceAll(",", ""));
					double s1 = Double.valueOf(businessRealPropertySecured
							.replaceAll(",", ""));
					double v2 = Double.valueOf(m.group(1)
							.replaceAll(",", ""));
					double s2 = Double.valueOf(m.group(2)
							.replaceAll(",", ""));
					v1 = v1 + v2;
					s1 = s1 + s2;
					businessRealPropertyValue =  Double.toString(v1); 
					businessRealPropertySecured =  Double.toString(s1);
				}
			}
		}
		
		if(src.matches(noneRegex) || src.startsWith("0.00")){
			businessRealPropertyValue = "0.00";
			businessRealPropertySecured = "0.00";
		}		
	}
	
	public String toString(){
		if(businessRealPropertyValue!=null) { 
			this.value = businessRealPropertyValue;
		}
		else
			this.value = "N.A. ";
		if(businessRealPropertySecured!=null){
			this.value += "\t" + businessRealPropertySecured;
		}
		else
			this.value += "\t N.A.";
		return this.value;
	}

}
