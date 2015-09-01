package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;

// @author Nan Wang

public class AttrCounty extends PDFAttribute {
  Pattern countyRegex = Pattern.compile("([A-Za-z]+),\\s+G[Aa]");

  public AttrCounty() {
    super();
    // TODO Auto-generated constructor stub
    name = "County";
    page = 0;
  }

  public void computeValue(String src) {
    if (this.value == null) {
      Matcher m = countyRegex.matcher(src);
      if (m.find()) {
        this.value = m.group(1);
      }
    }
  }
}
