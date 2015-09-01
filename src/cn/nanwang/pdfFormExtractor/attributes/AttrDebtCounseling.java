package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;

//@author Nan Wang

public class AttrDebtCounseling extends PDFAttribute {
 	static Pattern dot00Money = Pattern.compile("(?:^|[\\$\\s]+)((?:(?:(?:[\\d]{1,3})(?:,[\\d]{3}){0,3})|[\\d]+)\\.[\\d]{2})");

	public AttrDebtCounseling(){
		super();
		page = 16;
		name = "DebtCounselTotal \t DebtConselTimes";
		value = "N.A. \t N.A.";
	}
	
	public void computeValue(String src){
		src = src.toLowerCase();
		double totalValue = 0.0;
		int times = 0;
		String startString = "list all payments made or property transferred by ";
		String endString = "other transfers";
		int startIdx = src.indexOf(startString);
		int endIdx = src.indexOf(endString);
		if(startIdx >=0){
			if( endIdx >= startIdx){
				src = src.substring(startIdx,endIdx);
			}else
				src = src.substring(startIdx);
		}
		String[] lines = src.split("[\\r\\n]+");
		for(String line : lines){
			Matcher m = dot00Money.matcher(line);
			if(m.find()){
				String moneyFound = m.group(1);
				totalValue += Double.valueOf(moneyFound.replaceAll(",", ""));  
				times++;
			}
		}
		if(times> 0)
			value = String.valueOf(totalValue) + " \t " + String.valueOf(times);
	}

}
