package com.rosshambrick.standardlib;

import android.text.Editable;
import android.text.Html;
import android.text.Spanned;

import org.xml.sax.XMLReader;

public class HtmlFormatter {

    public static Spanned format(String html) {
        return Html.fromHtml(html, null, new Html.TagHandler() {
            @Override
            public void handleTag(boolean opening,
                                  String tag,
                                  Editable output,
                                  XMLReader xmlReader) {
                if (opening && tag.equals("li")) {
                    output.append("\n")
                          .append("\t\u2022 ");
                }
            }
        });
    }
}
