package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;

//@author Nan Wang

public class AttrCodeNumber extends PDFAttribute {
	static Pattern caseRegex = Pattern.compile(".*\\s+([0-9]{2}-[0-9]{4,5}-[a-z]{2,4})\\s+.*");

	public AttrCodeNumber() {
		super();
		// TODO Auto-generated constructor stub
		name = "Case Code";
		page = 0;
		isKey = true;
	}
	//find the case number after Case
	public void computeValue(String src){
		if(this.value == null){
			Matcher m = caseRegex.matcher(src);
			if(m.find()){
				this.value = m.group(1);
			}
		}
	}
}
