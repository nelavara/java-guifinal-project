import java.util.Date;
public class Hourly extends employee  //extends employee class, has two constructors.
{
	public Hourly(String lg, double sal, String realname, byte[]pass)  //for adding a regular employee
	{
		super(lg, sal, realname,pass);
	}
	
	 public Hourly (int id, String lg, double sal, Date dte, String nme,byte[]pass) //this is needed when writing in from a file.
	 {
		 super(id,lg,sal,dte,nme,pass);
	 }
	
	public double getPay(double sal, double hours) //calculates a person's pay and then returns it to the payroll class.
	{	
		
		sal = hours * sal;
		return sal;
	}

}
