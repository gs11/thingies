package se.gustavsinder.samplecalc;

import java.text.DecimalFormat;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

/*
 * TODO:
 * - Tooltips
 * - Enter key press
 * - Look at other projects to validate arch
 * 
 * - Modify Gradle script to support other platforms
 * - Gradle Packaging 
 * 
 */

public class SampleCalc {

	protected Shell shell;
	private Text calcTempoSampleTempo;
	private Text calcTempoPitchShift;
	private Text calcTempoNewTempo;
	private Text calcTimestretchSampleTempo;
	private Text calcTimestretchNewTempo;
	private Text calcTimestretchTimestretch;

	private final static double x = Math.pow(2.0, (1d/12d));

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SampleCalc window = new SampleCalc();
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
		Display.setAppName("SampleCalc");
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
	 * Calculates a new tempo from a base tempo with positive or negative semitones
	 * 
	 * @param sampleTempo the base tempo in BPM
	 * @param pitchShift positive or negative semitones
	 * @return an adjusted tempo in BPM or Error!
	 */
	protected String calculateTempo(Text sampleTempo, Text pitchShift) {
		try {
			calcTempoNewTempo.setText("");

			// Handle both dot & comma as the decimal separator
			double sampleTempoDouble = Double.valueOf(sampleTempo.getText().replace(",", "."));
			double pitchShiftDouble = Double.valueOf(pitchShift.getText().replace(",", "."));

			double newTempo = Math.pow(x, pitchShiftDouble) * sampleTempoDouble;

			DecimalFormat numberFormat = new DecimalFormat("#.000");
			return numberFormat.format(newTempo);
		} catch (NumberFormatException e) {
			// Input values are invalid doubles
			return "Error!";
		}
	}

	/**
	 * Calculates the timestretch from a base tempo and the desired new tempo
	 * 
	 * @param sampleTempo the base tempo in BPM
	 * @param newTempo the desired tempo in BPM
	 * @return a timestretch percentage or Error!
	 */
	protected String calculateTimestretch(Text sampleTempo, Text newTempo) {
		try {
			calcTimestretchTimestretch.setText("");

			// Handle both dot & comma as the decimal separator
			double sampleTempoDouble = Double.valueOf(sampleTempo.getText().replace(",", "."));
			double newTempoDouble = Double.valueOf(newTempo.getText().replace(",", "."));

			double timestretch = (newTempoDouble / sampleTempoDouble) * 100;

			DecimalFormat numberFormat = new DecimalFormat("#.000");
			return numberFormat.format(timestretch);
		} catch (NumberFormatException e) {
			// Input values are invalid doubles
			return "Error!";
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(411, 143);
		shell.setText("Sample Calculator");

		Label lblCalcTempoSampleTempo = new Label(shell, SWT.NONE);
		lblCalcTempoSampleTempo.setBounds(25, 10, 85, 14);
		lblCalcTempoSampleTempo.setText("Sample tempo");

		calcTempoSampleTempo = new Text(shell, SWT.BORDER);
		calcTempoSampleTempo.setBounds(25, 25, 85, 19);

		Label lblCalcTempoPitchShift = new Label(shell, SWT.NONE);
		lblCalcTempoPitchShift.setBounds(118, 10, 85, 14);
		lblCalcTempoPitchShift.setText("Pitch-shift");

		calcTempoPitchShift = new Text(shell, SWT.BORDER);
		calcTempoPitchShift.setBounds(118, 25, 85, 19);

		Label lblCalcTempoNewTempo = new Label(shell, SWT.NONE);
		lblCalcTempoNewTempo.setText("New tempo");
		lblCalcTempoNewTempo.setBounds(211, 10, 82, 14);

		calcTempoNewTempo = new Text(shell, SWT.BORDER);
		calcTempoNewTempo.setBounds(211, 25, 85, 19);
		calcTempoNewTempo.setEditable(false);

		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				calcTempoNewTempo.setText(
						calculateTempo(calcTempoSampleTempo, calcTempoPitchShift));
			}
		});
		btnNewButton.setBounds(299, 21, 94, 28);
		btnNewButton.setText("Calculate");

		// -----------------------------------------------------------------------------
		Label separator1 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator1.setBounds(25, 55, 362, 10);
		// -----------------------------------------------------------------------------

		Label lblCalcTimestretchSampleTempo = new Label(shell, SWT.NONE);
		lblCalcTimestretchSampleTempo.setText("Sample tempo");
		lblCalcTimestretchSampleTempo.setBounds(25, 71, 85, 14);

		calcTimestretchSampleTempo = new Text(shell, SWT.BORDER);
		calcTimestretchSampleTempo.setBounds(25, 86, 85, 19);

		Label lblcalcTimestretchNewTempo = new Label(shell, SWT.NONE);
		lblcalcTimestretchNewTempo.setText("New tempo");
		lblcalcTimestretchNewTempo.setBounds(118, 71, 85, 14);

		calcTimestretchNewTempo = new Text(shell, SWT.BORDER);
		calcTimestretchNewTempo.setBounds(118, 86, 85, 19);

		Label lblCalcTimestretchTimestretch = new Label(shell, SWT.NONE);
		lblCalcTimestretchTimestretch.setText("Timestretch");
		lblCalcTimestretchTimestretch.setBounds(211, 71, 82, 14);

		calcTimestretchTimestretch = new Text(shell, SWT.BORDER);
		calcTimestretchTimestretch.setBounds(211, 86, 85, 19);
		calcTimestretchTimestretch.setEditable(false);

		Button button = new Button(shell, SWT.NONE);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				calcTimestretchTimestretch.setText(
						calculateTimestretch(calcTimestretchSampleTempo, calcTimestretchNewTempo));
			}
		});
		button.setText("Calculate");
		button.setBounds(299, 82, 94, 28);
	}
}
