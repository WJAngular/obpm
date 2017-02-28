package cn.myapps.km.comments.ejb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.myapps.km.base.dao.DaoManager;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractBaseProcessBean;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.comments.dao.CommentsDAO;
import cn.myapps.km.disk.dao.NFileDAO;
import cn.myapps.km.disk.ejb.NFile;
import cn.myapps.km.disk.ejb.NFileProcess;
import cn.myapps.km.disk.ejb.NFileProcessBean;
import cn.myapps.km.util.Sequence;

public class CommentsProcessBean extends AbstractBaseProcessBean<Comments>
		implements CommentsProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8289920841384412153L;

	public NRuntimeDAO getDAO() throws Exception {
		// TODO Auto-generated method stub
		return DaoManager.getCommentsDAO(getConnection());
	}

	public void doCreate(NObject no) throws Exception {
		Comments comments = (Comments) no;
		try {
			// 1.设置id
			if (comments.getId() == null) {
				comments.setId(Sequence.getSequence());
			}
			beginTransaction();

			getDAO().create(comments);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
		}
	}

	public void doCreate(NObject no, HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		Comments comments = (Comments) no;
		NFileProcess fileProcess = new NFileProcessBean();
		try {
			
			if((int)countBy(comments.getFileId(),comments.getUserId())>0){
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(" 您已经评论过了，不能再评论！");
				return;
			}
			
			beginTransaction();
			doCreate(comments);

			NFile file = (NFile) fileProcess.doView(comments.getFileId());
			if (file != null) {
				if (comments.isGood()) {
					file.setGood(file.getGood() + 1);
				} else {
					file.setBad(file.getBad() + 1);
				}
				fileProcess.doUpdate(file);
			}
			response.getWriter().print(file.getGood()+"-"+file.getBad()+"-"+file.getScore());
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
		}
	}

	public void doRemove(String pk) throws Exception {
		// TODO Auto-generated method stub
		try {
			beginTransaction();
			Comments comments = (Comments) getDAO().find(pk);
			getDAO().remove(pk);
			// ((NFileDAO)DaoManager.getFileAccessDAO(getConnection())).remove(pk);
			NFile file = ((NFileDAO) DaoManager
					.getFileAccessDAO(getConnection())).find(comments
					.getFileId());
			file.setGood((int) countByGood(comments.getFileId()));
			file.setBad((int) countByBad(comments.getFileId()));
			((NFileDAO) DaoManager.getFileAccessDAO(getConnection()))
					.update(file);
			commitTransaction();

		} catch (Exception e) {
			rollbackTransaction();
		}
	}

	@Override
	public void doUpdate(NObject no) throws Exception {
		// TODO Auto-generated method stub
		Comments comments = (Comments) no;
		((CommentsDAO) getDAO()).update(no);
		NFile file = ((NFileDAO) DaoManager.getFileAccessDAO(getConnection()))
				.find(comments.getFileId());
		file.setGood((int) countByGood(comments.getFileId()));
		file.setBad((int) countByBad(comments.getFileId()));
		((NFileDAO) DaoManager.getFileAccessDAO(getConnection())).update(file);
	}

	@Override
	public Comments doView(String id) throws Exception {
		return (Comments) getDAO().find(id);
	}

	public long countByBad(java.lang.String fileID) throws Exception {
		return ((CommentsDAO) getDAO()).countByBad(fileID);
	}

	public long countByGood(java.lang.String fileID) throws Exception {
		return ((CommentsDAO) getDAO()).countByGood(fileID);
	}

	public long countBy(String fileId, String userId) throws Exception {
		return ((CommentsDAO) getDAO()).countBy(fileId,userId);
	}

}
