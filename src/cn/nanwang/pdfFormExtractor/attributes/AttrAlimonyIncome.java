package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;

// @author Nan Wang

public class AttrAlimonyIncome extends PDFAttribute {
  static Pattern moneyRegex =
      Pattern
          .compile("(?:^|[\\D&&[^\\.]]+)((?:[0-9]{1,3}(?:,[0-9]{3}){1,4}|[0-9]{3,4})(?:\\.[0-9]{2})?).*");
  static Pattern caseRegex = Pattern.compile(".*\\s+([0-9]{2}-[0-9]{4,5}-[a-z]{2,4})\\s+.*");
  int endline = -1;

  public AttrAlimonyIncome() {
    super();
    // TODO Auto-generated constructor stub
    name = "Alimony Monthly Income";
    page = 8;
  }

  public void computeValue(String src) {
    src = src.toUpperCase();

    if (endline >= 0)
      endline++;

    Matcher m3 = caseRegex.matcher(src);
    if (m3.find()) {
      endline = 0;
    }
    if (endline == 12 && this.value == null) {
      Matcher m2 = moneyRegex.matcher(src);
      if (m2.matches())
        this.value = m2.group(1);
    }

    if (src.indexOf("ALIMONY") >= 0) {
      Matcher m = moneyRegex.matcher(src);
      if (m.find()) {
        this.value = m.group(1);
      }
    }
  }
}
