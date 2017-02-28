package cn.myapps.core.dynaform.dts.datasource.ejb;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.dynaform.dts.datasource.dao.DataSourceDAO;
import cn.myapps.core.dynaform.dts.metadata.ddlutils.DBUtils;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.file.FileOperate;

public class DataSourceProcessBean extends AbstractDesignTimeProcessBean<DataSource> implements DataSourceProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1202553428428843236L;

	protected IDesignTimeDAO<DataSource> getDAO() throws Exception {
		return (DataSourceDAO)DAOFactory.getDefaultDAO(DataSource.class.getName());
	}

	public Connection getConnection(String dataSouceName, String application) throws Exception {
		return ((DataSourceDAO) getDAO()).getDataSource(dataSouceName, application).getConnection();
	}
	
	public Collection<?> queryDataSourceSQL(String dataSouceName, String sql, String application) throws Exception {
		Connection connection = this.getConnection(dataSouceName, application);
		try {
			List<?> pendingInfoList = (List<?>) DBUtils.doQuery(sql, connection);
			return pendingInfoList;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if(connection != null) connection.close();
		}
	}

	public Map<?, ?> findDataSourceSQL(String dataSouceName, String sql, String application) {
		try {
			List<?> pendingInfoList = (List<?>) queryDataSourceSQL(dataSouceName, sql, application);
			if (pendingInfoList != null && !pendingInfoList.isEmpty()) {
				return (Map<?, ?>) pendingInfoList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void createOrUpdate(String dataSouceName, String sql, String application) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = getConnection(dataSouceName, application);
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(sql);
			statement.executeUpdate();
			connection.commit();
		} catch (Exception e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			PersistenceUtils.closeStatement(statement);
			if (connection != null)
				connection.close();
		}

	}

	public void queryInsert(String dataSouceName, String sql, String application) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = getConnection(dataSouceName, application);
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(sql);
			statement.execute();
			connection.commit();
		} catch (Exception e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			PersistenceUtils.closeStatement(statement);
			if (connection != null)
				connection.close();
		}
	}

	public void remove(String dataSouceName, String sql, String application) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = getConnection(dataSouceName, application);
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(sql);
			statement.execute();
			connection.commit();
		} catch (Exception e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			PersistenceUtils.closeStatement(statement);
			if (connection != null)
				connection.close();
		}
	}

	public static void main(String[] args) {
		try {
			ApplicationProcess appProcess = (ApplicationProcess) ProcessFactory.createProcess(ApplicationProcess.class);
			DataSourceProcess dsProcess = new DataSourceProcessBean();
			ApplicationVO applicationVO = appProcess.doViewByName("广西质检系统");
			String sql = "SELECT top 1 sszs,sstp,ssjybh FROM ssjyb WHERE ssjybh='Z10-000254-2'";
			
			Map<?, ?> map = dsProcess.findDataSourceSQL("object", sql, applicationVO.getId());
			Blob blob = (Blob)map.get("sstp");
			FileOperate.writeFile("D:\\java\\workspace\\obpm\\src\\main\\webapp\\uploads\\temp\\11df-f2e6-31714265-8538-37537b468fc8sstp.jpg", blob.getBinaryStream());
			
			Blob blob2 = (Blob)map.get("sszs");
			FileOperate.writeFile("D:/test.doc", blob2.getBinaryStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
