package conf;

public class DatabaseConf {

	private static final String DateBaseUrl = "jdbc:mysql://162.105.30.30:3306/jobsearch?"
			+ "user=root&password=seke1726&useUnicode=true&characterEncoding=UTF-8";
	private static final String ClassName = "com.mysql.jdbc.Driver";
	private static final String StoreTable = "recruitment_v1";
	
	private static final int ExpireDate = -90;
	private static final int OnWorkingDate = 0;
	
	public static String getDatebaseurl() {
		return DateBaseUrl;
	}
	
	public static String getClassname() {
		return ClassName;
	}
	
	public static String getStoretable() {
		return StoreTable;
	}

	public static int getExpiredate() {
		return ExpireDate;
	}

	public static int getOnworkingdate() {
		return OnWorkingDate;
	}
}
