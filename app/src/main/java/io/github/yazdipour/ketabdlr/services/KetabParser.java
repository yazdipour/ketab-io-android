package io.github.yazdipour.ketabdlr.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.yazdipour.ketabdlr.models.Book;

public class KetabParser {

    public static Book BookPageToBook(Book book, String html) {
        Element element = Jsoup.parse(html).select("div#row").first();
        try {
            book.setTimeToRead(element.getElementsByClass("stat-desc").first().text());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String infoAndSum = element.getElementsByClass("tinytext").first().outerHtml() + element.getElementById("DisplayContent_1").outerHtml();
        book.setDetails("<style type=\"text/css\">*{text-align: center;direction:rtl;" +
                "font-family: MyFont;src: url(\\\"file:///android_asset/font/irsans.ttf\\\")}</style>" +
                infoAndSum
                        .replaceAll("<a ", "<span ")
                        .replaceAll("</a", "</span ")
                        .replace("دانلود نرم افزار مطالعه", "")
                        .replace("افزودن خلاصه فارسی", ""));
        return book;
    }

    public static List<Book> BookListElementToBooks(String html, String cookie) {
        String cssSelector = "div ol div.booklist";
        List<Book> bookList = new ArrayList<>();
        for (Element element : Jsoup.parse(html).select(cssSelector))
            try {
                bookList.add(CarouselElementToBook(element, cookie));
            } catch (Exception e) {
                e.printStackTrace();
            }
        return bookList;
    }

    private static Book CarouselElementToBook(Element element, String cookie) {
        Book book = new Book();
        book.setCookie(cookie);
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
            book.setCover(img.attr("src"));
            book.setAuthor(element.getElementsByTag("cite").first().text());
            book.setYear(element.getElementsByClass("year").first().text());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }
}
