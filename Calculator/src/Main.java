import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.util.*;

class Main {
  public static void main (String [] argv) throws Exception{
    new MyFrame ("123?");
  }
}

class SecondDisplay extends Label {
  DisplayLabel dl;
  
  public SecondDisplay(String title, DisplayLabel dl) {
    super(title);
	this.dl = dl;
    setSize (400, 80);
    setLocation (8, 45);
    setFocusable(false);
    setBackground (new Color(255, 255, 255));
	setText("0");
	dl.setLink(this);
  }
}

class DisplayLabel extends Label {
  int previousAction, action, taction, p;
  double a, b, v;
  boolean isItFirstAction, isEqualation, isCalculated, isFloat;
  String s;
  SecondDisplay sd;
	
  public DisplayLabel (String title) {
    super(title);
	eraseFields("0");
    setSize (400, 80);
    setLocation (8, 0);
    setFocusable(false);
    setBackground (new Color(255, 255, 255));
  }
	
  public void changeLabel(String s, boolean act) {
	try {
      if (isItFirstAction) {
	    if (!act) {
	      isItFirstAction = false;
	      previousAction = 1;
	      a = Double.valueOf(String.format("%.0f", (new Double(s).doubleValue())).replace(',', '.'));
	      b = 0;
	      this.s = this.s + s;
	    }
	  } else {
	    if (previousAction == 1 & !isCalculated) {
		  if (act & !s.equals(".") & !s.equals("=")) {
			if (this.s.charAt(this.s.length() - 1) == '.')
			  this.s = new StringBuffer(this.s).deleteCharAt(this.s.length() - 1).toString();
		    setAction(s, 2);
			this.s = this.s + s;
		  }
		  if (s.equals(".") & !isFloat) {
			isFloat = true;
			p = -1;
			this.s = this.s + s;
		  }
		  if (!act) {
			if (!isFloat)
			  a = a * 10 + Double.valueOf(String.format("%.0f", (new Double(s).doubleValue())).replace(',', '.'));
		    else {
			  a = a + Math.pow(10, p) * Double.valueOf(String.format("%.0f", (new Double(s).doubleValue())).replace(',', '.'));
			  p --;
			}
			this.s = this.s + s;
		  }
		} else {
		  if (previousAction == 2) {
			isFloat = false;
			if (act & !s.equals("=") & !s.equals(".")) {
		      setAction(s, 2);
			  this.s = (new StringBuffer(this.s).deleteCharAt(this.s.length() - 1).toString()) + s;
		    } else {
			  if (!act) {
			    b = Double.valueOf(String.format("%.4f", (new Double(s).doubleValue())).replace(',', '.'));
				this.s = this.s + s;
			    previousAction = 3;
			  }
			}
		  } else {
		    if (previousAction == 3) {
		      if (act & !s.equals(".")) {
		        setAction(s, 4);
				if (this.s.charAt(this.s.length() - 1) == '.')
			      this.s = new StringBuffer(this.s).deleteCharAt(this.s.length() - 1).toString();
			    if (s.equals("=")) {
				  if (action == 1)
					v = a + b;
				  if (action == 2)
					v = a - b;
				  if (action == 3)
					v = a * b;
				  if (action == 4)
					v = a / b;
				  a = 0;
				  b = 0;
				  previousAction = 1;
				  isEqualation = true;
				} else {
				  if (taction == 1)
					v = a + b;
			      if (taction == 2)
				    v = a - b;
				  if (taction == 3)
					v = a * b;
				  if (taction == 4)
					v = a / b;
				  a = v;
				  b = 0;
				  previousAction = 2;
				  this.s = this.s + s;
				}
		      }
			  if (s.equals(".")) {
			    isFloat = true;
			    p = -1;
				this.s = this.s + s;
			  }
			  else
				if (!act) {
		          if (!isFloat)
			        b = b * 10 + Double.valueOf(String.format("%.0f", (new Double(s).doubleValue())).replace(',', '.'));
		          else {
			        b = b + Math.pow(10, p) * Double.valueOf(String.format("%.0f", (new Double(s).doubleValue())).replace(',', '.'));
			        p --;
			      }
				  this.s = this.s + s;
		        }
			}
		  }
		}
	  }
	  if (Double.isInfinite(v) && (1 / 0 == 1));
      if (!isCalculated) {
        setText(this.s);
		sd.setText(Double.toString(new Double(String.format("%.4f", v).replace(',', '.'))));
	  }
      if (isEqualation)
        isCalculated = true;
    } catch (Exception e) {
	  if (!isCalculated) 
	    eraseFields(this.s);
	    sd.setText("Error!");
	}
  }
	
  public void setLink(SecondDisplay sd) {
    this.sd = sd;
  }  
	
  public void setAction(String s, int t) {
    String dict = "+-*/";
	taction = action;
	action = dict.indexOf(s) + 1;
	if (action <= 0 | action > 5)
	  action = taction;
	previousAction = t;
  }
  
  public void eraseFields(String title) {
    this.s = "";
    s = "";
    action = 0;
    taction = 0;
    v = 0;
	p = 0;
    a = 0;
    b = 0;
    previousAction = 0;
	isFloat = false;
    isItFirstAction = true;
    isCalculated = false;
    isEqualation = false;
    setText(title);
  }
}

class NumButton extends Button implements ActionListener, MouseListener {
  DisplayLabel db;
  String title;
	
  public NumButton (String title, int locx, int locy, DisplayLabel db){
    super(title);
    this.db = db;
    this.title = title;
    int wx = locx, hy = locy;
    setSize (100, 100);
    setLocation ((wx - 1) * 100, (hy - 1) * 100 + 24);
    setFocusable(false);
    setBackground (new Color(200, 200, 200));
    addActionListener(this);
    addMouseListener(this);
  }
  
  public void actionPerformed (ActionEvent ae) {
    db.changeLabel(title, false);
  }
    
  public void mouseEntered (MouseEvent me) {
    setBackground (new Color(150, 150, 150));
  }
    
  public void mouseClicked (MouseEvent me) {
  }
    
  public void mouseExited (MouseEvent me) {
    setBackground (new Color(200, 200, 200));
  }
		
  public void mousePressed (MouseEvent me) {
  }
			
  public void mouseReleased (MouseEvent me) {
  }
}

class ActionButton extends Button implements ActionListener, MouseListener {
  DisplayLabel db;
  String title;
	
  public ActionButton (String title, int locx, int locy, int sizex, int sizey, DisplayLabel db){
    super(title);
    this.db = db;
    this.title = title;
    int wx = locx, hy = locy;
    setSize (sizex, sizey);
    setLocation (wx, hy + 24);
    setFocusable(false);
    setBackground (new Color(200, 200, 200));
    addActionListener(this);
    addMouseListener(this);
  }
  
  public void actionPerformed (ActionEvent ae) {
    db.changeLabel(title, true);
  }
    
  public void mouseEntered (MouseEvent me) {
    setBackground (new Color(150, 150, 150));
  }
    
  public void mouseClicked (MouseEvent me) {
  }
    
  public void mouseExited (MouseEvent me) {
    setBackground (new Color(200, 200, 200));
  }
		
  public void mousePressed (MouseEvent me) {
  }
			
  public void mouseReleased (MouseEvent me) {
  }
}

class EraseButton extends Button implements ActionListener, MouseListener {
  DisplayLabel db;
  SecondDisplay sd;
  String title;
	
  public EraseButton (String title, int locx, int locy, int sizex, int sizey, DisplayLabel db, SecondDisplay sd){
    super(title);
    this.db = db;
	this.sd = sd;
    this.title = title;
    int wx = locx, hy = locy;
    setSize (sizex, sizey);
    setLocation (wx, hy + 24);
    setFocusable(false);
    setBackground (new Color(200, 200, 200));
    addActionListener(this);
    addMouseListener(this);
  }
  
  public void actionPerformed (ActionEvent ae) {
    db.eraseFields("0");
	sd.setText("0");
  }
    
  public void mouseEntered (MouseEvent me) {
    setBackground (new Color(150, 150, 150));
  }
    
  public void mouseClicked (MouseEvent me) {
  }
    
  public void mouseExited (MouseEvent me) {
    setBackground (new Color(200, 200, 200));
  }
		
  public void mousePressed (MouseEvent me) {
  }
			
  public void mouseReleased (MouseEvent me) {
  }
}

class MyFrame extends Frame implements WindowListener {
  NumButton b1, b2, b3, b4, b5, b6, b7, b8, b9, b0;
  ActionButton bequalate, bsum, bdiff, bmultiplicate, bdivide, bpoint;
  DisplayLabel db;
  SecondDisplay sd;
  EraseButton eb;
    
  public MyFrame (String title) {
    super(title);
    setLayout(null);
    setSize (400, 624);
    setLocation (200, 200);
    setBackground (new Color(200, 200, 200));
    addWindowListener(this);
    add (db = new DisplayLabel(""));
	add (sd = new SecondDisplay("", db));
    add (b1 = new NumButton("1", 1, 2, db));
    add (b2 = new NumButton("2", 2, 2, db));
    add (b3 = new NumButton("3", 3, 2, db));
    add (b4 = new NumButton("4", 1, 3, db));
    add (b5 = new NumButton("5", 2, 3, db));
    add (b6 = new NumButton("6", 3, 3, db));
    add (b7 = new NumButton("7", 1, 4, db));
    add (b8 = new NumButton("8", 2, 4, db));
    add (b9 = new NumButton("9", 3, 4, db));
    add (b0 = new NumButton("0", 2, 5, db));
	add (bpoint = new ActionButton(".", 200, 400, 100, 100, db));
    add (bequalate = new ActionButton("=", 0, 500, 400, 100, db));
    add (bsum = new ActionButton("+", 300, 100, 100, 100, db));
    add (bdiff = new ActionButton("-", 300, 200, 100, 100, db));
    add (bmultiplicate = new ActionButton("*", 300, 300, 100, 100, db));
    add (bdivide = new ActionButton("/", 300, 400, 100, 100, db));
	add (eb = new EraseButton("C", 0, 400, 100, 100, db, sd));
    setVisible (true);
  }
  public void windowClosing (WindowEvent we) {
    System.exit(0);
  }
  
  public void windowClosed (WindowEvent we) {
  }
		
  public void windowOpened (WindowEvent we) {
  }
		
  public void windowActivated (WindowEvent we) {
  }
		
  public void windowDeactivated (WindowEvent we) {
  }
			
  public void windowIconified (WindowEvent we) {
  }
			
  public void windowDeiconified (WindowEvent we) {
  }
}
