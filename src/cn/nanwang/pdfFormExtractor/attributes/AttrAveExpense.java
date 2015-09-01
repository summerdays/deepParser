package cn.nanwang.pdfFormExtractor.attributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nanwang.pdfFormExtractor.PDFAttribute;

//@author Nan Wang

public class AttrAveExpense extends PDFAttribute {

	Pattern moneyRegex = Pattern.compile("(?:^|[\\$\\s]+)([0-9]{1,3}(?:,[0-9]{3}){0,3}(?:\\.[\\d]{2})?)\\s*$");

	public AttrAveExpense() {
		super();
		// TODO Auto-generated constructor stub
		name = "Average Expense";
		page = 9;
		isKey = true;
		isDebug = false;
	}

	public void computeValue(String src){
		int numExpense=40;
		double[] e = new double[numExpense];

		int iExpense = 0;
		double sum = 0;
		String[] lines = src.split("[\\r\\n]+");
		
		for(int l = 1; l <= lines.length - 2; l++){
			if( lines[l].indexOf("Page") >= 0 || lines[l].indexOf("Chapter") >= 0 ){
				continue;
			}
			Matcher m = moneyRegex.matcher(lines[l]);
			if(m.find()){
				e[iExpense] = Double.valueOf(m.group(1).replaceAll(",", ""));
				if(isDebug){
					System.out.printf("%d. %g %g %s\n",  iExpense, e[iExpense], sum, lines[l]);
				}
				iExpense++;

				if(sum == 0 || sum!=  e[iExpense-1] )
					sum+= e[iExpense-1];
				else
					break;
				if(iExpense >= numExpense )
					break;
			}

		}
		if(this.value == null && sum!= 0 && sum == e[iExpense-1]){
			this.value = String.valueOf(e[iExpense-1]);				
		}

	}

	



}
