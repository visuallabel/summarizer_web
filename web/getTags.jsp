<?xml version="1.0" encoding="UTF-8"?>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="otula.summarizerweb.debug.Definitions"%>
<%@page import="otula.summarizerweb.debug.DebugCore"%>
<%@page import="otula.utils.XMLFormatter"%>
<%@page import="otula.utils.XMLUtils"%>
<%@ page language="java" contentType="application/xml; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String maxTags = request.getParameter(Definitions.PARAMETER_MAX_TAGS);
	String maxTweets = request.getParameter(Definitions.PARAMETER_MAX_TWEETS);
	out.print(new XMLFormatter().toString(
			DebugCore.getTags(
					(maxTags == null ? -1 : Integer.parseInt(maxTags)),
					(maxTweets == null ? -1 : Integer.parseInt(maxTweets)),
					StringUtils.split(request.getParameter(Definitions.PARAMETER_SCREENNAME), otula.summarizerweb.Definitions.SEPARATOR_VALUES))
					)
			);
%>
