package com.axreng.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static spark.Spark.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JPopupMenu.Separator;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static String idGerado = null; 
    private static String palavraProcurada = null;

    public static void main(String[] args) { 
    	
    	List <String> tknValidos = new ArrayList<String>();
    	
        get("/crawl/:id", (req, res) -> {
        	
        	res.type("application/json");
                	
        	if(req.params("id").equalsIgnoreCase(idGerado) || tknValidos.contains(req.params("id"))) {
        		
        		Boolean existe = tknValidos.contains(idGerado);
        		
        		if(existe == false)
        			tknValidos.add(idGerado);
        		
        		ListLinks ll = new ListLinks();
        		JsonObject o = new JsonObject();
        		o.addProperty("id", req.params("id"));
        		o.addProperty("status", "active");
        		
        		JsonArray respostaFinal = ll.getListLinks("http://hiring.axreng.com/", palavraProcurada);
        		
        		o.add("urls", respostaFinal);
        		
        		return o;
        	} else {
        		res.status(404);
        		Mensagem m = new Mensagem();
    			m.setStatus(404);
    			m.setMessage("crawl not found: " + req.params("id"));
    			Gson g = new Gson();
    			String jObj = g.toJson(m, Mensagem.class);
    			return jObj;        		 
        	}       		
        	
        });
        
		post("/crawl", (req, res) -> {

			res.type("application/json");

			JsonObject json = new JsonObject();

			JsonObject j2 = null;

			if (req.body() != null && !req.body().trim().equalsIgnoreCase("")) {

				if (JsonParser.parseString(req.body()).isJsonObject()) {
					j2 = (JsonObject) JsonParser.parseString(req.body());
				} else {
					res.status(400);
					Mensagem m = new Mensagem();
					m.setStatus(400);
					m.setMessage("request body must be valid json object");
					Gson g = new Gson();
					String jObj = g.toJson(m, Mensagem.class);
					return jObj;
				}
			}

			IdGenerator idGen = new IdGenerator();
			idGerado = idGen.generate();
			json.addProperty("id", idGerado);

			JsonElement palavra = null;

				if(j2 != null)
					palavra = j2.get("keyword");
				
				else {
					res.status(400);
					Mensagem m = new Mensagem();
					m.setStatus(400);
					m.setMessage("request body is required");
					Gson g = new Gson();
					String jObj = g.toJson(m, Mensagem.class);
					return jObj;
				}

				if (palavra == null && !JsonParser.parseString(req.body()).isJsonObject()) {					
					
						res.status(400);
						Mensagem m = new Mensagem();
						m.setStatus(400);
						m.setMessage("request body is required");
						Gson g = new Gson();
						String jObj = g.toJson(m, Mensagem.class);
						return jObj;
					
				}
				
				if (palavra == null && JsonParser.parseString(req.body()).isJsonObject()) {					
					
					res.status(400);
					Mensagem m = new Mensagem();
					m.setStatus(400);
					m.setMessage("field 'keyword' is required (from 4 up to 32 chars)");
					Gson g = new Gson();
					String jObj = g.toJson(m, Mensagem.class);
					return jObj;
				
				}
				
				if(palavra.getAsString().trim().equalsIgnoreCase("")) {
					res.status(400);
					Mensagem m = new Mensagem();
					m.setStatus(400);
					m.setMessage("request body is required");
					Gson g = new Gson();
					String jObj = g.toJson(m, Mensagem.class);
					return jObj;
				}
				
				if (palavra.getAsString().length() < 4 || palavra.getAsString().length() > 32) {
					res.status(400);
					Mensagem m = new Mensagem();
					m.setStatus(400);
					m.setMessage("field 'keyword' is required (from 4 up to 32 chars)");
					Gson g = new Gson();
					String jObj = g.toJson(m, Mensagem.class);
					return jObj;
				}
				
				if (palavra.getAsString().trim().equalsIgnoreCase("") && JsonParser.parseString(req.body()).isJsonObject()) {
					res.status(400);
					Mensagem m = new Mensagem();
					m.setStatus(400);
					m.setMessage("field 'keyword' is required (from 4 up to 32 chars)");
					Gson g = new Gson();
					String jObj = g.toJson(m, Mensagem.class);
					return jObj;
				}
			
			if(palavra != null && !palavra.getAsString().trim().equalsIgnoreCase("")) {
				String p = palavra.getAsString();
				palavraProcurada = p;
				return json.getAsJsonObject();
			}
			else {
				res.status(400);
				Mensagem m = new Mensagem();
				m.setStatus(400);
				m.setMessage("request body is required");
				Gson g = new Gson();
				String jObj = g.toJson(m, Mensagem.class);
				return jObj;
			}
		});       		
        		
        LOGGER.info("HTTP API initialized");
    }

}
