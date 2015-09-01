package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;


public class AttrYearlyIncome extends PDFAttribute {
	
	static Pattern yearRegex = Pattern.compile("(?:^|[\\D&&[^\\$]]+)(200[0-9]|199[1-9]|(?:[0-9]?[0-9][-/][0-9]?[0-9][-/](?:0[0-9])?)).*");
	static Pattern money2Regex = Pattern.compile("(?:^[\\$\\s]+)((?:[0-9]{1,6}(?:,[0-9]{3}){0,4})(?:\\.[0-9]{2})?).*");
	static Pattern moneyRegex = Pattern.compile("(?:^|[\\D&&[^\\.]]+)((?:[0-9]{1,3}(?:,[0-9]{3}){1,4}|[0-9]{3,4})(?:\\.[0-9]{2})?).*");
	String incomeAmount1 = null;
	String incomeAmount2 = null;
	String incomeAmount3 = null;
	
	public AttrYearlyIncome() {
		super();
		// TODO Auto-generated constructor stub
		name = "Yearly Income Amount 1 \t Yearly Income Amount 2 \t Yearly Income Amount 3";
		page = 1;
		value = "N.A. \t N.A. \t N.A.";
	}
	
	public void computeValue(String src){


		Matcher mYear = yearRegex.matcher(src);
		if(mYear.find()){
			int safeWhile = 0;
			do{
				//System.out.printf("Before Reduced :---%s---\n",lines[l]);
				src = src.replaceAll(mYear.group(1), "");
				mYear.reset();
				mYear = yearRegex.matcher(src);
				if(safeWhile++ > 100)
					break;
			}while(mYear.find());

			Matcher mMoney = moneyRegex.matcher(src);
			Matcher mMoney2 = money2Regex.matcher(src);

			if(mMoney.matches() | mMoney2.matches() ){
				if(incomeAmount1 == null){
					if(mMoney2.matches())
						incomeAmount1 = mMoney2.group(1);
					else if(mMoney.matches())
						incomeAmount1 = mMoney.group(1);
				}
				else if (incomeAmount2 == null){
					if(mMoney2.matches())
						incomeAmount2 = mMoney2.group(1);
					else if(mMoney.matches())
						incomeAmount2 = mMoney.group(1);
				}
				else if	(incomeAmount3 == null){
					if(mMoney2.matches())
						incomeAmount3 = mMoney2.group(1);
					else if(mMoney.matches())
						incomeAmount3 = mMoney.group(1);
				}
				
			}
			this.value = incomeAmount1 + "\t" + incomeAmount2 + "\t" + incomeAmount3;
		}
	}
	
}
