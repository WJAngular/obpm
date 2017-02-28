package cn.myapps.km.baike.search;

import java.io.File;
import java.io.UTFDataFormatException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;
import cn.myapps.km.baike.content.ejb.EntryContent;
import cn.myapps.km.baike.knowledge.ejb.KnowledgeQuestion;

/**
 * 对词条内容全文检索
 * @author Allen
 *
 */
public class BKSearchEngine {
	
private final static String INDEX_PATH = "index";

/*
public Collection<String> search(String sql,Connection con,String queryString)throws Exception  {  
	createIndex(sql, con);
     File indexDir = new File(indexPath);
     Directory dir = FSDirectory.open(indexDir);
    //请求字段  
    // 1，把要搜索的文本解析为 Query
    String[] fields = {"content"};
    QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_36, fields, getAnalyzer());
    Query query = queryParser.parse(queryString);
   
    IndexSearcher indexSearcher = new IndexSearcher(dir);
    Collection<String> EntryidCollection=new ArrayList<String>();
    Filter filter = null;
    TopDocs topDocs = indexSearcher.search(query, filter, 10000);
    System.out.println("总共有【" + topDocs.totalHits + "】条匹配结果");
    
    for (ScoreDoc scoreDoc : topDocs.scoreDocs){
    // 文档内部编号
    int index = scoreDoc.doc;
    // 根据编号取出相应的文档
    Document doc = indexSearcher.doc(index);
    EntryidCollection.add(doc.get("entryId"));
//    System.out.println("content = " + doc.get("content"));
//    System.out.println("id = " + doc.get("id"));
    
    }
    return EntryidCollection;
   }  

private  ResultSet getResult(String sql,Connection con) throws Exception{
      try{
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        return rs;
      }
      catch(SQLException e){
        System.out.println(e);
      }
      return null;
    }

private Analyzer getAnalyzer(){
		//return new IKAnalyzer();
	return new StandardAnalyzer(Version.LUCENE_36);
    }

private void createIndex(String sql,Connection con) throws Exception{
			
		ResultSet rs=getResult(sql,con);
		File indexDir = new File(indexPath);
		
		//如果文件夹不存在则创建  
		if  (!indexDir .exists()  && !indexDir .isDirectory())    
		{     
			indexDir .mkdir();  
		} else 
		{
		    System.out.println("//目录存在"+indexPath);
		}
		Directory dir = FSDirectory.open(indexDir);
       IndexWriter indexWriter = new IndexWriter(dir, getAnalyzer(), true,MaxFieldLength.LIMITED);
      try{
        while(rs.next()){
            Document doc=new Document();
            String content = rs.getString("CONTENT")== null ? "" : rs.getString("CONTENT");   
            Field fcontent = new Field("content",content, Field.Store.NO,Field.Index.ANALYZED);
            
            String id = rs.getString("ID")== null ? "" : rs.getString("id");   
            Field fId = new Field("id",id, Field.Store.YES,Field.Index.ANALYZED);
            
            String entryId = rs.getString("ENTRYID")== null ? "" : rs.getString("ENTRYID");   
            Field fEntryId= new Field("entryId",entryId, Field.Store.YES,Field.Index.ANALYZED);
            
            doc.add(fcontent);
            doc.add(fId);
            doc.add(fEntryId);
            indexWriter.addDocument(doc);
          }
        indexWriter.optimize();
            indexWriter.close();
      }
      catch(IOException e){
       e.printStackTrace();
      }
      catch(SQLException e){
       e.printStackTrace();
      }
    } 
	*/


	public static void createQuestionIndex(String questionTitle, KnowledgeQuestion question, String realPath, boolean isUpdate) throws Exception {
		Directory dir = null;
		IndexWriter indexWriter = null;
		long startTime = new Date().getTime();
		
		try {
			//File indexDir = new File(realPath + "\\" + entry.getId() + "\\index");
			File indexDir = new File(realPath+"\\"+INDEX_PATH);
			dir = FSDirectory.open(indexDir);
			Analyzer luceneAnalyzer = new IKAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36,
					luceneAnalyzer);
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			indexWriter = new IndexWriter(dir, iwc);

			if (isUpdate) {
				indexWriter.deleteDocuments(new Term("iddel", question.getId()
						.replaceAll("-", "")));
				System.out.println("删除索引成功!");
			}
		
			Document document = new Document();
			
			//问题ID
			Field fieldId = new Field("id", question.getId(), Field.Store.YES,
					Field.Index.NO);
			Field fieldId4del = new Field("iddel",  question.getId()
					.replaceAll("-", ""), Field.Store.NO,
					Field.Index.ANALYZED);// 建立用来删除索引的文档id,用于删除索引,id只能全英数字
			//问题标题
			Field fieldTitle = new Field("title", question.getTitle(), Field.Store.YES,
					Field.Index.NO);
			
			//问题内容
			Field filedcontent = new Field("content", question.getContent(), Field.Store.YES,
					Field.Index.ANALYZED);
			
			//内容作者
			Field filedAuthor = new Field("author", question.getAuthor().getId(), Field.Store.YES,
					Field.Index.ANALYZED);
			
			Field fieldBody = new Field("body",questionTitle  + question.getContent() , Field.Store.NO,
					Field.Index.ANALYZED);
			
			Field modifiedField = null;
			Date created = question.getCreated();
			if (created != null) {
				modifiedField = new Field("created",String.valueOf(created.getTime()), Field.Store.YES,
						Field.Index.NO);
			} else {
				modifiedField = new Field("created", "", Field.Store.YES,
						Field.Index.NO);
			}
			
			document.add(fieldId);
			document.add(filedcontent);
			document.add(fieldId4del);
			document.add(filedAuthor);
			document.add(fieldTitle);
			document.add(modifiedField);
			document.add(fieldBody);
			
			indexWriter.addDocument(document);
//			 indexWriter.forceMerge(1);
			indexWriter.commit();
			
			System.err.println("total-->"+indexWriter.numDocs());

		} catch (UTFDataFormatException e1) {

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (indexWriter != null) {
				indexWriter.close();
			}
			if (dir != null) {
				dir.close();
			}
			// PersistenceUtils.closeConnection();
		}
		// 测试一下索引的时间
		long endTime = new Date().getTime();
		System.out.println("这花费了" + (endTime - startTime) + " 毫秒建立文档索引!");
	}
	
	public static Collection<KnowledgeQuestion> searchQuestion(String queryString, String realPath, int pageSize, int curragePage) throws Exception {
		Collection<KnowledgeQuestion> list = new ArrayList<KnowledgeQuestion>();
		IndexReader reader = null;
		IndexSearcher searcher = null;
		ScoreDoc[] hits = null;

		BooleanQuery query = new BooleanQuery();
//		Analyzer analyzer = new IKAnalyzer();
		try {
			File indexFile = new File(realPath + "\\" + INDEX_PATH);
			if (indexFile.exists()) {
				reader = IndexReader.open(FSDirectory.open(indexFile));
				searcher = new IndexSearcher(reader);
//				String[] fields = { "title", "body" };
//				BooleanClause.Occur[] flags = { BooleanClause.Occur.SHOULD,
//						BooleanClause.Occur.SHOULD };
//				query = MultiFieldQueryParser.parse(Version.LUCENE_36,
//						queryString, fields, flags, analyzer);
				String[] queryStrings = queryString.split(" ");
				for(int i=0; i<queryStrings.length; i++){
					query.add(new TermQuery(new Term("body", queryStrings[i].toLowerCase())), BooleanClause.Occur.MUST);
				}
				if (searcher != null) {
					
					long startTime = new Date().getTime();
					
					TopDocs result = searcher.search(query, pageSize
							* (curragePage + 5));
					long endTime = new Date().getTime();

					hits = result.scoreDocs;

					if ((pageSize * curragePage - 10) > hits.length) {
						curragePage = (int) Math
								.ceil((double) hits.length / 10);
					}

					int begin = pageSize * (curragePage - 1);
					int end = Math.min(begin + pageSize, hits.length);
					if (hits.length > 0) {
						System.out.println("此次搜索共花费" + (endTime - startTime)
								+ "毫秒,找到" + hits.length + " 个结果");
						for (int i = begin; i < end; i++) {
							Document doc = searcher.doc(hits[i].doc);
							KnowledgeQuestion question = new KnowledgeQuestion();
							question.setId(doc.get("id"));
							question.setAuthor(doc.get("author"));
							question.setContent(doc.get("content"));
							long created = Long.parseLong(doc.get("created"));
							question.setCreated(new Date(created));
							question.setTitle(doc.get("title"));

							list.add(question);
						}
					}
				}
				//ServletActionContext.getRequest().setAttribute("_curragePage",curragePage);
				//ServletActionContext.getRequest().setAttribute("_rowCount",hits.length);
			}
		} catch (UTFDataFormatException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (searcher != null) {
				searcher.close();
			}
			if (reader != null) {
				reader.close();
			}
		}

		return list;
	}
	
	public static void createIndex(String entryName, String keyWord, EntryContent content, String realPath, boolean isUpdate) throws Exception {
		Directory dir = null;
		IndexWriter indexWriter = null;
		long startTime = new Date().getTime();
		
		try {
			//File indexDir = new File(realPath + "\\" + entry.getId() + "\\index");
			File indexDir = new File(realPath+"\\"+INDEX_PATH);
			dir = FSDirectory.open(indexDir);
			Analyzer luceneAnalyzer = new IKAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36,
					luceneAnalyzer);
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			indexWriter = new IndexWriter(dir, iwc);

			if (isUpdate) {
				indexWriter.deleteDocuments(new Term("iddel", content.getEntryId()
						.replaceAll("-", "")));
				System.out.println("删除索引成功!");
			}
			//File file = new File(realPath +"\\"+INDEX_PATH );
			// 增加document到索引去
			//System.out.println("File " + entry.getName() + "正在被索引.....");

			//int totalPages = 0;

			Document document = new Document();
			
			//词条内容ID
			Field fieldId = new Field("id", content.getId(), Field.Store.YES,
					Field.Index.NO);
			
			//词条ID
			Field fieldEntryId = new Field("entryId", content.getEntryId(), Field.Store.YES,
					Field.Index.NO);
			
			Field fieldId4del = new Field("iddel", content.getEntryId()
					.replaceAll("-", ""), Field.Store.NO,
					Field.Index.ANALYZED);// 建立用来删除索引的文档id,用于删除索引,id只能全英数字
			//内容简介
			//Field filedSummary = new Field("summary", content.getSummary(), Field.Store.YES,Field.Index.ANALYZED);
			
			//内容作者
			Field filedAuthor = new Field("author", content.getAuthor().getId(), Field.Store.YES,
					Field.Index.ANALYZED);
			
			Field fieldBody = new Field("body",entryName + keyWord + content.getSummary() + content.getContent(), Field.Store.NO,
					Field.Index.ANALYZED);
			
			Field modifiedField = null;
			Date passedTime = content.getHandleTime();
			if (passedTime != null) {
				modifiedField = new Field("passedTime",String.valueOf(passedTime.getTime()), Field.Store.YES,
						Field.Index.NO);
			} else {
				modifiedField = new Field("passedTime", "", Field.Store.YES,
						Field.Index.NO);
			}
		
			document.add(fieldId);
			document.add(fieldEntryId);
			document.add(fieldId4del);
			document.add(filedAuthor);
			document.add(modifiedField);
			//document.add(filedSummary);
			document.add(fieldBody);
			
			indexWriter.addDocument(document);
//			 indexWriter.forceMerge(1);
			indexWriter.commit();
			
			System.err.println("total-->"+indexWriter.numDocs());

		} catch (UTFDataFormatException e1) {

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (indexWriter != null) {
				indexWriter.close();
			}
			if (dir != null) {
				dir.close();
			}
			// PersistenceUtils.closeConnection();
		}
		// 测试一下索引的时间
		long endTime = new Date().getTime();
		System.out.println("这花费了" + (endTime - startTime) + " 毫秒建立文档索引!");
	}
	
	
	public static Collection<EntryContent> search(String queryString, String realPath, int pageSize, int curragePage) throws Exception {
		Collection<EntryContent> list = new ArrayList<EntryContent>();
		IndexReader reader = null;
		IndexSearcher searcher = null;
		ScoreDoc[] hits = null;

		BooleanQuery query = new BooleanQuery();
//		Analyzer analyzer = new IKAnalyzer();
		try {
			File indexFile = new File(realPath + "\\" + INDEX_PATH);
			if (indexFile.exists()) {
				reader = IndexReader.open(FSDirectory.open(indexFile));
				searcher = new IndexSearcher(reader);
//				String[] fields = { "title", "body" };
//				BooleanClause.Occur[] flags = { BooleanClause.Occur.SHOULD,
//						BooleanClause.Occur.SHOULD };
//				query = MultiFieldQueryParser.parse(Version.LUCENE_36,
//						queryString, fields, flags, analyzer);
				String[] queryStrings = queryString.split(" ");
				for(int i=0; i<queryStrings.length; i++){
					query.add(new TermQuery(new Term("body", queryStrings[i].toLowerCase())), BooleanClause.Occur.MUST);
				}
				if (searcher != null) {
					
					long startTime = new Date().getTime();
					
					TopDocs result = searcher.search(query, pageSize
							* (curragePage + 5));
					long endTime = new Date().getTime();

					hits = result.scoreDocs;

					if ((pageSize * curragePage - 10) > hits.length) {
						curragePage = (int) Math
								.ceil((double) hits.length / 10);
					}

					int begin = pageSize * (curragePage - 1);
					int end = Math.min(begin + pageSize, hits.length);
					if (hits.length > 0) {
						System.out.println("此次搜索共花费" + (endTime - startTime)
								+ "毫秒,找到" + hits.length + " 个结果");
						for (int i = begin; i < end; i++) {
							Document doc = searcher.doc(hits[i].doc);
							EntryContent content = new EntryContent();
							content.setId(doc.get("id"));
							content.setEntryId(doc.get("entryId"));
							content.setAuthor(doc.get("author"));
							long handleTime = Long.parseLong(doc.get("passedTime"));
							content.setHandleTime(new Date(handleTime));
							content.setSummary(doc.get("summary"));

							list.add(content);
						}
					}
				}
				//ServletActionContext.getRequest().setAttribute("_curragePage",curragePage);
				//ServletActionContext.getRequest().setAttribute("_rowCount",hits.length);
			}
		} catch (UTFDataFormatException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (searcher != null) {
				searcher.close();
			}
			if (reader != null) {
				reader.close();
			}
		}

		return list;
	}
	
	public static void main(String[] args) throws Exception {
		EntryContent content = new EntryContent();
		content.setId("123456");
		content.setEntryId("234567");
		content.setAuthor("sj0056");
		content.setHandleTime(new Date());
		content.setSummary("摘要");
		content.setContent("afasdfasdff成杰ffffffffffffffffffffff成杰");
		
		createIndex("苹果","水果", content, "d:\\test", true);
		
		Collection<EntryContent> list = search("苹果", "d:\\test",  1, 3);
		System.out.println("查询出记录个数:" + list.size());
		if (list !=null && list.size()>0) {
			for (Iterator<EntryContent> iter = list.iterator(); iter.hasNext();) {
				EntryContent vo = iter.next();
				System.out.println(vo.getContent());
			}
		}
		
	}
}
