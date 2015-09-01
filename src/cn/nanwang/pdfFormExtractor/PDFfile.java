package cn.nanwang.pdfFormExtractor;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import cn.nanwang.pdfFormExtractor.attributes.*;

// @author Nan Wang

public class PDFfile {
  // regular expression
  /*
   * String noneRegex = "^N[oO][nN][eE].*"; Pattern dateRegex =
   * Pattern.compile("Filed\\s+([0-9][0-9]/[0-9][0-9]/[0-9]{2,4})"); Pattern nameRegex =
   * Pattern.compile(
   * "^([A-Z][A-Za-z]+\\s?[A-Za-z]+\\s?,(?:\\s+[A-Z][A-Za-z][A-Za-z]+)(?:\\s+[A-Z][A-Za-z\\.]*)?)[\\s\\.,A-Za-z]*$"
   * ); Pattern caseRegex = Pattern.compile(".*\\s+([0-9]{2}-[0-9]{4,5}-[a-z]{2,4})\\s+.*"); Pattern
   * priorCaseRegex = Pattern.compile("([0-9]{2}-[0-9]{4,5}-[A-Za-z]{2,4})\\s+.*"); Pattern
   * yearRegex = Pattern.compile(
   * "(?:^|[\\D&&[^\\$]]+)(200[0-9]|199[1-9]|(?:[0-9]?[0-9][-/][0-9]?[0-9][-/](?:0[0-9])?)).*");
   * Pattern zipRegex = Pattern.compile("3[0-9]{4}([-\\s][0-9]{4})?$"); Pattern countyRegex =
   * Pattern.compile("([A-Za-z]+),\\s+G[Aa]"); Pattern money2Regex =
   * Pattern.compile("(?:^[\\$\\s]+)((?:[0-9]{1,6}(?:,[0-9]{3}){0,4})(?:\\.[0-9]{2})?).*"); Pattern
   * moneyRegex = Pattern.compile(
   * "(?:^|[\\D&&[^\\.]]+)((?:[0-9]{1,3}(?:,[0-9]{3}){1,4}|[0-9]{3,4})(?:\\.[0-9]{2})?).*"); Pattern
   * smallMoneyRegex = Pattern.compile(
   * "(?:^|[\\D&&[^\\.]]+)((?:[1-9][0-9]{0,2}(?:,[0-9]{3}){1,4}|[1-9][0-9]{1,6})(?:\\.[0-9]{2})?).*"
   * ); Pattern pureMoney =
   * Pattern.compile("(?:^|[\\$\\s]+)([0-9]{1,3}(?:,[0-9]{3}){0,3}(?:\\.[\\d]{2})?)$"); Pattern
   * twoMoneyRegex = Pattern.compile(
   * "(?:^|[\\D&&[^\\.]]+)([0-9]{1,3}(?:,[0-9]{3}){1,4}(?:\\.[0-9]{2})?)[\\s\\$\\D]+([0-9]{1,3}(?:,[0-9]{3}){1,4}(?:\\.[0-9]{2})?).*"
   * ); Pattern cashRegex = Pattern.compile("[Cc]ash[\\s\\.,-/]+"); Pattern bankRegex =
   * Pattern.compile("([Cc]hecking|[Ss]aving|[Aa]ccount|[Aa]cct|[Bb]ank|[Ss]avings)[\\s\\.,-/]+");
   * Pattern dot00Money =
   * Pattern.compile("(?:^|[\\$\\s]+)((?:(?:(?:[\\d]{1,3})(?:,[\\d]{3}){0,3})|[\\d]+)\\.[\\d]{2})");
   * Pattern withDollarMoney = Pattern.compile("\\$([\\d]+)"); Pattern dot00Money2 =
   * Pattern.compile(
   * "(?:^|[\\$\\s]+)((?:(?:(?:[\\d]{1,3})(?:,[\\d]{3}){0,3})|[\\d]+)\\.[\\d]{2}).*");
   */

  File file;
  private String summaryPage;
  PDFAttribute[] attrs = {new AttrName(), new AttrFiledDate(), new AttrDebtorName(),
      new AttrZipCode(),
      new AttrCounty(),
      new AttrCodeNumber(),
      new AttrPreCodeNumber(),
      new AttrYearlyIncome(),
      // new AttrBusinessRealProperty(),
      new AttrRealProperty(), new AttrTotalRealProperty(),

      new AttrCashOnHand(), new AttrBankAmount(), new AttrAutoValue(), new AttrTotalPersonal(),
      new AttrTotalMortgage(),

      new AttrVariousClaim(), new AttrTotalClaim(), new AttrMarital(), new AttrOccupation(),
      new AttrEmployedDuration(),

      new AttrAlimonyIncome(), new AttrPayrollDeuc(), new AttrSubTotalMonthIncome(),
      new AttrTotalMonthIncome(), new AttrVariousExpense(),

      new AttrAveExpense(), new AttrIncomeOthers(), new AttrPaymentsToCreditors(),
      new AttrRepossessions(), new AttrDebtCounseling()};

  HashMap<String, Integer> findAttr;

  public String[] attributeNames;

  static String[] pageIdentifier = {
      "Street Address of Debtor", // 0
      "employment or operation of business", "all real property",
      "Cash on hand",
      "Automobiles, trucks, trailers",
      "Other personal property", // 5
      "SCHEDULE D", "SCHEDULE F", "SCHEDULE I",
      "Laundry",
      "Prior Bankruptcy Case Filed Within", // 10
      "SUMMARY OF SCHEDULES", "List all payments on loans",
      "Income other than from employment or operation of business", "List all payments on loans",
      "Repossessions, foreclosures and returns", // 15
      "Payments related to debt counseling or bankruptcy"};


  public PDFfile(File file) {
    attributeNames = new String[attrs.length];
    findAttr = new HashMap<String, Integer>(attrs.length);

    for (int i = 0; i < attrs.length; i++) {
      attributeNames[i] = attrs[i].name;
      String className = attrs[i].getClass().getName();
      if (className.lastIndexOf('.') > 0) {
        className = className.substring(className.lastIndexOf('.') + 1);
      }
      findAttr.put(className, i);
    }
    attrs[0].assignValue(file.getName());
    this.file = file;
  }

  public void run() {
    try {
      boolean force = true;
      PDDocument reader = PDDocument.load(file);

      if (reader != null) {
        int numPages = reader.getNumberOfPages();
        for (int page = 1; page <= numPages; page++) {// page indexed in pdf starts from 1
          PDFTextStripper stripper = new PDFTextStripper();
          stripper.setSortByPosition(force);
          stripper.setForceParsing(force);
          stripper.setStartPage(page);
          stripper.setEndPage(page);
          String src = stripper.getText(reader);
          src = src.replaceAll("  |\\xA0", " ");

          for (int i = 0; i < pageIdentifier.length; i++) {
            if (src.indexOf(pageIdentifier[i]) >= 0
                || src.indexOf(pageIdentifier[i].toUpperCase()) >= 0) {
              getPDFAttr(src, i);
              continue;
            }
          }

        }
        checkSummary(summaryPage);
        reader.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Could not read file " + file.getName() + ".");
    }

  }

  void computeAttrByLines(String src, int p) {
    String[] lines = src.split("[\\r\\n]+");
    for (int l = 0; l < lines.length; l++) {
      if (p > 0 && l == lines.length - 1)
        continue;
      for (int i = 0; i < attrs.length; i++) {
        if (attrs[i].page == p) {
          attrs[i].computeValue(lines[l]);
        }
      }
    }
  }

  void getPDFAttr(String src, int p) {
    src = src.replaceAll("_", "");
    String[] lines;
    switch (p) {
      case 6: // For Schedule D and F
        if (src.indexOf("Summary of Schedules") < 0)
          return;
        computeAttrByLines(src, p);
        break;
      case 7:
        attrs[findAttr.get("AttrVariousClaim")].computeValue(src);
        lines = src.split("[\\r\\n]+");
        for (int l = 0; l < lines.length; l++) {
          attrs[findAttr.get("AttrTotalClaim")].computeValue(lines[l]);
        }
        break;
      case 9:
        attrs[findAttr.get("AttrVariousExpense")].computeValue(src);
        attrs[findAttr.get("AttrAveExpense")].computeValue(src);
        break;
      case 10:
        lines = src.split("[\\r\\n]+");
        for (int l = 0; l < lines.length; l++) {
          attrs[findAttr.get("AttrPreCodeNumber")].computeValue(lines[l],
              attrs[findAttr.get("AttrCodeNumber")].value); // compute Previous Code Number
        }
        break;
      case 11:
        summaryPage = src;
        break;
      case 13:
        attrs[findAttr.get("AttrIncomeOthers")].computeValue(src);
        break;
      case 14:
        attrs[findAttr.get("AttrPaymentsToCreditors")].computeValue(src);
        break;
      case 15:
        attrs[findAttr.get("AttrRepossessions")].computeValue(src);
        break;
      case 16:
        attrs[findAttr.get("AttrDebtCounseling")].computeValue(src);
        break;
      default:
        computeAttrByLines(src, p);
    }

  }

  void checkSummary(String src) {
    AttrSummary summary = new AttrSummary();
    summary.computeValue(src);
    int numMatches = 0;
    int[] matches = {-1, -1, -1, -1, -1, -1};
    for (int i = 0; i < summary.totals.length; i++) {
      double d = summary.totals[i];
      if (matches[5] < 0
          && attrs[findAttr.get("AttrTotalRealProperty")].value != null
          && d == Double.valueOf(attrs[findAttr.get("AttrTotalRealProperty")].value.replaceAll(",",
              ""))) {
        matches[5] = i;
        numMatches++;
        continue;
      }

      if (matches[4] < 0
          && attrs[findAttr.get("AttrTotalPersonal")].value != null
          && d == Double
              .valueOf(attrs[findAttr.get("AttrTotalPersonal")].value.replaceAll(",", ""))) {
        numMatches++;
        matches[4] = i;
        continue;
      }

      if (matches[3] < 0
          && attrs[findAttr.get("AttrTotalMortgage")].value != null
          && d == Double
              .valueOf(attrs[findAttr.get("AttrTotalMortgage")].value.replaceAll(",", ""))) {
        numMatches++;
        matches[3] = i;
        continue;
      }

      if (matches[2] < 0 && attrs[findAttr.get("AttrTotalClaim")].value != null
          && d == Double.valueOf(attrs[findAttr.get("AttrTotalClaim")].value.replaceAll(",", ""))) {
        numMatches++;
        matches[2] = i;
        continue;
      }


      if (matches[1] < 0
          && attrs[findAttr.get("AttrTotalMonthIncome")].value != null
          && d == Double.valueOf(attrs[findAttr.get("AttrTotalMonthIncome")].value.replaceAll(",",
              ""))) {
        numMatches++;
        matches[1] = i;
        continue;
      }

      if (matches[0] < 0 && attrs[findAttr.get("AttrAveExpense")].value != null
          && d == Double.valueOf(attrs[findAttr.get("AttrAveExpense")].value.replaceAll(",", ""))) {
        numMatches++;
        matches[0] = i;
        continue;
      }


      // System.out.println(i + " " + summary.totals[i]);
    }


    if (numMatches >= 3) {
      if (matches[5] == 8) {
        if (matches[0] == -1)
          matches[0] = 2;
        if (matches[1] == -1)
          matches[1] = 3;
        if (matches[2] == -1)
          matches[2] = 4;
        if (matches[3] == -1)
          matches[3] = 6;
        if (matches[4] == -1)
          matches[4] = 7;
      }


      if (matches[5] == 0) {
        if (matches[0] == -1)
          matches[0] = 6;
        if (matches[1] == -1)
          matches[1] = 5;
        if (matches[2] == -1)
          matches[2] = 4;
        if (matches[3] == -1)
          matches[3] = 2;
        if (matches[4] == -1)
          matches[4] = 1;
      }

    }

    if (numMatches >= 3) {
      if (attrs[findAttr.get("AttrAveExpense")].value != null
          && summary.totals[matches[0]] != Double
              .valueOf(attrs[findAttr.get("AttrAveExpense")].value.replaceAll(",", "")))
        attrs[findAttr.get("AttrAveExpense")].value = String.valueOf(summary.totals[matches[0]]);

      if (attrs[findAttr.get("AttrTotalMonthIncome")].value != null
          && summary.totals[matches[1]] != Double.valueOf(attrs[findAttr
              .get("AttrTotalMonthIncome")].value.replaceAll(",", "")))
        attrs[findAttr.get("AttrTotalMonthIncome")].value =
            String.valueOf(summary.totals[matches[1]]);

      if (attrs[findAttr.get("AttrTotalClaim")].value != null
          && summary.totals[matches[2]] != Double
              .valueOf(attrs[findAttr.get("AttrTotalClaim")].value.replaceAll(",", "")))
        attrs[findAttr.get("AttrTotalClaim")].value = String.valueOf(summary.totals[matches[2]]);

      if (attrs[findAttr.get("AttrTotalMortgage")].value != null
          && summary.totals[matches[2]] != Double
              .valueOf(attrs[findAttr.get("AttrTotalMortgage")].value.replaceAll(",", "")))
        attrs[findAttr.get("AttrTotalMortgage")].value =
            String.valueOf((summary.totals[matches[3]]));

      if (attrs[findAttr.get("AttrTotalPersonal")].value != null
          && summary.totals[matches[4]] != Double
              .valueOf(attrs[findAttr.get("AttrTotalPersonal")].value.replaceAll(",", "")))
        attrs[findAttr.get("AttrTotalPersonal")].value = String.valueOf(summary.totals[matches[4]]);

      if (attrs[findAttr.get("AttrTotalRealProperty")].value != null
          && summary.totals[matches[5]] != Double.valueOf(attrs[findAttr
              .get("AttrTotalRealProperty")].value.replaceAll(",", "")))
        attrs[findAttr.get("AttrTotalRealProperty")].value =
            String.valueOf(summary.totals[matches[5]]);


    }
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (PDFAttribute a : attrs) {
      sb.append(a + "\t");
    }
    return sb.toString();
  }


  public boolean isValid() {
    boolean validBit = true;

    for (PDFAttribute a : attrs) {
      if (a.isValid())
        continue;
      else {
        validBit = false;
      }
    }
    return validBit;
  }

  public boolean isValid(PrintStream output) {
    boolean validBit = true;

    for (PDFAttribute a : attrs) {
      if (a.isValid())
        continue;
      else {
        validBit = false;
      }
    }

    if (validBit == false) {
      StringBuilder sb = new StringBuilder();
      sb.append(attrs[0].value + " failed to find attributes in:( ");
      for (PDFAttribute a : attrs) {
        if (!a.isValid())
          sb.append(a.name + " ");
      }
      sb.append(").");
      output.println(sb.toString());
    } else {
      output.println(attrs[0].value + " is valid.");
    }

    return validBit;
  }



}
