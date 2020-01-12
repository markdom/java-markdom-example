package com.example.richtext;

import java.io.IOException;
import java.io.Reader;

public interface RichtextProviderFactory {

	public RichtextProvider fromCommonmark(Reader reader) throws IOException;

}
