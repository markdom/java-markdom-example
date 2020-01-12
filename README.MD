# Example project that shows a possible use-case for Markdom

This example illustrates a possible use-case, where an application needs to support minimal rich text in different contexts. Different context allow different types of rich-text. One context could, for instance, allow links, while another context doesn't. This is achieved by parsing rich-text as Commonmark text, representing the parsed Commonmark document internally as a Markdom document and using different Markdom handlers to produce various representations of that Markdom document.

Specifically, this example provides tollowing operations:

* Reading rich-text in form of Commonmark text.
* Creating a normalized version of that Commonmark text (e.g., one that always uses the same symbols for emphasises and so on) that keeps any undesired parts.
* Creating a list of warnings for all types of undesired rich-text that occur in the read Commonmark text.
* Creating a HTML representation of the rich-text that doesn't contain the undesired parts.

The last three operations are provided by an `RichtextProvider`. The first operation is provided by the `RichtextProviderFactory` which is able to read Commonmark text and returns corresponding `RichtextProvider`s.

A specific set of undesired `MarkdomNodeKind`s is represented as a `RichtextContext` enumeration value.

A `MarkdomRichtextProviderFactory` is constructed with such an `RichtextContext` and returns instances of `MarkdomRichtextProvider`.  A `MarkdomRichtextProvider` internally holds a `MarkdomDocument` and uses different `MarkdomHandler`s to implement its functionality.

```java	
RichtextProviderFactory factory = new MarkdomRichtextProviderFactory(RichtextContext.DETAILS);
RichtextProvider provider = factory.fromCommonmark(new InputStreamReader(Main.class.getResourceAsStream("/test.md")));

System.out.println(provider.toCommonmarkText());

System.out.println(String.join("\n", provider.toWarnings()));

System.out.println(provider.toHtmlElementsText(true));
```




