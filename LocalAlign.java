import java.io.*;
import java.util.*;


	public class LocalAlign {



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
//		    D[i][0] = 0;
//		for (int j = 1; j <= dbSeq.length(); j++)
//		    D[0][j] = 0;
////		System.out.println(alpha.get(qSeq.charAt(0)));
////				System.out.println(alpha);
//		for (int i = 1; i <= qSeq.length(); i++) {
//		    for (int j = 1; j <= dbSeq.length(); j++) {
//		     
//		      int scoreDiag = D[i - 1][j - 1] + scoreMat.get(alpha.get(qSeq.charAt(i-1))).get(alpha.get(dbSeq.charAt(j-1)));
//		      int scoreLeft = D[i][j - 1] + gap; // insertion
//		      int scoreUp = D[i - 1][j] + gap; // deletion
//		        // we take the max
//		        D[i][j] = Math.max(0, Math.max(Math.max(scoreDiag, scoreLeft), scoreUp));//change to max
////		        System.out.println(i+" "+j+" d=="+D[i][j]);
//
//		    }
//		}
//       // System.out.println(qSeq.length()+" "+dbSeq.length()+" d=="+D[qSeq.length()][dbSeq.length()]);
//		
//		int score=D[qSeq.length()][dbSeq.length()];
//		
//		System.out.println("Score of sequence"+score);
//	    List<String> sequenceList=traceback(D,qSeq.length(),dbSeq.length(),qSeq,dbSeq,scoreMat,alpha,gap);
//	    
//		return score;
//		
//	}
	
	public static void alignSequence(OutputSequence obj,ArrayList<ArrayList<Integer>> scoreMat,Map<Character,Integer> alpha,int gap)
	{
		String qSeq=obj.getQuerySequence();
		String dbSeq=obj.getDbSequence();
		
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
		
		int scoreOld=D[qSeq.length()][dbSeq.length()];
		
		//System.out.println("Score of sequence"+score);
	    //List<String> sequenceList=traceback(D,qSeq.length(),dbSeq.length(),qSeq,dbSeq,scoreMat,alpha,gap);
	    
		//return score;
		
		
		int[] a=new int[2];
		a=getMaxIndex(D);
	    int i=a[0];
	    int j=a[1];
	    int score=D[i][j];
	    
		List<String> sequenceList=traceback(D,qSeq.length(),dbSeq.length(),qSeq,dbSeq,scoreMat,alpha,gap);
	   
		obj.setQueryAlignment(sequenceList.get(0));
	    obj.setDbAlignment(sequenceList.get(1));
	    obj.setStartPositionQuery(Integer.parseInt(sequenceList.get(2))-1);//set i
	    obj.setStartPositionDb(Integer.parseInt(sequenceList.get(3))-1);//set j
	    obj.setScore(score);
		
	}
	
	  public static int[] getMaxIndex(int[ ][ ] a)
	  {
	      int maxVal = Integer.MIN_VALUE;
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
				t_aln=t_aln.append(".");
				s_aln=s_aln.append(dbSeq.charAt(j-1));
				j = j-1;
			}
			else if(D[i][j]-gap==D[i-1][j])
			{
				s_aln=s_aln.append(".");
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
				t_aln=t_aln.append(".");
				s_aln=s_aln.append(dbSeq.charAt(j-1));
				j = j-1;
		    }
		}
		else if (i > 1 && D[i][j]!=0) 
		{ 
			while (i > 1)
			{ 
				s_aln=s_aln.append(".");
				t_aln=t_aln.append(qSeq.charAt(i-1));
				i = i-1;
			}
		
		}
			String s_aln1=s_aln.reverse().toString();
			String t_aln1=t_aln.reverse().toString();
//			System.out.println("start position in query:"+i+" Query String: "+s_aln.toString());
//			System.out.println("start position in db:"+j+" Database String: "+t_aln.toString());	
			List<String> alignString=new ArrayList<String>();
			alignString.add(s_aln1);
			alignString.add(t_aln1);
			alignString.add(Integer.toString(i));//start position in query
			alignString.add(Integer.toString(j));//start pos in db
			return alignString;
		
	}
	
	}