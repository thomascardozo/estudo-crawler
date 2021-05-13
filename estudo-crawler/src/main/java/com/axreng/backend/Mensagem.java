package com.axreng.backend;

public class Mensagem {
	
	private String id;
	private Integer status;
	private String  message;
	private String statusSucces;
	private String[] urls;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatusSucces() {
		return statusSucces;
	}
	public void setStatusSucces(String statusSucces) {
		this.statusSucces = statusSucces;
	}
	public String[] getUrls() {
		return urls;
	}
	public void setUrls(String[] urls) {
		this.urls = urls;
	}
}
