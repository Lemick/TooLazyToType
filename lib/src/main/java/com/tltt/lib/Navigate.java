package com.tltt.lib;

import com.tltt.lib.dto.QuidQuestionDTO;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class Navigate {

    public static final String BASE_URL = "https://www.google.com/search?q=%s";

    public static void openSearchPage(QuidQuestionDTO quidQuestionDTO) throws URISyntaxException, IOException {
        String searchQuery = String.format(BASE_URL, URLEncoder.encode(quidQuestionDTO.getQuestionEntitled(), "UTF-8"));
        URI uri = new URI(searchQuery);
        java.awt.Desktop.getDesktop().browse(uri);
    }
}
