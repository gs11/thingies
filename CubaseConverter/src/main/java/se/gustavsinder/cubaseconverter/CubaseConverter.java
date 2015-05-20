package se.gustavsinder.cubaseconverter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.wb.swt.SWTResourceManager;

public class CubaseConverter {

	protected Shell shell;
	private Label lblFileInfo;
	private Composite composite;
	private Combo cmbOutputFileFormat;
	Button btnConvert;

	CubaseDocument cubaseDoc;

	private static final Color NICE_GREY = new Color(Display.getCurrent(), 237, 237, 237);
	private static final Image DROP_IMAGE = new Image(Display.getCurrent(), "src/main/resources/drop.png");
	private static final Image DROP_IMAGE_DARK = new Image(Display.getCurrent(), "src/main/resources/drop2.png");
	private static final String TEXT_CONVERT = "Convert!";
	private static final String TEXT_NOT_RECOGNIZED = "File type not recognized";
	private static final String TEXT_APP_NAME = "Cubase Converter";
	private static final String TEXT_NO_FILE_LOADED = "No file loaded";

	private Label lblOutputFormat;

	/*
	 * TODO:
	 * Should really everything be placed in createContents as of now?
	 * Should the CubaseDocument object be null if the document can't be idenfied? 
	 * 
	 * Finish upp layout:
	 * Own drop icon + make it darker during mouseOver
	 * Customizable out file name?
	 * Menu: Open -> Browse file?
	 * 
	 */

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CubaseConverter window = new CubaseConverter();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setBackground(NICE_GREY);
		shell.setSize(344, 304);
		shell.setText(TEXT_APP_NAME);

		composite = new Composite(shell, SWT.NONE);
		composite.setBounds(10, 10, 324, 200);
		composite.setBackground(NICE_GREY);
		composite.setBackgroundImage(DROP_IMAGE);

		lblFileInfo = new Label(shell, SWT.NONE);
		lblFileInfo.setBounds(10, 261, 209, 21);
		lblFileInfo.setText(TEXT_NO_FILE_LOADED);

		DropTarget dt = new DropTarget(composite, DND.DROP_DEFAULT | DND.DROP_MOVE );
		dt.setTransfer(new Transfer[] { FileTransfer.getInstance() });

		dt.addDropListener(new DropTargetAdapter() {
			public void drop(DropTargetEvent event) {
				String fileList[] = null;
				FileTransfer ft = FileTransfer.getInstance();
				if (ft.isSupportedType(event.currentDataType)) {
					fileList = (String[])event.data;
					cubaseDoc = new CubaseDocument(fileList[0]);

					if (cubaseDoc.getFileType() != null) {
						lblFileInfo.setText(cubaseDoc.getFileName().getName() + 
								" (" + cubaseDoc.getFileType() + " " + cubaseDoc.getFileVersion() + ")");
					} else {
						lblFileInfo.setText(TEXT_NOT_RECOGNIZED);
					}
				}
			}
			@Override
			public void dragEnter(DropTargetEvent event) {
				composite.setBackgroundImage(DROP_IMAGE_DARK);
				//System.out.println("Hover, add border & change background");
			}
			@Override
			public void dragLeave(DropTargetEvent event) {
				composite.setBackgroundImage(DROP_IMAGE);
				//System.out.println("Hover no more, restore border & background");
			}
		});

		cmbOutputFileFormat = new Combo(shell, SWT.NONE | SWT.READ_ONLY);
		cmbOutputFileFormat.setBounds(10, 233, 109, 22);

		cmbOutputFileFormat.add(CubaseDocument.AI_IDENTIFIER);
		cmbOutputFileFormat.add(CubaseDocument.SX_IDENTIFIER);

		btnConvert = new Button(shell, SWT.NONE);
		btnConvert.setBounds(246, 232, 94, 28);
		btnConvert.setText(TEXT_CONVERT);

		lblOutputFormat = new Label(shell, SWT.NONE);
		lblOutputFormat.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.BOLD));
		lblOutputFormat.setText("Output format");
		lblOutputFormat.setBounds(12, 218, 106, 14);

		btnConvert.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				//TODO: can we check if we're still inside the box?

				if (cubaseDoc != null && cubaseDoc.getFileType() != null && !cmbOutputFileFormat.getText().equals("")) {
					cubaseDoc.saveAsNewFile(cmbOutputFileFormat.getText());
				}
			}
		});
	}
}
