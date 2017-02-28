package cn.myapps.km.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.struts2.ServletActionContext;
import org.wltea.analyzer.lucene.IKAnalyzer;

import cn.myapps.km.disk.ejb.NFile;
import cn.myapps.km.disk.ejb.NFileProcessBean;
import cn.myapps.km.disk.ejb.SearchNFile;


public class SearchEngine {

	public static final String INDEXPATH = "E:\\全文搜索" + "\\index";

	// private static Directory dir = null;
	// private static IndexWriter indexWriter = null;

	private static HashMap<String, IndexWriter> _indexWriters = new HashMap();

	private final static Object _lock = new Object();

	public static void addPrivateDiskIndex(NFile nfile, String realPath,
			String ndiskid, boolean isUpdate) throws Exception {
		/*
		 * 这里放索引文件的位置
		 */
		// File indexDir = new File(INDEXPATH);
		Directory dir = null;
		IndexWriter indexWriter = null;
		long startTime = new Date().getTime();

		try {
			File indexDir = new File(realPath + "\\" + ndiskid + "\\index");
			dir = FSDirectory.open(indexDir);
			Analyzer luceneAnalyzer = new IKAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36,
					luceneAnalyzer);
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			indexWriter = new IndexWriter(dir, iwc);

			if (isUpdate) {
				indexWriter.deleteDocuments(new Term("iddel", nfile.getId()
						.replaceAll("-", "")));
				System.out.println("删除索引成功!");
			}

			File file = new File(realPath + nfile.getUrl());
			// 增加document到索引去
			if (file.exists() && file.isFile()) {
				System.out.println("File " + file.getName() + "正在被索引.....");

				PdfSummary summary = FileReaderAll(nfile,
						realPath + nfile.getUrl(), "GBK");
				String temp = summary != null ? summary.summary : "";
				int totalPages = summary != null ? summary.totalPages : 0;

				Document document = new Document();
				Field fieldId = new Field("id", nfile.getId(), Field.Store.YES,
						Field.Index.NO);
				Field fieldId4del = new Field("iddel", nfile.getId()
						.replaceAll("-", ""), Field.Store.NO,
						Field.Index.ANALYZED);// 建立用来删除索引的文档id,用于删除索引,id只能全英数字

				Field fieldTotalPages = new Field("totalPages",
						totalPages + "", Field.Store.YES, Field.Index.NO);

				document.add(fieldId);
				document.add(fieldId4del);
				document.add(fieldTotalPages);

				if (nfile.getName() != null) {
					Field fieldPath = new Field("title", nfile.getName(),
							Field.Store.YES, Field.Index.ANALYZED);
					document.add(fieldPath);
				}
				if (temp != null) {
					Field fieldBody = new Field("body", nfile.getName() + temp, Field.Store.NO,
							Field.Index.ANALYZED);
					document.add(fieldBody);
				}

				String memo = (nfile.getMemo()==null ? "" : nfile.getMemo()) + temp;

				memo = memo.length()>100 ? memo.substring(0,100):memo;

				Field fieldmemo = new Field("memo", memo,
						Field.Store.YES, Field.Index.ANALYZED);
				document.add(fieldmemo);

				Field modifiedField = new Field("modified",
						String.valueOf(new Date().getTime()), Field.Store.YES,
						Field.Index.NO);

				document.add(modifiedField);
				indexWriter.addDocument(document);
				// indexWriter.forceMerge(1);
				indexWriter.commit();
				int docCount = indexWriter.numDocs();
				System.err.println("total docs ->" + docCount);
			}

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

	/**
	 * 建立索引
	 * 
	 * @throws Exception
	 */
	public static void addPublicDiskIndex(NFile nfile, String realPath,
			String ndiskid, boolean isUpdate) throws Exception {
		/*
		 * 这里放索引文件的位置
		 */
		// File indexDir = new File(INDEXPATH);

		IndexWriter indexWriter = _indexWriters.get(ndiskid);
		long startTime = new Date().getTime();

		try {

			synchronized (_lock) {
				if (indexWriter == null) {
					File indexDir = new File(realPath + "\\" + ndiskid
							+ "\\index");

					Directory dir = null;
					dir = FSDirectory.open(indexDir);
					Analyzer luceneAnalyzer = new IKAnalyzer();
					IndexWriterConfig iwc = new IndexWriterConfig(
							Version.LUCENE_36, luceneAnalyzer);
					iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
					indexWriter = new IndexWriter(dir, iwc);

					_indexWriters.put(ndiskid, indexWriter);
				}
			}

			if (isUpdate) {
				indexWriter.deleteDocuments(new Term("iddel", nfile.getId()
						.replaceAll("-", "")));
				System.out.println("删除索引成功!");
			}

			File file = new File(realPath + nfile.getUrl());
			// 增加document到索引去
			if (file.exists() && file.isFile()) {
				System.out.println("File " + file.getName() + "正在被索引.....");

				PdfSummary summary = FileReaderAll(nfile,
						realPath + nfile.getUrl(), "GBK");
				String temp = summary != null ? summary.summary : "";
				int totalPages = summary != null ? summary.totalPages : 0;

				Document document = new Document();
				Field fieldId = new Field("id", nfile.getId(), Field.Store.YES,
						Field.Index.NO);
				Field fieldId4del = new Field("iddel", nfile.getId()
						.replaceAll("-", ""), Field.Store.NO,
						Field.Index.ANALYZED);// 建立用来删除索引的文档id,用于删除索引,id只能全英数字

				Field fieldTotalPages = new Field("totalPages",
						totalPages + "", Field.Store.YES, Field.Index.NO);

				document.add(fieldId);
				document.add(fieldId4del);
				document.add(fieldTotalPages);

				if (nfile.getName() != null) {
					Field fieldPath = new Field("title", nfile.getName(),
							Field.Store.YES, Field.Index.ANALYZED);
					document.add(fieldPath);
				}
				if (temp != null) {
					Field fieldBody = new Field("body", nfile.getName() + temp, Field.Store.NO,
							Field.Index.ANALYZED);
					document.add(fieldBody);
				}

				String memo = (nfile.getMemo()==null ? "" : nfile.getMemo()) + temp;

				memo = memo.length()>100 ? memo.substring(0,100):memo;

				Field fieldmemo = new Field("memo", memo,
						Field.Store.YES, Field.Index.ANALYZED);
				document.add(fieldmemo);

				Field modifiedField = new Field("modified",
						String.valueOf(new Date().getTime()), Field.Store.YES,
						Field.Index.NO);

				document.add(modifiedField);
				indexWriter.addDocument(document);
				// indexWriter.forceMerge(1);
				indexWriter.commit();
				int docCount = indexWriter.numDocs();
				System.err.println("total docs ->" + docCount);
			}

		} catch (UTFDataFormatException e1) {

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// if(indexWriter != null){
			// indexWriter.close();
			// }
			// if(dir != null){
			// dir.close();
			// }
			// PersistenceUtils.closeConnection();
		}
		// 测试一下索引的时间
		long endTime = new Date().getTime();
		System.out.println("这花费了" + (endTime - startTime) + " 毫秒建立文档索引!");
	}

	// private static boolean pdfFileisExists(File file, NFile nfile) throws
	// Exception{
	// boolean flag = false;
	// String fileName = file.getCanonicalPath();
	// if(!nfile.getType().equals(NFile.TYPE_PDF)){
	// String relatePdfPath = fileName.substring(0,fileName.lastIndexOf("\\")) +
	// "\\swf\\" + nfile.getId() + ".pdf";
	// File pdfFile = new File(relatePdfPath);
	// if(pdfFile.exists()){
	// flag = true;
	// }
	// }else {
	// File pdfFile = new File(fileName);
	// if(pdfFile.exists()){
	// flag = true;
	// }
	// }
	//
	// return flag;
	// }

	public static void deletePrivateDiskIndex(String nfileId, String realPath,
			String ndiskid) throws Exception {
		Directory dir = null;
		IndexWriter indexWriter = null;
		// 删除含有指定Term的Document数据
		try {
			File indexDir = new File(realPath + "\\" + ndiskid + "\\index");
			dir = FSDirectory.open(indexDir);
			Analyzer luceneAnalyzer = new IKAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36,
					luceneAnalyzer);
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			indexWriter = new IndexWriter(dir, iwc);

			indexWriter.deleteDocuments(new Term("iddel", nfileId.replaceAll(
					"-", "")));
			// indexWriter.forceMerge(1);
			indexWriter.commit();
			System.out.println("删除索引成功!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (indexWriter != null) {
				indexWriter.close();
			}
			if (dir != null) {
				dir.close();
			}
		}

	}

	/**
	 * 删除索引
	 * 
	 * @throws Exception
	 */
	public static void deletePublicDiskIndex(String nfileId, String realPath,
			String ndiskid) throws Exception {
		IndexWriter indexWriter = _indexWriters.get(ndiskid);
		// 删除含有指定Term的Document数据

		try {
			synchronized (_lock) {
				if (indexWriter == null) {
					File indexDir = new File(realPath + "\\" + ndiskid
							+ "\\index");

					Directory dir = null;
					dir = FSDirectory.open(indexDir);
					Analyzer luceneAnalyzer = new IKAnalyzer();
					IndexWriterConfig iwc = new IndexWriterConfig(
							Version.LUCENE_36, luceneAnalyzer);
					iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
					indexWriter = new IndexWriter(dir, iwc);

					_indexWriters.put(ndiskid, indexWriter);
				}
			}

			indexWriter.deleteDocuments(new Term("iddel", nfileId.replaceAll(
					"-", "")));
			// indexWriter.forceMerge(1);
			indexWriter.commit();
			System.out.println("删除索引成功!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public static Collection<SearchNFile> search(String queryString, String realPath,
			String ndiskid, int pageSize, int curragePage) throws Exception {
		Collection<SearchNFile> files = new ArrayList<SearchNFile>();
		IndexReader reader = null;
		IndexSearcher searcher = null;
		ScoreDoc[] hits = null;

		BooleanQuery query = new BooleanQuery();
//		Analyzer analyzer = new IKAnalyzer();
		try {
			File indexFile = new File(realPath + "\\" + ndiskid + "\\index");
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
							SearchNFile searchFile = new SearchNFile();
							searchFile.setId(doc.get("id"));
							searchFile.setName(doc.get("title"));

							String memo = doc.get("memo");
							memo = memo == null ? "" : memo;

							String body = doc.get("body");
							body = body == null ? "" : body;

							String summary = memo + body;
							if (summary.length() > 100) {
								searchFile.setMemo(summary.substring(0, 100)
										+ "......");
							} else {
								searchFile.setMemo(summary + "......");
							}
							long modifiedtime = Long.parseLong(doc
									.get("modified"));
							searchFile.setLastmodify(new Date(modifiedtime));
							searchFile.setTotalPages(doc.get("totalPages"));
							files.add(searchFile);
						}
					}
				}
				ServletActionContext.getRequest().setAttribute("_curragePage",
						curragePage);
				ServletActionContext.getRequest().setAttribute("_rowCount",
						hits.length);
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

		return files;
	}

	private final static class PdfSummary {
		private String summary;
		private int totalPages;
	}

	public static PdfSummary FileReaderAll(NFile nfile, String realFilePath,
			String charset) throws Exception {
		PdfSummary summary = null;

		NFileProcessBean nfileProcess = new NFileProcessBean();
		String temp = "";
		String pdfPath = "";
		String fileName = "";
		try {
			fileName = realFilePath.substring(
					realFilePath.lastIndexOf("\\") + 1,
					realFilePath.lastIndexOf("."));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("realPath-->" + realFilePath);
		}

		if (nfileProcess.isReadableFile(nfile.getType())) {
			if (!nfile.getType().equalsIgnoreCase(NFile.TYPE_PDF)) {
				pdfPath = realFilePath.substring(0,
						realFilePath.lastIndexOf("\\"))
						+ "\\swf\\" + fileName + ".pdf";
			} else {
				pdfPath = realFilePath;
			}

			if (new File(pdfPath).exists()) {
				FileInputStream in = null;
				PDDocument pdfdocument = null;
				try {
					in = new FileInputStream(pdfPath);
					// PDFParser parser = new PDFParser(in);
					// parser.parse();
					// PDDocument pdfdocument = parser.getPDDocument();
					pdfdocument = PDDocument.load(in);

					int totalPages = pdfdocument.getNumberOfPages();

					System.err.println("totalPages-->" + totalPages);
					PDFTextStripper stripper = new PDFTextStripper();

					// stripper = new PDFTextStripper();
					// 设置是否排序
					stripper.setSortByPosition(false);
					// 设置起始页
					stripper.setStartPage(1);
					// 设置结束页
					stripper.setEndPage(5);

					temp = stripper.getText(pdfdocument);

					summary = new PdfSummary();
					summary.summary = temp;
					summary.totalPages = totalPages;

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (in != null)
						in.close();
					if (pdfdocument != null)
						pdfdocument.close();

				}
			}
		}

		return summary;
	}

	public static void main(String[] args) throws IOException {
		Collection<NFile> files = new ArrayList<NFile>();
		String index = "E:\\workspaces\\trunk2012\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\obpm\\ndisk"
				+ "\\index";
		IndexReader reader = null;
		try {
			reader = IndexReader.open(FSDirectory.open(new File(index)));
		} catch (CorruptIndexException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		IndexSearcher searcher = new IndexSearcher(reader);
		ScoreDoc[] hits = null;

		Query query = null;
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		try {
			String[] fields = { "title", "body" };
			BooleanClause.Occur[] flags = { BooleanClause.Occur.SHOULD,
					BooleanClause.Occur.SHOULD };
			query = MultiFieldQueryParser.parse(Version.LUCENE_36, "我的测试文档",
					fields, flags, analyzer);
			if (searcher != null) {
				long startTime = new Date().getTime();
				TopDocs result = searcher.search(query, 50);
				long endTime = new Date().getTime();
				hits = result.scoreDocs;
				if (hits.length > 0) {
					System.out.println("此次搜索共花费" + (endTime - startTime)
							+ "毫秒,找到" + hits.length + " 个结果");
					for (int i = 0; i < hits.length; i++) {
						Document doc = searcher.doc(hits[i].doc);
						NFile file = new NFile();
						file.setId(doc.get("id"));
						file.setName(doc.get("title"));
						file.setContent(doc.get("body").substring(0, 5));
						System.out.println(doc.get("totalPages"));
						//file.setTotalPages(doc.get("totalPages"));
						files.add(file);
						System.out.println(file.getId());
						System.out.println(file.getName());
					}
				}
			}
		} catch (Exception e) {
			searcher.close();
			e.printStackTrace();
		} finally {
			searcher.close();
		}
	}

	protected void finalize() throws Throwable {
		super.finalize();
		for (Iterator<String> iter = _indexWriters.keySet().iterator(); iter
				.hasNext();) {
			String key = iter.next();
			IndexWriter indexWriter = _indexWriters.get(key);
			try {
				if (indexWriter != null) {
					if (indexWriter.getDirectory() != null) {
						indexWriter.getDirectory().close();
					}
					indexWriter.close();
				}
			} finally {
				_indexWriters.remove(key);
			}
		}
	}
}
