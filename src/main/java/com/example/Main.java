package com.example;

import java.io.IOException;
import java.io.InputStreamReader;

import com.example.richtext.MarkdomRichtextProviderFactory;
import com.example.richtext.RichtextContext;
import com.example.richtext.RichtextProvider;
import com.example.richtext.RichtextProviderFactory;

public class Main {

	public static void main(String[] args) throws IOException {

		RichtextProviderFactory factory = new MarkdomRichtextProviderFactory(RichtextContext.DETAILS);
		RichtextProvider provider = factory.fromCommonmark(new InputStreamReader(Main.class.getResourceAsStream("/test.md")));

		System.out.println(provider.toCommonmarkText());
		System.out.println();
		System.out.println();

		System.out.println(String.join("\n", provider.toWarnings()));
		System.out.println();
		System.out.println();

		System.out.println(provider.toHtmlElementsText(true));
		System.out.println();
		System.out.println();

	}

}
