package cn.myapps.core.user.ejb;

import cn.myapps.core.user.action.WebUser;

/**
 * 预览用户
 * @author xiuwei
 *
 */
public class PreviewUser extends WebUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9139480846494191507L;
	/*皮肤*/
	private String skinType;

	public PreviewUser(BaseUser vo) throws Exception {
		super(vo);
	}

	public String getSkinType() {
		return skinType;
	}

	public void setSkinType(String skinType) {
		this.skinType = skinType;
	}

}
