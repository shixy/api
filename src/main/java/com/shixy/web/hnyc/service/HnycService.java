package com.shixy.web.hnyc.service;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.shixy.web.hnyc.response.News;
import com.shixy.web.hnyc.response.NewsGroup;

@Path("/hnyc")
@Produces("application/json;charset=UTF-8")
public interface HnycService {

	/**
	 * 获取首页图片
	 * 
	 */
	@GET
	@Path("/index_images")
	public List<String> getImages();

	/**
	 * 社会责任
	 * 
	 */
	@GET
	@Path("/shzr")
	public List<News> getShzr();

	/**
	 * 通知公告
	 * 
	 */
	@GET
	@Path("/tzgg")
	public List<News> getTzgg();

	/**
	 * 烟草要闻
	 * 
	 */
	@GET
	@Path("/ycyw")
	public List<News> getYcyw();

	/**
	 * 办事指南
	 * 
	 */
	@GET
	@Path("/bszn")
	public List<NewsGroup> getBszn();

	/**
	 * 政策法规
	 * 
	 */
	@GET
	@Path("/zcfg")
	public List<NewsGroup> getZcfg();

	/**
	 * 企业文化
	 * 
	 */
	@GET
	@Path("/qywh")
	public List<NewsGroup> getQywh();

	/**
	 * 烟草农业
	 * 
	 */
	@GET
	@Path("/ycny")
	public List<NewsGroup> getYcny();

	/**
	 * 专卖管理
	 * 
	 */
	@GET
	@Path("/zmgl")
	public List<NewsGroup> getZmgl();

	/**
	 * 新闻详情
	 * 
	 */
	@GET
	@Path("/detail")
	public Map<String, String> getDetail(@QueryParam("url") String url);

}
