package com.example.richtext;

import java.io.IOException;
import java.io.Reader;
import java.util.Objects;
import java.util.function.Function;

import org.commonmark.node.Document;
import org.commonmark.parser.Parser;

import io.markdom.common.MarkdomNodeKind;
import io.markdom.handler.MarkdomDocumentMarkdomHandler;
import io.markdom.handler.commonmark.atlassian.AtlassianCommonmarkDocumentMarkdomDispatcher;
import io.markdom.handler.text.commonmark.CommonmarkTextConfiguration;
import io.markdom.model.basic.BasicMarkdomFactory;

public class MarkdomRichtextProviderFactory implements RichtextProviderFactory {

	public static final Function<MarkdomNodeKind, String> DEFAULT_UNDESIRED_NODE_KIND_MAPPER = kind -> {
		return "i18n.messages.richtext.undesiredNode." + kind;
	};

	private final MarkdomRichtextProviderRules markdomRules;

	private final CommonmarkTextConfiguration commonmarkTextConfiguration;

	private final Function<MarkdomNodeKind, String> undesiredNodeKindMapper;

	public MarkdomRichtextProviderFactory(MarkdomRichtextProviderRules markdomRules) {
		this(markdomRules, CommonmarkTextConfiguration.getDefault(), DEFAULT_UNDESIRED_NODE_KIND_MAPPER);
	}

	public MarkdomRichtextProviderFactory(MarkdomRichtextProviderRules markdomRules,
		CommonmarkTextConfiguration commonmarkTextConfiguration, Function<MarkdomNodeKind, String> undesiredNodeKindMapper) {
		this.markdomRules = Objects.requireNonNull(markdomRules);
		this.commonmarkTextConfiguration = Objects.requireNonNull(commonmarkTextConfiguration);
		this.undesiredNodeKindMapper = Objects.requireNonNull(undesiredNodeKindMapper);
	}

	@Override
	public RichtextProvider fromCommonmark(Reader reader) throws IOException {
		return new MarkdomRichtextProvider(
			new AtlassianCommonmarkDocumentMarkdomDispatcher(
				(Document) Parser.builder().build().parseReader(reader)
			).handle(
				new MarkdomDocumentMarkdomHandler(new BasicMarkdomFactory())
			),
			markdomRules,
			undesiredNodeKindMapper,
			commonmarkTextConfiguration
		);
	}

}
