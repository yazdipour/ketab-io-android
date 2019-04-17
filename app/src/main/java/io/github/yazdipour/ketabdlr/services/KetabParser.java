package io.github.yazdipour.ketabdlr.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.yazdipour.ketabdlr.models.Book;

public class KetabParser {

    public static Book BookPageToBook(Book book, String html) {
        Element element = Jsoup.parse(html).select("div#row").first();
        book.setTimeToRead(element.getElementsByClass("stat-desc").first().text());
        String infoAndSum = element.getElementsByClass("tinytext").first().outerHtml() + element.getElementById("DisplayContent_1").outerHtml();
        book.setDetails(infoAndSum.replaceAll("href=", "z="));
        return book;
    }

    public static List<Book> BookListElementToBooks(String html) {
        String cssSelector = "div ol div.booklist";
        List<Book> bookList = new ArrayList<>();
        for (Element element : Jsoup.parse(html).select(cssSelector))
            try {
                bookList.add(CarouselElementToBook(element));
            } catch (Exception e) {
                e.printStackTrace();
            }
        return bookList;
    }

    private static Book CarouselElementToBook(Element element) {
        Book book = new Book();
        Element a = element.getElementsByTag("a").first();
        Element img = a.getElementsByTag("img").first();
        book.setType(element.getElementsByClass("booktype").first().text());
        book.setUrl(a.attr("href"));
        try {
            Pattern pattern = Pattern.compile("(\\d+)");
            Matcher matcher = pattern.matcher(book.getUrl());
            if (matcher.find()) book.setId(Integer.parseInt(matcher.group(1)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            book.setName(img.attr("alt"));
            book.setCover(Api.BASE_URL + img.attr("src"));
            book.setAuthor(element.getElementsByTag("cite").first().text());
            book.setYear(element.getElementsByClass("year").first().text());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }
}
