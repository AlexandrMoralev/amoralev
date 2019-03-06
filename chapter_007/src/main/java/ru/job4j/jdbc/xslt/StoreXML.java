package ru.job4j.jdbc.xslt;

import java.io.File;
import java.util.List;

/**
 * StoreXML - преобразует List<Entry> в XML и записывает их в файл,
 *              передает имя XML файла в ConvertXSQT
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class StoreXML {
    private final File target;

    public StoreXML(File target) {
        this.target = target;
    }

    public boolean save(List<XmlUsage.Entry> list) {
        return false;
    }
/*
    Данные нужно сохранить в виде XML.

<entries>
<entry>
    <field>значение поля field</field>
</entry>
            ...
<entry>
    <field>значение поля field</field>
</entry>
</entries>

    Для создания xml файла нужно использовать технологию JAXB.*/
}
