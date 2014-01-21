package com.shixy.web.hnyc.service;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.shixy.web.hnyc.response.Cigar;
import com.shixy.web.hnyc.response.News;

@Path("/hnzy")
@Produces("application/json;charset=UTF-8")
public interface HnzyService {

	/**
	 * 获取首页图片
	 * 
	 */
	@GET
	@Path("/index_images")
	public List<String> getImages();

	/**
	 * 媒体聚焦
	 * 
	 */
	@GET
	@Path("/mtjj")
	public List<News> getMtjj();

	/**
	 * 企业动态
	 * 
	 */
	@GET
	@Path("/qydt")
	public List<News> getQydt();

	/**
	 * 企业公告
	 * 
	 */
	@GET
	@Path("/qygg")
	public List<News> getQygg();

	/**
	 * 企业文化
	 * 
	 */
	@GET
	@Path("/qywh")
	public Map<String, Object> getQywh();

	/**
	 * 企业简介
	 * 
	 */
	@GET
	@Path("/qyjj")
	public Map<String, Object> getQyjj();

	/**
	 * 获取烟列表
	 * 
	 */
	@GET
	@Path("/products")
	public List<Cigar> getProducts(@QueryParam("type") String type);

	/**
	 * 获取香烟详情数据
	 * 
	 */
	@GET
	@Path("/product/{aid}")
	public String getProduct(@PathParam("aid") String aid);

	/**
	 * 新闻详情
	 * 
	 */
	@GET
	@Path("/detail")
	public Map<String, String> getDetail(@QueryParam("url") String url);

}
