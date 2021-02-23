package scalaExec.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * The TextConsole component. An extension of the 
 * JTextPane class for user interaction in text
 * form.
 */
class TextConsole extends JTextPane {

	/**
	 * Default parameters
	 for basic font name and size.
	 */
	private static final int DEFAULT_FONT_SIZE = 20;
	private static final String DEFAULT_FONT_NAME = "Courier New";
	private static final int DEFAULT_WIDTH_CHARS = 80;
	private static final int DEFAULT_HEIGHT_CHARS = 25;

	private Font font = null;

	/**
	 * flag to set to true when the form is submitted
	 */
	private volatile boolean finished = false;
	MutableAttributeSet attrs = getInputAttributes();
	int widthChars = DEFAULT_WIDTH_CHARS;
	int heightChars = DEFAULT_HEIGHT_CHARS;

	/**
	 * Maximum numbr of characters that will hold
	 * in the console window == width * height
	 */
	int maxLength = pointToInt(widthChars, heightChars);

	/**
	 * The list of keys that shoould be processed
	 * by the JTextPane superclass.
	 */
	int[] processable = new int[] { 
			KeyEvent.VK_UP, 
			KeyEvent.VK_DOWN,
			KeyEvent.VK_LEFT, 
			KeyEvent.VK_RIGHT, 
			KeyEvent.VK_HOME,
			KeyEvent.VK_END };

	
	/**
	 * List of zones that support user input.
	 */
	private List formRanges = new ArrayList();

	/**
	 * Basic constructor: uses default values for
	 * font size (20) and name (Courier New). Input parameters include
	 * width and height. 
	 */
	public TextConsole(int width, int height) {
		this(width, height, DEFAULT_FONT_SIZE, DEFAULT_FONT_NAME);
	}

	
	/**
	 * Full constructor taking as input the width and height in number 
	 * of characters, font size and font name. 
	 */
	public TextConsole(int width, int height, int fontSize, String fontName) {
		font = new Font(fontName, Font.BOLD, fontSize);
		widthChars = width;
		heightChars = height;
		setFont(font);
		FontRenderContext fontRenderContext = new FontRenderContext(null, true,
				true);
		Rectangle2D stringBounds = font.getStringBounds(new char[] { 'l' }, 0,
				1, fontRenderContext);
		setPreferredSize(new Dimension(
				(int) ((widthChars + 1) * stringBounds.getWidth()), 
				(int) ((heightChars + 1.5) * stringBounds.getHeight())));
		setForeground(Color.WHITE);
		setBackground(Color.BLACK);
		setCaretColor(Color.WHITE);
		fill();
	}

	/**
	 * Rendering method. Overrides the paint() method 
	 * on the superclass to add antialiasing to the output
	 * screen. 
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		super.paint(g);
	}

	
	/**
	 * Method to fill the screen with blank characters (space)
	 */
	private void fill() {
		StringBuffer buffer = new StringBuffer();
		for (int j = 0; j < heightChars; j++) {
			for (int i = 0; i < widthChars; i++) {
				buffer.append(" ");
			}
			if (j < heightChars - 1) {
				buffer.append("\n");
			}
		}

		setText(buffer.toString());

	}

	/**
	 * Convert an X-Y position into a sequential index
	 * of a character in the text pane.
	 */
	private int pointToInt(int i, int j) {
		int ret = ((j - 1) * (widthChars + 1)) + (i - 1);
		return ret;
	}

	/**
	 * set the cursor position on screen 
	 */
	public void gotoPosition(int x, int y) {
		int start = pointToInt(x, y);
		setCaretPosition(start);
	}

	/**
	 * set the cursor position on the first
	 * form field on screen
	 */
	public void gotoFirstField() {
		if (formRanges.size() > 0) {
			FormRange firstRange = (FormRange) formRanges.get(0);
			setCaretPosition(firstRange.start);
		}
	}

	/**
	 * Clear the screen
	 */
	public void clear() {
		setForeground(Color.WHITE);
		fill();
		reset();
	}

	/**
	 * set the prescribed color to all characters in a given range
	 */
	public void color(int i, int j, Color color) {
		StyleConstants.setForeground(attrs, color);
		getStyledDocument().setCharacterAttributes(i, j - i,
		attrs, true);
	}

	/**
	 * Key processing function. Certain key events
	 * are delegated to the superclass. Others submit 
	 * the user input to the calling object or react to the input 
	 * internally.
	 */
	protected void processKeyEvent(KeyEvent e) {
		char keyChar = e.getKeyChar();
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER) {
			finished = true;
		}
		boolean needProcess = false;
		for (int i = 0; i < processable.length; i++) {
			if (processable[i] == keyCode) {
				needProcess = true;
			}
		}
		if (!needProcess) {
			int caretPosition = getCaretPosition();
			for (Iterator iter = formRanges.iterator(); iter.hasNext();) {
				FormRange range = (FormRange) iter.next();
				int checkPosition = caretPosition;
				if (keyCode == KeyEvent.VK_BACK_SPACE) {
					checkPosition = caretPosition - 1;
				}
				if (range.isInRange(checkPosition)) {
					if (e.getID() == KeyEvent.KEY_PRESSED) {
						setLocalColor(range.color);
						if (Character.isLetterOrDigit(keyChar)) {
							write("" + keyChar);
						} else if (keyCode == KeyEvent.VK_SPACE) {
							write(" ");
						} else if (keyCode == KeyEvent.VK_DELETE) {
							write(" ");
						} else if (keyCode == KeyEvent.VK_BACK_SPACE) {
							setCaretPosition(getCaretPosition() - 1);
							write(" ");
							setCaretPosition(getCaretPosition() - 1);
						}
					}
				}
			}
			if ((keyCode == KeyEvent.VK_TAB)
					&& (e.getID() == KeyEvent.KEY_PRESSED)) {
				boolean found = false;
				if (e.isShiftDown()) {
					Collections.reverse(formRanges);
				}
				for (Iterator iter = formRanges.iterator(); iter.hasNext()
						&& (!found);) {
					FormRange hotRange = (FormRange) iter.next();
					int start = hotRange.start;
					if (e.isShiftDown()) {
						if (start < caretPosition) {
							setCaretPosition(start);
							found = true;
						}
					} else {
						if (start > caretPosition) {
							setCaretPosition(start);
							found = true;
						}
					}
				}
				if (e.isShiftDown()) {
					Collections.sort(formRanges);
				}
				if (!found && (formRanges.size() > 0)) {
					setCaretPosition(((FormRange) formRanges.get(0)).start);
				}
			}
		}
		if (needProcess) {
			super.processKeyEvent(e);
		}
	}

	/**
	 * Set the foreground font color to that 
	 * of the foreground text under the cursor 
	 */
	private void setLocalColor(Color color) {
		setForeground(color);
	}

	/**
	 * reset the "finished" flag to false. This flag remains false
	 * so long as the user has not submitted the form and blocks the getValues()
	 * method from returning.
	 */
	public void reset() {
		finished = false;
	}

	/**
	 * Write text to screen with current foregound color
	 */
	public void write(String string) {
		int caretPosition = getCaretPosition();
		if (caretPosition + string.length() > maxLength) {
			string = string.substring(0, maxLength - caretPosition + 1);
		}
		int start = caretPosition;
		int end = caretPosition + string.length();
		setSelectionStart(start);
		setSelectionEnd(end);
		replaceSelection(string);
		setSelectionStart(getCaretPosition());
		color(start, end, getForeground());
	}

	/**
	 * Set the foreground to prescribed color and 
	 * write text to screen
	 */
	public void write(String string, Color color) {
		setForeground(color);
		write(string);
	}

	/**
	 * Move cursor to prescribed position and write 
	 * text to screen
	 */
	public void write(String string, int x, int y) {
		gotoPosition(x, y);
		write(string);
	}

	/**
	 * Move cursor to prescribed position, set foreground color
	 * to prescribed color and write text to screen
	 */
	public void write(String string, int x, int y, Color color) {
		setForeground(color);
		gotoPosition(x, y);
		write(string);
	}

	/**
	 * Return the values map to the calling object.
	 * Each value in the map is associated to a key added 
	 * during the call to addFormField().
	 */
	public Map getValues() {
		while (!finished) {
			//do nothing until not finished
		}
		Map map = new HashMap();
		for (Iterator iter = formRanges.iterator(); iter.hasNext();) {
			FormRange range = (FormRange) iter.next();
			try {
				String text = getText(range.start, range.end - range.start);
				map.put(range.name, text.trim());
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * Add a form field to the screen with prescribed
	 * name key and in a given width. the getValues()
	 * method will return the value associated to this key in
	 * the input map. 
	 */
	public void addFormField(String fieldName, int width) {
		int start = getCaretPosition();
		int end = getCaretPosition() + width;
		addFormRange(fieldName, start, end);
	}

	/**
	 * Add a form field in a prescribed Color.
	 * @see addFormField()
	 */
	public void addFormField(String fieldName, int width, Color color) {
		setForeground(color);
		addFormField(fieldName, width);
	}

	/**
	 * Move the cursor to a prescribed position on the screen and 
	 * add a form field.
	 * @see addFormField()
	 */
	public void addFormField(String fieldName, int x, int y, int width) {
		int start = pointToInt(x, y);
		int end = pointToInt(x + width, y);
		addFormRange(fieldName, start, end);
	}

	/**
	 * Move the cursor to a prescribed position on the screen,
	 * set the foreground color to the prescribed color and
	 * add a form field.
	 * @see addFormField()
	 */
	public void addFormField(String fieldName, int x, int y, int width,
			Color color) {
		setForeground(color);
		int start = pointToInt(x, y);
		int end = pointToInt(x + width, y);
		addFormRange(fieldName, start, end);
	}

	/**
	 * Add a range to the list of form ranges, from the prescribed
	 * start parameter to the end parameter 
	 */
	private void addFormRange(String fieldName, int start, int end) {
		FormRange range = new FormRange(fieldName, start, end, getForeground());
		formRanges.add(range);
		Collections.sort(formRanges);
		StyleConstants.setUnderline(attrs, true);
		getStyledDocument().setCharacterAttributes(start, end - start,attrs, true);
		color(start, end, getForeground());

	}

	/**
	 * Form range container class.
	 * Contains information about a form range, including
	 * - form field key name
	 * - start of range
	 * - end of range
	 * - color of range
	 */
	class FormRange implements Comparable {
		private String name;
		private int start;
		private int end;
		private Color color;

		/**
		 * Constructor using name, start, end and color
		 * to set into the object
		 */
		public FormRange(String name, int start, int end, Color color) {
			this.name = name;
			this.start = start;
			this.end = end;
			this.color = color;
		}

		/**
		 * Returns "true" if the given int is in the
		 * [start, end] range
		 */
		public boolean isInRange(int i) {
			return (i >= start) && (i < end);
		}

		/**
		 * Method to enable sorting on a list of ranges.
		 */
		public int compareTo(Object other) {
			FormRange otherRange = (FormRange) other;
			return new Integer(start).compareTo(new Integer(otherRange.start));
		}
	}
}
