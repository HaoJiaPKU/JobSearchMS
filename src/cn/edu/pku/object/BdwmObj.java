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
import cn.edu.pku.conf.BdwmConf;
import cn.edu.pku.util.TimeUtil;

public class BdwmObj extends AbstractObj {

	//pos_url, pos_publish_date + " " + id
	private static HashMap<String, String> virtualView
		= new HashMap<String, String>();
	//pos_url, pos_publish_date + " " + "-1"
	private static HashMap<String, BdwmObj> newData 
		= new HashMap<String, BdwmObj>();
	//pos_url, pos_publish_date + " " + id
	private static HashMap<String, String> updateData
		= new HashMap<String, String>();
	
	public BdwmObj() {
		super();
	}
	
	public BdwmObj(String postitle, String posSalary, String posLocation, String posPublishDate, String posType,
			String posExperience, String posDegree, String posRecruitNum, String posCategory, String posDescription,
			String posUrl, String comName, String comScale, String comType, String comIndustry, String comHost, String comLocation,
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
		this.comName = comName;
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

	public BdwmObj(BdwmObj b) {
		this.postitle = b.postitle;
		this.posSalary = b.posSalary;
		this.posLocation = b.posLocation;
		this.posPublishDate = b.posPublishDate;
		this.posType = b.posType;
		this.posExperience = b.posExperience;
		this.posDegree = b.posDegree;
		this.posRecruitNum = b.posRecruitNum;
		this.posCategory = b.posCategory;
		this.posDescription = b.posDescription;
		this.posUrl = b.posUrl;
		this.comName = b.comName;
		this.comScale = b.comScale;
		this.comType = b.comType;
		this.comIndustry = b.comIndustry;
		this.comHost = b.comHost;
		this.comLocation = b.comLocation;
		this.hasTag = b.hasTag;
		this.source = b.source;
		this.snapshotUrl = b.snapshotUrl;
		this.displayContent = b.displayContent;
	}
	
	/**
	 * 加载数据库视图缓存
	 * */
	public static void loadVirtualView() {
		virtualView.clear();
		newData.clear();
		updateData.clear();
		
		List list = new ArrayList<>();
		
		String url = DatabaseConf.getDatebaseurl();
		try {
			Class.forName(DatabaseConf.getClassname());
			Connection conn;
			try {
				conn = DriverManager.getConnection(url);
				String sql = "select "
						+ "id, pos_url, pos_publish_date "
						+ " from "
						+ DatabaseConf.getPositionbdwmtable() + " "
						+ " where "
						+ "source = '"
						+ BdwmConf.getSource() + "' "
						+ " and "
						+ "pos_publish_date > '"
						+ TimeUtil.getDate(DatabaseConf.getExpiredate()) + "';";

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
		    	delete(Long.parseLong(id));
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
		newData.clear();
		updateData.clear();
	}
		
	/**
	 * 将数据库视图缓存对象插入数据库
	 * */
	public static void insertVirtualViewData() {
		String url = DatabaseConf.getDatebaseurl();
		int counter = 0;
		try {
			Class.forName(DatabaseConf.getClassname());
			Connection conn;
			try {
				conn = DriverManager.getConnection(url);
				for (String key : newData.keySet()) {
					BdwmObj blobj = newData.get(key);
					String sql = "insert into "
							+ DatabaseConf.getPositionbdwmtable() + "("
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
							+ "com_name,"
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
							+ "'" + blobj.postitle + "',"
							+ "'" + blobj.posSalary + "',"
							+ "'" + blobj.posLocation + "',"
							+ "'" + blobj.posPublishDate + "',"
							+ "'" + blobj.posType + "',"
							+ "'" + blobj.posExperience + "',"
							+ "'" + blobj.posDegree + "',"
							+ "'" + blobj.posRecruitNum + "',"
							+ "'" + blobj.posCategory + "',"
							+ "'" + blobj.posDescription + "',"
							+ "'" + blobj.posUrl + "',"
							+ "'" + blobj.comName + "',"
							+ "'" + blobj.comScale + "',"
							+ "'" + blobj.comType + "',"
							+ "'" + blobj.comIndustry + "',"
							+ "'" + blobj.comHost + "',"
							+ "'" + blobj.comLocation + "',"
							+ "'" + blobj.hasTag + "',"
							+ "'" + BdwmConf.Source + "',"
							+ "'" + blobj.snapshotUrl + "',"
							+ "'" + blobj.displayContent + "');";
	
					PreparedStatement stmt;
					try {
						stmt = conn.prepareStatement(sql);
						try {
							stmt.executeUpdate();
							if (++ counter % 1000 == 0) {
								System.out.println(counter + " pieces of data inserted");
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
	 * 将待更新数据视图缓存更新到数据库
	 * */
	public static void updateDatebase() {
		String url = DatabaseConf.getDatebaseurl();
		int counter = 0;
		try {
			Class.forName(DatabaseConf.getClassname());
			Connection conn;
			try {
				conn = DriverManager.getConnection(url);
//				System.out.println("connection set up");
				for (String key : updateData.keySet()) {
					String str = updateData.get(key);
//					System.out.println(str);
					String pos_publish_date = str.substring(0, 10);
//					System.out.println(pos_publish_date);
					Long id = Long.parseLong(str.substring(10).trim());
//					System.out.println(id);
					String sql = "update "
							+ DatabaseConf.getPositionbdwmtable()
							+ " set "
							+ "pos_publish_date ='" + pos_publish_date + "' "
							+ " where "
							+ "id = " + id + ";";
//					System.out.println(sql);
					PreparedStatement stmt;
					try {
						stmt = conn.prepareStatement(sql);
						try {
							stmt.executeUpdate();
							if (++ counter % 1000 == 0) {
								System.out.println(counter + " pieces of data updated");
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
	 * 更新对象
	 * */
	public static void update(Long id, String key, String value) {
		String url = DatabaseConf.getDatebaseurl();
		try {
			Class.forName(DatabaseConf.getClassname());
			Connection conn;
			try {
				conn = DriverManager.getConnection(url);
				String sql = "update "
						+ DatabaseConf.getPositionbdwmtable()
						+ " set "
						+ key + "='" + value + "' "
						+ " where "
						+ "id = " + id + ";";

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
	public static void delete(Long id) {
		String url = DatabaseConf.getDatebaseurl();
		try {
			Class.forName(DatabaseConf.getClassname());
			Connection conn;
			try {
				conn = DriverManager.getConnection(url);
				String sql = "delete from "
						+ DatabaseConf.getPositionbdwmtable()
						+ " where "
						+ "id = " + id + ";";

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
				
				sql = "delete from "
						+ DatabaseConf.getPositiontagtable()
						+ " where "
						+ "recruitment_id = " + id + ";";

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
				
				sql = "delete from "
						+ DatabaseConf.getRelevancetable()
						+ " where "
						+ "recruitment_id = " + id + ";";

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
	public static void delete(String key, String value) {
		List list = new ArrayList<>();
		
		String url = DatabaseConf.getDatebaseurl();
		try {
			Class.forName(DatabaseConf.getClassname());
			Connection conn;
			try {
				conn = DriverManager.getConnection(url);
				
				String sql = "select "
						+ "id "
						+ " from "
						+ DatabaseConf.getPositionbdwmtable() + " "
						+ " where "
						+ "source = '"
						+ BdwmConf.getSource() + "' "
						+ " and "
						+ "pos_publish_date > '"
						+ TimeUtil.getDate(DatabaseConf.getExpiredate()) + "'"
						+ " and "
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
			delete(id);
		}
	}

	/**
	 * 判断某个对象是否存在于数据库中
	 * Warn : 代价太高建议不要使用
	 * */
	public boolean isExist() {
		String url = DatabaseConf.getDatebaseurl();
		try {
			Class.forName(DatabaseConf.getClassname());
			Connection conn;
			try {
				conn = DriverManager.getConnection(url);
				String sql = "select "
						+ "* "
						+ " from "
						+ DatabaseConf.getPositionbdwmtable()
						+ " where "
						+ "source = '"
						+ BdwmConf.getSource() + "' "
						+ " and "
						+ "pos_publish_date ='" + this.posPublishDate + "' "
						+ " and "
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
		String key = this.posUrl.substring(BdwmConf.HostUrl.length());
		String value = this.posPublishDate + " -1";//-1为了后面的index判断
//		System.out.println(key + " " + value);
		//如果没有当前的数据，直接加入到试图缓存和待插入的新数据中
		if (!virtualView.containsKey(key)) {
			virtualView.put(key, value);
			newData.put(key, new BdwmObj(this));
		} else {//存在当前的数据
			String preValue = virtualView.get(key);
			//如果历史数据的日期在当前数据之前
			if (preValue.compareTo(value) < 0) {
				//获得数据id
				long id = Long.parseLong(preValue.substring(11));
				//如果是新插入的数据，即数据库中没有这条数据，只需要更新视图缓存和待插入数据
				if (id == -1) {//-1
					virtualView.remove(key);
					newData.remove(key);
					virtualView.put(key, value);
					newData.put(key, new BdwmObj(this));
				//如果是原有历史数据，需要更新待更新数据视图和数据库视图缓存
				} else {
					//更新待更新数据视图
					if (updateData.containsKey(key)) {
						updateData.remove(key);
					}
					updateData.put(key, this.posPublishDate + " " + id);
					//update(id, "pos_publish_date", value.substring(0, 10));
					virtualView.remove(key);
					virtualView.put(key, this.posPublishDate + " " + id);
				}
			}
			//如果历史数据的日期在当前数据之后，什么也不做
		}
	}
	
	/**
	 * 执行数据对象到数据库的插入操作
	 * */
	public static void excuteStore() {
		System.out.println("update database: " + updateData.size());
		updateDatebase();
		System.out.println("insert new data: " + newData.size());
		insertVirtualViewData();
		clearVirtualView();
	}
	
	/**
	 * 将结果保存为list，因为:
	 * 1.ResultSet可能有最大数量限制；
	 * 2.需要在遍历的同时删除，以便节省空间;
	 * */
	public static List convertList(ResultSet rs) throws SQLException{
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
	public static void removeDuplicate() {
		HashMap<String, String> map = new HashMap<String, String>();
//		HashSet<Long> set = new HashSet<Long>();
		List list = new ArrayList<>();
		
		String url = DatabaseConf.getDatebaseurl();
		try {
			Class.forName(DatabaseConf.getClassname());
			Connection conn;
			try {
				conn = DriverManager.getConnection(url);
				String sql = "select "
						+ "id, pos_url, pos_publish_date "
						+ " from "
						+ DatabaseConf.getPositionbdwmtable() + " "
						+ " where "
						+ "source = '"
						+ BdwmConf.getSource() + "' "
						+ " and "
						+ "pos_publish_date > '"
						+ TimeUtil.getDate(DatabaseConf.getExpiredate()) + "';";

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
		    String key = hm.get("pos_url").toString().substring(BdwmConf.HostUrl.length());
		    String value = hm.get("pos_publish_date").toString();
		    String id = hm.get("id").toString();
		    value = value + " " + id;
			if (!map.containsKey(key)) {
				map.put(key, value);
			} else {
				String preValue = map.get(key);
				if (preValue.compareTo(value) < 0) {
					delete(Long.parseLong(preValue.substring(11)));
					map.remove(key);
					map.put(key, value);
				} else {
					delete(Long.parseLong(id));
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
		
		System.out.println(this.comName);
		System.out.println(this.comScale);
		System.out.println(this.comType);
		System.out.println(this.comIndustry);
		System.out.println(this.comHost);
		System.out.println(this.comLocation);
		
		System.out.println(this.snapshotUrl);
		System.out.println();
	}

}
