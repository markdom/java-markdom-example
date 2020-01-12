package com.example.richtext;

import java.util.Set;

import io.markdom.common.MarkdomNodeKind;

public interface MarkdomRichtextProviderRules {

	public Set<MarkdomNodeKind> getUndesiredNodeKinds();

}
