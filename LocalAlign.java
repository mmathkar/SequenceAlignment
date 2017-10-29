import java.io.*;
import java.util.*;


	public class LocalAlign {


	
	static ArrayList<ArrayList<Integer>> scoreMat;
	static Map<Character,Integer> alpha;
	
	
	public static void main(String[] args) throws IOException {
		// hw1 1 queryfile datafile alphabet scorematrix 10 -3 
		Output out = new SystemOut();
		
		
		//set score matrix
		scoreMat= new ArrayList<ArrayList<Integer>>();
		Scanner alphaText = new Scanner(new File("alphabet.txt"));
		Scanner input = new Scanner(new File("scoringmatrix.txt"));
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
	// score=setScore();
	 
	//set alpha
	alpha=new HashMap<Character,Integer>(); 
	int x=0;String line;
	line=alphaText.nextLine().toLowerCase();
	while(x<4 && line.charAt(x)!='\n')
	{
		//alphaText.nextLine();
		System.out.println(line.charAt(x));
		alpha.put(line.charAt(x), x);
		x++;
		
	}
	System.out.println(alpha); 
//	Scanner database = new Scanner(new File("database.txt"));
//	Scanner query = new Scanner(new File("query.txt"));
	String fn;
	String database="database.txt";
	fn="query.txt";
	String qSeq="",dbSeq="";
	int k=10;
	OutputSequence op=new OutputSequence();
	FastaSequence fsf= new FastaSequence(fn);
	FastaSequence db= new FastaSequence(database);
	List<Integer> score=new ArrayList<Integer>();
	String qId,dbId;
	
	for (int i=0; i< fsf.size(); i++)
	{
		qSeq=fsf.getSequence(i);
		for (int j=0; j< db.size(); j++)
		{
			qId=getId(fsf.getDescription());
			dbId=getId(db.getDescription());
			dbSeq=db.getSequence(j);
			score.add(alignSequence(qSeq,dbSeq,scoreMat,alpha,-3));
		}
	}
	
	//System.out.println(score);
	Collections.sort(score);
	//Arrays.sort(score);
	int s=score.size()-1;
	for(int i=0;i<k;i++)
	{
		System.out.println("score"+i+score.get(s-i));
	}
 }

	public static String getId(String desc)
	{
		int i=0,j=i+1;
		String s="";
		
		while(desc.charAt(i)!=':')
		{
			i++;
		}
		if(desc.charAt(i)==':')
		{
			while(desc.charAt(j)!=' ')
			{
				j++;
			}			
			s=desc.substring(i+1,j);
		}
		return s;
	}
	

	public static int alignSequence(String qSeq,String dbSeq,ArrayList<ArrayList<Integer>> scoreMat,Map<Character,Integer> alpha,int gap)
	{
		
		int[][] D = new int[qSeq.length() + 1][dbSeq.length() + 1];

		// First of all, compute insertions and deletions at 1st row/column
		for (int i = 1; i <= qSeq.length(); i++)
		    D[i][0] = 0;
		for (int j = 1; j <= dbSeq.length(); j++)
		    D[0][j] = 0;
//		System.out.println(alpha.get(qSeq.charAt(0)));
//				System.out.println(alpha);
		for (int i = 1; i <= qSeq.length(); i++) {
		    for (int j = 1; j <= dbSeq.length(); j++) {
		     
		      int scoreDiag = D[i - 1][j - 1] + scoreMat.get(alpha.get(qSeq.charAt(i-1))).get(alpha.get(dbSeq.charAt(j-1)));
		      int scoreLeft = D[i][j - 1] + gap; // insertion
		      int scoreUp = D[i - 1][j] + gap; // deletion
		        // we take the max
		        D[i][j] = Math.max(0, Math.max(Math.max(scoreDiag, scoreLeft), scoreUp));//change to max
//		        System.out.println(i+" "+j+" d=="+D[i][j]);

		    }
		}
       // System.out.println(qSeq.length()+" "+dbSeq.length()+" d=="+D[qSeq.length()][dbSeq.length()]);
		
		int score=D[qSeq.length()][dbSeq.length()];
		
		System.out.println("Score of sequence"+score);
	    List<String> sequenceList=traceback(D,qSeq.length(),dbSeq.length(),qSeq,dbSeq,scoreMat,alpha,gap);
	    
		return score;
		
	}
	
	  public static int[] getMaxIndex(int[ ][ ] a)
	  {
	      int maxVal = -99999;
	      int[] answerArray = new int[2];
	      for(int row = 0; row < a.length; row++)
	      {
	          for(int col = 0; col < a[row].length; col++)
	          {
	              if(a[row][col] > maxVal)
	              {
	                  maxVal = a[row][col];
	                  answerArray[0] = row;
	                  answerArray[1] = col;
	              }
	          }
	      }
	      return answerArray;
	  }
	
	public static List<String> traceback(int D[][],int q,int db,String qSeq,String dbSeq,ArrayList<ArrayList<Integer>> scoreMat,Map<Character,Integer> alpha,int gap)
	{
		StringBuffer s_aln = new StringBuffer();
	    StringBuffer t_aln = new StringBuffer();
	    int[] a=new int[2];
	    a=getMaxIndex(D);
	    int i=a[0];
	    int j=a[1];
		//int i=q,j=db;
        
		while(i>1 && j>1 && D[i][j]!=0)
		{
			int sc=scoreMat.get(alpha.get(qSeq.charAt(i-1))).get(alpha.get(dbSeq.charAt(j-1)));
			int d=D[i][j]-scoreMat.get(alpha.get(qSeq.charAt(i-1))).get(alpha.get(dbSeq.charAt(j-1)));
			int elsei=D[i][j]-gap;
			int Dij=D[i][j];
			if(D[i][j]-scoreMat.get(alpha.get(qSeq.charAt(i-1))).get(alpha.get(dbSeq.charAt(j-1))) == D[i-1][j-1])
			{
				t_aln=t_aln.append(qSeq.charAt(i-1));
				s_aln=s_aln.append(dbSeq.charAt(j-1));
				i = i-1;j = j-1;
			}
			else if(D[i][j]-gap==D[i][j-1])
			{
				t_aln=t_aln.append("_");
				s_aln=s_aln.append(dbSeq.charAt(j-1));
				j = j-1;
			}
			else if(D[i][j]-gap==D[i-1][j])
			{
				s_aln=s_aln.append("_");
				t_aln=t_aln.append(qSeq.charAt(i-1));
				i = i-1;
			}
			else
			{
				System.out.println("Error");
			}
		}
		
		if (j > 1 && D[i][j]!=0)  
		{ 
			while (j > 1)
		    {
				t_aln=t_aln.append("_");
				s_aln=s_aln.append(dbSeq.charAt(j-1));
				j = j-1;
		    }
		}
		else if (i > 1 && D[i][j]!=0) 
		{ 
			while (i > 1)
			{ 
				s_aln=s_aln.append("_");
				t_aln=t_aln.append(qSeq.charAt(i-1));
				i = i-1;
			}
		
		}
			String s_aln1=s_aln.toString();
			String t_aln1=t_aln.toString();
			System.out.println("start position in query:"+i+" Query String: "+s_aln.toString());
			System.out.println("start position in db"+j+" Database String: "+t_aln.toString());	
			List<String> alignString=new ArrayList<String>();
			alignString.add(s_aln1);
			alignString.add(t_aln1);
			return alignString;
		
	}
	public static int setScores(char x,char y)
	{
		int value=0;
		return value;
	}
	}