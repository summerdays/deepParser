package cn.nanwang.pdfFormExtractor.attributes;

import cn.nanwang.pdfFormExtractor.PDFAttribute;

// @author Nan Wang

public class AttrEmployedDuration extends PDFAttribute {

  public AttrEmployedDuration() {
    super();
    // TODO Auto-generated constructor stub
    name = "Employment Duration";
    page = 8;
  }

  public void computeValue(String src) {
    src = src.toLowerCase();
    if (src.indexOf("how long employed") >= 0) {
      this.value = src.replaceAll("how long employed", "");
      if (this.value.length() < 2)
        this.value = null;
    }
  }
}
