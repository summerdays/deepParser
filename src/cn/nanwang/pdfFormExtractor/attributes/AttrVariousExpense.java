package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;

// @author Nan Wang

public class AttrVariousExpense extends PDFAttribute {

  Pattern dot00Money = Pattern
      .compile("(?:^|[\\$\\s]+)((?:(?:(?:[\\d]{1,3})(?:,[\\d]{3}){0,3})|[\\d]+)\\.[\\d]{2})");

  String rent;
  String alimony;
  String supportDependentNotliving;
  String regularExpense;


  public AttrVariousExpense() {
    super();
    // TODO Auto-generated constructor stub
    name = "Rent \t Alimony \t Support of Dependents \t Regular Expense";
    page = 9;
    isDebug = false;
  }

  public void computeValue(String src) {
    int maxExpense = 40;
    double[] e = new double[maxExpense];

    int iExpense = 0;
    double sum = 0;
    if (isDebug) {
      System.out.println(src);
    }
    int beginIndex = src.indexOf("Complete a separate schedule of expenditures");
    int endIndex = src.indexOf("FOR CHAPTER 12");
    if (beginIndex >= 0 && endIndex >= 0) {
      src = src.substring(beginIndex, endIndex);
    } else if (beginIndex >= 0) {
      src = src.substring(beginIndex);
    }

    String[] lines = src.split("[\\r\\n]+");
    for (int l = 1; l <= lines.length - 2; l++) {
      Matcher m = dot00Money.matcher(lines[l]);
      if (m.find()) {
        e[iExpense] = Double.valueOf(m.group(1).replaceAll(",", ""));
        if (isDebug) {
          System.out.printf("%d. %g %g %s\n", iExpense, e[iExpense], sum, lines[l]);
        }
        if (this.alimony == null && lines[l].indexOf("Alimony") >= 0)
          this.alimony = String.valueOf(e[iExpense]);
        if (this.rent == null && lines[l].indexOf("Rent") >= 0)
          this.rent = String.valueOf(e[iExpense]);
        if (this.supportDependentNotliving == null
            && lines[l].indexOf("support of additional dependents") >= 0)
          this.supportDependentNotliving = String.valueOf(e[iExpense]);
        if (this.regularExpense == null && lines[l].indexOf("Regular expenses") >= 0)
          this.regularExpense = String.valueOf(e[iExpense]);


        if (sum == 0 || sum != e[iExpense]) {
          sum += e[iExpense];
        }
        iExpense++;

        if (iExpense >= maxExpense)
          break;
      }
    }



    if (isDebug) {
      System.out.printf("Total lines = %d\n", iExpense);
    }
    if (sum != 0 && sum == e[iExpense - 1]) {
      if (this.rent == null)
        this.rent = String.valueOf(e[0]);;
      if (iExpense >= 27) {
        if (iExpense == 27)
          ++iExpense;
        if (this.alimony == null)
          this.alimony = String.valueOf(e[iExpense - 6]);
        if (this.supportDependentNotliving == null)
          this.supportDependentNotliving = String.valueOf(e[iExpense - 5]);
        if (this.regularExpense == null)
          this.regularExpense = String.valueOf(e[iExpense - 4]);
      }
    }
  }

  public String toString() {
    if (rent == null)
      this.value = "N.A.";
    else
      this.value = rent;

    if (alimony == null)
      this.value += "\t N.A.";
    else
      this.value += "\t " + alimony;

    if (supportDependentNotliving == null)
      this.value += "\t N.A.";
    else
      this.value += "\t " + supportDependentNotliving;

    if (regularExpense == null)
      this.value += "\t N.A.";
    else
      this.value += "\t " + regularExpense;

    return this.value;
  }
}
