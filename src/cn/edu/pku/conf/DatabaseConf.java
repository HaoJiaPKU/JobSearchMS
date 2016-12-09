package cn.edu.pku.conf;

public class DatabaseConf {

	private static final String DateBaseUrl = "jdbc:mysql://162.105.30.30:3306/jobsearch?"
			+ "user=root&password=seke1726&useUnicode=true&characterEncoding=UTF-8";
	private static final String ClassName = "com.mysql.jdbc.Driver";
	private static final String PositionTable = "recruitment_v1";
	private static final String PositionTagTable = "recruitment_tag";
	private static final String RelevanceTable = "relevance";
	
	private static final int ExpireDate = -90;
	private static final int CrawlDate = 0;
	private static final int ParseDate = 0;
	
	public static String getDatebaseurl() {
		return DateBaseUrl;
	}
	
	public static String getClassname() {
		return ClassName;
	}
	
	public static String getPositiontable() {
		return PositionTable;
	}

	
	
	public static String getPositiontagtable() {
		return PositionTagTable;
	}

	public static String getRelevancetable() {
		return RelevanceTable;
	}

	public static int getExpiredate() {
		return ExpireDate;
	}

	
	public static int getCrawldate() {
		return CrawlDate;
	}

	public static int getParsedate() {
		return ParseDate;
	}
}
