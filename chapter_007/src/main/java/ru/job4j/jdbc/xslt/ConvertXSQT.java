package ru.job4j.jdbc.xslt;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

/**
 * ConvertXSQT - converts input XML file to an output XML file, using scheme template (XSLT)
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ConvertXSQT {

    /**
     * Transforms the input XML to another XML by template
     *
     * @param source input XML-file
     * @param dest   output XML-file
     * @param scheme transformation template
     * @throws TransformerException
     */
    public void convert(final File source,
                        final File dest,
                        final File scheme
    ) throws TransformerException {
        if (source == null || dest == null || scheme == null) {
            throw new NullPointerException();
        }
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(
                new StreamSource(scheme)
        );
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(
                new StreamSource(source),
                new StreamResult(dest)
        );
    }
}
