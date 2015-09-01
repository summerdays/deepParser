package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;

// @author Nan Wang

public class AttrBankAmount extends PDFAttribute {

  static Pattern bankRegex = Pattern
      .compile("([Cc]hecking|[Ss]aving|[Aa]ccount|[Aa]cct|[Bb]ank|[Ss]avings)[\\s\\.,-/]+");
  static Pattern smallMoneyRegex =
      Pattern
          .compile("(?:^|[\\D&&[^\\.]]+)((?:[1-9][0-9]{0,2}(?:,[0-9]{3}){1,4}|[1-9][0-9]{1,6})(?:\\.[0-9]{2})?).*");

  static String noneRegex = "^N[oO][nN][eE].*";

  public AttrBankAmount() {
    super();
    // TODO Auto-generated constructor stub
    name = "Bank Amount";
    page = 3;
  }

  public void computeValue(String src) {
    if (src.indexOf("Bankruptcy") >= 0 || src.indexOf("Case") >= 0)
      return;

    Matcher mBank = bankRegex.matcher(src);

    if (mBank.find()) {
      // System.out.println(lines[l]);
      Matcher m = smallMoneyRegex.matcher(src);
      if (m.find()) {
        if (this.value == null || this.value == "0.00")
          this.value = m.group(1);
        else {
          double amount = Double.valueOf(this.value.replaceAll(",", ""));
          amount += Double.valueOf(m.group(1).replaceAll(",", ""));
          this.value = String.valueOf(amount);
        }
      } else {
        if (this.value == null)
          this.value = "0.00";
      }
    }

  }
}
