package cn.nanwang.pdfFormExtractor.attributes;

import cn.nanwang.pdfFormExtractor.PDFAttribute;

// @author Nan Wang

public class AttrMarital extends PDFAttribute {

  public AttrMarital() {
    super();
    // TODO Auto-generated constructor stub
    name = "Marital Status";
    page = 8;
    isDebug = false;
  }

  public void computeValue(String src) {
    src = src.toLowerCase();
    if (src.indexOf("a married debtor") >= 0 || src.indexOf("spouses are separated") >= 0)
      return;
    if (isDebug)
      System.out.println(src);
    if (this.value == null) {
      if (src.indexOf("married") >= 0)
        this.value = "Married";
      if (src.indexOf("single") >= 0)
        this.value = "Single";
      if (src.indexOf("divorced") >= 0 || src.indexOf("separated") >= 0)
        this.value = "Divorced";
    }
  }
}
