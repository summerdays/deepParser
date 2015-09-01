package cn.nanwang.pdfFormExtractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.beans.*;

// @author Nan Wang

public class BankruptcyExtractor extends JPanel implements ActionListener, PropertyChangeListener {

  private static final long serialVersionUID = 201204192157L;

  private JProgressBar progressBar;
  private JButton openButton, startButton;
  private JTextArea taskOutput;
  private Task task;
  private JFileChooser fc;

  PDFfile[] cases;
  int numCases;
  JTextPrintStream JTextout;
  int progress = 0;
  File dir;

  /**
   * Invoked when the user presses the start button.
   */
  public void actionPerformed(ActionEvent evt) {
    if (evt.getSource() == openButton) {
      int returnVal = fc.showOpenDialog(BankruptcyExtractor.this);

      if (returnVal == JFileChooser.APPROVE_OPTION) {
        dir = fc.getSelectedFile();
      }
    } else if (evt.getSource() == startButton) {

      startButton.setEnabled(false);
      setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      // Instances of javax.swing.SwingWorker are not reusuable, so
      // we create new instances as needed.
      task = new Task();
      task.addPropertyChangeListener(this);
      task.execute();
    }
  }

  /**
   * Invoked when task's progress property changes.
   */
  public void propertyChange(PropertyChangeEvent evt) {
    if ("progress" == evt.getPropertyName()) {
      int progress = (Integer) evt.getNewValue();
      progressBar.setValue(progress);
      taskOutput.setCaretPosition(taskOutput.getDocument().getLength());

    }
  }

  private static void showGUI() {
    // Create and set up the window.
    JFrame frame = new JFrame("PDF Form Extractor");
    // make the frame half the height and width
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    frame.setSize(screenSize.width / 2, screenSize.height / 2);
    frame.setLocationRelativeTo(null);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Create and set up the content pane.
    JComponent newContentPane = new BankruptcyExtractor();
    newContentPane.setOpaque(true); // content panes must be opaque
    frame.setContentPane(newContentPane);

    // Display the window.
    frame.pack();
    frame.setVisible(true);

  }

  public BankruptcyExtractor() {
    super(new BorderLayout());


    // Create the UI.
    JPanel panel = new JPanel();

    openButton = new JButton("Open a Folder...");
    openButton.addActionListener(this);
    panel.add(openButton);

    startButton = new JButton("Start");
    startButton.setActionCommand("start");
    startButton.addActionListener(this);
    panel.add(startButton);

    progressBar = new JProgressBar(0, 100);
    progressBar.setValue(0);
    progressBar.setStringPainted(true);
    panel.add(progressBar);

    taskOutput = new JTextArea(24, 80);
    taskOutput.setMargin(new Insets(5, 5, 5, 5));
    taskOutput.setEditable(false);



    add(panel, BorderLayout.PAGE_START);
    add(new JScrollPane(taskOutput), BorderLayout.CENTER);
    setBorder(BorderFactory.createEmptyBorder());

    dir = new File(".");
    fc = new JFileChooser();
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
  }


  public static void main(String[] args) {
    OutputStream output;
    try {
      output = new FileOutputStream("/dev/null");
      PrintStream nullOut = new PrintStream(output);
      System.setErr(nullOut);

    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        showGUI();
      }
    });
  }


  class Task extends SwingWorker<Void, Void> {
    /*
     * Main task. Executed in background thread.
     */
    @Override
    public Void doInBackground() {

      // Initialize progress property.
      setProgress(0);

      final File[] files = dir.listFiles();
      PrintStream output;
      JTextout = new JTextPrintStream(taskOutput, System.out);
      final int NUM_CORES = 8;

      cases = new PDFfile[files.length];

      numCases = 0;
      for (int i = 1; i < files.length; i++) { // filenames.length
        if (files[i].getName().toLowerCase().endsWith(".pdf")) {
          cases[numCases] = new PDFfile(files[i]);
          numCases++;
        }
      }


      Thread[] threads = new Thread[NUM_CORES];

      for (int t = 0; t < NUM_CORES; t++) {
        final int T = t;
        threads[t] = new Thread() {
          public void run() {
            for (int i = T; i < numCases; i += NUM_CORES) {
              cases[i].run();
              cases[i].isValid(JTextout);
              if (T == 0) {
                progress = (int) (100.0 * (i + 1) / numCases);
                setProgress(Math.min(progress, 100));

              }
            }
          }
        };
        threads[t].start();

      }



      for (int t = 0; t < NUM_CORES; t++)
        try {
          threads[t].join();
        } catch (InterruptedException e) {
        }


      try {
        int fails = 0;
        int sucesses = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String outFileName = "BankruptcyExtractorOutput" + dateFormat.format(date) + ".txt";
        output = new PrintStream(new FileOutputStream(new File(dir, outFileName)));
        for (int i = 0; i < numCases; i++) { // filenames.length

          if (i == 0) {
            for (String s : cases[i].attributeNames) {
              output.print(s + "\t");
            }
            output.println();
          }


          if (cases[i].isValid()) {
            sucesses++;
            output.println(cases[i]);
          } else {
            fails++;
          }
        }


        JTextout.printf("%d Fails %d Suceeses\n", fails, sucesses);
        setProgress(100);

        output.close();


      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
      // Toolkit.getDefaultToolkit().beep();
      startButton.setEnabled(true);
      setCursor(null); // turn off the wait cursor
    }
  }

}
