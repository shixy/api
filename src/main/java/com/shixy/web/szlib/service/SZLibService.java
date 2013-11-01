package com.shixy.web.szlib.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.shixy.web.szlib.response.Area;
import com.shixy.web.szlib.response.Book;
import com.shixy.web.szlib.response.BookReview;
import com.shixy.web.szlib.response.LoanBook;
import com.shixy.web.szlib.response.News;
import com.shixy.web.szlib.response.ReverseInfo;

@Path("/szlib")
@Produces("application/json;charset=UTF-8")
public interface SZLibService {
	
	/**
	 * 登录图书馆
	 * @param username
	 * @param pwd
	 * @return
	 * @throws IOException
	 */
	@GET
	@Path("/login")
	public Object login(@QueryParam("username")String username,@QueryParam("password")String password) throws Exception;

	/**
	 * 查询已借阅图书
	 * @param readerNo
	 * @return
	 * @throws Exception 
	 */
	@GET
	@Path("/query/loanbooks")
	public List<LoanBook> getLoanBooks(@QueryParam("readerNo")String readerNo);
	
	/**
	 * 续借图书
	 * @param bookNo
	 * @throws IOException 
	 */
	@GET
	@Path("/renewbooks")
	public Map<String,String> renewBooks(@QueryParam("barcode")String barCode,@QueryParam("userName")String userName,@QueryParam("password")String password);
	
	/**
	 * 查询我当前的预借
	 * @throws IOException 
	 */
	@GET
	@Path("/query/reservebooks")
	public List<ReverseInfo> getReserveBooks(@QueryParam("readerNo")String readerNo);
	
	/**
	 * 查询我的历史借阅
	 * @return
	 * @throws IOException 
	 */
	@GET
	@Path("/query/renewbook/history")
	public Object getReserveBooksFinished(@QueryParam("readerNo")String readerNo,@QueryParam("title")String title,@QueryParam("page")String page);
	
	/**
	 * 预借图书
	 */
	@GET
	@Path("/reservebook")
	public void reserveBooks();
	
	/**
	 * 查询通知公告
	 * @throws IOException 
	 */
	@GET
	@Path("/query/notice")
	public List<News> getNotice(@QueryParam("page")int page);
	
	/**
	 * 查询展会讲座
	 * @param page
	 * @return
	 * @throws IOException
	 */
	@GET
	@Path("/query/forum")
	public List<News> getForum(@QueryParam("page")int page);
	
	/**
	 * 获取新闻明细内容
	 * @param newsId
	 * @return
	 * @throws IOException
	 */
	@GET
	@Path("/query/news/{id}")
	public Map<String, String> getNewsDatail(@PathParam("id")String newsId);
	/**
	 * 获取24小时自助图书馆列表
	 * @return
	 * @throws IOException 
	 */
	@GET
	@Path("/query/selflib")
	public List<Area> getSelflibList();
	/**
	 * 获取成员馆列表
	 * 数据来自http://www.szln.gov.cn/lib/library.do
	 * @return
	 * @throws IOException 
	 */
	@GET
	@Path("/query/childlib")
	public List<Area> getChildlibList();
	/**
	 * 查询图书
	 * @throws IOException 
	 */
	@GET
	@Path("/query/book")
	public List<Book> queryBooks(@QueryParam("searchType")String searchType,@QueryParam("bookType")String bookType,@QueryParam("key")String key,@QueryParam("page")String page);

	/**
	 * 获取书籍详细信息
	 * @param recordNo
	 * @return
	 */
	@GET
	@Path("/query/book/{no}")
	public Book getBook(@PathParam("no")String recordNo);
	
	/**
	 * 获取月度推荐图书
	 * @return
	 */
	@GET
	@Path("/query/newbook/{date}")
	public List<Map<String, String>> getNewBookList(@PathParam("date")String date);
	
	/**
	 * 获取自助图书馆图书列表
	 * @param libno
	 * @return
	 */
	@GET
	@Path("/query/lib/{libno}")
	public List<Map<String, String>> getLibBookList(@PathParam("libno")String libno);
	
	/**
	 * 获取豆瓣评论
	 * @param doubanId
	 * @param page
	 * @return
	 */
	@GET
	@Path("/book/{doubanId}/review/{page}")
	public List<BookReview> getBookReview(@PathParam("doubanId")String doubanId,@PathParam("page")int page);
	
	/**
	 * 修改密码
	 * @param username
	 * @param password
	 * @param newPwd
	 * @return
	 */
	@POST
	@Path("/user/changepwd")
	public Map<String, String> changePwd(@FormParam("userName")String username,@FormParam("password")String password,@FormParam("newPwd")String newPwd);
	/**
	 * 修改用户名
	 * @param username
	 * @param password
	 * @param loginName
	 * @return
	 */
	@POST
	@Path("/user/changeloginname")
	public Map<String, String> changeLoginName(@FormParam("userName")String username,@FormParam("password")String password,@FormParam("loginName")String loginName);
	
}
