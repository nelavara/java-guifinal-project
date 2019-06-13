import java.util.Date;

public class Salaried extends employee //salaried class extended from employee class
{
	public Salaried(String lg, double sal, String realname,byte[] pass)  //1st constructor is for adding an employee.
	{
		super(lg,sal,realname,pass);
		
	}
	public Salaried (int id, String lg, double sal, Date dte, String nme, byte[] pass) //2nd constructor is used to add employees back from file
	 {
		 super(id,lg,sal,dte,nme,pass);
	 }
	
	
	public double getPay (double sal, double hourly)  //Calculates a person pay. Hourly variable is included but not used here.
	{
		sal = sal/24;
		return sal;
	}

}
