import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MainController
{
	static ArrayList<ArrayList<Integer>> scoreMat;
	static Map<Character,Integer> alpha;
	
	public static void main(String[] args) throws IOException {
		// hw1 1 queryfile datafile alphabet scorematrix 10 -3 
		//set score matrix
		scoreMat= new ArrayList<ArrayList<Integer>>();
		
		Scanner alphaText = new Scanner(new File(args[3]+".txt"));
		Scanner input = new Scanner(new File(args[4]+".txt"));
		int gap=Integer.parseInt(args[6]);
		int k=Integer.parseInt(args[5]);
		
		while(input.hasNextLine())
			{
			 Scanner colReader = new Scanner(input.nextLine());
			 ArrayList col = new ArrayList();
			 while(colReader.hasNextInt())
			    {
			     col.add(colReader.nextInt());
			    }
			 scoreMat.add(col);
			}
		System.out.println(scoreMat); 
	
	 
	//set alpha
		alpha=new HashMap<Character,Integer>(); 
		int x=0;String line;
		line=alphaText.nextLine().toLowerCase();
		while(x<4 && line.charAt(x)!='\n')
			{
			System.out.println(line.charAt(x));
			alpha.put(line.charAt(x), x);
			x++;
			}
		
		System.out.println(alpha); 
		String fn;
		String database="database.txt";
		fn="query.txt";
		String qSeq="",dbSeq="";
		//int k=10;
		OutputSequence op=new OutputSequence();
		
		FastaSequence fsf= new FastaSequence(args[1]+".txt");//query
		FastaSequence db= new FastaSequence(args[2]+".txt");//database
		//List<Integer> score=new ArrayList<Integer>();
		
		List<OutputSequence> objList=new ArrayList<OutputSequence>();
		
		String qId,dbId;
		
		switch(Integer.parseInt(args[0]))
		{
		
		case 1:
			//GLOBAL ALIGNMENT
			for (int i=0; i< fsf.size(); i++)
			{
				qSeq=fsf.getSequence(i);
				for (int j=0; j< db.size(); j++)
				{
					OutputSequence obj=new OutputSequence();
					
					qId=globalAlign.getId(fsf.getDescription());
					dbId=globalAlign.getId(db.getDescription());
					dbSeq=db.getSequence(j);
					//score.add(globalAlign.alignSequence(qSeq,dbSeq,scoreMat,alpha,gap));
					
					obj.setDbSequence(dbSeq);
					obj.setQuerySequence(qSeq);	
					obj.setQSequenceId(qId);
					obj.setDbSequenceId(dbId);
					
					//ret_score=globalAlign.alignSequence(qSeq,dbSeq,scoreMat,alpha,gap);
					globalAlign.alignSequence(obj,scoreMat,alpha,gap);
					
					
					//Setters
					//obj.setScore(ret_score);
					
					objList.add(obj);
					
				}
			}
			
			break;
		case 2:
			//LOCAL ALIGNMENT
					for (int i=0; i< fsf.size(); i++)
					{
						qSeq=fsf.getSequence(i);
						for (int j=0; j< db.size(); j++)
						{
							OutputSequence obj=new OutputSequence();
							
							qId=globalAlign.getId(fsf.getDescription());
							dbId=globalAlign.getId(db.getDescription());
							dbSeq=db.getSequence(j);
							//score.add(globalAlign.alignSequence(qSeq,dbSeq,scoreMat,alpha,gap));
							
							obj.setDbSequence(dbSeq);
							obj.setQuerySequence(qSeq);	
							obj.setQSequenceId(qId);
							obj.setDbSequenceId(dbId);
							
							//ret_score=globalAlign.alignSequence(qSeq,dbSeq,scoreMat,alpha,gap);
							LocalAlign.alignSequence(obj,scoreMat,alpha,gap);
							
							
							//Setters
							//obj.setScore(ret_score);
							
							objList.add(obj);
							
						}
					}
					break;
			
		case 3:
			//DOVE TAIL ALIGNMENT
			for (int i=0; i< fsf.size(); i++)
			{
				qSeq=fsf.getSequence(i);
				for (int j=0; j< db.size(); j++)
				{
					OutputSequence obj=new OutputSequence();
					
					qId=globalAlign.getId(fsf.getDescription());
					dbId=globalAlign.getId(db.getDescription());
					dbSeq=db.getSequence(j);
					//score.add(globalAlign.alignSequence(qSeq,dbSeq,scoreMat,alpha,gap));
					
					obj.setDbSequence(dbSeq);
					obj.setQuerySequence(qSeq);	
					obj.setQSequenceId(qId);
					obj.setDbSequenceId(dbId);
					
					//ret_score=globalAlign.alignSequence(qSeq,dbSeq,scoreMat,alpha,gap);
					//DovetailAlign.alignSequence(obj,scoreMat,alpha,gap);
					
					
					//Setters
					//obj.setScore(ret_score);
					
					objList.add(obj);
					
				}
			}
			
			break;
		default:
			System.out.println("Incorrect Arguments");
			break;
			
		}//end switch
	
		//Collections.sort(score);
		//sort in descending order
		Collections.sort(objList, new Comparator<OutputSequence>() {
		    @Override 
		    public int compare(OutputSequence z1, OutputSequence z2) {
		        if (z1.getScore()< z2.getScore())
		            return 1;
		        if (z1.getScore()> z2.getScore())
		            return -1;
		        return 0;
		    }
		});
		
//		int s=score.size()-1;
//		for(int i=0;i<k;i++)
//		{
//			System.out.println("score"+i+score.get(s-i));
//		}
		
		System.out.println("Top k scores are:");
		PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
		//System.setOut(out);
		
		
		for(int i=0;i<k;i++)
			{
				System.out.println("Score="+objList.get(i).getScore());
				System.out.println(objList.get(i).getQSequenceId()+"\t"+objList.get(i).getStartPositionQuery()+"\t"+objList.get(i).getQueryAlignment());
				System.out.println(objList.get(i).getDbSequenceId()+"\t"+objList.get(i).getStartPositionDb()+"\t"+objList.get(i).getDbAlignment());
				
			}
		
		
		
		
		
 }
}