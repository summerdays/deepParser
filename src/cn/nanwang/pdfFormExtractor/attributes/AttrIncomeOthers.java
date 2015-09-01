package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;

public class AttrIncomeOthers extends PDFAttribute {
 	static Pattern dot00Money = Pattern.compile("(?:^|[\\$\\s]+)((?:(?:(?:[\\d]{1,3})(?:,[\\d]{3}){0,3})|[\\d]+)\\.[\\d]{2})");

	public AttrIncomeOthers(){
		super();
		name = "IncomeOthers";
		page = 13;
		value = "N.A.";
	}
	public void computeValue(String src){
		src = src.toLowerCase();
		double totalValue = 0.0;
		String startString = "state the amount of income received by the debtor other than from employment";
		String endString = "payments to creditors";
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
			}
		}
		if(totalValue!=0)
			value = String.valueOf(totalValue);
	}

}
