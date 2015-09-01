package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;

//@author Nan Wang

public class AttrCashOnHand extends PDFAttribute {
	
	static Pattern cashRegex = Pattern.compile("[Cc]ash[\\s\\.,-/]+");
	static Pattern smallMoneyRegex = Pattern.compile("(?:^|[\\D&&[^\\.]]+)((?:[1-9][0-9]{0,2}(?:,[0-9]{3}){1,4}|[1-9][0-9]{1,6})(?:\\.[0-9]{2})?).*");

    static String noneRegex = "^N[oO][nN][eE].*";

	public AttrCashOnHand() {
		super();
		// TODO Auto-generated constructor stub
		name = "Cash On Hand";
		page = 3;
	}
	
	public void computeValue(String src){
		if(src.indexOf("Bankruptcy") >=0 || src.indexOf("Case") >=0)
			return;
		
		Matcher mCash = cashRegex.matcher(src);
		
		if(mCash.find()){
			//System.out.println(lines[l]);
			Matcher m = smallMoneyRegex.matcher(src);
			if(m.find()){
				this.value=m.group(1);
			}else{
				if(this.value == null)
					this.value = "0.00";
			}
		}
	}	
}
