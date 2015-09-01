package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;

// @author Nan Wang

public class AttrSummary extends PDFAttribute {

  Pattern dot00Money = Pattern
      .compile("(?:^|[\\$\\s]+)((?:(?:(?:[\\d]{1,3})(?:,[\\d]{3}){0,3})|[\\d]+)\\.[\\d]{2})");
  static Pattern moneyRegex =
      Pattern
          .compile("(?:^|[\\D&&[^\\.]]+)((?:[0-9]{1,3}(?:,[0-9]{3}){1,4}|[0-9]{3,4})(?:\\.[0-9]{2})?).*");
  public double[] totals = new double[9];

  public AttrSummary() {
    super();
    // TODO Auto-generated constructor stub
    name = "Alimony Monthly Income";
    page = 11;
  }

  public void computeValue(String src) {
    int iter = 0;
    String[] lines = src.split("[\\r\\n]+");
    // System.out.println(src);
    for (int l = lines.length - 2; l >= 0 && iter < 9; l--) {
      Matcher m2 = dot00Money.matcher(lines[l]);
      if (!m2.find())
        continue;
      Matcher m = moneyRegex.matcher(lines[l]);
      if (m.find()) {
        totals[iter++] = Double.valueOf(m.group(1).replaceAll(",", ""));
      } else
        totals[iter++] = 0;
      // System.out.println(totals[iter-1]);
    }
  }
}
