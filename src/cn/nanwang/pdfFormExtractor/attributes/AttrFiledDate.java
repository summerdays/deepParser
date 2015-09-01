package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;


public class AttrFiledDate extends PDFAttribute {
	static Pattern dateRegex = Pattern.compile("Filed\\s+([0-9][0-9]/[0-9][0-9]/[0-9]{2,4})");

	public AttrFiledDate() {
		super();
		// TODO Auto-generated constructor stub
		name = "Filed Date";
		page = 0;
	}
	
	public void computeValue(String src){
		if(this.value == null){
			Matcher m = dateRegex.matcher(src);
			if(m.find()){
				this.value = m.group(1);
			}
		}
	}

}
