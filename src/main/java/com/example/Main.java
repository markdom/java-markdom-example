package com.example;

import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.xhtmlrenderer.pdf.ITextRenderer;

import com.example.richtext.MarkdomRichtextProviderFactory;
import com.example.richtext.RichtextContext;
import com.example.richtext.RichtextProvider;
import com.example.richtext.RichtextProviderFactory;

public class Main {

	private static final Map<String, String> ABBREVIATIONS = new HashMap<>();

	static {
		ABBREVIATIONS.put("js", "JavaScript");
		ABBREVIATIONS.put("mm", "millimeter");
}

	public static void main(String[] args) throws Exception {

		RichtextProviderFactory factory = new MarkdomRichtextProviderFactory(RichtextContext.DETAILS);
		RichtextProvider provider = factory.fromCommonmark(new InputStreamReader(Main.class.getResourceAsStream("/test.md")));

		System.out.println(provider.toCommonmarkText());
		System.out.println();
		System.out.println();

		System.out.println(String.join("\n", provider.toWarnings()));
		System.out.println();
		System.out.println();

		System.out.println(provider.toHtmlElementsText(ABBREVIATIONS, true));
		System.out.println();
		System.out.println();

		ITextRenderer renderer = new ITextRenderer();
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		renderer.setDocument(provider.toXhtmlDocument(documentBuilder, "test.md"), null);
		renderer.layout();

		renderer.createPDF(new FileOutputStream("test.pdf"));

	}

}
