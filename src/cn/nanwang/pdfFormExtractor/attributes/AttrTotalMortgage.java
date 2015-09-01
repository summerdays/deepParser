package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;


public class AttrTotalMortgage extends PDFAttribute {
	
	static Pattern moneyRegex = Pattern.compile("(?:^|[\\D&&[^\\.]]+)((?:[0-9]{1,3}(?:,[0-9]{3}){1,4}|[0-9]{3,4})(?:\\.[0-9]{2})?).*");
	static Pattern dot00Money2 = Pattern.compile("(?:^|[\\$\\s]+)((?:(?:(?:[\\d]{1,3})(?:,[\\d]{3}){0,3})|[\\d]+)\\.[\\d]{2}).*");
	
	public AttrTotalMortgage() {
		super();
		// TODO Auto-generated constructor stub
		name = "Total Secured Claim";
		page = 6;
		isKey = true;
	}
	
	public void computeValue(String src){
		//System.out.println(src);
		if(src.toLowerCase().contains("Value"))
			return;
		if (src.matches("[\\s0-9,\\.\\$]+") || src.indexOf("Total") >= 0) {
			Matcher m = moneyRegex.matcher(src);
			if (m.matches()) {
				if (this.value == null)
					this.value = m.group(1);
				else {
					double totalAmount = Double.valueOf(this.value
							.replaceAll(",", ""));
					double thisAmount = Double.valueOf(m.group(1)
							.replaceAll(",", ""));
					if (totalAmount < thisAmount)
						this.value = m.group(1);
				}
			}
		}
		
		Matcher m2 = dot00Money2.matcher(src);
		if (m2.find()) {
			if (this.value == null)
				this.value = m2.group(1);
			else {
				double totalAmount = Double.valueOf(this.value
						.replaceAll(",", ""));
				double thisAmount = Double.valueOf(m2.group(1)
						.replaceAll(",", ""));
				if (totalAmount < thisAmount)
					this.value = m2.group(1);
			}
		}
	}
	
}
