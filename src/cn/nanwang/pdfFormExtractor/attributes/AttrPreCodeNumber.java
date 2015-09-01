package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;


public class AttrPreCodeNumber extends PDFAttribute {
	static Pattern priorCaseRegex = Pattern.compile("([0-9]{2}-[0-9]{4,5}-[A-Za-z]{2,4})\\s+.*");

	public AttrPreCodeNumber() {
		super();
		// TODO Auto-generated constructor stub
		name = "Previous Code Number";
		page = 10;
	}
	
	public void computeValue(String src, String caseCode){
		if(this.value == null){
			Matcher m = priorCaseRegex.matcher(src);
			if (m.find()){
				this.value = m.group(1);
				if(caseCode != null & this.value.equals(caseCode))
					this.value = null;
			}
		}
	}

}
