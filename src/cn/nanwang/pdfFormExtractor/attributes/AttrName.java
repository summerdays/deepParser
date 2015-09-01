package cn.nanwang.pdfFormExtractor.attributes;

import cn.nanwang.pdfFormExtractor.PDFAttribute;

// @author Nan Wang

public class AttrName extends PDFAttribute {
  public AttrName() {
    super();
    this.name = "File Name";
    this.page = -1;
    this.isKey = true;
  }
}
