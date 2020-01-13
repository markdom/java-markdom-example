package com.example.richtext;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;

public interface RichtextProvider {

	public String toCommonmarkText();

	public List<String> toWarnings();

	public String toHtmlElementsText(boolean pretty);

	public Document toXhtmlDocument(DocumentBuilder documentBuilder, String title);

}
