
package application;

import javafx.scene.Scene;
import javafx.application.Application;
import javafx.stage.Stage;

//Imports for components in this application.
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;

//Support for date entry.
import javafx.scene.control.DatePicker;

//Icons etc.
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
//Layout, containers etc.
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

//Support for quitting.
import javafx.application.Platform;


//Date handling.
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


//Currency output formatting.
import java.text.NumberFormat;
import java.util.Locale;

//Alerts...
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class InterestCalculator extends Application {


	Label lblCapital, lblInterestRate, lblInvTerm;
	TextField txtfCapital, txtfInterestRate, txtfInvTerm;
	Button btnQuit, btnCalculate, btnChooseDates;
	CheckBox chkSimple, chkCompound;
	TextArea txtMain;
	
	public InterestCalculator() {
		
		lblCapital = new Label("Capital:");
		
		lblInterestRate = new Label("Interest rate:");
		lblInvTerm = new Label("Investment term (yrs):");
		
		txtfCapital = new TextField();
		txtfInterestRate = new TextField();
		txtfInvTerm = new TextField();
				
		btnQuit = new Button("Quit");
		btnCalculate = new Button("Calculate");
		btnChooseDates = new Button("...");
		
		//Set button sizes.
		btnQuit.setMinWidth(80);
		btnCalculate.setMinWidth(80);
		
		chkSimple = new CheckBox("Simple interest");
		chkCompound = new CheckBox("Compound interest");
				
		txtMain = new TextArea();
				
	}
	
	@Override
	public void init() { //Event handling: Respond to program events.
		//Clicking btnQUit quits the application.
		btnQuit.setOnAction(click -> Platform.exit()); // closes entire app
		
		//Handle events on Dialog button
		btnChooseDates.setOnAction(click -> showTermDialog());
		
		//Handle events on the calculate button
		//Calculate the interest due, and show analysis in main text area
		btnCalculate.setOnAction(click -> showInterestAnalysis());
				
	}//init()
	
	private void showInterestAnalysis() {
		
		txtMain.setText("");
		double capital = 0;
		double intRate = 0;
		double years = 0;
		
		
		try {
			capital = Double.parseDouble(txtfCapital.getText());
			intRate = Double.parseDouble(txtfInterestRate.getText());
			years = Double.parseDouble(txtfInvTerm.getText());
		}
		catch(NumberFormatException nfe) {
			System.err.print("No data or incorrect type of data entered!" + nfe.toString());
			txtMain.setText("Error: You must enter a valid number in all textfields!");
		}
		
		
		if(chkSimple.isSelected()) {
			
			showSimpleInterest(capital, intRate, years);
		}
		
	
		if(chkCompound.isSelected()) {
		
			showCompoundInterest(capital, intRate, years);
		}
		
	}
	
	// method to show simple interest
	private void showSimpleInterest(double cap, double irate, double yrs) {
		double interest = 0;
		double increasedCapital;
		String resultString = "";
		
		double interestAmount = getSimpleInterest(cap, irate, yrs);
		
		
		increasedCapital = cap + interestAmount;
		
		Locale currLocale = Locale.getDefault();
//		System.out.println(currLocale.getDisplayCountry());
//		System.out.println(currLocale.getDisplayLanguage());
		NumberFormat currFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()); // machine config
		
		
		resultString = "Simple Interest:" + "\n\tYears: " + yrs + "\n\tInitial Capital: " 
						+ currFormat.format(cap) + "\n\tInterest Earned: " + currFormat.format(interestAmount) + "\n\tFinal Amount: " 
						+ currFormat.format(increasedCapital) + "\n\n";
		
		
		if(txtMain.getText().contains("Error")) {
			
			txtMain.clear();
		}
		//show analysis in txtMain in the main ui
		txtMain.appendText(resultString); //append it not overwrite it!	
	}//showSimpleInt()
	
	// method for the compound interest
	private void showCompoundInterest(double cap, double irate, double yrs) {
		double interestAmt = 0;
		double increasedCapital = cap;
		double totalInterest = 0;
		String resultString = "";
		

		for(int i = 1; i<=yrs; i++) {
			interestAmt = getSimpleInterest(increasedCapital, irate, 1);
			totalInterest += interestAmt; // total interest increases
			increasedCapital += interestAmt;
		}
		
		//Format the currency
		Locale currLocale = Locale.getDefault();
//		System.out.println(currLocale.getDisplayCountry());
//		System.out.println(currLocale.getDisplayLanguage());
		// don't see euros? - use Locale.GERMANY instead
		NumberFormat currFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()); // machine config
		// NumberFormat currFormat = NumberFormat.getCurrencyInstance(Locale.JAPAN);
	
		resultString += "Compound Interest:" + "\n\tYear: " + yrs  
				+ "\n\tInitial Capital: " + currFormat.format(cap) + "\n\tInterest Earned: " 
				+ currFormat.format(totalInterest) 
				+ "\n\tFinal Amount: " + currFormat.format(increasedCapital) + "\n\n";
		
	
		txtMain.appendText(resultString);
	
		
	}
	
	
	private double getSimpleInterest(double cap, double irate, double yrs) {
		double simpleInterest = 0; 
	
		
		simpleInterest = cap * (irate/100) * yrs; 
		
		return simpleInterest;
		
	} //getSimpleInterest()
	
	private void showTermDialog() {
	
		Stage termDialogStage = new Stage();
	
		termDialogStage.setTitle("Select Investment Term:");
		
		
		termDialogStage.setHeight(300);
		termDialogStage.setWidth(850);
		
		
		VBox vbDialogMain = new VBox();
		
		// subcontainers
		GridPane gpDialog = new GridPane();
		HBox hbDialogBtns = new HBox();
		
		// add subcontainers to the main layout
		vbDialogMain.getChildren().addAll(gpDialog, hbDialogBtns);
		
		//Create components for the dialog
		Label lblTermStart = new Label("Investment Start Date:");
		Label lblTermEnd = new Label("Investment End Date:");
		
		//datepickers support uniform format date entry
		DatePicker dpStart = new DatePicker();
		DatePicker dpEnd = new DatePicker();
		
		//default dates (avoid null dates)
		dpStart.setValue(LocalDate.now());
		dpEnd.setValue(LocalDate.now());
		
		// buttons 
		Button btnCancel = new Button("Cancel");
		Button btnOk = new Button("Ok");
		
		//add controls to the layout
		gpDialog.add(lblTermStart, 0, 0, 1, 1); // cols, rows, colspan, rowspan
		gpDialog.add(dpStart, 1, 0, 2, 1);
		gpDialog.add(lblTermEnd, 0, 1);
		gpDialog.add(dpEnd, 1, 1);
		
		//Add buttons
		hbDialogBtns.getChildren().addAll(btnCancel, btnOk);
		
		//Set spacing and padding
		vbDialogMain.setPadding(new Insets(20));
		vbDialogMain.setSpacing(20);
		hbDialogBtns.setPadding(new Insets(20,0,0,0));
		hbDialogBtns.setSpacing(20);
		gpDialog.setVgap(20);
		gpDialog.setHgap(10);
		

		hbDialogBtns.setAlignment(Pos.BASELINE_RIGHT);
		
		
		btnCancel.setOnAction(click -> termDialogStage.close()); 
		
	
		btnOk.setOnAction(click -> {
			long years = 0;
			LocalDate startDate, endDate;

			try {
			
				startDate = dpStart.getValue();
				endDate = dpEnd.getValue();
				
				//invaid date
				if(startDate.isAfter(endDate)) { 
					Alert alertDates = new Alert(AlertType.ERROR);
					
				
					alertDates.setTitle("Date entry Error");
					alertDates.setHeaderText("Invalid Dates Entered.");
					alertDates.setContentText("The end date must occur after the start date.");
					
			
					alertDates.showAndWait(); 
					
				
					alertDates.setResizable(true);
				}
				
				
				//valid date
				else { 
					
					years = ChronoUnit.YEARS.between(startDate, endDate);
					
					
					if(years < 0) {
						years = 0;
//						Alert alertDate = new Alert(AlertType.WARNING);
//						
//						alertDate.setTitle("Invalid date input");
//						
					}
					
					txtfInvTerm.setText(years + "");
					
				
					termDialogStage.close();
					
				}
			}
			catch(NullPointerException npe) {
				Alert alertEmpty = new Alert(AlertType.ERROR);
			
				alertEmpty.setTitle("Missing dates");
				alertEmpty.setHeaderText("No dates selected.");
				alertEmpty.setContentText("Dates cannot be empty.");
				
				
				alertEmpty.showAndWait(); 
				
				alertEmpty.setResizable(true);
			}
		}); 
		
		
		Scene s = new Scene(vbDialogMain); //take in main container of dialog
		
		termDialogStage.setScene(s);
		
		termDialogStage.show();
			
	}//showTermDialog()
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		//User interface construction.
		//Set the title.
		primaryStage.setTitle("Interest Calculator v1.0");
		
		//Add an appropriate icon.
		try {
			primaryStage.getIcons().add(new Image("./Assets/ledger1.png"));
		}
		
		catch(IllegalArgumentException oopsie) {
			System.out.println("Image for icon is not in your project assets folder.");
			System.out.println(oopsie.getStackTrace());
		}
		
		//Set the width and height
		primaryStage.setWidth(700);
		primaryStage.setHeight(500);
		
		//Create a layout.
		VBox vbMain = new VBox(); //Main container.
		vbMain.setPadding(new Insets(10));
		vbMain.setSpacing(10);
		
		GridPane gp = new GridPane(); 
		//gridpane spacing
		gp.setHgap(10);								
		gp.setVgap(10);
		
		HBox hbButtons = new HBox();
		hbButtons.setSpacing(10);
		
	
		vbMain.getChildren().add(gp);
		
		
		gp.add(lblCapital, 0, 0);
		gp.add(txtfCapital, 1, 0);
		
		gp.add(lblInterestRate, 0, 1);
		gp.add(txtfInterestRate, 1, 1);
		
		gp.add(lblInvTerm, 0, 2);
		gp.add(txtfInvTerm, 1, 2);
		gp.add(btnChooseDates, 2, 2);
		
		gp.add(chkSimple, 1, 3);
		gp.add(chkCompound, 1, 4);
		
		vbMain.getChildren().add(txtMain);
		
		hbButtons.getChildren().addAll(btnQuit, btnCalculate);
		
		vbMain.getChildren().add(hbButtons);
		hbButtons.setAlignment(Pos.BASELINE_RIGHT);
		
		txtMain.minHeightProperty().bind(primaryStage.heightProperty().divide(2));
		
	
		Scene s = new Scene(vbMain);
		
	
		primaryStage.setScene(s);
		
		s.getStylesheets().add("application.css");
	
		primaryStage.show();
		
	}//start()

	public static void main(String[] args) {
		//Launch the application.
		launch(args);
	
	}//main()

}//class