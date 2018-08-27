package dms.entity;

import java.util.List;

public class AccidentReport {

	private int id;
	private String no;
	private String eventName;
	private String eventAddress;
	private String occurDate;
	private String relatedPerson;
	private String rank;
	private String eventSummary;
	private String affect;
	private String analysisAndReform;
	private String opinion;
	private String analysisMember;
	private String otherExplain;
	private String fillDepart;
	private String fillDate;
	private String fillPerson;
	private String responsiblePerson;
	private List<AccidentReportAttach> lara;
	private String isShow;

	public AccidentReport() {

	}

	public AccidentReport(String no, String eventName, String eventAddress, String occurDate, String relatedPerson,
			String rank, String eventSummary, String affect, String analysisAndReform, String opinion,
			String analysisMember, String otherExplain, String fillDepart, String fillDate, String fillPerson,
			String responsiblePerson) {
		this.no = no;
		this.eventName = eventName;
		this.eventAddress = eventAddress;
		this.occurDate = occurDate;
		this.relatedPerson = relatedPerson;
		this.rank = rank;
		this.eventSummary = eventSummary;
		this.affect = affect;
		this.analysisAndReform = analysisAndReform;
		this.opinion = opinion;
		this.analysisMember = analysisMember;
		this.otherExplain = otherExplain;
		this.fillDepart = fillDepart;
		this.fillDate = fillDate;
		this.fillPerson = fillPerson;
		this.responsiblePerson = responsiblePerson;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventAddress() {
		return eventAddress;
	}

	public void setEventAddress(String eventAddress) {
		this.eventAddress = eventAddress;
	}

	public String getOccurDate() {
		return occurDate;
	}

	public void setOccurDate(String occurDate) {
		this.occurDate = occurDate;
	}

	public String getRelatedPerson() {
		return relatedPerson;
	}

	public void setRelatedPerson(String relatedPerson) {
		this.relatedPerson = relatedPerson;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getEventSummary() {
		return eventSummary;
	}

	public void setEventSummary(String eventSummary) {
		this.eventSummary = eventSummary;
	}

	public String getAffect() {
		return affect;
	}

	public void setAffect(String affect) {
		this.affect = affect;
	}

	public String getAnalysisAndReform() {
		return analysisAndReform;
	}

	public void setAnalysisAndReform(String analysisAndReform) {
		this.analysisAndReform = analysisAndReform;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public String getAnalysisMember() {
		return analysisMember;
	}

	public void setAnalysisMember(String analysisMember) {
		this.analysisMember = analysisMember;
	}

	public String getOtherExplain() {
		return otherExplain;
	}

	public void setOtherExplain(String otherExplain) {
		this.otherExplain = otherExplain;
	}

	public String getFillDepart() {
		return fillDepart;
	}

	public void setFillDepart(String fillDepart) {
		this.fillDepart = fillDepart;
	}

	public String getFillDate() {
		return fillDate;
	}

	public void setFillDate(String fillDate) {
		this.fillDate = fillDate;
	}

	public String getFillPerson() {
		return fillPerson;
	}

	public void setFillPerson(String fillPerson) {
		this.fillPerson = fillPerson;
	}

	public String getResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(String responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

	public List<AccidentReportAttach> getLara() {
		return lara;
	}

	public void setLara(List<AccidentReportAttach> lara) {
		this.lara = lara;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

}
