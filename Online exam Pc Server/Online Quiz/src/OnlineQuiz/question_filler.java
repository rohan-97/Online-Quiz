package OnlineQuiz;

import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

public class question_filler {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Database.connect();
		File q=new File("C:\\Users\\Rohan\\Downloads\\kachra\\mcq.txt");
		File set=new File("C:\\Users\\Rohan\\Downloads\\kachra\\set2.txt");
		
		try {
			set.createNewFile();
			PrintStream ps=new PrintStream(set);
			Integer i=1;
			Scanner qs=new Scanner(q);
			while(i<=100)
			{
				String opt[]=new String[4];
				String qq=qs.nextLine();
				opt[0]=qs.nextLine();
				opt[1]=qs.nextLine();
				opt[2]=qs.nextLine();
				opt[3]=qs.nextLine();
				String ans=qs.nextLine();
				int index=(int)ans.trim().charAt(0)-97;
				qs.nextLine();
				ps.println("quest:"+qq.substring(5));
				ps.println("a:"+opt[0].substring(3));
				ps.println("b:"+opt[1].substring(3));
				ps.println("c:"+opt[2].substring(3));
				ps.println("d:"+opt[3].substring(3));
				ps.println("ans:"+opt[index].substring(3));
				Database.insertQuestion(i.toString(),qq.substring(5), opt[0].substring(3), opt[1].substring(3), opt[2].substring(3), opt[3].substring(3), opt[index].substring(3));
				i++;
			}
			ps.close();
			qs.close();
		} catch (Exception e) {
//			 TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
