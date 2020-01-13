# Example project that shows a possible use-case for Markdom

This example illustrates a possible use-case, where an application needs to minimal support rich-text in different contexts. Different contexts allow different forms of rich-text. One context could, for instance, allow links, while another context doesn't. This is achieved by parsing rich-text as Commonmark text, representing the parsed Commonmark document internally as a Markdom document and using different Markdom handlers to produce various representations of that Markdom document.

Specifically, this example provides the following operations:

* Reading rich-text in form of Commonmark text.
* Creating a normalized version of that Commonmark text (e.g., one that always uses the same symbols for emphasises and so on) that keeps any undesired parts.
* Creating a list of warnings for all forms of undesired rich-text that occur in the read Commonmark text.
* Creating a HTML text snippet representation of the rich-text that doesn't contain the undesired parts.
* Creating a XHTML document representation of the rich-text that doesn't contain the undesired parts (e.g. to be transformed into a PDF-file using a [Flying Saucer](https://github.com/flyingsaucerproject/flyingsaucer)).

The last three operations are provided by a `RichtextProvider`. The first operation is provided by the `RichtextProviderFactory` which reads Commonmark text and constructs corresponding instances of `RichtextProvider`.

A specific set of undesired forms of rich-text is represented as a `RichtextContext`.

A `MarkdomRichtextProviderFactory` is constructed with such a `RichtextContext` and constructs instances of `MarkdomRichtextProvider`.  A `MarkdomRichtextProvider` internally holds a `MarkdomDocument` and uses different implementatiojns of `MarkdomHandler` to perform its functionality.

```java	
RichtextProviderFactory factory = new MarkdomRichtextProviderFactory(RichtextContext.DETAILS);
RichtextProvider provider = factory.fromCommonmark(getCommonmarkTextAsInputStream()));

System.out.println(provider.toCommonmarkText());

System.out.println(String.join("\n", provider.toWarnings()));

System.out.println(provider.toHtmlElementsText(true));

ITextRenderer renderer = new ITextRenderer();
DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
renderer.setDocument(provider.toXhtmlDocument(documentBuilder, "test.md"), null);
renderer.layout();

renderer.createPDF(new FileOutputStream("test.pdf"));
```




