package com.example.richtext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.markdom.handler.html.DefaultHtmlDelegate;
import io.markdom.util.Attribute;
import io.markdom.util.Element;
import io.markdom.util.Nodes;
import io.markdom.util.Text;

public class AbbreviationsHtmlDelegate extends DefaultHtmlDelegate {

	private Map<String, String> abbreviations = new HashMap<>();

	public AbbreviationsHtmlDelegate(Map<String, String> abbreviations) {
		Objects.requireNonNull(abbreviations).forEach((key, value) -> this.abbreviations.put(key.toLowerCase(), value));
	}

	@Override
	public Nodes onTextContent(String text) {
		Nodes nodes = new Nodes();
		onTextContent(nodes, text);
		return nodes;
	}

	private void onTextContent(Nodes nodes, String text) {

		StringBuilder sentanceBilder = new StringBuilder();
		StringBuilder wordBuilder = new StringBuilder();

		for (Character c : text.toCharArray()) {
			int type = Character.getType(c);
			if (Character.isWhitespace(c) || Character.DIRECTIONALITY_WHITESPACE == type || Character.DASH_PUNCTUATION == type) {
				addWordToSentanceOrAsAbbreviation(nodes, sentanceBilder, wordBuilder);
				sentanceBilder.append(c);
			} else {
				wordBuilder.append(c);
			}
		}

		addWordToSentanceOrAsAbbreviation(nodes, sentanceBilder, wordBuilder);
		addRestOfSentance(nodes, sentanceBilder);

	}

	private void addWordToSentanceOrAsAbbreviation(Nodes nodes, StringBuilder sentanceBilder, StringBuilder wordBuilder) {
		if (0 != wordBuilder.length()) {
			String word = wordBuilder.toString();
			String title = abbreviations.get(word.toLowerCase());
			if (null != title) {
				nodes.add(new Text(sentanceBilder.toString()));
				nodes.add(new Element("abbr").add(new Attribute("title", title)).add(new Text(word)));
				sentanceBilder.setLength(0);
			} else {
				sentanceBilder.append(word);
			}
			wordBuilder.setLength(0);
		}
	}

	private void addRestOfSentance(Nodes nodes, StringBuilder sentanceBilder) {
		if (0 != sentanceBilder.length()) {
			nodes.add(new Text(sentanceBilder.toString()));
		}
	}

}
