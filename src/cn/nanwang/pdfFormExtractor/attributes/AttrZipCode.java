package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;

//@author Nan Wang

public class AttrZipCode extends PDFAttribute {
	
	static Pattern zipRegex =  Pattern.compile("3[0-9]{4}([-\\s][0-9]{4})?$");

	public AttrZipCode() {
		super();
		// TODO Auto-generated constructor stub
		name = "Zip Code";
		page = 0;
	}
	
	public void computeValue(String src){
		if(this.value == null){
			Matcher m = zipRegex.matcher(src);
			if(m.find()){
				this.value = m.group(0);
				this.value = this.value.replaceAll("\\s", "-");
			}
		}
	}
}
