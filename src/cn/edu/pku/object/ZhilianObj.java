package cn.edu.pku.object;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.edu.pku.conf.DatabaseConf;
import cn.edu.pku.conf.ZhilianConf;

public class ZhilianObj {

	private String postitle = new String ();
	private String posSalary = new String ();
	private String posLocation = new String ();
	private String posPublishDate = new String ();
	private String posType = new String ();
	private String posExperience = new String ();
	private String posDegree = new String ();
	private String posRecruitNum = new String ();
	private String posCategory = new String ();
	private String posDescription = new String ();
	private String posUrl = new String ();
	
	private String comScale = new String ();
	private String comType = new String ();
	private String comIndustry = new String ();
	private String comHost = new String ();
	private String comLocation = new String ();
	
	private int hasTag = 0;	
	private String source = new String ();
	private String snapshotUrl = new String ();
	private String displayContent = new String ();
	
	private static HashMap<String, String> virtualView = new HashMap<String, String>();
	private static HashMap<String, ZhilianObj> addSet = new HashMap<String, ZhilianObj>();
	
	public ZhilianObj() {
		
	}
	
	public ZhilianObj(String postitle, String posSalary, String posLocation, String posPublishDate, String posType,
			String posExperience, String posDegree, String posRecruitNum, String posCategory, String posDescription,
			String posUrl, String comScale, String comType, String comIndustry, String comHost, String comLocation,
			int hasTag, String source, String snapshotUrl, String displayContent) {
		super();
		this.postitle = postitle;
		this.posSalary = posSalary;
		this.posLocation = posLocation;
		this.posPublishDate = posPublishDate;
		this.posType = posType;
		this.posExperience = posExperience;
		this.posDegree = posDegree;
		this.posRecruitNum = posRecruitNum;
		this.posCategory = posCategory;
		this.posDescription = posDescription;
		this.posUrl = posUrl;
		this.comScale = comScale;
		this.comType = comType;
		this.comIndustry = comIndustry;
		this.comHost = comHost;
		this.comLocation = comLocation;
		this.hasTag = hasTag;
		this.source = source;
		this.snapshotUrl = snapshotUrl;
		this.displayContent = displayContent;
	}

	public ZhilianObj(ZhilianObj z) {
		this.postitle = z.postitle;
		this.posSalary = z.posSalary;
		this.posLocation = z.posLocation;
		this.posPublishDate = z.posPublishDate;
		this.posType = z.posType;
		this.posExperience = z.posExperience;
		this.posDegree = z.posDegree;
		this.posRecruitNum = z.posRecruitNum;
		this.posCategory = z.posCategory;
		this.posDescription = z.posDescription;
		this.posUrl = z.posUrl;
		this.comScale = z.comScale;
		this.comType = z.comType;
		this.comIndustry = z.comIndustry;
		this.comHost = z.comHost;
		this.comLocation = z.comLocation;
		this.hasTag = z.hasTag;
		this.source = z.source;
		this.snapshotUrl = z.snapshotUrl;
		this.displayContent = z.displayContent;
	}
	
	/**
	 * 加载数据库视图缓存
	 * */
	public static void loadVirtualView() {
		virtualView.clear();
		addSet.clear();
		
		List list = new ArrayList<>();
		
		String url = DatabaseConf.getDatebaseurl();
		try {
			Class.forName(DatabaseConf.getClassname());
			Connection conn;
			try {
				conn = DriverManager.getConnection(url);
				String sql = "select id, pos_url, pos_publish_date from "
						+ DatabaseConf.getPositiontable() + ";";

				PreparedStatement stmt;
				try {
					stmt = conn.prepareStatement(sql);
					ResultSet rs = stmt.executeQuery(sql);
					try {
						list = convertList(rs);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Iterator it = list.iterator();  
		while(it.hasNext()) {   
		    Map hm = (Map)it.next();
		    String key = hm.get("pos_url").toString().substring(24);
		    String value = hm.get("pos_publish_date").toString();
		    String id = hm.get("id").toString();
		    value = value + " " + id;
		    if (value.length() <= 10) {
		    	deleteZhilianObj(Long.parseLong(id));
		    	continue;
		    }
		    virtualView.put(key, value);
		}  
	}
	
	/**
	 * 清除数据库视图缓存
	 * */
	public static void clearVirtualView() {
		virtualView.clear();
		addSet.clear();
	}
		
	/**
	 * 将数据库视图缓存对象插入数据库
	 * */
	public static void insertZhilianObjs() {
		String url = DatabaseConf.getDatebaseurl();
		try {
			Class.forName(DatabaseConf.getClassname());
			Connection conn;
			try {
				conn = DriverManager.getConnection(url);
				for (String key : addSet.keySet()) {
					ZhilianObj zlobj = addSet.get(key);
					String sql = "insert into " + DatabaseConf.getPositiontable() + "("
							+ "pos_title,"
							+ "pos_salary,"
							+ "pos_location,"
							+ "pos_publish_date,"
							+ "pos_type,"
							+ "pos_experience,"
							+ "pos_degree,"
							+ "pos_recruit_num,"
							+ "pos_category,"
							+ "pos_description,"
							+ "pos_url,"
							+ "com_scale,"
							+ "com_type,"
							+ "com_industry,"
							+ "com_host,"
							+ "com_location,"
							+ "has_tag,"
							+ "source,"
							+ "snapshot_url,"
							+ "display_content)"
							+ " values("
							+ "'" + zlobj.postitle + "',"
							+ "'" + zlobj.posSalary + "',"
							+ "'" + zlobj.posLocation + "',"
							+ "'" + zlobj.posPublishDate + "',"
							+ "'" + zlobj.posType + "',"
							+ "'" + zlobj.posExperience + "',"
							+ "'" + zlobj.posDegree + "',"
							+ "'" + zlobj.posRecruitNum + "',"
							+ "'" + zlobj.posCategory + "',"
							+ "'" + zlobj.posDescription + "',"
							+ "'" + zlobj.posUrl + "',"
							+ "'" + zlobj.comScale + "',"
							+ "'" + zlobj.comType + "',"
							+ "'" + zlobj.comIndustry + "',"
							+ "'" + zlobj.comHost + "',"
							+ "'" + zlobj.comLocation + "',"
							+ "'" + zlobj.hasTag + "',"
							+ "'" + ZhilianConf.Source + "',"
							+ "'" + zlobj.snapshotUrl + "',"
							+ "'" + zlobj.displayContent + "');";
	
					PreparedStatement stmt;
					try {
						stmt = conn.prepareStatement(sql);
						try {
							stmt.executeUpdate();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						stmt.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 删除指定对象，同时会删除关联表里相同id的数据
	 * @param id 指定id
	 * */
	public static void deleteZhilianObj(Long id) {
		String url = DatabaseConf.getDatebaseurl();
		try {
			Class.forName(DatabaseConf.getClassname());
			Connection conn;
			try {
				conn = DriverManager.getConnection(url);
				String sql = "delete from " + DatabaseConf.getPositiontable()
					+ " where id = " + id + ";";

				PreparedStatement stmt;
				try {
					stmt = conn.prepareStatement(sql);
					try {
						stmt.executeUpdate();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				sql = "delete from " + DatabaseConf.getPositiontagtable()
				+ " where recruitment_id = " + id + ";";

				try {
					stmt = conn.prepareStatement(sql);
					try {
						stmt.executeUpdate();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				sql = "delete from " + DatabaseConf.getRelevancetable()
				+ " where recruitment_id = " + id + ";";

				try {
					stmt = conn.prepareStatement(sql);
					try {
						stmt.executeUpdate();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 删除指定对象，需要注意删除关联表里相同id的数据
	 * @param key 指定键
	 * @param value 指定值
	 * */
	public static void deleteZhilianObjs(String key, String value) {
		List list = new ArrayList<>();
		
		String url = DatabaseConf.getDatebaseurl();
		try {
			Class.forName(DatabaseConf.getClassname());
			Connection conn;
			try {
				conn = DriverManager.getConnection(url);
				
				String sql = "select id from "
						+ DatabaseConf.getPositiontable() + " "
						+ " where "
						+ key + " = '"
						+ value + "';";

				PreparedStatement stmt;
				try {
					stmt = conn.prepareStatement(sql);
					ResultSet rs = stmt.executeQuery(sql);
					try {
						list = convertList(rs);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HashSet<Long> removeItemsId = new HashSet<Long> ();
		Iterator it = list.iterator();  
		while(it.hasNext()) {   
		    Map hm = (Map)it.next();
		    String id = hm.get("id").toString();
		    removeItemsId.add(Long.parseLong(id));
		} 
		for (Long id : removeItemsId) {
			deleteZhilianObj(id);
		}
	}

	/**
	 * 判断某个对象是否存在于数据库中
	 * */
	public boolean isExist() {
		String url = DatabaseConf.getDatebaseurl();
		try {
			Class.forName(DatabaseConf.getClassname());
			Connection conn;
			try {
				conn = DriverManager.getConnection(url);
				String sql = "select * from " + DatabaseConf.getPositiontable() + " where "
						 + "pos_publish_date ='" + this.posPublishDate + "' and "
						 + "pos_url = '" + this.posUrl + "';";

				Statement stmt = conn.createStatement();;
				try {
					ResultSet rs = stmt.executeQuery(sql);
					try {
						if (rs.next()) {
							return true;
						} else {
							return false;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
		
	/**
	 * 数据对象预存储到数据库缓存，同时判断数据库与当前数据是否过期
	 * */
	public void preStore() {
		String key = this.posUrl.substring(ZhilianConf.HostUrl.length());
		String value = this.posPublishDate + " 0000000000";
		System.out.println(key + " " + value);
		if (!virtualView.containsKey(key)) {
			virtualView.put(key, value);
			addSet.put(key, new ZhilianObj(this));
		} else {
			String preValue = virtualView.get(key);
			if (preValue.compareTo(value) < 0) {
				long index = Long.parseLong(preValue.substring(11));
				if (index == 0) {
					virtualView.remove(key);
					addSet.remove(key);
				} else {
					deleteZhilianObj(index);
					virtualView.remove(key);
				}
				virtualView.put(key, value);
				addSet.put(key, new ZhilianObj(this));
			}
		}
	}
	
	/**
	 * 执行数据对象到数据库的插入操作
	 * */
	public static void excuteStore() {
		insertZhilianObjs();
		clearVirtualView();
	}
	
	/**
	 * 将结果保存为list，因为:
	 * 1.ResultSet可能有最大数量限制；
	 * 2.需要在遍历的同时删除，以便节省空间;
	 * */
	private static List convertList(ResultSet rs) throws SQLException{
		List list = new ArrayList();
		ResultSetMetaData md = rs.getMetaData();//获取键名
		int columnCount = md.getColumnCount();//获取行的数量
		while (rs.next()) {
			Map rowData = new HashMap();//声明Map
			for (int i = 1; i <= columnCount; i ++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));//获取键名及值
			}
			list.add(rowData);
		}
		return list;
	}
	
	/**
	 * 删除数据库中的重复数据，仅以URL作为判断标准，时间不同的情况下保留最近的数据
	 * 如果在某条数据处住，直接删除数据，可能存在使线程挂起的字符，暂时不明原因
	 * */
	public static void removeDuplicateObject() {
		HashMap<String, String> map = new HashMap<String, String>();
//		HashSet<Long> set = new HashSet<Long>();
		List list = new ArrayList<>();
		
		String url = DatabaseConf.getDatebaseurl();
		try {
			Class.forName(DatabaseConf.getClassname());
			Connection conn;
			try {
				conn = DriverManager.getConnection(url);
				String sql = "select id, pos_url, pos_publish_date from "
						+ DatabaseConf.getPositiontable() + ";";

				PreparedStatement stmt;
				try {
					stmt = conn.prepareStatement(sql);
					ResultSet rs = stmt.executeQuery(sql);
					try {
//						System.out.println(rs.getFetchSize());
						list = convertList(rs);
//						System.out.println(list.size());
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		Iterator it = list.iterator();   
		while(it.hasNext()) {   
		    Map hm = (Map)it.next();
		    String key = hm.get("pos_url").toString().substring(ZhilianConf.HostUrl.length());
		    String value = hm.get("pos_publish_date").toString();
		    String id = hm.get("id").toString();
		    value = value + " " + id;
			if (!map.containsKey(key)) {
				map.put(key, value);
			} else {
				String preValue = map.get(key);
				if (preValue.compareTo(value) < 0) {
					deleteZhilianObj(Long.parseLong(preValue.substring(11)));
					map.remove(key);
					map.put(key, value);
				} else {
					deleteZhilianObj(Long.parseLong(id));
				}
			}
		}
	}
	
	/**
	 * 打印数据对象
	 * */
	public void printObj() {
		System.out.println(this.postitle);
		System.out.println(this.posSalary);
		System.out.println(this.posLocation);
		System.out.println(this.posPublishDate);
		System.out.println(this.posType);
		System.out.println(this.posExperience);
		System.out.println(this.posDegree);
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
	
	public String getPosDegree() {
		return posDegree;
	}
	
	public void setPosDegree(String posDegree) {
		this.posDegree = posDegree;
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

	public String getSnapshotUrl() {
		return snapshotUrl;
	}
	
	public void setSnapshotUrl(String snapshotUrl) {
		this.snapshotUrl = snapshotUrl;
	}

	
	public int getHasTag() {
		return hasTag;
	}

	
	public void setHasTag(int hasTag) {
		this.hasTag = hasTag;
	}

	
	public String getSource() {
		return source;
	}

	
	public void setSource(String source) {
		this.source = source;
	}

	
	public String getDisplayContent() {
		return displayContent;
	}

	
	public void setDisplayContent(String displayContent) {
		this.displayContent = displayContent;
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
		result = prime * result + ((displayContent == null) ? 0 : displayContent.hashCode());
		result = prime * result + hasTag;
		result = prime * result + ((posCategory == null) ? 0 : posCategory.hashCode());
		result = prime * result + ((posDescription == null) ? 0 : posDescription.hashCode());
		result = prime * result + ((posDegree == null) ? 0 : posDegree.hashCode());
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
		if (displayContent == null) {
			if (other.displayContent != null)
				return false;
		} else if (!displayContent.equals(other.displayContent))
			return false;
		if (hasTag != other.hasTag)
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
		if (posDegree == null) {
			if (other.posDegree != null)
				return false;
		} else if (!posDegree.equals(other.posDegree))
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
