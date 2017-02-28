<%!
  public static OracleConnection getConnection() throws Exception{ 
	  Connection conn = null;

	  PropertyUtil.load("report");
      String drv = PropertyUtil.get("REPORT_DRIVER");
      String url=PropertyUtil.get("REPORT_CONN_URL");
      String usr = PropertyUtil.get("REPORT_USER");
      String psw = PropertyUtil.get("REPORT_PWD");
      PropertyUtil.clear();
      
	  Driver d = (Driver) Class.forName(drv).newInstance();
	  DriverManager.registerDriver(d);
	  java.util.Properties prop = new java.util.Properties();
	  prop.setProperty("user", usr);
	  prop.setProperty("password", psw);
	  OracleConnection connoracle = null;
	  String procestring = "";

	  connoracle = (OracleConnection) DriverManager.getConnection(url, prop);

	  return connoracle;
  }
%>