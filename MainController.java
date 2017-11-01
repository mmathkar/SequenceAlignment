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
		long startTime = System.currentTimeMillis();
		
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
		
		String qSeq="",dbSeq="";
		
		
		
		
		FastaSequence fsf= new FastaSequence(args[1]+".txt");//query
		FastaSequence db= new FastaSequence(args[2]+".txt");//database
		//List<Integer> score=new ArrayList<Integer>();
		
		List<OutputSequence> objList=new ArrayList<OutputSequence>();
		
		String qId,dbId;
		PrintStream out = new PrintStream(new FileOutputStream("outputQueryNewLocal.txt"));
		System.setOut(out);
		
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
					OutputSequence ret_obj=new OutputSequence();
					
					qId=globalAlign.getId(fsf.getDescription(i));//*****changed
					dbId=globalAlign.getId(db.getDescription(j));
					dbSeq=db.getSequence(j);
								
					obj.setDbSequence(dbSeq);
					obj.setQuerySequence(qSeq);	
					obj.setQSequenceId(qId);
					obj.setDbSequenceId(dbId);
					
					//ret_score=globalAlign.alignSequence(qSeq,dbSeq,scoreMat,alpha,gap);
					ret_obj=globalAlign.alignSequence(obj,scoreMat,alpha,gap);
					
					//********************************************************
					
//					System.out.println("start position in query:"+ret_obj.getStartPositionQuery()+" Query String: "+ret_obj.getQueryAlignment());
//					System.out.println("id:"+ret_obj.getDbSequenceId()+"start position in db:"+ret_obj.getStartPositionDb()+"Length"+ret_obj.getDbAlignment().length()+" Database String: ");
//					System.out.println(ret_obj.getDbAlignment());
					
					
					//Setters
					//obj.setScore(ret_score);
					
					objList.add(ret_obj);
					
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
							
							qId=LocalAlign.getId(fsf.getDescription(i));
							dbId=LocalAlign.getId(db.getDescription(j));
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
					
					qId=DovetailAlign.getId(fsf.getDescription(i));
					dbId=DovetailAlign.getId(db.getDescription(j));
					dbSeq=db.getSequence(j);
					//score.add(globalAlign.alignSequence(qSeq,dbSeq,scoreMat,alpha,gap));
					
					obj.setDbSequence(dbSeq);
					obj.setQuerySequence(qSeq);	
					obj.setQSequenceId(qId);
					obj.setDbSequenceId(dbId);
					
					//ret_score=globalAlign.alignSequence(qSeq,dbSeq,scoreMat,alpha,gap);
					DovetailAlign.alignSequence(obj,scoreMat,alpha,gap);
										
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
		

		System.out.println("Query Size:"+fsf.size());
		System.out.println("Top k scores are:");
		
		//for entire query.txt
		
		for(int i=0;i<k;i++)
			{
				System.out.println("Score="+objList.get(i).getScore());
				System.out.println(objList.get(i).getQSequenceId()+"\t"+objList.get(i).getStartPositionQuery()+"\t"+objList.get(i).getQueryAlignment());
				System.out.println(objList.get(i).getDbSequenceId()+"\t"+objList.get(i).getStartPositionDb()+"\t"+objList.get(i).getDbAlignment());
				
			}
		
		
		
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime);	
		
		
 }
}