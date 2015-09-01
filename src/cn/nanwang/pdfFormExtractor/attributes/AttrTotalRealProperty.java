package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;

// @author Nan Wang

public class AttrTotalRealProperty extends PDFAttribute {

  static Pattern moneyRegex =
      Pattern
          .compile("(?:^|[\\D&&[^\\.]]+)((?:[0-9]{1,3}(?:,[0-9]{3}){1,4}|[0-9]{3,4})(?:\\.[0-9]{2})?).*");
  static String noneRegex = "^N[oO][nN][eE].*";
  String realPropertyValue = null;
  String realPropertySecured = null;


  public AttrTotalRealProperty() {
    super();
    // TODO Auto-generated constructor stub
    name = "Total Real Property";
    page = 2;
    isKey = true;
  }

  public void computeValue(String src) {
    if (src.matches(noneRegex) || src.startsWith("0.00")) {
      this.value = "0.00";
    }

    Matcher m2 = moneyRegex.matcher(src);

    if (m2.matches()) {
      // System.out.println(lines[l]);
      if (this.value == null)
        this.value = m2.group(1);
      else {
        double totalAmount = Double.valueOf(this.value.replaceAll(",", ""));
        double thisAmount = Double.valueOf(m2.group(1).replaceAll(",", ""));
        if (totalAmount < thisAmount)
          this.value = m2.group(1);
      }
    }
  }
}
