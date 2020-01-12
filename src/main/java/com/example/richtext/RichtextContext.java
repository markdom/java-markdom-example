package com.example.richtext;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import io.markdom.common.MarkdomNodeKind;

public enum RichtextContext implements MarkdomRichtextProviderRules {

	DESCRIPTION(
		EnumSet.of(
			MarkdomNodeKind.CODE_BLOCK,
			MarkdomNodeKind.COMMENT_BLOCK,
			MarkdomNodeKind.DIVISION_BLOCK,
			MarkdomNodeKind.HEADING_BLOCK,
			MarkdomNodeKind.ORDERED_LIST_BLOCK,
			MarkdomNodeKind.QUOTE_BLOCK,
			MarkdomNodeKind.UNORDERED_LIST_BLOCK,
			MarkdomNodeKind.IMAGE_CONTENT,
			MarkdomNodeKind.LINK_CONTENT
		)
	),

	DETAILS(
		EnumSet.of(
			MarkdomNodeKind.CODE_BLOCK,
			MarkdomNodeKind.COMMENT_BLOCK,
			MarkdomNodeKind.IMAGE_CONTENT,
			MarkdomNodeKind.LINK_CONTENT
		)
	);

	private final Set<MarkdomNodeKind> undesiredNodeKinds;

	private RichtextContext(Set<MarkdomNodeKind> undesiredNodeKinds) {
		this.undesiredNodeKinds = Collections.unmodifiableSet(undesiredNodeKinds);
	}

	@Override
	public Set<MarkdomNodeKind> getUndesiredNodeKinds() {
		return undesiredNodeKinds;
	}


}
