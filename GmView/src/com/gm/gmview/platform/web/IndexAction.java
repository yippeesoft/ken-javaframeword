package com.gm.gmview.platform.web;

import com.gm.gmview.framework.web.BaseAction;
import com.gm.gmview.platform.PlatformManager;

public class IndexAction extends BaseAction {
	private String url;

	public String execute() throws Exception {
		this.request.put("name", PlatformManager.getManager().getProjectName());
		this.url = "/project/test/login.jsp";
		return SUCCESS;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}