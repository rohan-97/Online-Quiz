package OnlineQuiz;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
public class PcServer {

	public static void main(String[] args) {
		Database.connect();
		int port=2018;
		try {
		ServerSocket ss=new ServerSocket(port);
		while(true){
		ArrayList<String[]> al;
			Socket sock=ss.accept();
			ObjectOutputStream oos=new ObjectOutputStream(sock.getOutputStream());
			 al=Database.queryQuestion();
			oos.writeObject(al);
			ObjectInputStream ois=new ObjectInputStream(sock.getInputStream());
			ArrayList stud=(ArrayList)ois.readObject();
			if(stud!=null)
			{
				System.out.println("Student Name"+stud.get(0));
				System.out.println("Student Roll No."+stud.get(1));
				System.out.println("Student Branch"+stud.get(2));
				System.out.println("Student Sex"+stud.get(3));
				String ans[][]=(String[][]) stud.get(4);
				
				System.out.println();
				for(int i=0;i<(int)stud.get(6);i++)
				{
					for(int j=0;j<2;j++)
						System.out.print(ans[j][i]+" ");
					System.out.println();
				}
				System.out.println();
				System.out.println("Marks"+stud.get(5));
				Database.insertStudent(String.valueOf(stud.get(0)), String.valueOf(stud.get(1)), String.valueOf(stud.get(2)), String.valueOf(stud.get(3)), String.valueOf(stud.get(5)), String.valueOf(stud.get(6)));
			}
			
			ois.close();
			oos.close();
			sock.close();

		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	}
	}
}
