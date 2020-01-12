package com.example.richtext;

import java.io.StringWriter;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import io.markdom.common.MarkdomNodeKind;
import io.markdom.handler.AuditingMarkdomHandler;
import io.markdom.handler.BlacklistNodeKindMarkdomFilterHandler;
import io.markdom.handler.ConsumingNodeKindMarkdomAuditHandler;
import io.markdom.handler.FilteringMarkdomHandler;
import io.markdom.handler.IdleMarkdomHandler;
import io.markdom.handler.NodeKindMarkdomAudit;
import io.markdom.handler.NodeKindMarkdomFilter;
import io.markdom.handler.html.jsoup.JsoupHtmlDocumentMarkdomHandler;
import io.markdom.handler.text.commonmark.CommonmarkTextConfiguration;
import io.markdom.handler.text.commonmark.CommonmarkTextMarkdomHandler;
import io.markdom.model.MarkdomDocument;

public class MarkdomRichtextProvider implements RichtextProvider {

	private final MarkdomDocument markdomDocument;

	private final MarkdomRichtextProviderRules markdomRules;

	private final CommonmarkTextConfiguration commonmarkTextConfiguration;

	private final Function<MarkdomNodeKind, String> undesiredNodeKindMapper;

	public MarkdomRichtextProvider(MarkdomDocument markdomDocument, MarkdomRichtextProviderRules markdomRules,
		CommonmarkTextConfiguration commonmarkTextConfiguration, Function<MarkdomNodeKind, String> undesiredNodeKindMapper) {
		this.markdomDocument = Objects.requireNonNull(markdomDocument);
		this.markdomRules = Objects.requireNonNull(markdomRules);
		this.commonmarkTextConfiguration = Objects.requireNonNull(commonmarkTextConfiguration);
		this.undesiredNodeKindMapper = Objects.requireNonNull(undesiredNodeKindMapper);
	}

	public String toCommonmarkText() {
		return markdomDocument.handle(
			new CommonmarkTextMarkdomHandler<>(
				commonmarkTextConfiguration,
				new StringWriter()
			)
		).toString();
	}
	
	public List<String> toWarnings() {
		return compileMessages(
			markdomRules.getUndesiredNodeKinds(),
			auditMarkdomDocument()
		);
	}

	private List<String> compileMessages(Set<MarkdomNodeKind> undesiredTypes, Set<MarkdomNodeKind> occuringTypes) {
		List<String> messages = new LinkedList<>();
		for (MarkdomNodeKind nodeKind : MarkdomNodeKind.values()) {
			if (occuringTypes.contains(nodeKind)) {
				if (undesiredTypes.contains(nodeKind)) {
					messages.add(undesiredNodeKindMapper.apply(nodeKind));
				}
			}
		}
		return messages;
	}

	private Set<MarkdomNodeKind> auditMarkdomDocument() {
		Set<MarkdomNodeKind> occuringNodeKinds = EnumSet.noneOf(MarkdomNodeKind.class);
		markdomDocument.handle(
			new AuditingMarkdomHandler<>(
				new IdleMarkdomHandler<>(),
				new NodeKindMarkdomAudit(
					new ConsumingNodeKindMarkdomAuditHandler(
						occuringNodeKinds::add
					)
				)
			)
		);
		return occuringNodeKinds;
	}
	
	public String toHtmlElementsText(boolean pretty) {
		return markdomDocument.handle(
			new FilteringMarkdomHandler<>(
				new JsoupHtmlDocumentMarkdomHandler(),
				new NodeKindMarkdomFilter(
					new BlacklistNodeKindMarkdomFilterHandler(
						markdomRules.getUndesiredNodeKinds()
					)
				)
			)
		).asElementsText(pretty);
	}


}
