<?xml version="1.0" encoding="UTF-8"?>
<%@page import="otula.utils.XMLFormatter"%>
<%@page import="otula.utils.XMLUtils"%>
<%@page import="otula.summarizerweb.SummarizerCore"%>
<%@ page language="java" contentType="application/xml; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	out.print(new XMLFormatter().toString(SummarizerCore.addTask(XMLUtils.toXMLDocument(request.getInputStream()))));
%>