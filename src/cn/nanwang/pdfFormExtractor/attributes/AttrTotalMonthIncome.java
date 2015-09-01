package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;



public class AttrTotalMonthIncome extends PDFAttribute {
	static Pattern moneyRegex = Pattern.compile("(?:^|[\\D&&[^\\.]]+)((?:[0-9]{1,3}(?:,[0-9]{3}){1,4}|[0-9]{3,4})(?:\\.[0-9]{2})?).*");
	static Pattern caseRegex = Pattern.compile(".*\\s+([0-9]{2}-[0-9]{4,5}-[a-z]{2,4})\\s+.*");

	int endline = -1;
	public AttrTotalMonthIncome() {
		super();
		// TODO Auto-generated constructor stub
		name = "Total Monthly Income";
		page = 8;
		isKey = true;
		isDebug = false;
	}
	
	public void computeValue(String src){
		src = src.toLowerCase();			
		if(isDebug)
			System.out.println(src);
		
		if(endline >= 0)
			endline++;		
		//Matcher m3 = caseRegex.matcher(src);
		if(src.indexOf("monthly income") >= 0){
			endline = 0;
		}
		if(endline>=0 && endline <= 2 && this.value == null){
			Matcher m2 = moneyRegex.matcher(src);
			if(m2.matches())
				this.value = m2.group(1);
		}
		
		if(src.indexOf("monthly income") >= 0 ){
			Matcher m = moneyRegex.matcher(src);
			if(m.find()){
				this.value = m.group(1);
			}else if(src.contains(" 0.00") || src.contains("$0.00")){
				this.value = "0";
			}
		}
	}
}
