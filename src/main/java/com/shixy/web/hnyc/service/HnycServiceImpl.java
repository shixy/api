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
import com.shixy.web.hnyc.Constants;
import com.shixy.web.hnyc.response.News;
import com.shixy.web.hnyc.response.NewsGroup;

@Service("hnycService")
public class HnycServiceImpl implements HnycService {

	private static final String ERR_MSG = "连接服务器失败";

	@Override
	public List<String> getImages() {
		List<String> result = new ArrayList<String>();
		try {
			Document doc = Jsoup.connect(Constants.BASE_URL).timeout(1000 * 60).get();
			String html = doc.html();
			Pattern p = Pattern.compile("(?<=pics\\s{0,100}\\+=\\s{0,100}\").{2,}(?=\")");
			Matcher matcher = p.matcher(html);
			while (matcher.find()) {
				result.add(Constants.BASE_URL + matcher.group());
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
		return result;
	}

	@Override
	public List<News> getShzr() {
		return getNewsList(Constants.SHZR);
	}

	@Override
	public List<News> getTzgg() {
		return getNewsList(Constants.TZGG);
	}

	@Override
	public List<News> getYcyw() {
		return getNewsList(Constants.YCYW);
	}

	private List<News> getNewsList(String url) {
		List<News> result = new ArrayList<News>();
		try {
			Document doc = Jsoup.connect(url).timeout(1000 * 60).get();
			Element con = doc.getElementById("www_zzjs_net");
			Elements els = con.getElementsByTag("li");
			int size = els.size();
			int maxSize = size > 50 ? 50 : size;
			for (int i = 0; i < maxSize; i++) {
				Element el = els.get(i);
				Element ea = el.select("a").first();
				Element ed = el.select("span.date").first();
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

	private List<NewsGroup> getNewsGroup(String url) {
		List<NewsGroup> result = new ArrayList<NewsGroup>();
		try {
			Document doc = Jsoup.connect(url).timeout(1000 * 60).get();
			Element con = doc.getElementById("centercontainer");
			Elements groupEls = con.select(".rightlb_left_t h2");
			Elements listElsContainer = con.select(".rightlb_left_con .rightlb_left_1");

			for (int i = 0; i < groupEls.size(); i++) {
				NewsGroup group = new NewsGroup();
				List<News> newsList = new ArrayList<News>();
				group.setGroupName(groupEls.get(i).text());
				Elements newsEls = listElsContainer.get(i).select("li a");

				for (int j = 0; j < newsEls.size(); j++) {
					Element el = newsEls.get(j);
					News news = new News();
					news.setTitle(el.text());
					news.setUrl(el.attr("href"));
					newsList.add(news);
				}
				group.setNews(newsList);
				result.add(group);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
		return result;
	}

	@Override
	public List<NewsGroup> getBszn() {
		return getNewsGroup(Constants.BSZN);
	}

	@Override
	public List<NewsGroup> getZcfg() {
		return getNewsGroup(Constants.ZCFG);
	}

	@Override
	public List<NewsGroup> getQywh() {
		return getNewsGroup(Constants.QYWH);
	}

	@Override
	public List<NewsGroup> getYcny() {
		return getNewsGroup(Constants.YCNY);
	}

	@Override
	public List<NewsGroup> getZmgl() {
		return getNewsGroup(Constants.ZMGL);
	}

	@Override
	public Map<String, String> getDetail(String url) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			Document doc = Jsoup.connect(Constants.BASE_URL + url).timeout(1000 * 60).get();
			Element con = doc.getElementsByClass("fen1_b").first();
			Elements imgs = con.select("img");
			if (imgs != null) {
				for (int i = 0; i < imgs.size(); i++) {
					Element img = imgs.get(i);
					String src = img.attr("src");
					// img.attr("src", Constants.BASE_URL + src);
					img.attr("_src", Constants.BASE_URL + src);
				}
			}

			result.put("result", con.html());
		} catch (Exception e) {
			e.printStackTrace();
			throw new WsException(ERR_MSG, e.getCause());
		}
		return result;
	}

}
