import java.io.*;
import java.util.*;

	public class globalAlign {

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
	

//	public static int alignSequence(String qSeq,String dbSeq,ArrayList<ArrayList<Integer>> scoreMat,Map<Character,Integer> alpha,int gap)
//	{
//		
//		int[][] D = new int[qSeq.length() + 1][dbSeq.length() + 1];
//
//		// First of all, compute insertions and deletions at 1st row/column
//		for (int i = 1; i <= qSeq.length(); i++)
//		    D[i][0] = D[i - 1][0] + gap;
//		for (int j = 1; j <= dbSeq.length(); j++)
//		    D[0][j] = D[0][j - 1] + gap;
////		System.out.println(alpha.get(qSeq.charAt(0)));
////				System.out.println(alpha);
//		for (int i = 1; i <= qSeq.length(); i++) {
//		    for (int j = 1; j <= dbSeq.length(); j++) {
//		     
//		      int scoreDiag = D[i - 1][j - 1] + scoreMat.get(alpha.get(qSeq.charAt(i-1))).get(alpha.get(dbSeq.charAt(j-1)));
//		      int scoreLeft = D[i][j - 1] + gap; // insertion
//		      int scoreUp = D[i - 1][j] + gap; // deletion
//		        // we take the max
//		        D[i][j] = Math.max(Math.max(scoreDiag, scoreLeft), scoreUp);//change to max
////		        System.out.println(i+" "+j+" d=="+D[i][j]);
//
//		    }
//		}
//        System.out.println(qSeq.length()+" "+dbSeq.length()+" d=="+D[qSeq.length()][dbSeq.length()]);
//		
//		int score=D[qSeq.length()][dbSeq.length()];
//		
//		//System.out.println(D);
//	    List<String> sequenceList=traceback(D,qSeq.length(),dbSeq.length(),qSeq,dbSeq,scoreMat,alpha,gap);
//	    
//		return score;
//		
//	}
	
	
	
	public static OutputSequence alignSequence(OutputSequence obj,ArrayList<ArrayList<Integer>> scoreMat,Map<Character,Integer> alpha,int gap)
	{
		String qSeq=obj.getQuerySequence();
		String dbSeq=obj.getDbSequence();
		
		int[][] D = new int[qSeq.length() + 1][dbSeq.length() + 1];

		// First of all, compute insertions and deletions at 1st row/column
		for (int i = 1; i <= qSeq.length(); i++)
		    D[i][0] = D[i - 1][0] + gap;
		for (int j = 1; j <= dbSeq.length(); j++)
		    D[0][j] = D[0][j - 1] + gap;
//		System.out.println(alpha.get(qSeq.charAt(0)));
//				System.out.println(alpha);
		for (int i = 1; i <= qSeq.length(); i++) {
		    for (int j = 1; j <= dbSeq.length(); j++) {
		     
		      int scoreDiag = D[i - 1][j - 1] + scoreMat.get(alpha.get(qSeq.charAt(i-1))).get(alpha.get(dbSeq.charAt(j-1)));
		      int scoreLeft = D[i][j - 1] + gap; // insertion
		      int scoreUp = D[i - 1][j] + gap; // deletion
		        // we take the max
		        D[i][j] = Math.max(Math.max(scoreDiag, scoreLeft), scoreUp);//change to max
//		        System.out.println(i+" "+j+" d=="+D[i][j]);

		    }
		}
//        System.out.println(qSeq.length()+" "+dbSeq.length()+" d=="+D[qSeq.length()][dbSeq.length()]);
		
		int score=D[qSeq.length()][dbSeq.length()];
		
		//System.out.println(D);
		//set alignments
	    List<String> sequenceList=traceback(D,qSeq.length(),dbSeq.length(),qSeq,dbSeq,scoreMat,alpha,gap);
	    obj.setQueryAlignment(sequenceList.get(0));
	    obj.setDbAlignment(sequenceList.get(1));	    
	    obj.setStartPositionQuery(Integer.parseInt(sequenceList.get(2))-1);//set i
	    obj.setStartPositionDb(Integer.parseInt(sequenceList.get(3))-1);//set j	    
	    obj.setScore(score);
	    
//	    System.out.println("start position in query:"+obj.getStartPositionQuery()+" Query String: "+obj.getQueryAlignment());
//		System.out.println("start position in db:"+obj.getStartPositionDb()+"Length"+obj.getDbAlignment().length()+" Database String: ");
//		System.out.println(obj.getDbAlignment());
	    
	    
		return obj;
		
	}
	
//	public static List<String> traceback(int D[][],int q,int db,String qSeq,String dbSeq,ArrayList<ArrayList<Integer>> scoreMat,Map<Character,Integer> alpha,int gap)
//	{
//		StringBuffer s_aln = new StringBuffer();
//	    StringBuffer t_aln = new StringBuffer();
//		int i=q,j=db;
//
//		while(i>1 && j>1)
//		{
//				
//			
//			if(D[i][j]-scoreMat.get(alpha.get(qSeq.charAt(i-1))).get(alpha.get(dbSeq.charAt(j-1))) == D[i-1][j-1])
//			{
//				t_aln=t_aln.append(qSeq.charAt(i-1));
//				s_aln=s_aln.append(dbSeq.charAt(j-1));
//				i = i-1;j = j-1;
//				
//			}
//			else if(D[i][j]-gap==D[i][j-1])
//			{
//				t_aln=t_aln.append(".");
//				s_aln=s_aln.append(dbSeq.charAt(j-1));
//				j = j-1;
//			}
//			else if(D[i][j]-gap==D[i-1][j])
//			{
//				s_aln=s_aln.append(".");
//				t_aln=t_aln.append(qSeq.charAt(i-1));
//				i = i-1;
//			}
//			else
//			{
//				System.out.println("Error");
//			}
//		}
//		
//		if (j > 1)  
//		{ 
//			while (j > 1)
//		    {
//				t_aln=t_aln.append(".");
//				s_aln=s_aln.append(dbSeq.charAt(j-1));
//				j = j-1;
//		    }
//		}
//		else if (i > 1) 
//		{ 
//			while (i > 1)
//			{ 
//				s_aln=s_aln.append(".");
//				t_aln=t_aln.append(qSeq.charAt(i-1));
//				i = i-1;
//			}
//		
//		}
////		    s_aln.reverse();
////		    t_aln.reverse();
//			String s_aln1=s_aln.reverse().toString();
//			String t_aln1=t_aln.reverse().toString();
//			
////			System.out.println("Query String: "+s_aln);
////			System.out.println("Database String: "+t_aln);	
//			System.out.println("start position in query:"+i+" Query String: "+s_aln1);
//			System.out.println("start position in db:"+j+" Database String: "+t_aln1);
//			
//			if(t_aln.length()==0)
//				System.out.println("**********Empty db string  "+t_aln1);
//			
//			List<String> alignString=new ArrayList<String>();
//			alignString.add(s_aln1);
//			alignString.add(t_aln1);
//			alignString.add(Integer.toString(i));//start position in query
//			alignString.add(Integer.toString(j));//start pos in db
//			
//			//OutputSequence out=new OutputSequence();
//			return alignString;
//		
//	}
	
	
	public static List<String> traceback(int D[][],int q,int db,String qSeq,String dbSeq,ArrayList<ArrayList<Integer>> scoreMat,Map<Character,Integer> alpha,int gap)
	{
//		StringBuffer s_aln = new StringBuffer();
//	    StringBuffer t_aln = new StringBuffer();
		String s_aln="";
		String t_aln="";
		int i=q,j=db;

		while(i>1 && j>1)
		{
				
			
			if(D[i][j]-scoreMat.get(alpha.get(qSeq.charAt(i-1))).get(alpha.get(dbSeq.charAt(j-1))) == D[i-1][j-1])
			{
				t_aln=qSeq.charAt(i-1)+t_aln;
				s_aln=dbSeq.charAt(j-1)+s_aln;
				i = i-1;j = j-1;
				
			}
			else if(D[i][j]-gap==D[i][j-1])
			{
				t_aln="."+t_aln;
				s_aln=dbSeq.charAt(j-1)+s_aln;
				j = j-1;
			}
			else if(D[i][j]-gap==D[i-1][j])
			{
				s_aln="."+s_aln;
				t_aln=qSeq.charAt(i-1)+t_aln;
				i = i-1;
			}
			else
			{
				System.out.println("Error");
			}
		}
		
		if (j > 1)  
		{ 
			while (j > 1)
		    {
				t_aln="."+t_aln;
				s_aln=dbSeq.charAt(j-1)+s_aln;
				j = j-1;
		    }
		}
		else if (i > 1) 
		{ 
			while (i > 1)
			{ 
				s_aln="."+s_aln;
				t_aln=qSeq.charAt(i-1)+t_aln;
				i = i-1;
			}
		
		}
//		    s_aln.reverse();
//		    t_aln.reverse();
//			String s_aln1=s_aln.reverse().toString();
//			String t_aln1=t_aln.reverse().toString();
			
//			System.out.println("Query String: "+s_aln);
//			System.out.println("Database String: "+t_aln);	
//			System.out.println("start position in query:"+i+" Query String: "+s_aln);
//			System.out.println("start position in db:"+j+" Database String: "+t_aln);
			
			if(t_aln.length()==0)
				System.out.println("**********Empty db string  "+t_aln);
			
			List<String> alignString=new ArrayList<String>();
			alignString.add(s_aln);
			alignString.add(t_aln);
			alignString.add(Integer.toString(i));//start position in query
			alignString.add(Integer.toString(j));//start pos in db
			
			//OutputSequence out=new OutputSequence();
			return alignString;
		
	}
	
	}