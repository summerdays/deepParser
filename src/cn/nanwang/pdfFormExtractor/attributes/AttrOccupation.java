package cn.nanwang.pdfFormExtractor.attributes;

import cn.nanwang.pdfFormExtractor.PDFAttribute;



public class AttrOccupation extends PDFAttribute {
 
	public AttrOccupation() {
		super();
		// TODO Auto-generated constructor stub
		name = "Occupation";
		page = 8;
	}
	
	public void computeValue(String src){
		src = src.toLowerCase();
		if(src.indexOf("occupation") >= 0){
			this.value = src.replaceAll("occupation", "");
			if(this.value.length() < 2)
				this.value = null;
		}
	}
}
