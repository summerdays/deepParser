package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;

// @author Nan Wang

public class AttrVariousClaim extends PDFAttribute {

  static Pattern dot00Money = Pattern
      .compile("(?:^|[\\$\\s]+)((?:(?:(?:[\\d]{1,3})(?:,[\\d]{3}){0,3})|[\\d]+)\\.[\\d]{2})");
  static Pattern money2Regex = Pattern
      .compile("(?:^[\\$\\s]+)((?:[0-9]{1,6}(?:,[0-9]{3}){0,4})(?:\\.[0-9]{2})?).*");
  static Pattern moneyRegex =
      Pattern
          .compile("(?:^|[\\D&&[^\\.]]+)((?:[0-9]{1,3}(?:,[0-9]{3}){1,4}|[0-9]{3,4})(?:\\.[0-9]{2})?).*");
  static Pattern dot00Money2 = Pattern
      .compile("(?:^|[\\$\\s]+)((?:(?:(?:[\\d]{1,3})(?:,[\\d]{3}){0,3})|[\\d]+)\\.[\\d]{2}).*");

  String creditClaim;
  String medicalClaim;
  String insuranceClaim;
  String chargeClaim;
  String studentloan;
  String loanClaim;
  String accountClaim;

  public AttrVariousClaim() {
    super();
    // TODO Auto-generated constructor stub
    name =
        "Credit Card Claim \t Medical Claim \t Insurance Claim \t Charge CLaim \t Student Loan \t Other Loan \t Account Claim";
    page = 7;
    isDebug = false;
  }

  public void computeValue(String src) {
    src = src.toLowerCase();
    String[] parts = src.split("\\.[\\d]{2}\\s");

    for (int ipart = 0; ipart < parts.length; ipart++) {
      parts[ipart] = parts[ipart].replace("account no", "");
      parts[ipart] = parts[ipart].replace("account nu", "");
      parts[ipart] = parts[ipart].replace("account num", "");
      parts[ipart] = parts[ipart].replace("any account", "");
      if (isDebug) {
        System.out.println(ipart + " " + parts[ipart]);
      }
      String[] words = parts[ipart].split("[\\s]+");
      if (parts[ipart].indexOf("credit card") >= 0 || parts[ipart].indexOf("creditcard") >= 0
          || parts[ipart].indexOf("credit account") >= 0
          || parts[ipart].indexOf("line of credit") >= 0 || parts[ipart].indexOf("visa") >= 0
          || parts[ipart].indexOf("master") >= 0 || parts[ipart].indexOf("american express") >= 0
          || parts[ipart].indexOf("general merchandise") >= 0
          || parts[ipart].indexOf("charge card") >= 0
          || parts[ipart].indexOf("charge account") >= 0) {
        this.creditClaim =
            accumValue(this.creditClaim, words[words.length - 1].replaceAll("[$,]", ""));
        if (isDebug)
          System.out.printf("%s\t%s\n", words[words.length - 1].replaceAll("\\D", ""),
              this.creditClaim);
        continue;
      }

      if (parts[ipart].indexOf("medical") >= 0 || parts[ipart].indexOf("health") >= 0
          || parts[ipart].indexOf("hospital") >= 0) {
        this.medicalClaim =
            accumValue(this.medicalClaim, words[words.length - 1].replaceAll("[$,]", ""));
        continue;
      }

      if (parts[ipart].indexOf("insurance") >= 0) {
        this.insuranceClaim =
            accumValue(this.insuranceClaim, words[words.length - 1].replaceAll("[$,]", ""));
        continue;
      }


      if (parts[ipart].indexOf("chargeprivilege") >= 0
          || parts[ipart].indexOf("charge privilege") >= 0) {
        this.chargeClaim =
            accumValue(this.chargeClaim, words[words.length - 1].replaceAll("[$,]", ""));
        continue;
      }

      if (parts[ipart].indexOf("student loan") >= 0) {
        this.studentloan =
            accumValue(this.studentloan, words[words.length - 1].replaceAll("[$,]", ""));
        continue;
      }

      if (parts[ipart].indexOf("loan") >= 0) {
        this.loanClaim = accumValue(this.loanClaim, words[words.length - 1].replaceAll("[$,]", ""));
        continue;
      }

      if (parts[ipart].indexOf("account") >= 0) {
        this.accountClaim =
            accumValue(this.accountClaim, words[words.length - 1].replaceAll("[$,]", ""));
        continue;
      }
    }

  }

  String accumValue(String left, String right) {
    String result;
    if (left == null)
      result = right;
    else {
      double temp = 0;
      try {
        temp = Double.valueOf(left) + Double.valueOf(right);
      } catch (NumberFormatException e) {

      }
      result = String.valueOf(temp);
    }
    return result;
  }

  public String toString() {

    if (creditClaim == null)
      this.value = "N.A.";
    else
      this.value = creditClaim;

    if (medicalClaim == null)
      this.value += "\t N.A.";
    else
      this.value += "\t " + medicalClaim;

    if (insuranceClaim == null)
      this.value += "\t N.A.";
    else
      this.value += "\t " + insuranceClaim;

    if (chargeClaim == null)
      this.value += "\t N.A.";
    else
      this.value += "\t " + chargeClaim;

    if (studentloan == null)
      this.value += "\t N.A.";
    else
      this.value += "\t " + studentloan;

    if (loanClaim == null)
      this.value += "\t N.A.";
    else
      this.value += "\t " + loanClaim;

    if (accountClaim == null)
      this.value += "\t N.A.";
    else
      this.value += "\t " + accountClaim;

    return this.value;
  }


}
