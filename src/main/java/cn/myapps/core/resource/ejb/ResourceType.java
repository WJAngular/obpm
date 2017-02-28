package cn.myapps.core.resource.ejb;

public class ResourceType {
	
	
	public static final String ACTION_TYPE_NONE = "00";
	
	public static final String ACTION_TYPE_VIEW = "01";
	
	public static final String ACTION_TYPE_ACTIONCLASS = "02";
	
	public static final String ACTION_TYPE_OTHERURL = "03";
	
	public static final String ACTION_TYPE_REPORT = "04";
	
	public static final String RESOURCE_TYPE_MENU = "00";
	
	public static final String RESOURCE_TYPE_PAGE = "01";
	
	public static final String RESOURCE_TYPE_IMP = "09";

	public static final String RESOURCE_TYPE_EXCIMP = "08";
	
	public static final String RESOURCE_TYPE_HTML = "10";
	
	public static final String ACTION_TYPE_IMP = "05";
	
	public static final String RESOURCE_TYPE_MOBILE = "100";
	
	public static final String ISPROTECTED_YES = "yes";
	
	public static final String ISPROTECTED_NO = "no";
	
	public static final String[] TYPES = { "", "00", "01", "100"};

	public static final String[] NAMES = { "", "menu", "page", "Mobile"};
	
	public static final String[] MOBILEICOS = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15"};

	public static final String[] ICOTYPES = {"0001","001","010","0010","0100","011","100","101","111","1000","1001","1010","1011","1100","1110"};
	/**
	 * Get the type name.
	 * @param type The type.
	 * @return The type name.
	 */
	public static String getName(String type) {
		if (type == null)
			return null;

		for (int i = 0; i < TYPES.length; i++)
			if (type.equals(TYPES[i]))
				return NAMES[i];

		return "";
	}

	/**
	 * Get the type code.
	 * @param name The type name.
	 * @return The type code.
	 */
	public static String getType(String name) {
		if (name == null)
			return null;

		for (int i = 0; i < NAMES.length; i++)
			if (name.equals(TYPES[i]))
				return TYPES[i];

		return "";
	}
}
