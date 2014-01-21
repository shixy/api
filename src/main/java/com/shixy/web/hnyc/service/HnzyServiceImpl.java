package com.shixy.web.hnyc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.shixy.web.WsException;
import com.shixy.web.hnyc.HnzyConstants;
import com.shixy.web.hnyc.response.Cigar;
import com.shixy.web.hnyc.response.News;
import com.shixy.web.utils.HttpUtils;
import com.shixy.web.utils.StringUtils;

@Service("hnzyService")
public class HnzyServiceImpl implements HnzyService {
	private static final String ERR_MSG = "连接服务器失败";

	@Override
	public List<String> getImages() {
		List<String> result = new ArrayList<String>();
		try {
			Document doc = Jsoup.connect(HnzyConstants.BASE_URL).timeout(1000 * 60).get();
			String html = doc.html();
			Pattern p = Pattern.compile("(?<=<imgurl>)[^<]*");
			Matcher matcher = p.matcher(html);
			while (matcher.find()) {
				result.add(HnzyConstants.BASE_URL + matcher.group());
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
		return result;
	}

	@Override
	public List<News> getMtjj() {
		return getList(HnzyConstants.MTJJ);
	}

	@Override
	public List<News> getQydt() {
		return getList(HnzyConstants.QYDT);
	}

	@Override
	public List<News> getQygg() {
		return getList(HnzyConstants.QYGG);
	}

	@Override
	public Map<String, Object> getQywh() {
		Map<String, Object> result = new HashMap<String, Object>();
		List<News> storyList = new ArrayList<News>();
		try {
			Document doc = Jsoup.connect(HnzyConstants.QYWH).timeout(1000 * 60).get();
			Element whEl = doc.select(".container_right>div:eq(3)").first();
			whEl.select("p").remove();
			Elements listEl = doc.select(".container_right>table").first().select("table:eq(1) td a");
			for (int i = 0; i < listEl.size(); i++) {
				Element el = listEl.get(i);
				News news = new News();
				news.setTitle(el.text());
				news.setUrl(el.attr("href"));
				storyList.add(news);
			}
			result.put("whtx", whEl.html());
			result.put("story", storyList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
		return result;
	}

	@Override
	public Map<String, Object> getQyjj() {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			Document doc = Jsoup.connect(HnzyConstants.QYJJ).timeout(1000 * 60).get();
			Element content = doc.select(".container_content").first();
			result.put("result", content.text());
		} catch (Exception e) {
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
		return result;
	}

	@Override
	public Map<String, String> getDetail(String url) {
		Map<String, String> result = new HashMap<String, String>();
		String html;
		try {
			Document doc = Jsoup.connect(HnzyConstants.BASE_URL + url).timeout(1000 * 60).get();
			Element con = doc.getElementsByClass("container_content").first();
			Elements scripts = con.select("script");
			if (scripts != null) {
				scripts.remove();
			}
			Elements imgs = con.select("img");
			if (imgs != null) {
				for (int i = 0; i < imgs.size(); i++) {
					Element img = imgs.get(i);
					String src = img.attr("src");
					// img.attr("src", HnzyConstants.BASE_URL + src);
					img.attr("_src", HnzyConstants.BASE_URL + src);
				}
			}
			Element content = con.getElementById("wen_content");
			if (content != null) {
				html = content.html();
			} else {
				html = con.html();
			}
			result.put("result", html);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
		return result;
	}

	private List<News> getList(String url) {
		List<News> result = new ArrayList<News>();
		try {
			Document doc = Jsoup.connect(url).timeout(1000 * 60).get();
			Elements listEl = doc.select(".nlist .n-title");
			for (int i = 0; i < listEl.size(); i++) {
				Element el = listEl.get(i);
				Element ea = el.select("a").first();
				Element ed = el.select("span").first();
				News news = new News();
				news.setTitle(ea.text());
				news.setPublishDate(ed.text());
				news.setUrl(ea.attr("href"));
				result.add(news);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
		return result;

	}

	public List<Cigar> getProducts(String type) {
		String url = StringUtils.isEquals(type, "baisha") ? HnzyConstants.BS : HnzyConstants.FRW;
		List<Cigar> result = new ArrayList<Cigar>();
		try {
			Document doc = Jsoup.connect(url).timeout(1000 * 60).get();
			Elements listEl = doc.select(".pslideshow li a");
			for (int i = 0; i < listEl.size(); i++) {
				Element el = listEl.get(i);
				String title = el.attr("title");
				String aid = el.attr("aid");
				String image = el.select("img").first().attr("mark");
				Cigar cigar = new Cigar();
				cigar.setName(title);
				cigar.setAid(aid);
				image = HnzyConstants.BASE_URL + image;
				cigar.setImage(image);
				result.add(cigar);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
		return result;
	}

	@Override
	public String getProduct(String aid) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("m", "cms");
		param.put("q", "list");
		param.put("type", "ajaxp");
		param.put("aid", aid);
		return HttpUtils.httpPostString(HnzyConstants.BASE_URL, param);
	}
}
