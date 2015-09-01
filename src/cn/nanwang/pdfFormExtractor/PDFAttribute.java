package cn.nanwang.pdfFormExtractor;

// @author Nan Wang

public class PDFAttribute {

  protected String name;
  protected String value;
  protected int page;
  int index;
  protected boolean isKey;
  protected boolean isDebug;

  public PDFAttribute() {
    isKey = false;
    value = null;
    isDebug = false;
  }

  public void assignValue(String v) {
    this.value = v;
  }

  public void computeValue(String src) {

  }

  public void computeValue(String src, String value2) {
    // TODO Auto-generated method stub

  }

  public boolean isValid() {
    if (this.isKey) {
      return this.value != null;
    } else
      return true;
  }

  public String printName() {
    return this.name;
  }

  public String toString() {
    if (this.value == null)
      return "N.A.";

    return this.value;
  }

  public void setDebug(boolean isDebug) {
    this.isDebug = isDebug;
  }

  public boolean isDebug() {
    return isDebug;
  }

  static public String addTwoString(String s1, String s2) {
    double v1;
    if (s1 != null) {
      v1 = Double.valueOf(s1.replaceAll(",", ""));
    } else
      v1 = 0;
    double v2 = Double.valueOf(s2.replaceAll(",", ""));

    v1 = v1 + v2;
    String s3 = Double.toString(v1);
    return s3;
  }

}
