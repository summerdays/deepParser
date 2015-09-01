package cn.nanwang.pdfFormExtractor.attributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;


public class AttrDebtorName extends PDFAttribute{
	
	static Pattern nameRegex = Pattern.compile("^([A-Z][A-Za-z]+\\s?[A-Za-z]+\\s?,(?:\\s+[A-Z][A-Za-z][A-Za-z]+)(?:\\s+[A-Z][A-Za-z\\.]*)?)[\\s\\.,A-Za-z]*$");

	public AttrDebtorName() {
		super();
		// TODO Auto-generated constructor stub
		name = "Debtor Name";
		page = 0;
	}
	
	public void computeValue(String src){
		if(this.value == null){
			Matcher m = nameRegex.matcher(src);
			if(m.find()){
				this.value = m.group(1);
				//this.value = this.value.replaceAll("\\s", "");
			}
		}
	}

	
}
