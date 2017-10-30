
public class OutputSequence
{ 
	int score;
	int i,j;
	
	String qAlign,dbAlign,qSeq,dbSeq;
	String qid,dbId;
	 public int getScore() {
	        return score;
	   }
	   public void setScore(int score) {
	    this.score = score;
	   }	   
	   public String getQSequenceId() {
	        return qid;
	   }
	   public void setQSequenceId(String qid) {
	    this.qid = qid;
	   }
	   
	   public String getDbSequenceId() {
	        return dbId;
	   }
	   public void setDbSequenceId(String dbId) {
	    this.dbId = dbId;
	   }
	   public String getQuerySequence() {
	        return qSeq;
	   }
	   public void setQuerySequence(String qSeq) {
	    this.qSeq = qSeq;
	   }
	   public String getDbSequence() {
	        return dbSeq;
	   }
	   public void setDbSequence(String dbSeq) {
	    this.dbSeq = dbSeq;
	   }
	   
	   public String getQueryAlignment() {
	        return qAlign;
	   }
	   public void setQueryAlignment(String qAlign) {
	    this.qAlign = qAlign;
	   }
	   public String getDbAlignment() {
	        return dbAlign;
	   }
	   public void setDbAlignment(String dbAlign) {
	    this.dbAlign = dbAlign;
	   }
	   public int getStartPositionQuery() {
	        return i;
	   }
	   public void setStartPositionQuery(int i) {
	    this.i = i;
	   }
	   
	   public int getStartPositionDb() {
	        return j;
	   }
	   public void setStartPositionDb(int j) {
	    this.j = j;
	   }
	
}
