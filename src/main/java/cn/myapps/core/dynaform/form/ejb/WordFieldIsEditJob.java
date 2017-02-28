package cn.myapps.core.dynaform.form.ejb;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import cn.myapps.constans.Environment;
import cn.myapps.util.timer.Job;

public class WordFieldIsEditJob extends Job {
	
	public final static Logger LOG = Logger.getLogger(WordFieldIsEditJob.class);

	public void run() {
		try {
			LOG.debug("********************* WordFieldIsEdit Job Start ********************");
			Map<String,WordFieldIsEdit> map = Environment.wordFieldIsEdit;
			Date curDate = new Date(); // 当前日期
			if(!map.isEmpty()){
				Set<Entry<String, WordFieldIsEdit>> set = map.entrySet();
				for (Iterator<Entry<String, WordFieldIsEdit>> iterator = set.iterator(); iterator.hasNext();) {
					Entry<String, WordFieldIsEdit> entry = (Entry<String, WordFieldIsEdit>) iterator
							.next();
					String wordid = entry.getKey();
					WordFieldIsEdit word = entry.getValue();
					//把word激活时间与当前时间差2分钟以上的移除掉
					if(curDate.getTime() - word.getEditTime().getTime() >= WordFieldIsEdit.JOB_PEIROD){
						Environment.wordFieldIsEdit.remove(wordid);
					}
				}
			}
			LOG.debug("********************* WordFieldIsEdit Job End ********************");
		} catch (Exception e) {
			LOG.error("WordFieldIsEdit Job Error: ", e);
		}
	}

}
