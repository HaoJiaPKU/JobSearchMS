package object;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.jdbc.PreparedStatement;

public class ZhilianObj {

	private String postitle = new String ();
	private String posSalary = new String ();
	private String posLocation = new String ();
	private String posPublishDate = new String ();
	private String posType = new String ();
	private String posExperience = new String ();
	private String posDgree = new String ();
	private String posRecruitNum = new String ();
	private String posCategory = new String ();
	private String posDescription = new String ();
	private String posUrl = new String ();
	
	private String comScale = new String ();
	private String comType = new String ();
	private String comIndustry = new String ();
	private String comHost = new String ();
	private String comLocation = new String ();
	
	private String source = "智联招聘";
	private String snapshotUrl = new String ();
	
	public ZhilianObj() {
		
	}
	
	public void saveZhilianObj() {
		String url = "jdbc:mysql://162.105.30.30:3306/jobsearch?"
				+ "user=root&password=seke1726&useUnicode=true&characterEncoding=UTF-8";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn;
			try {
				conn = DriverManager.getConnection(url);
				String sql = "insert into recruitment_("
						+ "pos_title,"
						+ "pos_salary,"
						+ "pos_location,"
						+ "pos_publish_date,"
						+ "pos_type,"
						+ "pos_experience,"
						+ "pos_dgree,"
						+ "pos_recruit_num,"
						+ "pos_category,"
						+ "pos_description,"
						+ "pos_url,"
						+ "com_scale,"
						+ "com_type,"
						+ "com_industry,"
						+ "com_host,"
						+ "com_location,"
						+ "source)"
						+ " values("
						+ "'" + this.postitle + "',"
						+ "'" + this.posSalary + "',"
						+ "'" + this.posLocation + "',"
						+ "'" + this.posPublishDate + "',"
						+ "'" + this.posType + "',"
						+ "'" + this.posExperience + "',"
						+ "'" + this.posDgree + "',"
						+ "'" + this.posRecruitNum + "',"
						+ "'" + this.posCategory + "',"
						+ "'" + this.posDescription + "',"
						+ "'" + this.posUrl + "',"
						+ "'" + this.comScale + "',"
						+ "'" + this.comType + "',"
						+ "'" + this.comIndustry + "',"
						+ "'" + this.comHost + "',"
						+ "'" + this.comLocation + "',"
						+ "'" + this.source + "');";
				System.out.println(sql);

				java.sql.PreparedStatement stmt;
				try {
					stmt = conn.prepareStatement(sql);
					try {
						stmt.executeUpdate();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printObj() {
		System.out.println(this.postitle);
		System.out.println(this.posSalary);
		System.out.println(this.posLocation);
		System.out.println(this.posPublishDate);
		System.out.println(this.posType);
		System.out.println(this.posExperience);
		System.out.println(this.posDgree);
		System.out.println(this.posRecruitNum);
		System.out.println(this.posCategory);
		System.out.println(this.posDescription);
		System.out.println(this.posUrl);
		
		System.out.println(this.comScale);
		System.out.println(this.comType);
		System.out.println(this.comIndustry);
		System.out.println(this.comHost);
		System.out.println(this.comLocation);
		
		System.out.println(this.snapshotUrl);
		System.out.println(this.source);
		System.out.println();
	}
	
	public String getPosSalary() {
		return posSalary;
	}
	
	public void setPosSalary(String posSalary) {
		this.posSalary = posSalary;
	}
	
	public String getPosLocation() {
		return posLocation;
	}
	
	public void setPosLocation(String posLocation) {
		this.posLocation = posLocation;
	}
	
	public String getPosPublishDate() {
		return posPublishDate;
	}
	
	public void setPosPublishDate(String posPublishDate) {
		this.posPublishDate = posPublishDate;
	}
	
	public String getPosType() {
		return posType;
	}
	
	public void setPosType(String posType) {
		this.posType = posType;
	}
	
	public String getPosExperience() {
		return posExperience;
	}
	
	public void setPosExperience(String posExperience) {
		this.posExperience = posExperience;
	}
	
	public String getPosDgree() {
		return posDgree;
	}
	
	public void setPosDgree(String posDgree) {
		this.posDgree = posDgree;
	}
	
	public String getPosRecruitNum() {
		return posRecruitNum;
	}
	
	public void setPosRecruitNum(String posRecruitNum) {
		this.posRecruitNum = posRecruitNum;
	}
	
	public String getPosCategory() {
		return posCategory;
	}
	
	public void setPosCategory(String posCategory) {
		this.posCategory = posCategory;
	}
	
	public String getComScale() {
		return comScale;
	}
	
	public void setComScale(String comScale) {
		this.comScale = comScale;
	}
	
	public String getComType() {
		return comType;
	}
	
	public void setComType(String comType) {
		this.comType = comType;
	}
	
	public String getComIndustry() {
		return comIndustry;
	}
	
	public void setComIndustry(String comIndustry) {
		this.comIndustry = comIndustry;
	}
	
	public String getComHost() {
		return comHost;
	}
	
	public void setComHost(String comHost) {
		this.comHost = comHost;
	}
	
	public String getComLocation() {
		return comLocation;
	}
	
	public void setComLocation(String comLocation) {
		this.comLocation = comLocation;
	}

	public String getPosDescription() {
		return posDescription;
	}

	public void setPosDescription(String posDescription) {
		this.posDescription = posDescription;
	}

	public String getPostitle() {
		return postitle;
	}

	public void setPostitle(String postitle) {
		this.postitle = postitle;
	}

	public String getPosUrl() {
		return posUrl;
	}

	public void setPosUrl(String posUrl) {
		this.posUrl = posUrl;
	}


	public String getSource() {
		return source;
	}
	

	public void setSource(String source) {
		this.source = source;
	}
	

	public String getSnapshotUrl() {
		return snapshotUrl;
	}
	

	public void setSnapshotUrl(String snapshotUrl) {
		this.snapshotUrl = snapshotUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comHost == null) ? 0 : comHost.hashCode());
		result = prime * result + ((comIndustry == null) ? 0 : comIndustry.hashCode());
		result = prime * result + ((comLocation == null) ? 0 : comLocation.hashCode());
		result = prime * result + ((comScale == null) ? 0 : comScale.hashCode());
		result = prime * result + ((comType == null) ? 0 : comType.hashCode());
		result = prime * result + ((posCategory == null) ? 0 : posCategory.hashCode());
		result = prime * result + ((posDescription == null) ? 0 : posDescription.hashCode());
		result = prime * result + ((posDgree == null) ? 0 : posDgree.hashCode());
		result = prime * result + ((posExperience == null) ? 0 : posExperience.hashCode());
		result = prime * result + ((posLocation == null) ? 0 : posLocation.hashCode());
		result = prime * result + ((posPublishDate == null) ? 0 : posPublishDate.hashCode());
		result = prime * result + ((posRecruitNum == null) ? 0 : posRecruitNum.hashCode());
		result = prime * result + ((posSalary == null) ? 0 : posSalary.hashCode());
		result = prime * result + ((posType == null) ? 0 : posType.hashCode());
		result = prime * result + ((posUrl == null) ? 0 : posUrl.hashCode());
		result = prime * result + ((postitle == null) ? 0 : postitle.hashCode());
		result = prime * result + ((snapshotUrl == null) ? 0 : snapshotUrl.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ZhilianObj other = (ZhilianObj) obj;
		if (comHost == null) {
			if (other.comHost != null)
				return false;
		} else if (!comHost.equals(other.comHost))
			return false;
		if (comIndustry == null) {
			if (other.comIndustry != null)
				return false;
		} else if (!comIndustry.equals(other.comIndustry))
			return false;
		if (comLocation == null) {
			if (other.comLocation != null)
				return false;
		} else if (!comLocation.equals(other.comLocation))
			return false;
		if (comScale == null) {
			if (other.comScale != null)
				return false;
		} else if (!comScale.equals(other.comScale))
			return false;
		if (comType == null) {
			if (other.comType != null)
				return false;
		} else if (!comType.equals(other.comType))
			return false;
		if (posCategory == null) {
			if (other.posCategory != null)
				return false;
		} else if (!posCategory.equals(other.posCategory))
			return false;
		if (posDescription == null) {
			if (other.posDescription != null)
				return false;
		} else if (!posDescription.equals(other.posDescription))
			return false;
		if (posDgree == null) {
			if (other.posDgree != null)
				return false;
		} else if (!posDgree.equals(other.posDgree))
			return false;
		if (posExperience == null) {
			if (other.posExperience != null)
				return false;
		} else if (!posExperience.equals(other.posExperience))
			return false;
		if (posLocation == null) {
			if (other.posLocation != null)
				return false;
		} else if (!posLocation.equals(other.posLocation))
			return false;
		if (posPublishDate == null) {
			if (other.posPublishDate != null)
				return false;
		} else if (!posPublishDate.equals(other.posPublishDate))
			return false;
		if (posRecruitNum == null) {
			if (other.posRecruitNum != null)
				return false;
		} else if (!posRecruitNum.equals(other.posRecruitNum))
			return false;
		if (posSalary == null) {
			if (other.posSalary != null)
				return false;
		} else if (!posSalary.equals(other.posSalary))
			return false;
		if (posType == null) {
			if (other.posType != null)
				return false;
		} else if (!posType.equals(other.posType))
			return false;
		if (posUrl == null) {
			if (other.posUrl != null)
				return false;
		} else if (!posUrl.equals(other.posUrl))
			return false;
		if (postitle == null) {
			if (other.postitle != null)
				return false;
		} else if (!postitle.equals(other.postitle))
			return false;
		if (snapshotUrl == null) {
			if (other.snapshotUrl != null)
				return false;
		} else if (!snapshotUrl.equals(other.snapshotUrl))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}
}
