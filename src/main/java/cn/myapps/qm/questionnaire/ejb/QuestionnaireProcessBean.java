package cn.myapps.qm.questionnaire.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.qm.answer.ejb.AnswerProcessBean;
import cn.myapps.qm.answer.ejb.AnswerVO;
import cn.myapps.qm.base.dao.BaseDAO;
import cn.myapps.qm.base.dao.DaoManager;
import cn.myapps.qm.base.ejb.AbstractBaseProcessBean;
import cn.myapps.qm.notification.QuestionnaireNotificationService;
import cn.myapps.qm.notification.QuestionnaireNotificationServiceImpl;
import cn.myapps.qm.questionnaire.dao.AbstractQuestionnaireDAO;
import cn.myapps.qm.questionnaire.dao.QuestionnaireDAO;
import cn.myapps.qm.util.CensusUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;

public class QuestionnaireProcessBean extends
		AbstractBaseProcessBean<QuestionnaireVO> implements
		QuestionnaireProcess {

	public ValueObject doUpdate(ValueObject vo) throws Exception {
		return ((QuestionnaireDAO) getDAO()).update(vo);
	}

	public void doRemove(String pk) throws Exception {
		AnswerProcessBean answerProcessBean = new AnswerProcessBean();
		DataPackage<AnswerVO> datas = answerProcessBean
				.doViewForQuestionnaire(pk);
		if (datas != null) {
			for (Iterator<AnswerVO> iter = datas.datas.iterator(); iter
					.hasNext();) {
				AnswerVO answer = iter.next();
				answerProcessBean.doRemove(answer.getId());
			}
		}

		((QuestionnaireDAO) getDAO()).delete(pk);
	}

	@Override
	public void doRemove(String[] pks) throws Exception {
		super.doRemove(pks);
	}

	@Override
	public Collection<QuestionnaireVO> doSimpleQuery(ParamsTable params,
			WebUser user) throws Exception {
		return null;
	}

    public ValueObject doSave(ValueObject vo) throws Exception {
	try {
	    QuestionnaireVO questionnaireVO = (QuestionnaireVO) vo;
	    int score = CensusUtil.totalScore((QuestionnaireVO) vo);
	    questionnaireVO.setScore(score);

	    UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);

	    String scope = questionnaireVO.getScope();
	    Set<String> idBuffer = new HashSet<String>();
	    Set<String> nameBuffer = new HashSet<String>();
	    if (QuestionnaireVO.SCOPE_USER.equalsIgnoreCase(scope)) {
		
		//添加相应角色的用户信息
		idBuffer.add(questionnaireVO.getOwnerIds());
		nameBuffer.add(questionnaireVO.getOwnerNames());
		
	    } else if (QuestionnaireVO.SCOPE_ROLE.equalsIgnoreCase(scope)) {
		String roleId = questionnaireVO.getOwnerIds();
		
		//添加相应角色的用户信息
		for (String role : roleId.split(";")) {
		    if (StringUtil.isBlank(role))
			continue;
		    Collection<UserVO> users = userProcess.queryByRole(role);
		    for (UserVO user : users) {
			idBuffer.add(user.getId());
			nameBuffer.add(user.getName());
		    }
		}

	    } else if (QuestionnaireVO.SCOPE_DEPT.equalsIgnoreCase(scope)) {
		String deptId = questionnaireVO.getOwnerIds();
		
		//添加相应部门的用户信息
		for (String dept : deptId.split(";")) {
		    if (StringUtil.isBlank(dept))
			continue;
		    Collection<UserVO> users = userProcess.queryByDepartment(dept);
		    for (UserVO user : users) {
			idBuffer.add(user.getId());
			nameBuffer.add(user.getName());
		    }
		}

	    } else if (QuestionnaireVO.SCOPE_DEPTANDROLE.equalsIgnoreCase(scope)) {
		String roleIdAnddeptId = questionnaireVO.getOwnerIds();

		//添加相应部门的用户信息
		String deptId = roleIdAnddeptId.split(";;")[0];
		for (String dept : deptId.split(";")) {
		    if (StringUtil.isBlank(dept))
			continue;
		    Collection<UserVO> users = userProcess.queryByDepartment(dept);
		    for (UserVO user : users) {
			idBuffer.add(user.getId());
			nameBuffer.add(user.getName());
		    }
		}
		
		//添加相应角色的用户信息
		String roleId = roleIdAnddeptId.split(";;")[1];
		for (String role : roleId.split(";")) {
		    if (StringUtil.isBlank(role))
			continue;
		    Collection<UserVO> users = userProcess.queryByRole(role);
		    for (UserVO user : users) {
			idBuffer.add(user.getId());
			nameBuffer.add(user.getName());
		    }
		}

	    }

	    StringBuffer id = new StringBuffer();
	    StringBuffer name = new StringBuffer();
	    for(String idStr:idBuffer){
		id.append(idStr).append(";");
	    }
	    for(String nameStr:nameBuffer){
		name.append(nameStr).append(";");
	    }
	    questionnaireVO.setActorIds(id.toString().substring(0,id.toString().length() - 1));
	    questionnaireVO.setActorNames(name.toString().substring(0,name.toString().length() - 1));

	    if(QuestionnaireVO.STATUS_PUBLISH == questionnaireVO.getStatus())
	    	questionnaireVO.setPublishDate(new Date());
	    
	    if (StringUtil.isBlank(questionnaireVO.getId())) {
		questionnaireVO.setId(Sequence.getSequence());
		questionnaireVO = (QuestionnaireVO) doCreate(questionnaireVO);
	    } else {
		questionnaireVO = (QuestionnaireVO) doUpdate(questionnaireVO);
	    }
    	QuestionnaireNotificationService questionnaireNotificationService = new QuestionnaireNotificationServiceImpl();
	    if(QuestionnaireVO.STATUS_PUBLISH == questionnaireVO.getStatus()){
			//发送消息
			questionnaireNotificationService.publish(questionnaireVO);
	    }else if(QuestionnaireVO.STATUS_RECOVER == questionnaireVO.getStatus()){
			questionnaireNotificationService.completeQuestionnaire(questionnaireVO);
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
	return vo;
    }

	@Override
	public ValueObject doCreate(ValueObject vo) throws Exception {
		((QuestionnaireVO) vo).setCreateDate(new Date());
		return super.doCreate(vo);
	}

	@Override
	public BaseDAO getDAO() throws Exception {
		return DaoManager.getQuestionnaireDAO(getConnection());
	}

	@Override
	public DataPackage<QuestionnaireVO> doQuery(ParamsTable params, WebUser user)
			throws Exception {
		DataPackage<QuestionnaireVO> datas = ((QuestionnaireDAO) getDAO())
				.query(user);
		return datas;
	}

	public DataPackage<QuestionnaireVO> doQuery(WebUser user) throws Exception {
		DataPackage<QuestionnaireVO> datas = ((QuestionnaireDAO) getDAO())
				.query(user);
		return datas;
	}

	public QuestionnaireVO doShowResult(String pk, WebUser user)
			throws Exception {
		QuestionnaireVO vo = (QuestionnaireVO) doView(pk);
		if (vo != null) {
			AnswerProcessBean answerBean = new AnswerProcessBean();
			DataPackage<AnswerVO> datas = answerBean.doViewForQuestionnaire(pk);
			if (datas != null) {
				String json = CensusUtil.createCensus(datas.datas);
				vo.setChartJson(json);
			}
		}
		return vo;
	}
	
	public QuestionnaireVO showReportForm(String pk)
			throws Exception {
		QuestionnaireVO vo = (QuestionnaireVO) doView(pk);
		if (vo != null) {
			AnswerProcessBean answerBean = new AnswerProcessBean();
			DataPackage<AnswerVO> datas = answerBean.doViewForQuestionnaire(pk);
			if (datas != null) {
				String json = CensusUtil.reportForm(datas.datas);
				vo.setChartJson(json);
			}
		}
		return vo;
	}

	public String doShowVoteNumber(String pk, String holder_id, WebUser user)
			throws Exception {
		QuestionnaireVO vo = (QuestionnaireVO) doView(pk);
		String json = "";
		if (vo != null) {
			AnswerProcessBean answerBean = new AnswerProcessBean();
			DataPackage<AnswerVO> datas = answerBean.doViewForQuestionnaire(pk);
			if (datas != null) {
				json = CensusUtil.voteNumber(datas.datas, holder_id);
			}
		}
		return json;
	}

	/**
	 * 查询当前用户需要填写的问卷
	 */
	public DataPackage<QuestionnaireVO> doQueryByPublish(WebUser user)
			throws Exception {
		return ((QuestionnaireDAO) getDAO())
				.queryByPublish(user);
	}

	/**
	 * 新建问卷
	 */
	public QuestionnaireVO doNew() throws Exception {
		QuestionnaireVO vo = new QuestionnaireVO();
		vo.setTitle("我的问卷");

		JSONArray holders = new JSONArray();
		for (int i = 0; i < 2; i++) {
			JSONObject holder = new JSONObject();
			holder.put("id", UUID.randomUUID().toString());
			holder.put("topic", "请输入题目");
			holder.put("type", "radio");
			JSONArray options = new JSONArray();
			for (int j = 0; j < 2; j++) {
				JSONObject option = new JSONObject();
				option.put("name", "选项");
				options.put(option);
			}
			holder.put("options", options);
			holders.put(holder);
		}

		vo.setContent(holders.toString());
		vo.setScope(QuestionnaireVO.SCOPE_USER);
		return vo;
	}

	/**
	 * 查询根据条件查找实例集合
	 * 
	 * @param params
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public DataPackage<QuestionnaireVO> doQueryByFilter(String s_title,
			int page, int lines, WebUser user) throws Exception {
		return ((QuestionnaireDAO) getDAO()).queryByFilter(s_title, page,
				lines, user);
	}

	public ValueObject doReBuild(ValueObject vo) throws Exception {
		AnswerProcessBean answerProcessBean = new AnswerProcessBean();
		DataPackage<AnswerVO> datas = answerProcessBean
				.doViewForQuestionnaire(vo.getId());
		if (datas != null) {
			for (Iterator<AnswerVO> iter = datas.datas.iterator(); iter
					.hasNext();) {
				AnswerVO answer = iter.next();
				answerProcessBean.doRemove(answer.getId());
			}
		}
		return vo;
	}

	@SuppressWarnings("deprecation")
	public DataPackage<QuestionnaireVO> doQueryByPublishDone(WebUser user)
			throws Exception {
		DataPackage<QuestionnaireVO> datas = ((QuestionnaireDAO) getDAO())
				.queryByPublishDone(user);
		Collection<QuestionnaireVO> newData = new ArrayList<QuestionnaireVO>();
		if (datas.datas != null) {
			for (Iterator<QuestionnaireVO> iter = datas.datas.iterator(); iter
					.hasNext();) {
				QuestionnaireVO questionnaire = iter.next();
				// 判断用户是否需要填写该问卷
				boolean flag = questionnaire.validatePublish(user);
				if (flag) {
					newData.add(questionnaire);
				}

			}
		}
		datas.setDatas(newData);
		return datas;
	}

	@Override
	public DataPackage<QuestionnaireVO> doQueryMyPublish(String title,
			int status, int page, int lines, WebUser user) throws Exception {
		return ((AbstractQuestionnaireDAO) getDAO()).doQueryMyPublish(title, status, page,
				lines, user);
	}

	@Override
	public DataPackage<QuestionnaireVO> doQueryMyPartake(String title,
			int status, int page, int lines, WebUser user) throws Exception {
		DataPackage<QuestionnaireVO> dataPackage = new DataPackage<QuestionnaireVO>();
		if(AnswerVO.STATUS_ALL==status){
			dataPackage = ((AbstractQuestionnaireDAO) getDAO())
					.doQueryMyPartakeAll(title, status, page, lines, user);
		}else if(AnswerVO.STATUS_FILLING==status){
			dataPackage = ((AbstractQuestionnaireDAO) getDAO()).doQueryMyPartake(title, status, page,
					lines, user);
			
		}else if(AnswerVO.STATUS_FILLED==status){
			dataPackage = ((AbstractQuestionnaireDAO) getDAO()).doQueryMyPartake(title, status, page,
					lines, user);
			
		} else{
			throw new Exception("请输入正确的status参数");
		}
		
		return dataPackage;
	}

	@Override
	public boolean addPaticipate(String id) throws Exception {
		return ((QuestionnaireDAO) getDAO()).addPaticipate(id);
	}
	
}
