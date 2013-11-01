package com.shixy.web.szlib.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shixy.web.WsException;
import com.shixy.web.szlib.Constant;
import com.shixy.web.szlib.response.Area;
import com.shixy.web.szlib.response.Book;
import com.shixy.web.szlib.response.BookReview;
import com.shixy.web.szlib.response.ChildLib;
import com.shixy.web.szlib.response.LoanBook;
import com.shixy.web.szlib.response.News;
import com.shixy.web.szlib.response.ReverseInfo;
import com.shixy.web.szlib.response.SelfLib;
import com.shixy.web.szlib.response.User;

@Service("szLibService")
public class SZLibServiceImpl implements SZLibService  {
	
	private static final String ERR_MSG = "连接服务器失败";
	
	public Object login(String username,String password){
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", username);
		map.put("password", password);
		Document doc = null;
		try {
			Response response = this.getLoginResponse(username, password);
			doc = response.parse();
			String message = doc.getElementsByTag("message").text();
			if(message.equals("OK")){
				User user = new User();
				user.setLibrary(response.cookie("library"));
				user.setLoginName(response.cookie("Username"));
				user.setRecordNo(response.cookie("recordno"));
				user.setUserId(response.cookie("UserID"));
				user.setUserName(URLDecoder.decode(response.cookie("Name"),"UTF-8"));
				//通过挂失页面获取读书证卡号
				Connection conLost = Jsoup.connect(Constant.BASE_URL+Constant.LOST_URL).timeout(1000*60);
				for(Entry<String, String> entry :response.cookies().entrySet()){
					conLost.cookie(entry.getKey(), entry.getValue());
				}
				Document docLost = conLost.get();
				String card = docLost.select("table.tab_1 tr:eq(0) td:eq(1)").text();
				user.setCardNo(card);
				return user;
			}else{
				map = new HashMap<String, String>();
				map.put("error", message);
				return map;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
		
	}
	
	public List<LoanBook> getLoanBooks(String readerNo){
		try {
			Document doc = Jsoup.connect(Constant.BASE_URL+Constant.LOAN_BOOK_URL).timeout(1000*60)
			.data("readerno",readerNo).parser(Parser.xmlParser()).get();
			Elements elments = doc.getElementsByTag("meta");
			List<LoanBook> list = new ArrayList<LoanBook>();
			for(Element el : elments){
				LoanBook book = new LoanBook();
				book.setRecordNo(getXmlTagValue(el,"recordno"));
				book.setBarCode(getXmlTagValue(el,"barcode"));
				book.setCanRenew(Integer.valueOf(getXmlTagValue(el,"canrenew")));
				book.setRenew(Integer.valueOf(getXmlTagValue(el,"renew")));
				book.setTitle(getXmlTagValue(el,"title"));
				book.setCallNo(getXmlTagValue(el,"callno"));
				book.setVolumeNo(getXmlTagValue(el,"volumeno"));
				book.setLoanDate(getXmlTagValue(el,"loandate"));
				book.setReturnDate(getXmlTagValue(el,"returndate"));
				book.setLocal(getXmlTagValue(el,"local"));
				book.setCirType(getXmlTagValue(el,"cirtype"));
				book.setPrice(getXmlTagValue(el,"price"));
				book.setServiceType(getXmlTagValue(el,"servicetype"));
				book.setISBN(getXmlTagValue(el,"ISBN"));
				list.add(book);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
	}
	
	private String getXmlTagValue(Element element,String tagName){
		return element.getElementsByTag(tagName).get(0).html();
	}
	
	public Map<String,String> renewBooks(String barCode,String userName,String password){
		try {
			//先登录获取session等cookie
			Response response = this.getLoginResponse(userName, password);
			
			String url = Constant.BASE_URL+Constant.RENEW_BOOK_URL+"?1=1";
			for(String code : barCode.split(",")){
				url += "&v_select="+code;
			}
			Connection con = Jsoup.connect(url);
			for(Entry<String, String> entry :response.cookies().entrySet()){
				con.cookie(entry.getKey(), entry.getValue());
			}
			con.method(Method.POST);
			Response res = con.execute();
			String txt = res.body();
			Map<String,String> result = new HashMap<String, String>();
			if(txt.indexOf("续借成功")>-1){
				result.put("status", "success");
				result.put("msg", "续借成功");
			}else{
				result.put("status", "error");
				result.put("msg", txt);
			}
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
	}
	
	public List<ReverseInfo> getReserveBooks(String readerNo){
		try {
			Document doc = Jsoup.connect(Constant.BASE_URL+Constant.RESERVE_BOOK_URL)
					.data("readerno",readerNo,"finish","false","curpage","1","title","")
					.parser(Parser.xmlParser()).get();
			
			Elements elments = doc.getElementsByTag("record");
			List<ReverseInfo> list = new ArrayList<ReverseInfo>();
			for(Element el : elments){
				ReverseInfo info = new ReverseInfo();
				info.setRegDate(getXmlTagValue(el,"regdate"));
				info.setTitle(getXmlTagValue(el,"title"));
				info.setLoanAddr(getXmlTagValue(el,"loanaddr"));
				info.setStatus(getXmlTagValue(el,"status"));
				info.setBarCode(getXmlTagValue(el,"barcode"));
				info.setRecordId(getXmlTagValue(el,"recordid"));
				info.setEndDate(getXmlTagValue(el,"enddate"));
				info.setDeadline(getXmlTagValue(el,"deadline"));
				info.setMetaTable(getXmlTagValue(el,"metatable"));
				info.setMetaId(getXmlTagValue(el,"metaid"));
				list.add(info);
			}
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
		
	}
	
	public Object getReserveBooksFinished(String readerNo,String title,String page){
		try {
			Document doc = Jsoup.connect(Constant.BASE_URL+Constant.RESERVE_BOOK_URL).timeout(1000*60)
							.data("readerno",readerNo,"finish","true","title",title,"curpage",page)
							.parser(Parser.xmlParser()).get();
		} catch (Exception e) {
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
		//TODO 解析xml
		return "";
	}
	
	public void reserveBooks(){
		
	}
	
	public List<News> getNotice(int page){
		return getNews("22", page);
	}
	
	public List<News> getForum(int page){
		return getNews("23", page);
	}
	
	/**
	 * 查询新闻列表
	 * @param cid
	 * @param page
	 * @return
	 * @throws IOException
	 */
	private List<News> getNews(String cid,int page){
		if(page == 0){
			page = 1;
		}
		try {
			Document doc = Jsoup.connect(Constant.BASE_URL+Constant.NEWS_URL).timeout(1000*60)
			.data("cid",cid,"pager.offset",String.valueOf((page-1)*10)).get();
			Elements rows = doc.select(".newstab tbody tr");
			List<News> list = new ArrayList<News>();
			for(Element row : rows){
				Element a = row.select("a").get(0);
				String id = a.attr("href").split("=")[1];
				String title = a.text();
				String date = row.select("td").get(0).text();
				News news = new News(id,title,date);
				list.add(news);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
	}
	
	public Map<String,String> getNewsDatail(String newsId){
		try {
			Map<String, String> map = new HashMap<String, String>();
			Document doc = Jsoup.connect(Constant.BASE_URL+Constant.NEWS_DETAIL_URL).data("itemid",newsId).timeout(1000*60).get();
			String content = doc.select(".edittext").get(0).html();
			String title = doc.select(".detailtitle h3").text();
			map.put("result", content);
			map.put("title", title);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
	}
	
	public List<Area> getSelflibList(){
		try {
			Document doc = Jsoup.connect(Constant.BASE_URL+Constant.SELF_LIB_URL).timeout(1000*60).get();
			Elements tables = doc.select("#showdiv3_5 table");
			List<Area> areas = new ArrayList<Area>();
			for(Element table : tables){
				Area area = new Area();
				String areaName = table.select("thead td").text();
				area.setAreaName(areaName);
				List<SelfLib> libs = new ArrayList<SelfLib>();
				for(Element lib : table.select("tbody td a")){
					String no = lib.attr("href").split("=")[1];
					String adress = lib.text().split("]")[1];
					SelfLib selfLib = new SelfLib(no, adress);
					libs.add(selfLib);
				}
				area.setSelflibs(libs);
				areas.add(area);
			}
			return areas;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
	}
	
	public List<Book> queryBooks(String searchType,String bookType,String key,String page){
		String pageNum = "20";
		
		//v_tablearray  文献类型.　 bibliosm　图书 | serbibm 期刊 | apabibm 电子书 | mmbibm 音像资料
		//v_index  检索途径 .  all 任意词 | title 书名 | author 作者 | subject 关键字 | classno 分类号 | isbn | callno 索书号 | publisher 出版社
		//v_value  search key 
		//cirtype 图书类型 . cirtype_l 可外借图书 |  cirtype_r 可阅览图书  | local_reserve 可预借图书
		//sortfield 排序字段. score 匹配度，其他暂不考虑
		//sorttype  desc
		Map<String, String> map = new HashMap<String, String>();
		map.put("v_tablearray","bibliosm");
		map.put("sortfield","score");
		map.put("sorttype","desc");
		map.put("v_index",searchType);
		map.put("v_value",key);
		map.put("cirtype",bookType);
		map.put("pageNum", pageNum);
		map.put("v_page", page);
		try {
			Document doc = Jsoup.connect(Constant.BASE_URL+Constant.QUERY_BOOK_URL).data(map).timeout(1000*60).post();
			Elements  list = doc.select(".booklist li");
			List<Book> books = new ArrayList<Book>();
			List<String> recordNoArr = new ArrayList<String>();
			List<String> tableArr = new ArrayList<String>();
			for(Element bookEl : list){
				String title = bookEl.select("h3.title").text();
				String desc = bookEl.select("div.text").text();
				String[] authorArr = bookEl.select(".info .author").text().split("：");
				String author = authorArr.length>1?authorArr[1]:"";
				String[] publisherArr = bookEl.select(".info .publisher").text().split("：");
				String publisher = publisherArr.length>1?publisherArr[1]:"";
				String[] publishDateArr = bookEl.select(".info .dates").text().split("：");
				String publishDate = publishDateArr.length>1?publishDateArr[1]:"";
				String recordNo = bookEl.select("a.see").attr("name").split(",")[1];
				Book book = new Book(recordNo, title, author, publisher, publishDate, desc, false);
				books.add(book);
				recordNoArr.add(recordNo);
				tableArr.add("bibliosm");
			}
		
			//获取书籍是否可以预借
			if(list.size()>0){
				Document canReverseDoc = Jsoup.connect(Constant.BASE_URL+Constant.CAN_REVERSE_URL).timeout(1000*60)
				.data("tableList",StringUtil.join(tableArr, ","),"metaidList",StringUtil.join(recordNoArr, ","))
				.parser(Parser.xmlParser()).get();
			
				for(Element el : canReverseDoc.select("preloan")){
					int index = Integer.valueOf(el.attr("no"));
					books.get(index).setCanReverse(Boolean.valueOf(el.text()));
				}
			}
			
			return books;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
	}

	@Override
	public Book getBook(String recordNo) {
		Set<String> adress = new HashSet<String>();
		Book book = new Book();
		try {
			//从深图网站获取书籍isbn  馆藏地址
			Document doc = Jsoup.connect(Constant.BASE_URL+Constant.BOOK_URL).timeout(1000*60)
			.data("v_tablearray","bibliosm","v_recno",recordNo,"v_curtable","bibliosm","site","null")
			.get();
			String title = doc.select(".booksinfo h3.title a").text();
			Elements info = doc.select(".righttop ul");
			String[] authorArr = info.select("li:eq(0)").text().split("：");
			String author = authorArr.length>1?authorArr[1]:"";
			String price = info.select("li:eq(1)").text().split("：")[1];
			String pub = info.select("li:eq(2)").text().split("：")[1];
			String publisher = pub.split(",")[0];
			String publishDate = pub.split(",")[1];
			String[] isbnArr = info.select("li:eq(4)").text().split("：");
			String isbn = isbnArr.length>1?isbnArr[1]:"";
			
			Elements tables =  doc.select(".infoshow table.table_1");
			for(Element table : tables){
				if(table.previousElementSibling().text().equals("可外借馆藏")){
					for(Element tr : table.select("tbody tr")){
						String text = tr.select("td:eq(2)").text();//馆藏地址
						//String barcode = tr.select("td:eq(0)").text();//条码号
						adress.add(text);
					}
				}
			}
			
			if(doc.html().indexOf("document.getElementById('yujie').style.display='none'")>0){
				book.setCanReverse(false);//是否可预借
			}else{
				book.setCanReverse(true);//是否可预借
			}
			
			
			book.setTitle(title);
			book.setRecordNo(recordNo);
			book.setIsbn(isbn);
			book.setCollectAddress(adress);
			book.setAuthor(author);
			book.setPrice(price);
			book.setPublisher(publisher);
			book.setPublishDate(publishDate);
			
			//从豆瓣获取书籍的评分等基本信息
			try {
				RestTemplate restTemplate = new RestTemplate();
				HashMap obj = restTemplate.getForObject("https://api.douban.com/v2/book/isbn/"+isbn, HashMap.class);
				if(obj.get("msg") == null){
					HashMap<String, Object> rate = (HashMap<String, Object>)obj.get("rating");
					book.setAverage((String)rate.get("average"));//评分
					book.setNumRaters((Integer)rate.get("numRaters"));//评论人数
					book.setDoubanId((String)obj.get("id"));//豆瓣id
					book.setImage((String)obj.get("image"));//封面
					book.setPage((String)obj.get("pages"));//页码
				}
			} catch (Exception e) {
				// 豆瓣查询不到
				book.setAverage("0.0");//评分
				book.setNumRaters(0);//评论人数
				book.setDoubanId("");//豆瓣id
				book.setImage("");//封面
				book.setPage("");//页码
			}
			return book;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
		
	}
	
	@Override
	public List<Area> getChildlibList() {
		List<Area> list = new ArrayList<Area>();
		Area area = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/ChildLib.txt"),"UTF-8"));
			String data = br.readLine();
			while(data != null){
				if(data.startsWith("#")){
					if(area != null){
						list.add(area);
					}
					area = new Area();
					area.setAreaName(data.replaceAll("#", ""));
					area.setChildlibs(new ArrayList<ChildLib>());
				}else{
					String[] arr = data.split("\t");
					String tel = arr.length>2?arr[2]:"";
					//String tel = arr[2]
					ChildLib lib = new ChildLib(arr[0],arr[1],tel);
					area.getChildlibs().add(lib);
				}
				data = br.readLine();	
			}
			list.add(area);
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public List<Map<String, String>> getNewBookList(String date) {
		//date 201309
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		try {
			Document doc = Jsoup.connect(Constant.BASE_URL+Constant.NEW_BOOK_LIST).timeout(1000*60)
			.data("zhuanlan","BBK","time",date).get();
			Elements books = doc.select("table.bookbox");
			books.remove(books.size()-1);
			for(Element book : books){
				Map<String, String> map = new HashMap<String, String>();
				String title = book.select("tr td").eq(0).text().split("\\|")[0];
				if(title.length()>10){
					title = title.substring(0, 10)+"...";
				}
				String recordNo = getRecordNo(book.select("tr td a").attr("href"));
				map.put("title", title);
				map.put("recordNo", recordNo);
				list.add(map);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
		return list;
	}
	
	@Override
	public List<Map<String, String>> getLibBookList(String libno) {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		try {
			Document doc = Jsoup.connect(Constant.BASE_URL+Constant.LIB_BOOK_LIST).timeout(1000*60)
			.data("v_ServiceAddr",libno).get();
			Elements books = doc.getElementById("f1").select("li");
			for(Element book : books){
				String title = book.text();
				String recordNo = getRecordNo(book.select("a").attr("href"));
				Map<String, String> map = new HashMap<String, String>();
				map.put("title", title);
				map.put("recordNo", recordNo);
				list.add(map);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
		return list;
	}
	
	/**
	 * 从链接中提取记录ID
	 * @param href
	 * @return
	 */
	private String getRecordNo(String href){
		return href.split("&")[1].split("=")[1];
	}
	
	/**
	 * <feed xmlns="http://www.w3.org/2005/Atom" xmlns:db="http://www.douban.com/xmlns/" xmlns:gd="http://schemas.google.com/g/2005" xmlns:openSearch="http://a9.com/-/spec/opensearchrss/1.0/" xmlns:opensearch="http://a9.com/-/spec/opensearchrss/1.0/">
			<title>不诚实的诚实真相 的评论</title>
			<link href="http://book.douban.com/subject/20476804/reviews" rel="alternate"/>
			<opensearch:startIndex>1</opensearch:startIndex>
			<opensearch:totalResults>19</opensearch:totalResults>
			<entry>
				<id>http://api.douban.com/review/5733342</id>
				<title>真相太残酷！我们都在互相欺骗中过完这一生！</title>
				<author>
					<link href="http://api.douban.com/people/1253313" rel="self"/>
					<link href="http://www.douban.com/people/regulusun/" rel="alternate"/>
					<link href="http://img3.douban.com/icon/u1253313-20.jpg" rel="icon"/>
					<name>熊猫哥</name>
					<uri>http://api.douban.com/people/1253313</uri>
				</author>
				<published>2013-01-09T18:44:35+08:00</published>
				<updated>2013-08-06T10:05:31+08:00</updated>
				<link href="http://api.douban.com/review/5733342" rel="self"/>
				<link href="http://www.douban.com/review/5733342/" rel="alternate"/>
				<link href="http://api.douban.com/book/subject/20476804" rel="http://www.douban.com/2007#subject"/>
				<summary>
				丹·艾瑞里又出新书了，这本《不诚实的诚实真相》中文版还是由中信引进。心理学和行为经济学相关领域，近几年热度不减，正如90年代末开始经济学成为显学...
				</summary>
				<db:comments value="2"/>
				<db:useless value="2"/>
				<db:votes value="14"/>
				<gd:rating max="5" min="1" value="4"/>
			</entry>
		</feed>
	 */
	@Override
	public List<BookReview> getBookReview(String doubanId, int page) {
		String pageNum = "20";
		String start = String.valueOf((page-1)*20+1);
		List<BookReview> list = new ArrayList<BookReview>();
		try {
			Document doc = Jsoup.connect("http://api.douban.com/book/subject/"+doubanId+"/reviews").timeout(1000*60)
				.data("start-index",start,"max-results",pageNum).parser(Parser.xmlParser()).get();
			
			//String total = doc.getElementsByTag("opensearch:totalResults").text();
			for(Element entry : doc.select("entry")){
				String title = entry.select("title").text();
				String user = entry.select("author name").text();
				String userIcon = entry.select("author link[rel=icon]").attr("href");
				String publishDate = entry.select("published").text();
				publishDate = publishDate.split("\\+")[0].replace("T", " ");
				String summary = entry.select("summary").text();
				String url = entry.select(">link[rel=alternate]").attr("href");
				BookReview review = new BookReview(title,user,userIcon, publishDate, summary, url);
				list.add(review);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
		return list;
	}
	
	private Response getLoginResponse(String username,String pwd) throws Exception{
		Connection conLogin = Jsoup.connect(Constant.BASE_URL+Constant.LOGIN_URL).data("username",username,"password",pwd).timeout(1000*60);
		conLogin.method(Method.GET);
		Response response = conLogin.execute();
		return response;
	}
	
	@Override
	public Map<String, String> changeLoginName(String username,String pwd,String loginName) {
		try {
			Response response = this.getLoginResponse(username, pwd);
			Connection con = Jsoup.connect(Constant.BASE_URL+Constant.CHANGE_LOGIN_NAME).timeout(1000*60)
			.data("loginName",loginName);
			for(Entry<String, String> entry :response.cookies().entrySet()){
				con.cookie(entry.getKey(), entry.getValue());
			}
			Document doc = con.post();
			String msg = doc.select("div.memberbox_1 >*:last-child").text();
			Map<String, String> map = new HashMap<String, String>();
			if(msg.indexOf("修改失败")>-1){
				map.put("failure", msg);
			}else{
				map.put("success", msg);
			}
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
	}
	
	@Override
	public Map<String, String> changePwd(String username,String pwd, String newPwd) {
		try {
			Response response = this.getLoginResponse(username, pwd);
			Connection con = Jsoup.connect(Constant.BASE_URL+Constant.CHANGE_PWD).timeout(1000*60)
			.data("v_NewPwd1",newPwd,"v_OldPwd",pwd).parser(Parser.xmlParser());
			
			for(Entry<String, String> entry :response.cookies().entrySet()){
				con.cookie(entry.getKey(), entry.getValue());
			}
			Document doc = con.post();
			String msg = doc.select("message").text();
			Map<String, String> map = new HashMap<String, String>();
			if(msg.indexOf("success")>-1){
				map.put("success", "密码修改成功");
			}else{
				map.put("failure", msg);
			}
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
