package cn.myapps.pm.project.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.pm.base.dao.AbstractBaseDAO;
import cn.myapps.pm.project.ejb.Member;
import cn.myapps.pm.project.ejb.Project;
import cn.myapps.pm.util.ConnectionManager;
import cn.myapps.util.StringUtil;

public abstract class AbstractProjectDAO extends AbstractBaseDAO{
	
	private static final Logger log = Logger.getLogger(AbstractProjectDAO.class);


	public AbstractProjectDAO(Connection conn) throws Exception {
		super(conn);
		this.tableName = "PM_PROJECT";
	}
	
	protected abstract String buildLimitString(String sql, int page, int lines,
			String orderbyFile, String orderbyMode) throws SQLException ;
	
	public ValueObject create(ValueObject vo) throws Exception {
		Project project = (Project)vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO "
				+ getFullTableName("PM_PROJECT")
				+ " (ID,NAME,CREATOR,CREATOR_ID,CREATE_DATE,MANAGER,MANAGER_ID,TASKS_TOTAL,FINISHED_TASKS_NUM,DOMAIN_ID,NOTIFICATION) values (?,?,?,?,?,?,?,?,?,?,?)";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, project.getId());
			stmt.setString(2, project.getName());
			stmt.setString(3, project.getCreator());
			stmt.setString(4, project.getCreatorId());
			if (project.getCreateDate() == null) {
				stmt.setNull(5, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(project.getCreateDate().getTime());
				stmt.setTimestamp(5, ts);
			}
			stmt.setString(6, project.getManager());
			stmt.setString(7, project.getManagerId());
			stmt.setInt(8, project.getTasksTotal());
			stmt.setInt(9, project.getFinishedTasksNum());
			stmt.setString(10, project.getDomainid());
			stmt.setBoolean(11,project.isNotification());
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		
		return vo;
	}

	public ValueObject find(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM "
				+ getFullTableName("PM_PROJECT") + " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			Project project = null;
			if (rs.next()) {
				project = new Project();
				setProperties(project, rs);
			}
			rs.close();
			return project;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
	}

	public void remove(String pk) throws Exception {
		PreparedStatement stmt = null;

		String sql = "DELETE FROM " + getFullTableName(tableName)
				+ " WHERE ID=?";
		
		String sql2 = "DELETE FROM " + getFullTableName("PM_PROJECT_MEMBER_SET")
				+ " WHERE PROJECT_ID=?";
		
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, pk);
			log.debug(sql);
			stmt.execute();
			stmt = connection.prepareStatement(sql2);
			stmt.setString(1, pk);
			log.debug(sql2);
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		
	}

	public ValueObject update(ValueObject vo) throws Exception {
		Project project = (Project) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE "
				+ getFullTableName("PM_PROJECT")
				+ " SET NAME=?,CREATOR=?,CREATOR_ID=?,CREATE_DATE=?,MANAGER=?,MANAGER_ID=?,TASKS_TOTAL=?,FINISHED_TASKS_NUM=?,DOMAIN_ID=?,NOTIFICATION=? WHERE ID=?";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);
			
			stmt.setString(1, project.getName());
			stmt.setString(2, project.getCreator());
			stmt.setString(3, project.getCreatorId());
			if (project.getCreateDate() == null) {
				stmt.setNull(4, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(project.getCreateDate().getTime());
				stmt.setTimestamp(4, ts);
			}
			stmt.setString(5, project.getManager());
			stmt.setString(6, project.getManagerId());
			stmt.setInt(7, project.getTasksTotal());
			stmt.setInt(8, project.getFinishedTasksNum());
			stmt.setString(9, project.getDomainid());
			stmt.setBoolean(10, project.isNotification());
			stmt.setString(11, project.getId());
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return vo;
		
	}
	
	/**
	 * 根据项目名称查询项目总数
	 * @param name
	 * 		项目名称
	 * @return
	 * 		项目总数
	 * @throws Exception
	 */
	public long countByName(String name) throws Exception{
		PreparedStatement stmt = null;

		String sql = "SELECT COUNT(*) FROM "
				+ getFullTableName("PM_PROJECT") + " WHERE NAME=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, name);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return 0;
	}
	
	public Collection<Project> queryMyProject(ParamsTable params,WebUser user) throws Exception {
		
		Collection<Project> rtn = new ArrayList<Project>();
		
		String name =params.getParameterAsString("name");
		
		PreparedStatement stmt = null;

		String sql = "SELECT distinct(p.ID),p.NAME,p.CREATOR,p.CREATOR_ID,p.CREATE_DATE,p.MANAGER,p.MANAGER_ID,p.TASKS_TOTAL,p.FINISHED_TASKS_NUM,p.DOMAIN_ID,p.NOTIFICATION FROM "
			+ getFullTableName(tableName) +" p, "
			+ getFullTableName("PM_PROJECT_MEMBER_SET") +" m "
			+ "WHERE p.ID=m.PROJECT_ID AND m.USER_ID=? AND p.DOMAIN_ID=? ";
		if(!StringUtil.isBlank(name)){
			sql += " AND NAME like ?";
		}
		
		
		sql += " ORDER BY CREATE_DATE DESC";
		

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, user.getId());
			stmt.setString(2, user.getDomainid());
			if(!StringUtil.isBlank(name)){
				stmt.setString(3, "%"+name+"%");
			}
			
			ResultSet rs = stmt.executeQuery();
			Project project = null;
			while(rs.next()){
				project = new Project();
				setProperties(project, rs);
				rtn.add(project);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return rtn;
	}
	
	public Collection<?> simpleQuery(ParamsTable params,WebUser user) throws Exception {
		
		Collection<Project> rtn = new ArrayList<Project>();
		
		String name =params.getParameterAsString("name");
		
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM "
			+ getFullTableName(tableName) +" WHERE DOMAIN_ID=?";
		if(!StringUtil.isBlank(name)){
			sql += " AND NAME like ?";
		}
		
		sql += " ORDER BY CREATE_DATE DESC";
		

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, user.getDomainid());
			if(!StringUtil.isBlank(name)){
				stmt.setString(2, "%"+name+"%");
			}
			ResultSet rs = stmt.executeQuery();
			Project project = null;
			while(rs.next()){
				project = new Project();
				setProperties(project, rs);
				rtn.add(project);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return rtn;
	}
	
	/**
	 * 查询项目的成员集合
	 * @param projectId
	 * 		项目主键
	 * @return
	 * @throws Exception
	 */
	public Collection<Member> queryMembersByProject(String projectId,String memberGroup) throws Exception{
		Collection<Member> rtn = new ArrayList<Member>();
		
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM "
			+ getFullTableName("PM_PROJECT_MEMBER_SET") +" WHERE PROJECT_ID=?";
		
		
		if(!StringUtil.isBlank(memberGroup) && memberGroup != "" ){
			if("noFollower".equalsIgnoreCase(memberGroup)){
				sql += " AND MEMBER_TYPE != ? ";
			}
		}

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, projectId);
			if(!StringUtil.isBlank(memberGroup) && memberGroup != "" ){
				if("noFollower".equalsIgnoreCase(memberGroup)){
					stmt.setInt(2, Member.MEMBER_FOLLOWER);
				}
			}

			ResultSet rs = stmt.executeQuery();
			Member member = null;
			while(rs.next()){
				member = new Member();
				setMemberProperties(member, rs);
				rtn.add(member);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return rtn;
	}
	
	/**
	 * 添加项目成员
	 * @param members
	 * 		成员集合
	 * @param projectId
	 * 		项目主键
	 * @throws Exception
	 */
	public void addMembers(Collection<Member> members,String projectId) throws Exception {
		PreparedStatement stmt = null;
		String sql = "INSERT "
			+getFullTableName("PM_PROJECT_MEMBER_SET")
			+"(USER_ID,USER_NAME,MEMBER_TYPE,PROJECT_ID) VALUES(?,?,?,?)";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			for(Member member : members){
				stmt.setString(1, member.getUserId());
				stmt.setString(2, member.getUserName());
				stmt.setInt(3, member.getMemberType());
				stmt.setString(4, projectId);
				stmt.addBatch();
			}
			stmt.executeBatch();
		} catch (Exception e) {
			throw e;
		}finally{
			ConnectionManager.closeStatement(stmt);
		}
	}
	
	/**
	 * 删除项目成员
	 * @param userId
	 * 		用户主键
	 * @param projectId
	 * 		项目主键
	 * @throws Exception
	 */
	public void deleteMember(String userId,String projectId, int memberType) throws Exception{
		PreparedStatement stmt = null;
		String sql = "DELETE FROM "
			+getFullTableName("PM_PROJECT_MEMBER_SET")
			+" WHERE PROJECT_ID=? AND USER_ID=? AND MEMBER_TYPE =?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, projectId);
			stmt.setString(2, userId);
			stmt.setInt(3, memberType);;
			stmt.execute();
		} catch (Exception e) {
			throw e;
		}finally{
			ConnectionManager.closeStatement(stmt);
		}
	}
	
	/**
	 * 设置用户为项目经理
	 * @param userId
	 * 		用户主键
	 * @param projectId
	 * 		项目主键
	 * @throws Exception
	 */
	public void setProjectManager(String userId,String projectId) throws Exception {
		PreparedStatement stmt = null;
		String sql = "UPDATE "
			+getFullTableName("PM_PROJECT_MEMBER_SET")+" SET MEMBER_TYPE= "+Member.MEMBER_REGULAR
			+" WHERE PROJECT_ID=? AND MEMBER_TYPE=1";
		
		String sql2 = "UPDATE "
			+getFullTableName("PM_PROJECT_MEMBER_SET")+" SET MEMBER_TYPE= "+Member.MEMBER_MANAGER
			+" WHERE PROJECT_ID=? AND USER_ID=?";
		
		String sql3 = "UPDATE "
			+getFullTableName(tableName)+" SET MANAGER_ID=?"
			+" WHERE ID=?";
		
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, projectId);
			log.info(sql);
			stmt.execute();
			
			stmt = connection.prepareStatement(sql2);
			stmt.setString(1, projectId);
			stmt.setString(2, userId);
			log.info(sql2);
			stmt.execute();
			
			stmt = connection.prepareStatement(sql3);
			stmt.setString(1, userId);
			stmt.setString(2, projectId);
			log.info(sql2);
			stmt.execute();
			
		} catch (Exception e) {
			throw e;
		}finally{
			ConnectionManager.closeStatement(stmt);
		}
	}
	
	
	public void addFinishedTasksNum(String projectId) throws Exception {
		PreparedStatement stmt = null;
		String sql = "UPDATE "
			+getFullTableName(tableName)+" SET FINISHED_TASKS_NUM=FINISHED_TASKS_NUM+1"
			+" WHERE ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, projectId);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		}finally{
			ConnectionManager.closeStatement(stmt);
		}
	}

	public void addTasksTotal(String projectId) throws Exception {
		PreparedStatement stmt = null;
		String sql = "UPDATE "
			+getFullTableName(tableName)+" SET TASKS_TOTAL=(SELECT COUNT(*) FROM PM_TASK WHERE PROJECT_ID =?)"
			+" WHERE ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, projectId);
			stmt.setString(2, projectId);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		}finally{
			ConnectionManager.closeStatement(stmt);
		}
	}

	public void subtractFinishedTasksNum(String projectId) throws Exception {
		PreparedStatement stmt = null;
		String sql = "UPDATE "
			+getFullTableName(tableName)+" SET FINISHED_TASKS_NUM=FINISHED_TASKS_NUM-1"
			+" WHERE ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, projectId);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		}finally{
			ConnectionManager.closeStatement(stmt);
		}
	}

	public void subtractTasksTotal(String projectId) throws Exception {
		PreparedStatement stmt = null;
		String sql = "UPDATE "
			+getFullTableName(tableName)+" SET TASKS_TOTAL=TASKS_TOTAL-1"
			+" WHERE ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, projectId);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		}finally{
			ConnectionManager.closeStatement(stmt);
		}
	}
	
	void setProperties(Project project, ResultSet rs) throws Exception {
		try { 
			project.setId(rs.getString("ID"));
			project.setName(rs.getString("NAME"));
			project.setCreator(rs.getString("CREATOR"));
			project.setCreatorId(rs.getString("CREATOR_ID"));
			project.setCreateDate(rs.getTimestamp("CREATE_DATE"));
			project.setManager(rs.getString("MANAGER"));
			project.setManagerId(rs.getString("MANAGER_ID"));
			project.setTasksTotal(rs.getInt("TASKS_TOTAL"));
			project.setFinishedTasksNum(rs.getInt("FINISHED_TASKS_NUM"));
			project.setDomainid(rs.getString("DOMAIN_ID"));
			project.setNotification(rs.getBoolean("NOTIFICATION"));
		} catch (SQLException e) {
			throw e;
		}

	}
	
	void setMemberProperties(Member member, ResultSet rs) throws Exception {
		try {
			member.setUserName(rs.getString("USER_NAME"));
			member.setUserId(rs.getString("USER_ID"));
			member.setMemberType(rs.getInt("MEMBER_TYPE"));;
		} catch (SQLException e) {
			throw e;
		}

	}

}
