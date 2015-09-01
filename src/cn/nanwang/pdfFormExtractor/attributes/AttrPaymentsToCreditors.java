package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;

public class AttrPaymentsToCreditors extends PDFAttribute {
 	static Pattern dot00Money = Pattern.compile("(?:^|[\\$\\s]+)((?:(?:(?:[\\d]{1,3})(?:,[\\d]{3}){0,3})|[\\d]+)\\.[\\d]{2})");

	public AttrPaymentsToCreditors(){
		super();
		page = 14;
		name = "PaymentsToCreditors";
		value = "N.A.";
	}
	public void computeValue(String src){
		src = src.toLowerCase();

		double totalValue = 0.0;
		String startString = "list all payments on loans";
		String endString = "suits and administrative proceedings, executions, garnishments and attachments";
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
