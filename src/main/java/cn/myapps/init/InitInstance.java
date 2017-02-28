package cn.myapps.init;


import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;

public class InitInstance implements IInitialization {

	public void run() throws InitializationException {
		try {
			ApplicationProcess process = (ApplicationProcess) ProcessFactory
					.createProcess(ApplicationProcess.class);
			// Collection colls = process.doSimpleQuery(null, null);
			if (process.isEmpty()) {
				ApplicationVO vo = new ApplicationVO();
				vo.setId(Sequence.getSequence());
				vo.setSortId(Sequence.getTimeSequence());
				vo.setName("Default");
				vo.setDescription("OBPM - Default System");
//				vo.setDbtype(DefaultProperty.getProperty("DB_TYPE"));
				process.doUpdate(vo);

//				new InitResource().run(vo.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
