import java.lang.*;
import java.io.*;
import java.util.*;
/**
 * This class will read first sequence from a Fasta format file 
 */

 public class FastaSequence {

     String [] description;
     String [] sequence;

    public FastaSequence(String filename)
    {
       	readSequenceFromFile(filename);
    }

    void readSequenceFromFile(String file)
    {
	List<String> desc= new ArrayList<String>();
	List<String> seq = new ArrayList<String>();
	try{
        BufferedReader in     = new BufferedReader( new FileReader( file ) );
        StringBuffer   buffer = new StringBuffer();
        String         line   = in.readLine();
     
        if( line == null )
            throw new IOException( file + " is an empty file" );
     
        if( line.charAt( 0 ) != '>' )
            throw new IOException( "First line of " + file + " should start with '>'" );
        else
            desc.add(line);
        for( line = in.readLine().trim(); line != null; line = in.readLine() )
	{
            if( line.length()>0 && line.charAt( 0 ) == '>' )
	    {
		seq.add(buffer.toString());
		buffer = new StringBuffer();
		desc.add(line);
	    } else  
            	buffer.append( line.trim() );
        }   
        if( buffer.length() != 0 )
	     seq.add(buffer.toString());
      }catch(IOException e)
      {
        System.out.println("Error when reading "+file);
        e.printStackTrace();
      }

	description = new String[desc.size()];
	sequence = new String[seq.size()];
	for (int i=0; i< seq.size(); i++)
	{
	  description[i]=(String) desc.get(i);
	  
	  sequence[i]=(String) seq.get(i);
	}
 
    }
    
    //return first sequence as a String
   // public String getSequence(){ return sequence[0];}

    //return first xdescription as String
   // public String getDescription(){return description[0];}

    //return sequence as a String
    public String getSequence(int i){ return sequence[i];}

    //return description as String
    public String getDescription(int i){return description[i];}
    
    public int size(){return sequence.length;}
    
    /*public static void main(String [] args) throws Exception
    {
	String fn ="";
//if (args.length>0) fn=args[0];
//	else 
//	{
//   System.out.print("Enter the name of the FastaFile:");
//   fn = (new BufferedReader(new InputStreamReader(System.in))).readLine();
//	
//	
//	}
	//Scanner fn = new Scanner(new File("query.txt"));
//	fn="query.txt";
	fn="database2.txt";
	FastaSequence fsf= new FastaSequence(fn);
	System.out.println(fsf.size());
	System.out.println("Sequence db: \n"+ fsf.getSequence(1));
	
	fn="query.txt";
	FastaSequence q= new FastaSequence(fn);
	System.out.println(q.size());
	System.out.println("SequenceQ : \n"+ q.getSequence(1));
	
//	for (int i=0; i< fsf.size(); i++)
//	{
//	System.out.println("One sequence read from file "+ fn +" with length "+fsf.getSequence().length() );
//	System.out.println("description: \n"+ fsf.getDescription(i));
//	System.out.println("Sequence: \n"+ fsf.getSequence(i));
//	}
    }*/

}
