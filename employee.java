
import java.util.*;
public abstract class employee {  //Employee class
	protected String logname;  //Employee login
	protected double salary;  //Employee Salary
	protected String empname;  //Employee name
    protected final Date date;  //Date of hire
    protected final int empID;  //Employee ID
    protected byte passArray [];  //Byte array, password converted to it after encryption.
    public static int nextId;  //Increments employee ID, after one is added

    
    public employee (String lg, double sal, String realname, byte pass[]) //constructor, used addEmployee and newEmployee functions in class payroll
    {
    	date = new Date();
    	empID = nextId++;
    	empname = realname;
    	salary = sal;
    	logname = lg;
    	passArray = pass;

    }
    
    public employee (int id, String lg, double sal, Date dte, String nme, byte pass[])  //constructor used by payroll constructor to populate arraylist from a file
    {
    	date = dte;
    	empID = id = nextId++;
    	logname = lg;
    	if (sal > 50 && sal <=10000)
    	{
    		sal = setSalary(sal);
    	}
    	salary = sal;
    	empname = nme;
    	passArray = pass;

    
    }
 

    public String getLogname ()  //gets the login name, used by function dologin
    {
    	return logname;
    }
    
    public int getID ()  //gets the id, needed by function dologin
    {
    	return empID;
    }
    
    public void setNewName (String nn) //sets a new name, needed by function changeEmp
    {
    	empname = nn;
    }
    
    public void setNewSalary (double sal)  //sets a new salary, needed by function changeEmp
    {
    	salary = setSalary(sal);
    }
    
   public double setSalary(double sal)  //sets the salary, takes the hourly rate entered by user and converts to estimated yearly salary
   {                                    //Used if the user enters in an unreasonably low number for yearly salary instead of hourly rate.
	   double fsalary;
	   fsalary = sal*40;
	   fsalary = fsalary*52;
	   return fsalary;   
   
   }
   
   public byte[] getPassword() //Password getter.
   {
	   return passArray;
   }
   
   public String toString()  //Tostring function which outputs data of ArrayList to the screen
   {
	   StringBuilder passline = new StringBuilder();
	   for (byte exists: passArray)
	   {
		   passline.append(String.format("%02X", exists));
	   }
	   if (salary <= 50)  //This was done for better formatting, it displays the projected salary an hourly employee would make over 1 year.
	   {
		   double yearly_Salary;
		   yearly_Salary = setSalary(salary);
		   String employeeprint = String.format("Employee ID: %05d\tEmployee Logon Name: %s\tEmployee Salary: %.2f\tDate of Hire: %tD\tEmployee Name: %s\tPassword Protected: %b",empID,logname,yearly_Salary,date,empname,passline);
		   return employeeprint;
	   }
	   else  //Displays regular salaried employee data.
	   {
		  
		   String employeeprint = String.format("Employee ID: %05d\tEmployee Logon Name: %s\tEmployee Salary: %.2f\tDate of Hire: %tD\tEmployee Name: %s\tPassword Protected: %b" ,empID,logname,salary,date,empname,passline);
		   return employeeprint;
	   }	  
	   
   }
   
   public String toStringff()  //To string function which outputs data to the file.
   {
	   StringBuilder passline = new StringBuilder();
	   for (byte exists: passArray)
	   {
		   passline.append(String.format("%02X", exists));
	   }
	   String employeeptf = String.format("%d,%s,%.2f,%tD,%s,%s,\r\n",empID,logname,salary,date,empname,passline);
	   return employeeptf;
   }
   
   public abstract double getPay(double sal, double hourly);  //abstract getPay function
 
   
}



