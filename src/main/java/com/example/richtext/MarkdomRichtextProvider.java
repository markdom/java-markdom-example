package com.example.richtext;

import java.io.StringWriter;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;

import io.markdom.common.MarkdomNodeKind;
import io.markdom.handler.AuditingMarkdomHandler;
import io.markdom.handler.FilteringMarkdomHandler;
import io.markdom.handler.IdleMarkdomHandler;
import io.markdom.handler.audit.nodekind.NodeKindMarkdomAudit;
import io.markdom.handler.filter.nodekind.NodeKindMarkdomFilter;
import io.markdom.handler.html.jsoup.JsoupHtmlDocumentMarkdomHandler;
import io.markdom.handler.html.w3c.XhtmlDocumentMarkdomHandler;
import io.markdom.handler.text.commonmark.CommonmarkTextConfiguration;
import io.markdom.handler.text.commonmark.CommonmarkTextMarkdomHandler;
import io.markdom.model.MarkdomDocument;

public class MarkdomRichtextProvider implements RichtextProvider {

	private final MarkdomDocument markdomDocument;

	private final Set<MarkdomNodeKind> undesiredNodeKinds;

	private final Function<MarkdomNodeKind, String> undesiredNodeKindMapper;

	private final CommonmarkTextConfiguration commonmarkTextConfiguration;

	public MarkdomRichtextProvider(MarkdomDocument markdomDocument, MarkdomRichtextProviderRules markdomRules,
		Function<MarkdomNodeKind, String> undesiredNodeKindMapper, CommonmarkTextConfiguration commonmarkTextConfiguration) {
		this.markdomDocument = Objects.requireNonNull(markdomDocument);
		this.undesiredNodeKinds = Objects.requireNonNull(markdomRules).getUndesiredNodeKinds();
		this.undesiredNodeKindMapper = Objects.requireNonNull(undesiredNodeKindMapper);
		this.commonmarkTextConfiguration = Objects.requireNonNull(commonmarkTextConfiguration);
	}

	@Override
	public String toCommonmarkText() {
		return markdomDocument.handle(
			new CommonmarkTextMarkdomHandler<>(
				commonmarkTextConfiguration,
				new StringWriter()
			)
		).toString();
	}
	
	@Override
	public List<String> toWarnings() {
		return compileMessages(auditMarkdomDocument());
	}

	private Set<MarkdomNodeKind> auditMarkdomDocument() {
		Set<MarkdomNodeKind> occuringNodeKinds = EnumSet.noneOf(MarkdomNodeKind.class);
		markdomDocument.handle(
			new AuditingMarkdomHandler<>(
				new IdleMarkdomHandler<>(),
				new NodeKindMarkdomAudit(occuringNodeKinds::add)
			)
		);
		return occuringNodeKinds;
	}
	
	private List<String> compileMessages(Set<MarkdomNodeKind> occuringNodeKinds) {
		List<String> messages = new LinkedList<>();
		for (MarkdomNodeKind nodeKind : MarkdomNodeKind.values()) {
			if (occuringNodeKinds.contains(nodeKind)) {
				if (undesiredNodeKinds.contains(nodeKind)) {
					messages.add(undesiredNodeKindMapper.apply(nodeKind));
				}
			}
		}
		return messages;
	}
	
	@Override
	public String toHtmlElementsText(Map<String, String> abbreviations, boolean pretty) {
		return markdomDocument.handle(
			new FilteringMarkdomHandler<>(
				new JsoupHtmlDocumentMarkdomHandler(new AbbreviationsHtmlDelegate(abbreviations)),
				new NodeKindMarkdomFilter(undesiredNodeKinds::contains)
			)
		).asNodesText(pretty);
	}
	
	@Override
	public Document toXhtmlDocument(DocumentBuilder documentBuilder, String title) {
		return markdomDocument.handle(
			new FilteringMarkdomHandler<>(
				new XhtmlDocumentMarkdomHandler(documentBuilder, title),
				new NodeKindMarkdomFilter(undesiredNodeKinds::contains)
			)
		).asDocument();
	}

}
