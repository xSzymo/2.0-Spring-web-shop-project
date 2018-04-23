package com.shop.controllers.shop;

import com.shop.configuration.ApplicationProperties;
import com.shop.data.services.BooksService;
import com.shop.data.services.CategoriesService;
import com.shop.data.tables.Book;
import com.shop.data.tables.Category;
import com.shop.others.RepositoriesAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.LinkedList;

@Controller
@RequestMapping("shop")
public class ShopBookmarkers {
    @Autowired
    private CategoriesService categoriesService;
    @Autowired
    private BooksService booksService;

    @RequestMapping(value = "{categoryName}", method = RequestMethod.GET)
    public String shopBookmarker(@PathVariable String categoryName, Model model) {
        Category category = categoriesService.findOneByName(categoryName);
        model.addAttribute("books", category.getBooks());
        return "shop/categorySite";
    }

    @RequestMapping(value = "categorySite/{id}", method = RequestMethod.GET)
    public RedirectView addItemToBasket(@PathVariable Long id, @RequestParam("number") String number,
                                        HttpServletRequest request, Model model) {
        Book book = booksService.findOne(id);
        Iterable<Category> categories = categoriesService.findAll();
        Category category = null;
        for (Category x : categories) {
            for (Book x1 : x.getBooks()) {
                if (book.getId() == x1.getId()) {
                    category = x;
                }
            }
        }
        if (number == null || number.equals(""))
            return new RedirectView(ApplicationProperties.PROJECT_NAME + "shop/" + category.getName());

        LinkedList<Book> basketWithAllBooks = Shop.getBasketWithAllBooks(request);
        HashSet<Book> basket = Shop.getBasket(request);

        boolean is = false;
        for (Book x : basket)
            if (x.getId() == book.getId())
                is = true;

        if (is) {
            for (int i = 0; i < Long.parseLong(number); i++)
                basketWithAllBooks.add(book);
        } else {
            for (int i = 0; i < Long.parseLong(number); i++)
                basketWithAllBooks.add(book);
            basket.add(book);
        }
        request.getSession().setAttribute("basketWithAllBooks", basketWithAllBooks);
        request.getSession().setAttribute("basket", basket);

        return new RedirectView(ApplicationProperties.PROJECT_NAME + "shop/" + category.getName());
    }
}
