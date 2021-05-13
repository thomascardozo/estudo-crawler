package com.axreng.backend;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;

public class ListLinks {

	
	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}	

	private static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1) + ".";
		else
			return s;
	}
	
	
	public JsonArray getListLinks(String url, String termoPesquisa) throws IOException {
		
		JsonArray retorno = new JsonArray();
		
		Document doc = Jsoup.connect(url).get();

		Elements elementosTag = doc.select(termoPesquisa);
		
		Elements links = doc.select("a[href]");
			
		System.out.println("Elementos com a tag: " + termoPesquisa);
		
		for(Element el: elementosTag) {
			System.out.println(el.text());
			retorno.add(el.text());
		}
		for (Element link : links) {
			print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
			if (link.attr("abs:href").startsWith(url))
				retorno.add(link.attr("abs:href").trim());
		}
		
		return retorno;
	}
}
