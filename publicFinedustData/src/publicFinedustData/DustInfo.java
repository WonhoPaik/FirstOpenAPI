package publicFinedustData;

public class DustInfo {
	private int clearVal; // 해제농도
	private int sn; //관리번호
	private String districtName; //지역명
	private String dataDate; //발령일
	private int issueVal; //발령농도
	private String issueTime; //발령시간
	private String clearDate; //해제일
	private String issueDate; //발령일
	private String moveName; //권역명
	private String clearTime; //해제시간
	private String issueGbn; //경보단계
	private String itemCode; //항목명
	
	public DustInfo() {
		super();
	}

	public DustInfo(int clearVal, int sn, String districtName, String dataDate, int issueVal, String issueTime,
			String clearDate, String issueDate, String moveName, String clearTime, String issueGbn, String itemCode) {
		super();
		this.clearVal = clearVal;
		this.sn = sn;
		this.districtName = districtName;
		this.dataDate = dataDate;
		this.issueVal = issueVal;
		this.issueTime = issueTime;
		this.clearDate = clearDate;
		this.issueDate = issueDate;
		this.moveName = moveName;
		this.clearTime = clearTime;
		this.issueGbn = issueGbn;
		this.itemCode = itemCode;
	}

	public int getClearVal() {
		return clearVal;
	}

	public void setClearVal(int clearVal) {
		this.clearVal = clearVal;
	}

	public int getSn() {
		return sn;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getDataDate() {
		return dataDate;
	}

	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}

	public int getIssueVal() {
		return issueVal;
	}

	public void setIssueVal(int issueVal) {
		this.issueVal = issueVal;
	}

	public String getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	public String getClearDate() {
		return clearDate;
	}

	public void setClearDate(String clearDate) {
		this.clearDate = clearDate;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getMoveName() {
		return moveName;
	}

	public void setMoveName(String moveName) {
		this.moveName = moveName;
	}

	public String getClearTime() {
		return clearTime;
	}

	public void setClearTime(String clearTime) {
		this.clearTime = clearTime;
	}

	public String getIssueGbn() {
		return issueGbn;
	}

	public void setIssueGbn(String issueGbn) {
		this.issueGbn = issueGbn;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Override
	public String toString() {
		return "[해제농도=" + clearVal + ", 관리번호=" + sn + ", 지역명=" + districtName + ", 데이터생성날짜="
				+ dataDate + ", 발령농도=" + issueVal + ", 발령시간=" + issueTime + ", 해제일=" + clearDate
				+ ", 발령일=" + issueDate + ", 권역명=" + moveName + ", 해제시간=" + clearTime + ", 경보단계="
				+ issueGbn + ", 항목명=" + itemCode + "]";
	}
	
	
}
