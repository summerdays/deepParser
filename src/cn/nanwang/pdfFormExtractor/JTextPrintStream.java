package cn.nanwang.pdfFormExtractor;

import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JTextArea;

//@author Nan Wang

public class JTextPrintStream extends PrintStream{
	//The JTextArea to which the output stream will be redirected.
    private JTextArea textArea;

    /**
     * Method TextAreaPrintStream
     * The constructor of the class.
     * @param the JTextArea to wich the output stream will be redirected.
     * @param a standard output stream (needed by super method)
     * @return 
     **/
    public JTextPrintStream(JTextArea area, OutputStream out) {
    	super(out);
    	textArea = area;
    }
    /**
     * Method println
     * prints a new line.
     **/
    public void println() {
    		textArea.append("\n");
    }
    
    /**
     * Method println
     * @param the String to be output in the JTextArea textArea (private
     * attribute of the class).
     * After having printed such a String, prints a new line.
     **/
    public void println(String string) {
    		textArea.append(string + "\n");
    }



    /**
     * Method print
     * @param the String to be output in the JTextArea textArea (private
     * attribute of the class).
     **/
    public void print(String string) {
    	textArea.append(string);
    }

}
