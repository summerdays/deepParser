package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;


public class AttrTotalPersonal extends PDFAttribute {
	
	Pattern dot00Money = Pattern.compile("(?:^|[\\$\\s]+)((?:(?:(?:[\\d]{1,3})(?:,[\\d]{3}){0,3})|[\\d]+)\\.[\\d]{2})");
	Pattern money2Regex = Pattern.compile("(?:^[\\$\\s]+)((?:[0-9]{1,6}(?:,[0-9]{3}){0,4})(?:\\.[0-9]{2})?).*");
	Pattern moneyRegex = Pattern.compile("(?:^|[\\D&&[^\\.]]+)((?:[0-9]{1,3}(?:,[0-9]{3}){1,4}|[0-9]{3,4})(?:\\.[0-9]{2})?).*");
	Pattern dot00Money2 = Pattern.compile("(?:^|[\\$\\s]+)((?:(?:(?:[\\d]{1,3})(?:,[\\d]{3}){0,3})|[\\d]+)\\.[\\d]{2}).*");

	public AttrTotalPersonal() {
		super();
		// TODO Auto-generated constructor stub
		name = "Total Personal Property";
		page = 5;
		isKey = true;
	}
	
	public void computeValue(String src){
		if (src.matches("[\\s0-9,\\.\\$]+") || src.startsWith("Total")) {
			Matcher m2 = money2Regex.matcher(src);
			Matcher m = moneyRegex.matcher(src);
			if (m.matches() | m2.matches()) {
				if (this.value == null){
					if(m2.matches())
						this.value = m2.group(1);
					else if(m.matches())
						this.value = m.group(1);
				}
				else {
					double totalAmount = Double.valueOf(this.value
							.replaceAll(",", ""));
					double thisAmount = 0;
					if(m2.matches())
						thisAmount = Double.valueOf(m2.group(1)
							.replaceAll(",", ""));
					else if(m.matches())
						thisAmount = Double.valueOf(m.group(1)
								.replaceAll(",", ""));
					if (totalAmount < thisAmount)
						this.value = m.group(1);
				}
			}
		}

		Matcher m2 = dot00Money2.matcher(src);
		if (m2.find()) {
			//System.out.println(src);
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
