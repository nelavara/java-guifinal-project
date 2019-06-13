import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.security.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.*;
import javafx.event.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.collections.ObservableList;

public class payroll extends Application 
{
	ArrayList<employee> obj = new ArrayList<employee>();  //Creation of array list to hold employee objects/
	ArrayList<employee> tobj = new ArrayList<employee>();  //Array list for terminated employees.
	Text title = new Text();
	Text line0 = new Text();
	Text line1 = new Text();
	Text line2 = new Text();
	Text line3 = new Text();
	Text line4 = new Text();
	Text line5 = new Text();
	VBox inital = new VBox();
	Button option1 = new Button();
	Button option2 = new Button();
	String fileName = "test123";
	HBox buttons = new HBox();
	boolean passmatch=false;
	boolean firstRun = true;
	boolean bossLoggedin = false;
	boolean logFailure = true;
	boolean removeEmp = false;
	String Empname;
	String EmplogName;
	Double Empsalary;
	String pfn = "payroll.txt";
	String ftwt = "employees.txt";
    double screenController = 0;
    int SOH = 0;
    int empToEdit=-1;
    int whoIsLogged = -1;
	
	
	public void setInitalDisplay()  //set The inital display
	{
		inital.setSpacing(5);
		title = new Text("Employee Database \t\tAuthor: Evan Perry");
		line0 = new Text("Directions:");
		line1 = new Text("Press Open to open an existing database file");
		line2 = new Text("Press New to create a new employee database");
		buttons.setSpacing(5);		
		option1 = new Button("Open");
		option2 = new Button ("New");
		buttons.getChildren().addAll(option1,option2);
	
	}
	
	public void askForFileName(Stage st)
	{
		screenController = 7;
		buildGui(st,screenController);
		
	}
	
	public void addUser (Stage st)
	{
		if (firstRun == true) 
		{
			screenController =1;
			buildGui(st,screenController);
			
		}		
		else
		{
			screenController =2;
			buildGui(st,screenController);			
		}
		
	}
	
	public void populateEmployee(String logname, double salary, String name, byte [] encrypted)  //adds new entry to the array
	{
		System.out.println("OK");
		if (SOH == 0)
		{
			obj.add(new Salaried(logname, salary, name, encrypted));			
		}
		else if (SOH == 1)
		{
			obj.add(new Hourly(logname, salary, name, encrypted));
		}
		
		System.out.println("Added to array successfully");
	}
	
	public byte[] getNewPassword (String pass1, String pass2, Stage st)  //compares the passwords
	{
		byte [] password1;
		byte [] password2;
		
		try
		{
			MessageDigest enc = MessageDigest.getInstance("SHA-256");
			enc.update(pass1.getBytes()); //password encrypted
			password1 = (enc.digest());  //password converted to bytes
			enc.update(pass2.getBytes()); //repeated for second entry.
			password2 = (enc.digest());
			
			
		}catch(Exception ex) {throw new RuntimeException(ex);}
		if (Arrays.equals(password1, password2))
		{
			passmatch=true;
			firstRun = false;
			populateEmployee(EmplogName, Empsalary, Empname, password1);
			if (bossLoggedin == true)
			{
				displayMenu(st);				
			}
			else if(bossLoggedin == false)
			{
				dologon(st);
			}
			return password1;
		}
		else//two byte arrays compared after encryption.
		{
			screenController = 3;
			buildGui(st, screenController);			
		}
		return password1;
	}
	
	private void dologon (Stage st)  //allows for login
	{
		screenController = 4;
		buildGui(st, screenController);		
	}
	
	public void logonfailed(Stage st)  //displayed if logon failed.
	{
		screenController =5;
		buildGui(st, screenController);		
		
	}
	public boolean isNumber(String test)  //checks if its is a number
	{
		try
		{
			Empsalary = Double.parseDouble(test);
		}catch(NumberFormatException e) {return false;}
		return true;
	}
	
	private boolean passwordchecker (String pass, int y)  //checks password for correction.
	{
		boolean tester = false;
		byte [] compare;
		byte [] compare1;
		try
		{
			MessageDigest enc = MessageDigest.getInstance("SHA-256");
			enc.update(pass.getBytes());
			compare = (enc.digest());			
		}catch(Exception ex) {throw new RuntimeException(ex);}
		compare1 = obj.get(y).getPassword();
		if (Arrays.equals(compare, compare1))
		{
			tester = true;
			return tester;
		}
		else
		{
			return tester;			
		}
	}
	public void changeEmp(Stage st)  //calls proper screen to change Employee information
	{
		screenController =  9;
		buildGui(st,screenController);
	}
	
	public void displayMenu (Stage st) //display's menu screen for the boss.
	{
		screenController = 6;
		buildGui(st, screenController);
		
	}
	
	
	public void start (Stage st)  //Inital start version.
	{
		screenController = 0;
		buildGui(st, screenController);
		
	}
	
	public void listEmployees (Stage st)  //Displays list of employees screen.
	{
		screenController = 8;
		buildGui (st, screenController);
	}
	
	public void checkEmp(Stage st, int idEntered)  //check if the employee id entered exists.
	{
		
		for (int i = 0; i<obj.size(); i++)
		{
			if (idEntered==(obj.get(i).getID()))
			{
				empToEdit=idEntered;
				System.out.println("found");
				//If emp found, call function buildGui to display choices.
				break;
			}
		}
		if (empToEdit == -1)
		{
			screenController = 14;
			buildGui(st,screenController);
		}
		else if(removeEmp == true && idEntered != 0)
		{
			obj.remove(idEntered);
			removeEmp = false;
			screenController = 15;
			buildGui(st,screenController);
		}
		else if (removeEmp == true && idEntered == 0)
		{
			removeEmp = false;
			screenController = 16;
			buildGui(st,screenController);
		}
		else
		{
			screenController = 10;
			buildGui(st,screenController);
			
		}
	}
	public void changeEmpName(String name, int id)  //changes the employee name.
	{
		obj.get(id).setNewName(name);
	}
	public void changeEmpSalary(double newSalary, int id)  //change the employees salary
	{
		int sid = obj.get(id).getID();
		String lg = obj.get(id).getLogname();
		Date dte = obj.get(id).date;
		String nme = obj.get(id).empname;
		byte [] holder = obj.get(id).getPassword();
		obj.remove(id);
		if(SOH == 0)
		{
			obj.add(new Salaried(sid,lg,Empsalary,dte,nme,holder));
		}
		else if(SOH == 1)
		{
			obj.add(new Hourly(sid,lg,Empsalary,dte,nme,holder));
		}
	}
	
	public void termEmp(Stage st)  //terminate the employee.
	{
		screenController = 13;
		buildGui(st, screenController);
	}
	
	public void payEmp(Stage st)  //pay all employees.
	{
		screenController = 17;
		buildGui(st, screenController);
	}
	
	public void payTime(double hoursWorked, Stage st)  //helper to pay all employees
	{
		double sal;
		double hours;
		Date date = new Date();
		File pay = new File(pfn);
		try
		{
			PrintWriter printPay = new PrintWriter(pay);
				hours = hoursWorked;
				System.out.print("Payroll Report:\t\t\tDate: ");
				printPay.print("Payroll Report:\t\t\tDate: ");			
				System.out.println(date);
				printPay.println(date);
				VBox nf = new VBox();
				final ObservableList<String> emps = FXCollections.observableArrayList();
				for (int y = 0; y< obj.size(); y++)
				{
						sal = obj.get(y).salary;
						sal=obj.get(y).getPay(sal, hours);
						String salresults = String.format("Employee ID: %05d\tEmployee name: %s\tEmployee Pay: %.2f", obj.get(y).getID(), obj.get(y).empname,sal);
						emps.add(String.format("Employee ID: %05d\tEmployee name: %s\tEmployee Pay: %.2f", obj.get(y).getID(), obj.get(y).empname,sal));
						System.out.println(salresults);
						printPay.println(salresults);
				}	
				final ListView <String> allEmps = new ListView<String>(emps);
				allEmps.setPrefSize(2000,2000);
				allEmps.setEditable(false);
				ScrollPane list = new ScrollPane();
				list.setVbarPolicy(ScrollBarPolicy.ALWAYS);
				list.setHbarPolicy(ScrollBarPolicy.ALWAYS);
				list.setContent(allEmps);
				list.setPrefSize(800,600);
				option1 = new Button ("Return to MENU");
				option1.setOnAction(new EventHandler<ActionEvent>() {
					public void handle (ActionEvent e) {
						displayMenu(st);
					}
				});
				nf.getChildren().addAll(list,option1);
				Scene listS = new Scene(nf, 800,400);
				st.setTitle("List of Employees");
				st.setScene(listS);
				st.show();
			printPay.close();
		} catch (IOException e) {e.printStackTrace(); System.exit(0);}	
	}
	
	public void logOut(Stage st)  //logs out current user.
	{
		bossLoggedin = false;
		writeToFile(st);
		
	}
	
	public void empLogOut(Stage st)  //logs out non boss user.
	{
		bossLoggedin = false;
		screenController = 4;
		buildGui(st, screenController);
	}
	
	public void writeToFile(Stage st)   //writes the currently contents of the arraylist to a text file with the name specified by the user.
	{
		
		screenController = 20;
		buildGui(st, screenController);
		try 
		{

			File wtf = new File (ftwt);
			FileOutputStream writer = new FileOutputStream(wtf);
			ObjectOutputStream oWriter = new ObjectOutputStream (writer);
			for (int y = 0; y<obj.size(); ++y)
			{
				oWriter.flush();
				employee temp = obj.get(y);
				oWriter.writeObject(temp.toStringff());
			}
			writer.flush();	
			writer.close();
		}
		catch (IOException e) {e.printStackTrace(); System.exit(0);}		
	}
	
	public void populateArray(Stage st, String filename)  //Writes data in from a file to the array.
	{
		String temp;
		String lg;
		String nme;
		Date dte;
		double sal;
		int id = 0;
		boolean fileExists;
		try {  //Try block to open file and begin reading from it.
			FileInputStream fis = new FileInputStream(filename);  //Opens file and opens scanner to begin reading.
			Scanner sc = new Scanner (fis);
			while (sc.hasNext())  //Reads file line by line.
			{
				temp = sc.nextLine();    //Line read into temp, then broken into smaller pieces to be placed into correct data members.
				String [] divided = temp.split(",");
				String part1 = divided[0];
				char[] arry =part1.toCharArray();
				for (int y = 0; y<part1.length(); y++)
				{
					if (Character.isDigit(arry[y]))
					{
						id = Character.getNumericValue(arry[y]);
					}
				}
				String part2 = divided[1];
				lg=part2;
				String part3 = divided[2];
				sal=Double.parseDouble(part3);
				String part4 = divided[3];
				DateFormat test = new SimpleDateFormat ("MM/dd/yy", Locale.ENGLISH);
				try {
					dte = test.parse(part4);
				} catch (ParseException e) {
					dte=new Date();
				}
				String part5 = divided[4];
				nme = part5;
				String part6 = divided[5];
				byte [] passF = convertToByte(part6);
				if (sal <=50)  //Enforces if salary is under 50 it goes to hourly, otherwise it goes to salaried.
				{
					obj.add(new Hourly (id,lg,sal,dte,nme,passF));
				}
				else
				{
					obj.add(new Salaried (id, lg, sal, dte, nme,passF));	
					
				}
			}
			fileExists = true;  //sets the boolean to true that the file exists.
			fis.close();  //closes the file
			sc.close(); // closes the scanner to the file.
		}
		catch (FileNotFoundException e)  //catch blocks to deal with unexpected errors.
		{
			System.out.println("File does not exist!");
			fileExists = false;
		}
		catch (IOException e)
		{
			fileExists = false;
		}
		if (fileExists == false)
		{
			screenController =1;
			buildGui(st, screenController);
			
		}
	}
	
	public static byte[] convertToByte (String hexrep)  //converts hex from file to byte code for password comparison later.
	{
		int length = hexrep.length();
		byte [] passF = new byte[length/2];
		for (int i = 0; i<length; i+=2)
		{
			passF[i/2] = (byte) ((Character.digit(hexrep.charAt(i), 16)<<4)
					+ Character.digit(hexrep.charAt(i+1), 16));
		}
		return passF;
	}
	
	public void buildGui(Stage st, double controller)  //Displays the gui based on which screen is called.
	{
		if (controller == 0)  //inital display
		{
			setInitalDisplay();
			inital.getChildren().addAll(title,line0,line1,line2,buttons);
			Scene sn = new Scene (inital, 380,160);		
			st.setTitle("Employee Database Assignment 5");
			Font f1 = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 18);
			Font f2 = Font.font("Times New Roman", FontWeight.NORMAL, FontPosture.REGULAR, 16);
			title.setFont(f1);
			line0.setFont(f1);
			line1.setFont(f2);
			line2.setFont(f2);
			st.setScene(sn);
			st.show();
			option1.setOnAction(new EventHandler<ActionEvent>(){
				public void handle (ActionEvent event) {
					askForFileName(st);
				}
			});
			option2.setOnAction(new EventHandler<ActionEvent>(){
				public void handle (ActionEvent event) {
					addUser(st);
				}
			});
			
		}
		else if (controller == 1) //boss data entry
		{
			Pane ab = new Pane();
			VBox abss = new VBox();
			option1 = new Button("Submit");
			Label nameField = new Label ("Enter your name: ");
			TextField name = new TextField();
			Label userName = new Label ("Enter desired username: ");
			TextField un = new TextField();
			Label sal = new Label ("Enter desired salary: ");
			TextField sl = new TextField();
			Label pass = new Label ("Enter desired password: ");
			PasswordField pwrd = new PasswordField();
			Label pass2 = new Label ("Reenter your desired password: ");
			PasswordField pwrd2 = new PasswordField();
			abss.getChildren().addAll(nameField,name,userName,un,sal,sl,pass,pwrd,pass2,pwrd2,option1);
			ab.getChildren().addAll(abss);
			Scene firstUser = new Scene (ab, 225,250);
			st.setTitle("Enter Your Information");
			st.setScene(firstUser);
			st.show();
			option1.setOnAction(new EventHandler<ActionEvent>() {
				public void handle (ActionEvent submitHandler) {
					if((name.getText()!= null && !name.getText().isEmpty() && un.getText()!= null && !un.getText().isEmpty() && sl.getText()!= null && !sl.getText().isEmpty() && pwrd.getText()!= null && !pwrd.getText().isEmpty()&& pwrd2.getText()!= null && !pwrd.getText().isEmpty() && isNumber(sl.getText())))
					{
						String secret = pwrd.getText();
						String secret2 = pwrd2.getText();
						Empname = name.getText();
						EmplogName = un.getText();
						Empsalary = Double.parseDouble(sl.getText());
						getNewPassword(secret,secret2,st);				
					}
					else
					{
						screenController = 19;
						buildGui(st, screenController);
					}
				}
			});
		}
		else if (controller == 2)  //employee data entry
		{
			Pane ab = new Pane();
			VBox abss = new VBox();
			option1 = new Button("Submit");
			Label nameField = new Label ("Enter employee name: ");
			TextField name = new TextField();
			Label userName = new Label ("Enter employee username: ");
			TextField un = new TextField();
			Label sal = new Label ("Enter employee salary: ");
			TextField sl = new TextField();
			final ToggleGroup salOrHr = new ToggleGroup();
			RadioButton sallie = new RadioButton ("Select for Salary Employee");
			sallie.setToggleGroup(salOrHr);
			RadioButton hrr = new RadioButton ("Select for Hourly Employee");
			hrr.setToggleGroup(salOrHr);	
			
			Label pass = new Label ("Enter employee password: ");
			PasswordField pwrd = new PasswordField();
			Label pass2 = new Label ("Reenter employee password: ");
			PasswordField pwrd2 = new PasswordField();
			abss.getChildren().addAll(nameField,name,userName,un,sallie,hrr,sal,sl,pass,pwrd,pass2,pwrd2,option1);
			ab.getChildren().addAll(abss);
			Scene newEmp = new Scene (ab, 225,275);
			st.setTitle("Enter Employee Info");
			st.setScene(newEmp);
			st.show();
			option1.setOnAction(new EventHandler<ActionEvent>() {
				public void handle (ActionEvent submitHandler) {
					if((name.getText()!= null && !name.getText().isEmpty() && un.getText()!= null && !un.getText().isEmpty() && sl.getText()!= null && !sl.getText().isEmpty() && pwrd.getText()!= null && !pwrd.getText().isEmpty()&& pwrd2.getText()!= null && !pwrd.getText().isEmpty() && isNumber(sl.getText())))
					{
						if (sallie.isSelected())
						{
							SOH = 0;
							System.out.println(SOH);
						}
						else if (hrr.isSelected())
						{
							SOH = 1;
						}						
						
						String secret = pwrd.getText();
						String secret2 = pwrd2.getText();
						Empname = name.getText();
						EmplogName = un.getText();
						Empsalary = Double.parseDouble(sl.getText());
						
						getNewPassword(secret,secret2,st);				
					}
					else
					{
						screenController = 19;
						buildGui(st,screenController);
					}
				}
			});
		}
		else if (controller == 3) //passwords do not match
		{
			VBox ipass = new VBox();
	        Pane ipa = new Pane();
			title = new Text ("Passwords Do not match");
			line1 = new Text ("Try again");
			option1 = new Button ("Okay");
			Label password = new Label ("Enter your password: ");
			PasswordField pwrd = new PasswordField();
			Label password0 = new Label ("Reneter your password:");
			PasswordField pwrd2 = new PasswordField();
			ipass.getChildren().addAll(title,line1,password,pwrd,password0,pwrd2,option1);
			ipa.getChildren().addAll(ipass);
			Scene wrongpass = new Scene(ipa, 225,250);
			st.setTitle("Password MisMatch");
			st.setScene(wrongpass);
			passmatch=false;
			st.show();
			option1.setOnAction(new EventHandler<ActionEvent>(){
				public void handle (ActionEvent handler) {
					if((pwrd.getText()!= null && !pwrd.getText().isEmpty() && pwrd2.getText()!= null && !pwrd2.getText().isEmpty()))
					{
						getNewPassword(pwrd.getText(),pwrd2.getText(),st);
				}
			}});
			
		}
		else if (controller == 4 ) //displays login screen
		{
			VBox logon = new VBox();
			logon.setSpacing (10);
			logon.setAlignment(Pos.BASELINE_CENTER);
			Pane log = new Pane();
			title = new Text("Log On Required!");
			Label username = new Label ("Enter your username: ");
			TextField name = new TextField();
			Label password = new Label ("Enter your password: ");
			PasswordField pwrd = new PasswordField();
			option1 = new Button ("Submit");
			logon.getChildren().addAll(title,username,name,password,pwrd,option1);
			log.getChildren().addAll(logon);
			Scene logscreen = new Scene (log, 175,225);
			st.setTitle("Logon");
			st.setScene(logscreen);
			st.show();
			option1.setOnAction(new EventHandler<ActionEvent>() {
				public void handle (ActionEvent handler) {
					if((pwrd.getText()!=null && !pwrd.getText().isEmpty() && name.getText()!= null && !name.getText().isEmpty()))
					{
						for (int y = 0; y<obj.size(); y++)
						{
			
							String tester = obj.get(y).getLogname();
							if(name.getText().equals(tester))
							{
								if(true==passwordchecker(pwrd.getText(),y))
								{
									if(y== 0)
									{
										bossLoggedin = true;
										displayMenu(st);  //displays the boss's menu
										break;
									}
									else
									{
										whoIsLogged = obj.get(y).getID();
										bossLoggedin = false;
										screenController = 21;
										buildGui(st,screenController);
										break;
										//display Employee menu
									}
								}
								else
								{
									logonfailed(st);							
								}
							}
							else if(!name.getText().equals(tester)&& y == obj.size()-1)
							{
								logonfailed(st);
							}
						}
					}
					else
					{
						logonfailed(st);
					}
				}
			});
			
		}
		else if (controller == 5) //if login fails
		{
			VBox logfail = new VBox();
			logfail.setSpacing(10);
			logfail.setAlignment(Pos.BASELINE_CENTER);
			Pane fail = new Pane();
			title = new Text ("LOG ON FAILURE");
			line1 = new Text ("Click Okay to try again.");
			option1 =  new Button ("OKay");
			option2 = new Button ("Quit");
			logfail.getChildren().addAll(title,line1,option1,option2);
			fail.getChildren().addAll(logfail);
			Scene failure = new Scene(fail, 175,225);
			st.setTitle("Log on failed");
			st.setScene(failure);
			st.show();	
			option1.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent handler) {
					dologon(st);
				}
			});
			option2.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent handler) {
					System.exit(0);
				}
			});
		}
		else if (controller == 6) //displays the boss's menu
		{
			VBox menu = new VBox();
			Pane menuDisp = new Pane();
			menu.setSpacing (10);
			menu.setAlignment(Pos.BASELINE_CENTER);
			title = new Text ("Menu");
			option1 = new Button ("Add Employee");
			option2 = new Button ("List Employees");
			Button option0 = new Button ("Change Employee Data");
			Button option3 = new Button ("Terminate Employee");
			Button option4 = new Button ("Pay Employees");
			Button option5 = new Button ("Logout");
			Button option6 = new Button ("Quit");
			menu.getChildren().addAll(title,option1,option2,option0,option3,option4,option5,option6);
			menuDisp.getChildren().addAll(menu);
			Scene md = new Scene (menuDisp, 150,300);
			st.setTitle("Menu");
			st.setScene(md);
			st.show();
			option1.setOnAction(new EventHandler<ActionEvent>() {
				public void handle (ActionEvent e) {
					firstRun = false;
					addUser(st);
				}
			});
			option2.setOnAction(new EventHandler<ActionEvent>() {
				public void handle (ActionEvent e) {
					//list employees
					listEmployees(st);
				}
			});
			option0.setOnAction(new EventHandler<ActionEvent>() {
				public void handle (ActionEvent e) {
					changeEmp(st);
					//Change Employee Data
				}
			});
			option3.setOnAction(new EventHandler<ActionEvent>() {
				public void handle (ActionEvent e) {
					termEmp(st);
					//Terminate Employee
				}
			});
			option4.setOnAction(new EventHandler<ActionEvent>() {
				public void handle (ActionEvent e) {
					payEmp(st);
					//PayEmployees
				}
			});
			option5.setOnAction(new EventHandler<ActionEvent>() {
				public void handle (ActionEvent e) {
					logOut(st);
					//Logout
					
				}
			});
			option6.setOnAction(new EventHandler<ActionEvent>(){
				public void handle (ActionEvent e) {
					System.exit(0);
					//Quit
					
				}
			});
		}
		else if (controller == 7) //asks for file name from the user
		{
			Pane affn = new Pane();
			HBox nf = new HBox ();
			option1 = new Button("Okay");
			Label nameField = new Label("Enter a File Name Please:");
			TextField nff = new TextField();
			nf.getChildren().addAll(nameField,nff,option1);
			affn.getChildren().addAll(nf);
			Scene fileNameReq = new Scene (affn, 380,50);
			st.setTitle("File Name Request");
			Font f1 = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR,14);
			line0.setFont(f1);
			st.setScene(fileNameReq);
			st.show();
			option1.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent okayhandler) {
					if((nff.getText() != null && !nff.getText().isEmpty()))
					{
						fileName=nff.getText();
						populateArray(st,fileName);
						screenController = 4;
						ftwt = fileName;
						buildGui(st, screenController);
					}
				}
					});
		}
		else if (controller == 8)  //this will display a list of all Employees that are currently employeed.
		{
			VBox nf = new VBox();
			final ObservableList<String> emps = FXCollections.observableArrayList();
			for (int i = 0; i<obj.size(); i++)
			{
				emps.add(obj.get(i).toString());
			}
			final ListView <String> allEmps = new ListView<String>(emps);
			allEmps.setPrefSize(2000,2000);
			allEmps.setEditable(false);
			ScrollPane list = new ScrollPane();
			list.setVbarPolicy(ScrollBarPolicy.ALWAYS);
			list.setHbarPolicy(ScrollBarPolicy.ALWAYS);
			list.setContent(allEmps);
			list.setPrefSize(800,600);
			option1 = new Button ("Return to MENU");
			option1.setOnAction(new EventHandler<ActionEvent>() {
				public void handle (ActionEvent e) {
					displayMenu(st);
				}
			});
			nf.getChildren().addAll(list,option1);
			Scene listS = new Scene(nf, 800,400);
			st.setTitle("List of Employees");
			st.setScene(listS);
			st.show();
			
		}
		else if (controller == 9)  //displays screen askiing for Id of employee to be edited.
		{
			Pane nff = new Pane();
			VBox nf = new VBox();
			nf.setSpacing(10);
			Label directions= new Label("Please enter the Employee ID of the employee you wish to edit.");
			TextField edit = new TextField();
			option1 = new Button("Okay");
			nf.getChildren().addAll(directions,edit,option1);
			nff.getChildren().addAll(nf);
			Scene empEdit = new Scene (nff, 500,300);
			st.setTitle("Edit Employee");
			st.setScene(empEdit);
			st.show();
			option1.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent okayhandler) {
					checkEmp(st,Integer.parseInt(edit.getText()));
				}
			});
		}
		else if (controller == 10)  //Gives user a choice to change the employee's name or salary
		{
			Pane nff = new Pane();
			VBox nf = new VBox();
			nf.setSpacing(10);
			option1 = new Button ("Change Employee Name");
			option2 = new Button ("Change Employee Salary");
			nf.getChildren().addAll(option1,option2);
			nff.getChildren().addAll(nf);
			Scene changeEmp = new Scene (nff, 200,200);
			st.setTitle("Edit Employee");
			st.setScene(changeEmp);
			st.show();
			option1.setOnAction(new EventHandler<ActionEvent>() {
				public void handle (ActionEvent changeName) {
					screenController = 11;
					buildGui(st, screenController);
					// display change name, then change the employee's name
				}
			});
			option2.setOnAction(new EventHandler<ActionEvent>() {
				public void handle (ActionEvent changeSalary) {
					screenController = 12;
					buildGui(st, screenController);
					//change salary
				}
			});
		}
		else if (controller == 11)  //Allows user to enter new name of employee.
		{
			Pane nff = new Pane();
			VBox nf = new VBox();
			nf.setSpacing(10);
			Label newName = new Label ("Please enter the new name for the employee");
			TextField edit = new TextField();
			option1 = new Button ("Submit");
			nf.getChildren().addAll(newName,edit,option1);
			nff.getChildren().addAll(nf);
			Scene changeName = new Scene(nff, 300,300);
			st.setTitle("Edit Employee Name");
			st.setScene(changeName);
			st.show();
			option1.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent submitHandler) {
					if(edit.getText()!= null && !edit.getText().isEmpty())
					{
						Empname=edit.getText();	
						changeEmpName(Empname,empToEdit);
						empToEdit = -1;
						screenController = 6;
						buildGui(st, screenController);
					}
					
				}
			});
		}
		else if (controller == 12)  //Allows for employee salary to be changed/
		{
			Pane nff = new Pane();
			VBox nf = new VBox();
			nf.setSpacing(10);
			final ToggleGroup salOrHr = new ToggleGroup();
			RadioButton sallie = new RadioButton ("Select for Salary Employee");
			sallie.setToggleGroup(salOrHr);
			RadioButton hrr = new RadioButton ("Select for Hourly Employee");
			hrr.setToggleGroup(salOrHr);	
			Label pay = new Label("Please enter the Employee's new salary");
			TextField changeSal = new TextField();
			option1 = new Button("Submit");
			nf.getChildren().addAll(sallie,hrr,pay,changeSal,option1);
			nff.getChildren().addAll(nf);
			Scene changeSalary = new Scene(nff, 300,300);
			st.setTitle("Edit employee Salary");
			st.setScene(changeSalary);
			st.show();
			option1.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent submitHandler) {
					if(changeSal.getText()!=null && !changeSal.getText().isEmpty())
					{
						if(sallie.isSelected())
						{
							SOH = 0;
						}
						else if (hrr.isSelected())
						{
							SOH = 1;
						}
						Empsalary = Double.parseDouble(changeSal.getText());
						changeEmpSalary(Empsalary,empToEdit);
						empToEdit = -1;
						screenController = 6;
						buildGui(st,screenController);
					}
				}
			});
		}
		else if (controller == 13)  //Asks for Employee ID in order to terminate an employee
		{
			removeEmp = true;
			Pane nff = new Pane();
			VBox nf = new VBox();
			nf.setSpacing(10);
			Label term = new Label("Please enter employee ID to terminate.");
			TextField termed = new TextField();
			option1 = new Button("Submit");
			nf.getChildren().addAll(term,termed,option1);
			nff.getChildren().addAll(nf);
			Scene termEmp = new Scene(nff, 300,300);
			st.setTitle("Terminate Employee");
			st.setScene(termEmp);
			st.show();
			option1.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent okayhandler) {
					checkEmp(st,Integer.parseInt(termed.getText()));
				}
			});
		}
		else if (controller == 14)  //Employee not found error.
		{
			Pane nff = new Pane();
			VBox nf = new VBox();
			nf.setSpacing(10);
			title = new Text("Employee Not Found");
			line1 = new Text("Try again later");
			option1 = new Button("Okay");
			nf.getChildren().addAll(title,line1,option1);
			nff.getChildren().addAll(nf);
			Scene failed = new Scene(nff, 300,300);
			st.setTitle("Not Found");
			st.setScene(failed);
			st.show();
			option1.setOnAction(new EventHandler <ActionEvent>(){
				public void handle(ActionEvent okayhandler) {
					screenController = 6;
					buildGui(st,screenController);
				}
			});
			
		}
		else if (controller == 15)  //Employee terminated screen
		{
			Pane nff = new Pane();
			VBox nf = new VBox();
			nf.setSpacing(10);
			title = new Text ("Employee Terminated.");
			option1 = new Button("Okay");
			nf.getChildren().addAll(title,option1);
			nff.getChildren().addAll(nf);
			Scene removed = new Scene (nff, 300,300);
			st.setTitle("Employee Removed");
			st.setScene(removed);
			st.show();
			option1.setOnAction(new EventHandler <ActionEvent>() {
				public void handle(ActionEvent okayhandler) {
					screenController = 6;
					buildGui(st, screenController); 
				}
			});
		}
		else if (controller == 16)  //Prevents the boss from terminating himself
		{
			Pane nff = new Pane();
			VBox nf = new VBox();
			nf.setSpacing(10);
			title = new Text ("The Boss can not be Terminated!");
			option1 = new Button("Okay");
			nf.getChildren().addAll(title,option1);
			nff.getChildren().addAll(nf);
			Scene error = new Scene(nff, 300,300);
			st.setTitle("Error");
			st.setScene(error);
			st.show();
			option1.setOnAction(new EventHandler <ActionEvent>() {
				public void handle(ActionEvent okayhandler) {
					screenController = 6;
					buildGui(st, screenController);
				}
			});
			
		}
		else if (controller == 17)  //Asks for number of hours worked to help calculate total pay for employees.
		{
			Pane nff = new Pane();
			VBox nf = new VBox();
			nf.setSpacing(10);
			Label pay = new Label("Please type the number of hours worked by all Employees.");
			TextField payEmps = new TextField();
			option1 = new Button("Submit");
			nf.getChildren().addAll(pay,payEmps,option1);
			nff.getChildren().addAll(nf);
			Scene payTime = new Scene(nff, 400,300);
			st.setTitle("Hours worked");
			st.setScene(payTime);
			st.show();
			option1.setOnAction(new EventHandler <ActionEvent>() {
				public void handle(ActionEvent okayhander) {
					if (isNumber(payEmps.getText()))
					{
						payTime(Double.parseDouble(payEmps.getText()),st);
					}
					else if(!isNumber(payEmps.getText()))
					{
						screenController = 18;
						buildGui(st,screenController);
					}
				}
			});
		}
		else if (controller == 18) //Invalid data entry file entry
		{
			Pane nff = new Pane();
			VBox nf = new VBox();
			nf.setSpacing(10);
			title = new Text("Invalid Data entry, try again later");
			option1 = new Button ("Okay");
			nf.getChildren().addAll(title,option1);
			nff.getChildren().addAll(nf);
			Scene error = new Scene(nff, 300,300);
			st.setTitle("Error");
			st.setScene(error);
			st.show();
			option1.setOnAction(new EventHandler <ActionEvent>() {
				public void handle(ActionEvent okayhandler) {
					screenController = 6;
					buildGui(st,screenController);
				}
			});
		}
		else if (controller == 19) //Invalid data entry error display
		{
			Pane nff = new Pane();
			VBox nf = new VBox();
			nf.setSpacing(10);
			title = new Text("Invalid Data entry, try again.");
			option1 = new Button ("Okay");
			nf.getChildren().addAll(title,option1);
			nff.getChildren().addAll(nf);
			Scene error = new Scene(nff, 300,300);
			st.setTitle("Error");
			st.setScene(error);
			st.show();
			option1.setOnAction(new EventHandler <ActionEvent>() {
				public void handle (ActionEvent okayhandler) {
					addUser(st);
				}
			});
		}
		else if (controller == 20)  //Asks for a name for a database then saves it.
		{
			Pane nff = new Pane();
			VBox nf = new VBox();
			nf.setSpacing(10);
			Label fn = new Label ("Please enter the name for your database file");
			TextField fileName = new TextField();
			option1 = new Button ("Submit");
			nf.getChildren().addAll(fn,fileName,option1);
			nff.getChildren().addAll(nf);
			Scene saveFile = new Scene(nff,300,300);
			st.setTitle("Save File");
			st.setScene(saveFile);
			st.show();
			option1.setOnAction(new EventHandler <ActionEvent>() {
				public void handle (ActionEvent submitHandler) {
					if (fileName.getText()!= null && !fileName.getText().isEmpty())
					{
						ftwt = fileName.getText();
						screenController = 4;
						buildGui(st,screenController);
					}
					else
					{
						ftwt = "employees.txt";
						screenController = 4;
						buildGui(st,screenController);
					}
						
				}
			});
		}
		else if (controller == 21)  //Displays an employees information, and the employee can either log out or quit.
		{
			Pane nff = new Pane();
			VBox nf = new VBox();
			nf.setSpacing(10);
			title = new Text("Employee Information: ");
			line1 = new Text(String.format("Employee name: \t%s", obj.get(whoIsLogged).empname));
			line2 = new Text(String.format("Date of Hire: \t%tD",obj.get(whoIsLogged).date));
			line3 = new Text(String.format("Employee Salary: \t%.2f", obj.get(whoIsLogged).salary));
			option1 = new Button("Logout");
			option2 = new Button("Quit");
			nf.getChildren().addAll(title,line1,line2,line3,option1,option2);
			nff.getChildren().addAll(nf);
			Scene displayData = new Scene(nff, 300,300);
			st.setTitle("Employee Information");
			st.setScene(displayData);
			st.show();
			option1.setOnAction(new EventHandler <ActionEvent>() {
				public void handle(ActionEvent button1) {
					empLogOut(st);
				}
			});
			option2.setOnAction(new EventHandler <ActionEvent>() {
				public void handle(ActionEvent button2) {
					obj.remove(whoIsLogged);
					empLogOut(st);
				}
			});
			
			
		}
	}
}



