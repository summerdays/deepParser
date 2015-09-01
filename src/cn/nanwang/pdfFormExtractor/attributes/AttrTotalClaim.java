package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;

// @author Nan Wang

public class AttrTotalClaim extends PDFAttribute {

  static Pattern dot00Money = Pattern
      .compile("(?:^|[\\$\\s]+)((?:(?:(?:[\\d]{1,3})(?:,[\\d]{3}){0,3})|[\\d]+)\\.[\\d]{2})");
  static Pattern money2Regex = Pattern
      .compile("(?:^[\\$\\s]+)((?:[0-9]{1,6}(?:,[0-9]{3}){0,4})(?:\\.[0-9]{2})?).*");
  static Pattern moneyRegex =
      Pattern
          .compile("(?:^|[\\D&&[^\\.]]+)((?:[0-9]{1,3}(?:,[0-9]{3}){1,4}|[0-9]{3,4})(?:\\.[0-9]{2})?).*");
  static Pattern dot00Money2 = Pattern
      .compile("(?:^|[\\$\\s]+)((?:(?:(?:[\\d]{1,3})(?:,[\\d]{3}){0,3})|[\\d]+)\\.[\\d]{2}).*");

  public AttrTotalClaim() {
    super();
    // TODO Auto-generated constructor stub
    name = "Total Claim";
    page = 7;
    isKey = true;
  }

  public void computeValue(String src) {
    if (src.matches("[\\s0-9,\\.\\$]+") || src.startsWith("Total")) {
      Matcher m = moneyRegex.matcher(src);
      if (m.matches()) {
        if (this.value == null) {
          this.value = m.group(1);
        } else {
          double totalAmount = Double.valueOf(this.value.replaceAll(",", ""));
          double thisAmount = Double.valueOf(m.group(1).replaceAll(",", ""));
          if (totalAmount < thisAmount)
            this.value = m.group(1);
        }
      }
    }

    Matcher m2 = dot00Money2.matcher(src);

    if (m2.find()) {
      // System.out.println(src);
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
