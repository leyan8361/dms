package dms.entity;

public class AccidentReportAttach {

	private int id;
	private int reportId;
	private String path;
	private String isShow;

	public AccidentReportAttach() {

	}

	public AccidentReportAttach(int reportId, String path) {
		this.reportId = reportId;
		this.path = path;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

}
