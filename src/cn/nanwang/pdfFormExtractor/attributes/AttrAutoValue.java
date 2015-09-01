package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;

//@author Nan Wang

public class AttrAutoValue extends PDFAttribute {
	
	Pattern moneyRegex = Pattern.compile("(?:^|[\\$\\s]+)((?:(?:(?:[\\d]{1,3})(?:,[\\d]{3}){0,3})|[\\d]+)\\.[\\d]{2})");
	Pattern yearRegex = Pattern.compile("(?:^|[\\D&&[^\\$]]+)(200[0-9]|199[1-9]|(?:[0-9]?[0-9][-/][0-9]?[0-9][-/](?:0[0-9])?)).*");
	
	public AttrAutoValue() {
		super();
		// TODO Auto-generated constructor stub
		name = "Automobiles Value";
		page = 4;
	}
	
	public void computeValue(String src){
		Matcher mYear = yearRegex.matcher(src);

		if (mYear.find()) {
			//System.out.println(src);
			int tmp = 0;
			if( (tmp = src.indexOf("miles") ) >= 0){
				src = src.substring(tmp);	
			}
			//A single line might contain multiple automobiles, 
			tmp = 0;	
			do { 
				src = src.replaceAll(mYear.group(1), "");
				mYear.reset();
				mYear = yearRegex.matcher(src);
				if (tmp++ > 100)
					break;
			} while (mYear.find());
			
			Matcher m = moneyRegex.matcher(src);
			
			if (m.find()) {

				if (this.value == null)
					this.value = m.group(1);
				else {
					 
					this.value =  addTwoString(this.value, m.group(1));
				}
			}
			//System.out.println(this.value);
		}
	}
	
}
