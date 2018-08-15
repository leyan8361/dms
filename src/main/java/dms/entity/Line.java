package dms.entity;

/**
 * ÏßÂ·
 * 
 * @author ACER
 *
 */
public class Line {

	private int id;
	private int lineNo;
	private String idShow;

	public Line() {

	}

	public Line(int lineNo) {
		this.lineNo = lineNo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLineNo() {
		return lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	public String getIdShow() {
		return idShow;
	}

	public void setIdShow(String idShow) {
		this.idShow = idShow;
	}

}
