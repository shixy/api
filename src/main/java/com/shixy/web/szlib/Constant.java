package com.shixy.web.szlib;

public class Constant {
	
	public static final String BASE_URL = "http://www.szlib.gov.cn/";
	/**
	 * 登录url
	 */
	public static final String LOGIN_URL = "MyLibrary/readerLogin.jsp";
	/**
	 * 挂失
	 */
	public static final String LOST_URL = "MyLibrary/ReportLostN.jsp";
	
	/**
	 * 借阅书籍记录url
	 */
	public static final String LOAN_BOOK_URL = "MyLibrary/getloanlist.jsp";
	/**
	 * 续借书籍url
	 */
	public static final String RENEW_BOOK_URL = "MyLibrary/response.jsp";
	
	/**
	 * 预借记录URL
	 */
	public static final String RESERVE_BOOK_URL = "MyLibrary/ReserveHistory.jsp";
	
	/**
	 * 新闻链接URL
	 */
	public static final String NEWS_URL = "news.jsp";
	
	/**
	 * 新闻明细URL
	 */
	public static final String NEWS_DETAIL_URL = "newsshow.jsp";
	
	/**
	 * 24小时自助馆URL
	 */
	public static final String SELF_LIB_URL = "guide_selfhelp.jsp";
	
	/**
	 * 书籍查询URL
	 */
	public static final String QUERY_BOOK_URL = "Search/searchshow.jsp";
	
	/**
	 * 书籍明细URL
	 * http://www.szlib.gov.cn/Search/searchdetail.jsp?v_tablearray=bibliosm&v_recno=2193585&v_curtable=bibliosm&site=null
	 */
	public static final String BOOK_URL = "Search/searchdetail.jsp";
	
	/**
	 * 查询书籍是否可以预借
	 */
	public static final String CAN_REVERSE_URL = "Search/getpreLoan.jsp";
	
	/**
	 * 新书列表
	 */
	public static final String NEW_BOOK_LIST = "Topic/newbooklist.jsp";
	
	/**
	 * 自助图书馆图书列表
	 */
	public static final String LIB_BOOK_LIST = "Search/DispSelfLibBook.jsp";
	
	/**
	 * 修改密码
	 */
	public static final String CHANGE_PWD = "MyLibrary/responseForCodeM.jsp";
	/**
	 * 修改登录名
	 */
	public static final String CHANGE_LOGIN_NAME = "MyLibrary/LoginNameChange.jsp";
	
}
