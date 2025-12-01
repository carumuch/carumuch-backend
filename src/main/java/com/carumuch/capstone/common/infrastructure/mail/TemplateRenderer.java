package com.carumuch.capstone.common.infrastructure.mail;

import java.util.Map;

public interface TemplateRenderer {
	String render(String templateName, Map<String, Object> model);
}
