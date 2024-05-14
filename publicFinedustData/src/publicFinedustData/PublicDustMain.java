package publicFinedustData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PublicDustMain {
	public static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		// 웹 접속을 통해서 미세먼지 정보를 ArrayList에 담아옴.
		ArrayList<DustInfo> DustInfoList = null;
		ArrayList<DustInfo> DustInfoSelectList = null;

		boolean exitFlag = false;
		while (!exitFlag) {
			System.out.println("1.웹정보가져오기, 2.테이블 저장하기, 3.테이블 읽어오기, 4.수정하기, 5.삭제하기, 6.종료");
			System.out.println("선택>>");
			int count = Integer.parseInt(sc.nextLine());
			switch (count) {
			case 1:
				DustInfoList = webConnection();
				break;
			case 2:
				if (DustInfoList.size() < 1) {
					System.out.println("공공데이터로부터 가져온 자료가 없습니다.");
					continue;
				}
				insertDustInfo(DustInfoList);
				break;
			case 3:
				DustInfoSelectList = selectDustInfo();
				printDustInfo(DustInfoSelectList);
				break;
			case 4:
				int data = updateInputSn();
				if (data != 0) {
					updateDustInfo(data);
				}
				break;
			case 5:
				deleteDustInfo();
				break;
			case 6:
				exitFlag = true;
				break;
			}
		} // end of while
		System.out.println("The end");
	}

//수정할 Serial Number 선택
	public static int updateInputSn() {
		ArrayList<DustInfo> imsiDustInfoList = selectDustInfo();
		printDustInfo(imsiDustInfoList);
		System.out.print("update Sn >>");
		int data = Integer.parseInt(sc.nextLine());
		return data;
	}
	
	
	public static void updateDustInfo(int data) {
		String sql = "update finedust set dataDate = sysdate where sn = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, data);
			int i = pstmt.executeUpdate();
			if (i == 1) {
				System.out.println("미세먼지정보 수정 완료");
			} else {
				System.out.println("미세먼지정보 수정 실패");
			}
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
	}
	public static void printDustInfo(ArrayList<DustInfo> dustInfoSelectList) {
		if (dustInfoSelectList.size() < 1) {
			System.out.println("출력할 미세먼지 정보가 없습니다.");
			return;
		}
		for (DustInfo data : dustInfoSelectList) {
			System.out.println(data.toString());
		}
	}
	// 버스정보 가져오기
		public static ArrayList<DustInfo> selectDustInfo() {
			ArrayList<DustInfo> dustInfoList = null;
			String sql = "select * from finedust";
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				con = DBUtil.getConnection();
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				dustInfoList = new ArrayList<DustInfo>();
				while (rs.next()) {
					DustInfo di = new DustInfo();
					di.setClearVal(rs.getInt("clearval"));
					di.setSn(rs.getInt("sn"));
					di.setDistrictName(rs.getString("districtname"));
					di.setDataDate(String.valueOf(rs.getDate("datadate")));
					di.setIssueVal(rs.getInt("issueval"));
					di.setIssueTime(rs.getString("issueTime"));
					di.setClearDate(String.valueOf(rs.getDate("clearDate")));
					di.setIssueDate(String.valueOf(rs.getDate("issueDate")));
					di.setMoveName(rs.getString("moveName"));
					di.setClearTime(rs.getString("clearTime"));
					di.setIssueGbn(rs.getString("issueGbn"));
					di.setItemCode(rs.getString("itemCode"));
					dustInfoList.add(di);
				}
			} catch (SQLException se) {
				System.out.println(se);
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (pstmt != null)
						pstmt.close();
					if (con != null)
						con.close();
				} catch (SQLException se) {
				}
			}
			return dustInfoList;
		}
		// 공공데이터 버스정보 삭제
		public static void deleteDustInfo() {
			int count = getCountDustInfo();
			if (count == 0) {
				System.out.println("미세먼지 정보 내용이 없습니다.");
				return;
			}
			String sql = "delete from finedust";
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				con = DBUtil.getConnection();
				pstmt = con.prepareStatement(sql);
				int i = pstmt.executeUpdate();
				if (i != 0) {
					System.out.println("모든 미세먼지정보 삭제 완료");
				} else {
					System.out.println("모든 미세먼지정보 삭제 실패");
				}
			} catch (SQLException e) {
				System.out.println("e=[" + e + "]");
			} catch (Exception e) {
				System.out.println("e=[" + e + "]");
			} finally {
				try {
					// 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
					if (pstmt != null)
						pstmt.close();
					if (con != null)
						con.close();
				} catch (SQLException e) {
				}
			}
		}
		public static int getCountDustInfo() {
			int count = 0;
			String sql = "select count(*) as cnt from finedust";
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				con = DBUtil.getConnection();
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					count = (rs.getInt("cnt"));
				}
			} catch (SQLException se) {
				System.out.println(se);
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (pstmt != null)
						pstmt.close();
					if (con != null)
						con.close();
				} catch (SQLException se) {
				}
			}
			return count;
		}
		// 공공데이터를 테이블에 저장
		public static void insertDustInfo(ArrayList<DustInfo> dustInfoList) {
			if (dustInfoList.size() < 1) {
				System.out.println("입력할 데이터가 없습니다.");
				return;
			}
			// 저장하기 전에 테이블에 있는 내용을 삭제
			deleteDustInfo();

			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				con = DBUtil.getConnection();
				for (DustInfo data : dustInfoList) {
					String sql = "insert into finedust values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
					pstmt = con.prepareStatement(sql);
					pstmt.setInt(1, data.getClearVal());
					pstmt.setInt(2, data.getSn());
					pstmt.setString(3, data.getDistrictName());
					pstmt.setString(4, data.getDataDate());
					pstmt.setInt(5, data.getIssueVal());
					pstmt.setString(6, data.getIssueTime());
					pstmt.setString(7, data.getClearDate());
					pstmt.setString(8, data.getIssueDate());
					pstmt.setString(9, data.getMoveName());
					pstmt.setString(10, data.getClearTime());
					pstmt.setString(11, data.getIssueGbn());
					pstmt.setString(12, data.getItemCode());
					int value = pstmt.executeUpdate();

					if (value == 1) {
						System.out.println(data.getDistrictName() + "지역 등록완료");
					} else {
						System.out.println(data.getDistrictName() + "지역 등록실패");
					}
				} // end of for문
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (pstmt != null) {
						pstmt.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
		public static ArrayList<DustInfo> webConnection() {
			ArrayList<DustInfo> list = new ArrayList<>();
			// 1. 요청 url 생성
			StringBuilder urlBuilder = new StringBuilder(
					"http://apis.data.go.kr/B552584/UlfptcaAlarmInqireSvc/getUlfptcaAlarmInfo");
			try {
				urlBuilder.append("?" + URLEncoder.encode("year", "UTF-8") + "=" +URLEncoder.encode("2020", "UTF-8"));
				urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
				urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8"));
				urlBuilder.append("&" + URLEncoder.encode("returnType", "UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8"));
				urlBuilder.append("&" + URLEncoder.encode("serviceKey", "UTF-8") + "=SwVVLmU%2BGO3CAoCXd8fveS7cPiWqMjoStk7ipjzNe7uMlKIsrxfDpdVtXOhVxlgBT5WPd8NsS7%2BokQEiVaR8zQ%3D%3D");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			// 2.서버주소 Connection con
			URL url = null;
			HttpURLConnection conn = null;
			try {
				url = new URL(urlBuilder.toString()); // 웹서버주소 action
				conn = (HttpURLConnection) url.openConnection(); // 접속요청 get방식
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Content-type", "application/json");
				System.out.println("Response code: " + conn.getResponseCode());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 3. 요청내용을 전송 및 응답처리
			BufferedReader br = null;
			try {
				// conn.getResponseCode() 서버에서 상태코드를 알려주는 값
				int statusCode = conn.getResponseCode();
				System.out.println(statusCode);
				if (statusCode >= 200 && statusCode <= 300) {
					br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				} else {
					br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				}
				Document doc = parseXML(conn.getInputStream());
				// a. field 태그객체 목록으로 가져온다.
				NodeList descNodes = doc.getElementsByTagName("item");
				// b. Corona19Data List객체 생성

				// c. 각 item 태그의 자식태그에서 정보 가져오기
				for (int i = 0; i < descNodes.getLength(); i++) {
					// item
					Node item = descNodes.item(i);
					DustInfo DustInfo = new DustInfo();
					// item 자식태그에 순차적으로 접근
					for (Node node = item.getFirstChild(); node != null; node = node.getNextSibling()) {
						//System.out.println(node.getNodeName() + " : " + node.getTextContent());

						switch (node.getNodeName()) {
						case "clearVal":
							DustInfo.setClearVal(Integer.parseInt(node.getTextContent()));
							break;
						case "sn":
							DustInfo.setSn(Integer.parseInt(node.getTextContent()));
							break;
						case "districtName":
							DustInfo.setDistrictName(node.getTextContent());
							break;
						case "dataDate":
							DustInfo.setDataDate(node.getTextContent());
							break;
						case "issueVal":
							DustInfo.setIssueVal(Integer.parseInt(node.getTextContent()));
							break;
						case "issueTime":
							DustInfo.setIssueTime(node.getTextContent());
							break;
						case "clearDate":
							DustInfo.setClearDate(node.getTextContent());
							break;
						case "issueDate":
							DustInfo.setIssueDate(node.getTextContent());
							break;
						case "moveName":
							DustInfo.setMoveName(node.getTextContent());
							break;
						case "clearTime":
							DustInfo.setClearTime(node.getTextContent());
							break;
						case "issueGbn":
							DustInfo.setIssueGbn(node.getTextContent());
							break;
						case "itemCode":
							DustInfo.setItemCode(node.getTextContent());
							break;
						}
					}
					// d. List객체에 추가
					list.add(DustInfo);
				}
				// e.최종확인
				for (DustInfo data : list) {
					System.out.println(data);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return list;
		}
		// xml을 객체로 바꿔주는 역할
		public static Document parseXML(InputStream inputStream) {
			DocumentBuilderFactory objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder objDocumentBuilder = null;
			Document doc = null;
			try {
				objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();
				doc = objDocumentBuilder.parse(inputStream);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) { // Simple API for XML e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return doc;
		}
}
