package cn.myapps.km.comments.ejb;

import java.util.Date;

import org.junit.Test;

public class CommentsProcessBeanTest {
	
	
	CommentsProcess process = new CommentsProcessBean();
	
	@Test
	public void testCreate(){
		try {
			Comments vo = new Comments();
			vo.setFileId("11e2-b3b9-abb9ac50-9b77-9d3e99080c9e");
			vo.setUserId("11e2-95de-0f5c4639-8763-7dbf812b1f64");
			vo.setUserName("小李");
			vo.setAssessmentDate(new Date());
			vo.setGood(true);
			vo.setBad(false);
			vo.setContent(null);
			process.doCreate(vo);
		} catch (Exception e) {
			
		}
	}

}
