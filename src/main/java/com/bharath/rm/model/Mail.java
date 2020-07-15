package com.bharath.rm.model;

import java.util.Map;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 12, 2020 10:05:27 PM
 	* Class Description
*/
public class Mail {

    private String from;
    private String to;
    private String subject;
    private String content;
    private Map<String, Object> model; 
    private String template;

    public Mail(String from, String to, String subject) {
        this.from = from;
        this.to = to;
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
}