package ie.gmit.sw;

/**
 * An object that holds the Shingle ID as well as an integer holding the hashcode for that Shingle
 * 
 * @author yonjeremy
 */
public class Shingle {
	private int docId;
	private int hashCode;
	/**
	 * Constructs a Shingle with a document Id and a hashcode
	 * 
	 * @param docId Contains the document ID that the Shingle is related to.
	 * @param hashCode Contains the hashCode of the Shingle content
	 */
	public Shingle(int docId, int hashCode) {
		super();
		this.docId = docId;
		this.hashCode = hashCode;
	}
	/**
	 * @return Integer that holds the document ID
	 */
	public int getDocId() {
		return docId;
	}
	/**
	 * @param docId Gives the Shingle an Integer document ID
	 */
	public void setDocId(int docId) {
		this.docId = docId;
	}
	/**
	 * @return Integer that holds the hashcode of the Shingle
	 */
	public int getHashCode() {
		return hashCode;
	}
	/**
	 * @param hashCode Gives the Shingle an Integer Hashcode of the Shingle
	 */
	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}

	

}
