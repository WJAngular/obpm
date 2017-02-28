package cn.myapps.pm.task.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.pm.base.action.BaseAction;
import cn.myapps.pm.tag.ejb.Tag;
import cn.myapps.pm.task.ejb.Follower;
import cn.myapps.pm.task.ejb.Remark;
import cn.myapps.pm.task.ejb.SubTask;
import cn.myapps.pm.task.ejb.Task;
import cn.myapps.pm.task.ejb.TaskProcess;
import cn.myapps.pm.task.ejb.TaskProcessBean;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;
import cn.myapps.util.sequence.SequenceException;

public class TaskAction extends BaseAction<Task> {

    /**
     * 
     */
    private static final long serialVersionUID = 1805181334170954042L;

    /**
     * 下载文件流
     */
    private InputStream inputStream;

    /**
     * 下载文件名
     */
    private String filename;

    public TaskAction() {
	super();
	content = new Task();
	process = new TaskProcessBean();
    }

    public String doNewTaskId() throws SequenceException {
	String TaskId = Sequence.getSequence();
	addActionResult(true, "添加成功", TaskId);
	return SUCCESS;
    }

    public String doCreate() {
	try {
	    WebUser user = getUser();
	    Task task = (Task) getContent();
	    task = (Task) ((TaskProcess) process).doCreate(task, user);
	    addActionResult(true, "添加成功", task);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;
    }

    public String doView() {

	try {
	    ParamsTable params = getParams();
	    String id = params.getParameterAsString("id");

	    Task task = (Task) process.doView(id);
	    addActionResult(true, null, task);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;
    }

    /**
     * 更新任务
     * 
     * @return
     */
    public String doUpdate() {
	try {
		    Task vo = (Task) getContent();
		    WebUser user = getUser();
		    if(!StringUtil.isBlank(vo.getId())){
		    	Task task = ((TaskProcess) process).doUpdate(vo,user);
		    	addActionResult(true, "修改成功", task);
		    }else{
		    	addActionResult(false, "修改任务失败", vo);
		    }
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}

	return SUCCESS;
    }

    /**
     * 更新任务基础字段
     * 
     * @return
     */
    public String doSimpleUpdate() {
	WebUser user = getUser();
	ParamsTable params = getParams();
	String id = params.getParameterAsString("id");
	String updateField = params.getParameterAsString("updateField");
	String updateValue = params.getParameterAsString("updateValue");

	try {
	    Task task = (Task) ((TaskProcess) process).doSimpleUpdate(id, updateField, updateValue, user);
	    addActionResult(true, "修改成功", task);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}

	return SUCCESS;
    }

    public String doUpdateEndDate() {
	WebUser user = getUser();
	ParamsTable params = getParams();
	String id = params.getParameterAsString("id");
	int delta = params.getParameterAsInteger("delta");

	try {
	    Task t = (Task) process.doView(id);
	    if (t == null)
		throw new OBPMValidateException("找不到对象");

	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(t.getEndDate());
	    calendar.add(Calendar.DAY_OF_YEAR, delta);

	    Task task = (Task) ((TaskProcess) process).doSimpleUpdate(id, "endDate",
		    new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()), user);
	    addActionResult(true, "修改成功", task);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}

	return SUCCESS;
    }

    public String doUpdateDate() {
	ParamsTable params = getParams();
	String id = params.getParameterAsString("id");
	int delta = params.getParameterAsInteger("delta");

	try {
	    Task task = (Task) process.doView(id);
	    if (task == null)
		throw new OBPMValidateException("找不到对象");

	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(task.getEndDate());
	    calendar.add(Calendar.DAY_OF_YEAR, delta);
	    task.setEndDate(calendar.getTime());

	    calendar.setTime(task.getStartDate());
	    calendar.add(Calendar.DAY_OF_YEAR, delta);
	    task.setStartDate(calendar.getTime());

	    process.doUpdate(task);
	    addActionResult(true, "修改成功", task);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}

	return SUCCESS;
    }

    /**
     * 添加项目关注人
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public String doAddFollowers() {

	try {
	    WebUser user = getUser();
	    ParamsTable params = getParams();
	    String taskId = params.getParameterAsString("id");
	    String _followers = params.getParameterAsString("followers");
	    Collection<Follower> followers = new ArrayList<Follower>();

	    JSONArray jsonArray = JSONArray.fromObject(_followers);
	    for (Iterator<JSONObject> iterator = jsonArray.iterator(); iterator.hasNext();) {
		JSONObject item = iterator.next();
		Follower follower = new Follower();
		follower.setUserId(item.getString("userId"));
		follower.setUserName(item.getString("userName"));
		follower.setDomainId(user.getDomainid());
		followers.add(follower);
	    }

	    ((TaskProcess) process).addFollowers(taskId, followers, user);
	    addActionResult(true, "添加成功", followers);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;
    }

    /**
     * 移除项目关注人
     * 
     * @return
     */
    public String doRemoveFollower() {
	try {
	    WebUser user = getUser();
	    ParamsTable params = getParams();
	    String taskId = params.getParameterAsString("id");
	    String userId = params.getParameterAsString("followerId");
	    String userName = params.getParameterAsString("followerName");

	    ((TaskProcess) process).deleteFollower(userId, userName, taskId, user);
	    addActionResult(true, "删除成功", null);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;

    }

    /**
     * 关注任务
     * 
     * @return
     */
    public String doFollow() {

	try {
	    WebUser user = getUser();
	    ParamsTable params = getParams();
	    String taskId = params.getParameterAsString("id");
	    ((TaskProcess) process).doFollow(taskId, user);
	    addActionResult(true, "关注成功", null);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;
    }

    /**
     * 取消关注任务
     * 
     * @return
     */
    public String doUnFollow() {

	try {
	    WebUser user = getUser();
	    ParamsTable params = getParams();
	    String taskId = params.getParameterAsString("id");

	    ((TaskProcess) process).doUnFollow(taskId, user);
	    addActionResult(true, "取消关注成功", null);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;
    }

    /**
     * 完成任务
     * 
     * @return
     */
    public String doComplete() {

	try {
	    WebUser user = getUser();
	    ParamsTable params = getParams();
	    String taskId = params.getParameterAsString("id");

	    ((TaskProcess) process).doComplete(taskId, user);
	    addActionResult(true, "", null);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;
    }

    /**
     * 更新任务的状态
     * 
     * @return
     */
    public String doUpdateTaskStatus() {

	WebUser user = getUser();
	ParamsTable params = getParams();
	String id = params.getParameterAsString("id");
	Integer status = params.getParameterAsInteger("status");
	if ("null".equals(status)) { // 任务默认为新建
	    status = Task.STATUS_NEW;
	}

	try {
	    Task task_test = (Task) process.doView(id);
	    if (task_test == null)
		throw new OBPMValidateException("找不到对象");

	    Task task = (Task) ((TaskProcess) process).doUpdateTaskStatus(id, status, user);
	    addActionResult(true, "修改成功", task);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}

	return SUCCESS;
    }

    /**
     * 重做任务
     * 
     * @return
     */
    public String doRedoTask() {

	try {
	    WebUser user = getUser();
	    ParamsTable params = getParams();
	    String taskId = params.getParameterAsString("id");

	    ((TaskProcess) process).redoTask(taskId, user);
	    addActionResult(true, "", null);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;
    }

    /**
     * 删除任务
     * 
     * @return
     */
    public String doDelete() {

	try {
	    ParamsTable params = getParams();
	    String id = params.getParameterAsString("id");

	    ((TaskProcess) process).doRemove(id);
	    addActionResult(true, "删除成功", null);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;
    }

    /**
     * 添加子任务
     * 
     * @return
     */
    public String doCreateSubTask() {

	try {
	    ParamsTable params = getParams();
	    WebUser user = getUser();
	    String id = params.getParameterAsString("id");
	    String subTaskName = params.getParameterAsString("name");

	    SubTask subTask = new SubTask(subTaskName);

	    ((TaskProcess) process).createSubTask(id, subTask, user);
	    addActionResult(true, "添加成功", subTask);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;
    }

    /**
     * 删除子任务
     * 
     * @return
     */
    public String doDeleteSubTask() {

	try {
	    ParamsTable params = getParams();
	    WebUser user = getUser();
	    String id = params.getParameterAsString("id");
	    String subTaskId = params.getParameterAsString("subTaskId");

	    ((TaskProcess) process).removeSubTask(id, subTaskId, user);
	    addActionResult(true, "删除成功", null);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;
    }

    /**
     * 完成子任务
     * 
     * @return
     */
    public String doCompleteSubTask() {

	try {
	    ParamsTable params = getParams();
	    WebUser user = getUser();
	    String id = params.getParameterAsString("id");
	    String subTaskId = params.getParameterAsString("subTaskId");

	    SubTask subTask = ((TaskProcess) process).completeSubTask(id, subTaskId, user);
	    addActionResult(true, "修改成功", subTask);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;
    }

    /**
     * 重做子任务
     * 
     * @return
     */
    public String doRedoSubTask() {

	try {
	    ParamsTable params = getParams();
	    WebUser user = getUser();
	    String id = params.getParameterAsString("id");
	    String subTaskId = params.getParameterAsString("subTaskId");

	    SubTask subTask = ((TaskProcess) process).redoSubTask(id, subTaskId, user);
	    addActionResult(true, "修改成功", subTask);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;
    }

    /**
     * 保存子任务
     * 
     * @return
     */
    public String doUpdateSubTask() {

	try {
	    ParamsTable params = getParams();
	    WebUser user = getUser();
	    String id = params.getParameterAsString("id");
	    String subTaskId = params.getParameterAsString("subTaskId");
	    String subTaskName = params.getParameterAsString("name");

	    SubTask subTask = ((TaskProcess) process).updateSubTask(id, subTaskId, subTaskName, user);
	    addActionResult(true, "修改成功", subTask);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;
    }

    /**
     * 添加备注
     * 
     * @return
     */
    public String doCreateRemark() {

	try {
	    ParamsTable params = getParams();
	    WebUser user = getUser();
	    String id = params.getParameterAsString("id");
	    String remarkContent = params.getParameterAsString("content");
	    Remark remark = new Remark(remarkContent, user.getName());
	    remark.setUserId(user.getId());

	    ((TaskProcess) process).createRemark(id, remark, user);
	    addActionResult(true, "添加成功", remark);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;
    }

    /**
     * 删除备注
     * 
     * @return
     */
    public String doDeleteRemark() {

	try {
	    ParamsTable params = getParams();
	    WebUser user = getUser();
	    String id = params.getParameterAsString("id");
	    String remarkId = params.getParameterAsString("remarkId");

	    ((TaskProcess) process).removeRemark(id, remarkId, user);
	    addActionResult(true, "删除成功", null);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;
    }

    /**
     * 保存备注
     * 
     * @return
     */
    public String doUpdateRemark() {

	try {
	    ParamsTable params = getParams();
	    WebUser user = getUser();
	    String id = params.getParameterAsString("id");
	    String remarkId = params.getParameterAsString("remarkId");
	    String remarkContnet = params.getParameterAsString("content");

	    Remark remark = ((TaskProcess) process).updateRemark(id, remarkId, remarkContnet, user);
	    addActionResult(true, "修改成功", remark);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;
    }

    public String doQuery() {
	try {
	    ParamsTable params = getParams();
	    String name = params.getParameterAsString("name");
	    int status = params.getParameterAsInteger("status");
	    String type = params.getParameterAsString("type");
	    String currDate = params.getParameterAsString("currDate");
	    if (StringUtil.isBlank(currDate)) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		currDate = format.format(new Date());
	    }
	    String dateRangeType = params.getParameterAsString("dateRangeType");
	    Integer page = params.getParameterAsInteger("_currpage");
	    Integer lines = params.getParameterAsInteger("_rowcount");
	    if (page == null || page <= 0)
		page = 1;
	    if (lines == null || lines <= 0)
		lines = 30;
	    WebUser user = getUser();

	    Collection<Task> list = null;
	    if ("my".equals(type)) {
		list = ((TaskProcess) process).queryMyTasks(name, status, currDate, dateRangeType, user);
	    } else if ("follow".equals(type)) {
		list = ((TaskProcess) process).queryMyFollowTasks(name, status, currDate, dateRangeType, page, lines,
			user);
	    } else if ("entrust".equals(type)) {
		list = ((TaskProcess) process).queryMyEntrustTasks(name, status, currDate, dateRangeType, page, lines,
			user);
	    } else {
		throw new OBPMValidateException("查询条件不匹配");
	    }

	    addActionResult(true, "", list);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}

	return SUCCESS;
    }

    /**
     * 查询我的任务
     * 
     * @return
     */
    public String doQueryMyTasks() {
	try {
	    ParamsTable params = getParams();
	    String name = params.getParameterAsString("name");
	    String tmp_status = params.getParameterAsString("status");
	    int status = Task.STATUS_NULL;
	    if (!StringUtil.isBlank(tmp_status)) {
		status = new Integer(tmp_status);
	    }
	    String currDate = params.getParameterAsString("currDate");
	    if (StringUtil.isBlank(currDate)) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		currDate = format.format(new Date());
	    }
	    String dateRangeType = params.getParameterAsString("dateRangeType");

	    WebUser user = getUser();
	    Collection<Task> list = ((TaskProcess) process).queryMyTasks(name, status, currDate, dateRangeType, user);
	    addActionResult(true, "", list);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}

	return SUCCESS;
    }

    /**
     * 查询我的任务
     * 
     * @return
     */
    public String doQueryMyTasks4CalendarView() {
	try {
	    ParamsTable params = getParams();
	    String name = params.getParameterAsString("name");
	    String startDate = params.getParameterAsString("startDate");
	    String endDate = params.getParameterAsString("endDate");
	    String tmp_status = params.getParameterAsString("status");
	    int status = Task.STATUS_NULL;
	    if (!StringUtil.isBlank(tmp_status)) {
		status = new Integer(tmp_status);
	    }

	    WebUser user = getUser();
	    Collection<Task> list = ((TaskProcess) process).queryMyTasks4CalendarView(name, status, startDate, endDate,
		    user);
	    addActionResult(true, "", list);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}

	return SUCCESS;
    }

    /**
     * 查询我关注的任务
     * 
     * @return
     */
    public String doQueryMyFollowTasks() {
	try {
	    ParamsTable params = getParams();
	    String name = params.getParameterAsString("name");

	    String tmp_status = params.getParameterAsString("status");
	    int status = Task.STATUS_NULL;
	    if (!StringUtil.isBlank(tmp_status)) {
		status = new Integer(tmp_status);
	    }
	    String currDate = params.getParameterAsString("currDate");
	    if (StringUtil.isBlank(currDate)) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		currDate = format.format(new Date());
	    }
	    String dateRangeType = params.getParameterAsString("dateRangeType");

	    Integer page = params.getParameterAsInteger("_currpage");
	    Integer lines = params.getParameterAsInteger("_rowcount");
	    if (page == null || page <= 0)
		page = 1;
	    if (lines == null || lines <= 0)
		lines = 30;

	    WebUser user = getUser();
	    Collection<Task> list = ((TaskProcess) process).queryMyFollowTasks(name, status, currDate, dateRangeType,
		    page, lines, user);
	    addActionResult(true, "", list);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}

	return SUCCESS;
    }

    /**
     * 根据项目查询任务集合
     * 
     * @return
     */
    public String doQueryTasksByProject() {
	try {
	    ParamsTable params = getParams();
	    String projectId = params.getParameterAsString("projectId");
	    String taskName = params.getParameterAsString("taskName");
	    String executerId = params.getParameterAsString("executorId");
	    String createrId = params.getParameterAsString("creatorId");
	    String tmp_status = params.getParameterAsString("status");
	    String overdueStatus = params.getParameterAsString("overdue");
	    int status = Task.STATUS_ON;
	    if (!StringUtil.isBlank(tmp_status)) {
		status = new Integer(tmp_status);
	    }
	    
	    String tmp_level = params.getParameterAsString("level");
	    int level = Task.LEVEL_NULL;
	    if (!StringUtil.isBlank(tmp_level)) {
		level = new Integer(tmp_level);
	    }

	    String tag = params.getParameterAsString("tag");

	    String currDate = params.getParameterAsString("nowDate");

	    if (StringUtil.isBlank(currDate)) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		currDate = format.format(new Date());
	    }
	    String dateRangeType = params.getParameterAsString("endDate");

	    String orderName = params.getParameterAsString("orderName");
	    String orderBy = params.getParameterAsString("orderBy");

	    Integer page = params.getParameterAsInteger("pageNo");
	    Integer lines = params.getParameterAsInteger("linesPerPage");
	    if (page == null || page <= 0)
		page = 1;
	    if (lines == null || lines <= 0)
		lines = 30;

	    WebUser user = getUser();
 
	    DataPackage<Task> datas = ((TaskProcess) process).queryTasksByProject(projectId, taskName, status, level,
		    executerId, createrId, currDate, dateRangeType, tag, orderName, orderBy, page, lines, overdueStatus, user);

	    addActionResult(true, "", datas);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}

	return SUCCESS;
    }
    
   

    /**
     * 根据项目查询任务集合
     * 
     * @return
     */
    public String doQueryTasksByTag() {
	try {
	    ParamsTable params = getParams();
	    String tagName = params.getParameterAsString("tagName");
	    String name = params.getParameterAsString("name");
	    int status = params.getParameterAsInteger("status");
	    String currDate = params.getParameterAsString("currDate");
	    if (StringUtil.isBlank(currDate)) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		currDate = format.format(new Date());
	    }
	    String dateRangeType = params.getParameterAsString("dateRangeType");

	    Integer page = params.getParameterAsInteger("_currpage");
	    Integer lines = params.getParameterAsInteger("_rowcount");
	    if (page == null || page <= 0)
		page = 1;
	    if (lines == null || lines <= 0)
		lines = 30;

	    WebUser user = getUser();
	    Collection<Task> list = ((TaskProcess) process).queryTasksByTag(tagName, name, status, currDate,
		    dateRangeType, page, lines, user);
	    addActionResult(true, "", list);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}

	return SUCCESS;
    }

    /**
     * 添加任务标签
     * 
     * @return
     */
    public String doAddTaskTag() {
	try {
	    ParamsTable params = getParams();
	    WebUser user = getUser();
	    String id = params.getParameterAsString("id");
	    String tagName = params.getParameterAsString("tagName");
	    Tag tag = ((TaskProcess) process).addTag(id, tagName, user);
	    Task task =(Task) ((TaskProcess) process).doView(id);
	    addActionResult(true, "添加成功", task);
	    
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}

	return SUCCESS;
    }

    /**
     * 删除任务标签
     * 
     * @return
     */
    public String doRemoveTaskTag() {
	try {
	    ParamsTable params = getParams();
	    WebUser user = getUser();
	    String id = params.getParameterAsString("id");
	    String tagName = params.getParameterAsString("tagName");

	    ((TaskProcess) process).removeTag(id, tagName, user);
	    Task task =(Task) ((TaskProcess) process).doView(id);
	    addActionResult(true, "移除成功", task);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}

	return SUCCESS;
    }

    public String doSetProject() {
	try {
	    ParamsTable params = getParams();
	    WebUser user = getUser();
	    String id = params.getParameterAsString("id");
	    String projectId = params.getParameterAsString("projectId");
	    String projectName = params.getParameterAsString("projectName");

	    Task task = ((TaskProcess) process).setProject(id, projectId, projectName, user);
	    addActionResult(true, "添加成功", task);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}

	return SUCCESS;
    }

    public String doRemoveProject() {

	try {
	    ParamsTable params = getParams();
	    WebUser user = getUser();
	    String id = params.getParameterAsString("id");
	    ((TaskProcess) process).removeProject(id, user);
	    addActionResult(true, "移除成功", null);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}

	return SUCCESS;
    }

    public String doUpdateTaskExecutor() {
	try {
	    ParamsTable params = getParams();
	    WebUser user = getUser();

	    String id = params.getParameterAsString("id");
	    String executorId = params.getParameterAsString("executorId");
	    String executorName = params.getParameterAsString("executorName");

	    Task task = ((TaskProcess) process).updateTaskExecutor(id, executorId, executorName, user);
	    addActionResult(true, "修改成功", task);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;
    }

    public String doGetUsers() {
	try {
	    WebUser user = getUser();
	    UserProcess userPorcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);

	    Collection<UserVO> list = userPorcess.doQueryByHQL("from " + UserVO.class.getName() + " where domainid='"
		    + user.getDomainid() + "' and status=1 and dimission=1");
	    addActionResult(true, "", list);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;
    }

    public String doDeleteAttachment() {
	try {
	    System.out.println("doDeleteAttachment()执行！");
	    ParamsTable params = getParams();
	    String id = params.getParameterAsString("id");
	    String key = params.getParameterAsString("key");
	    Task task = ((TaskProcess) process).deleteAttachment(id, key);
	    String realPath = ServletActionContext.getServletContext().getRealPath("");// 服务器目录的绝对路径
	    String savePath = realPath + "\\task\\" + task.getId(); // 存放此文件的绝对路径
	    File file = new File(savePath);
	    File temp = null;
	    File[] filelist = file.listFiles();
	    for (int i = 0; i < filelist.length; i++) {
		temp = filelist[i];
		if (temp.getName().startsWith(key)) {
		    temp.delete();// 删除文件}
		}
	    }

	    addActionResult(true, null, "");
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;
    }

    public String doDownload() throws Exception {
	File file = null;
	ParamsTable params = getParams();
	String taskid = params.getParameterAsString("taskid");
	String id = params.getParameterAsString("id");
	if (!StringUtil.isBlank(id)) {
	    String realPath = ServletActionContext.getServletContext().getRealPath("");
	    String savePath = realPath + "\\task\\" + taskid;
	    Task task = (Task) process.doView(taskid);
	    String attachment = task.getAttachment();
	    JSONObject attachmentJson = JSONObject.fromObject(attachment);
	    String name = attachmentJson.getJSONObject(id).getString("name");
	    filename = new String(name.getBytes(),"ISO8859-1");
	    String ext = filename.substring(filename.lastIndexOf("."));
	    file = new File(savePath + "\\" + id + ext);
	    if (file == null || !file.exists())
		throw new Exception("找不到文件！");
	    setInputStream(new FileInputStream(file));
	}
	return SUCCESS;
    }

    public InputStream getInputStream() {
	return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
	this.inputStream = inputStream;
    }

    public String getFilename() {
	return filename;
    }

    public void setFilename(String filename) {
	this.filename = filename;
    }
    
    
    /**
     * 根据项目查询任务集合
     * 
     * @return
     */
    public String doQueryTasks() {
	try {
	    ParamsTable params = getParams();
	    String projectId = params.getParameterAsString("projectId");
	    String taskName = params.getParameterAsString("taskName");
	    String executerId = params.getParameterAsString("executorId");
	    String createrId = params.getParameterAsString("creatorId");
	    Integer status ;
	    String _status = params.getParameterAsString("status");
	    if(StringUtil.isBlank(_status)){
	    	status = null;
	    }else{
	    	status = params.getParameterAsInteger("status");
	    }
	    String overdueStatus = params.getParameterAsString("overdue");
	    Integer level = params.getParameterAsInteger("level");
	    String _level = params.getParameterAsString("level");
	    if(StringUtil.isBlank(_level)){
	    	level = null;
	    }else{
	    	level = params.getParameterAsInteger("level");
	    }
	    String tag = params.getParameterAsString("tag");

	    String currDate = params.getParameterAsString("nowDate");

	    if (StringUtil.isBlank(currDate)) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		currDate = format.format(new Date());
	    }
	    String dateRangeType = params.getParameterAsString("endDate");

	    String orderName = params.getParameterAsString("orderName");
	    String orderBy = params.getParameterAsString("orderBy");

	    Integer page = params.getParameterAsInteger("pageNo");
	    Integer lines = params.getParameterAsInteger("linesPerPage");
	    if (page == null || page <= 0)
		page = 1;
	    if (lines == null || lines <= 0)
		lines = 100;

	    WebUser user = getUser();
 
	    DataPackage<Task> datas = ((TaskProcess) process).queryTasks(projectId, taskName, status, level,
		    executerId, createrId, currDate, dateRangeType, tag, orderName, orderBy, page, lines, overdueStatus, user);

	    addActionResult(true, "", datas);
	} catch (Exception e) {
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}

	return SUCCESS;
    }

}
