package com.example.richtext;

import java.util.List;

public interface RichtextProvider {

	public String toCommonmarkText();

	public List<String> toWarnings();

	public String toHtmlElementsText(boolean pretty);

}
