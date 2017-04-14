package OnlineQuiz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Database {
	static Connection con;
	static int id=1;
	public static boolean connect()
	{
	try {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		System.out.println("driver registered");
		con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","ds","ds");
		System.out.println("connection created");
	} catch (SQLException e) {
		return false;
	}
		return true;
	}
	
	public static void displayTable()
	{
		try {
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from questions");
			while(rs.next())
			{
				System.out.print(rs.getString(2)+" ");
				System.out.print(rs.getString(3)+" ");
				System.out.print(rs.getString(4)+" ");
				System.out.print(rs.getString(5)+" ");
				System.out.print(rs.getString(6)+" ");
				System.out.print(rs.getString(7)+" ");
				System.out.println();
			}
			System.out.println("in database");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void insertStudent(String name,String roll,String branch,String gender,String marks,String total)
	{
		Statement stmt;
		try {
			stmt = con.createStatement();
			System.out.println("insert into students values('"+roll+"','"+name+"','"+branch+"','"+gender+"','"+marks+"','"+total+"');");
			stmt.executeQuery("insert into students values('"+roll+"','"+name+"','"+branch+"','"+gender+"','"+marks+"','"+total+"')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void insertQuestion(String idl,String question,String a,String b,String c,String d,String ans)
	{
		Statement stmt;
		try {
			stmt = con.createStatement();
			System.out.println("insert into questions values('"+idl+"','"+question+"','"+a+"','"+b+"','"+c+"','"+d+"','"+ans+"');");
			stmt.executeQuery("insert into questions values('"+idl+"','"+question+"','"+a+"','"+b+"','"+c+"','"+d+"','"+ans+"')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static ArrayList<String[]> queryQuestion()
	{
		ArrayList<String[]> al=new ArrayList<>();
		
		try {
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from questions");
			while(rs.next())
			{
				String Question[]=new String[7];
				for(int i=0;i<7;i++){
					Question[i]=rs.getString(i+1);
				}
				al.add(Question);
				/*for(String k:Question)
					System.out.print(k+" ");
			*/	Question=null;
				System.gc();
			}
//			System.out.println(al);
			System.out.println("in database");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		return al;
	}

	

	
}


