package com.shop.controllers.administratorSite.orders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.shop.data.enums.EnumPayments;
import com.shop.data.tables.Address;
import com.shop.data.tables.Book;
import com.shop.data.tables.CouponCode;
import com.shop.data.tables.Order;
import com.shop.others.RepositoriesAccess;

@Controller
@RequestMapping("administratorSite/orders")
public class CreateOrders {

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createSite(Model model) {
		EnumPayments[] kindOfPayment = EnumPayments.values();
		String[] paymentName = new String[kindOfPayment.length];
		int i = 0;
		for (EnumPayments x : kindOfPayment)
			paymentName[i++] = x.name();

		model.addAttribute("payments", paymentName);

		model.addAttribute("books", null);

		Iterable<Book> books = RepositoriesAccess.booksRepository.findAll();
		model.addAttribute("allBooks", books);
		return "administratorSite/ordersManager/create";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String createOrder(@RequestParam("shippingAddressStreet") String shippingAddressStreet,
			@RequestParam("shippingAddressPostalCode") String shippingAddressPostalCode,
			@RequestParam("shippingAddressCity") String shippingAddressCity,
			@RequestParam("shippingAddressCountry") String shippingAddressCountry,

			@RequestParam("billingAddressStreet") String billingAddressStreet,
			@RequestParam("billingAddressPostalCode") String billingAddressPostalCode,
			@RequestParam("billingAddressCity") String billingAddressCity,
			@RequestParam("billingAddressCountry") String billingAddressCountry,

			@RequestParam("couponCode") String couponCode,
			@RequestParam("couponCodeDiscount") double couponCodeDiscount,

			@RequestParam("billingAddress") Long billingAddressId,
			@RequestParam("shippingAddress") Long shippingAddressId, @RequestParam("couponCodeId") Long couponCodeId,

			@RequestParam("payment") Object payment, @RequestParam("price") String price,
			@RequestParam("realized") boolean realized, HttpServletRequest request, Model model, String... books) {
		EnumPayments[] kindOfPayment = EnumPayments.values();
		EnumPayments paymentType = null;

		for (EnumPayments x : kindOfPayment)
			if (x.name().equals(payment))
				paymentType = x;

		// System.out.println(payment);

		Order order = new Order();
		Address billingAddress = null;
		Address shippingAddress = null;
		CouponCode couponCodes = null;

		if (billingAddressId != null)
			billingAddress = RepositoriesAccess.addressRepository.findById(billingAddressId);
		if (shippingAddressId != null)
			shippingAddress = RepositoriesAccess.addressRepository.findById(shippingAddressId);
		if (couponCodeId != null)
			couponCodes = RepositoriesAccess.couponCodesRepository.findById(couponCodeId);

		if (billingAddress != null) {
			billingAddress.setCity(billingAddressCity);
			billingAddress.setCountry(billingAddressCountry);
			billingAddress.setPostalCode(billingAddressPostalCode);
			billingAddress.setStreet(billingAddressStreet);
		} else {
			billingAddress = new Address();
			billingAddress.setCity(billingAddressCity);
			billingAddress.setCountry(billingAddressCountry);
			billingAddress.setPostalCode(billingAddressPostalCode);
			billingAddress.setStreet(billingAddressStreet);
		}

		if (shippingAddress != null) {
			shippingAddress.setCity(shippingAddressCity);
			shippingAddress.setCountry(shippingAddressCountry);
			shippingAddress.setPostalCode(shippingAddressPostalCode);
			shippingAddress.setStreet(shippingAddressStreet);
		} else {
			shippingAddress = new Address();
			shippingAddress.setCity(shippingAddressCity);
			shippingAddress.setCountry(shippingAddressCountry);
			shippingAddress.setPostalCode(shippingAddressPostalCode);
			shippingAddress.setStreet(shippingAddressStreet);
		}

		if (couponCodes != null) {
			couponCodes.setCode(couponCode);
			couponCodes.setCodeDiscount(couponCodeDiscount);
		} else {
			couponCodes = new CouponCode();
			couponCodes.setCode(couponCode);
			couponCodes.setCodeDiscount(couponCodeDiscount);
		}
		RepositoriesAccess.addressRepository.save(billingAddress);
		RepositoriesAccess.addressRepository.save(shippingAddress);
		RepositoriesAccess.couponCodesRepository.save(couponCodes);

		System.out.println("halo");
		order.setPrice(new BigDecimal(price));
		order.setRealized(realized);
		order.setPaymentMethod(paymentType);
		order.setShippingAddress(shippingAddress);
		order.setBillingAddress(billingAddress);
		order.setCouponCodes(couponCodes);

		ArrayList<Book> b = new ArrayList<Book>();

		if (books != null) {
			for (int i = 0; i < books.length; i++) {
				if (RepositoriesAccess.booksRepository.findByName(books[i]) != null) {
					b.add(RepositoriesAccess.booksRepository.findByName(books[i]));
				}
			}
		}

		RepositoriesAccess.booksRepository.save(b);

		order.getBooks().addAll(b);
		RepositoriesAccess.ordersRepository.save(order);

		addNeedObjects(model, couponCodeId, billingAddressId, shippingAddressId, books);
		model.addAttribute("msg", "Success");
		return "administratorSite/ordersManager/create";
	}

	@RequestMapping(value = "/createAddress", method = RequestMethod.POST)
	public String createAddress(@RequestParam("street") String street, @RequestParam("postalCode") String postalCode,
			@RequestParam("city") String city, @RequestParam("country") String country,
			@RequestParam("billingAddress") Long billingAddressId,
			@RequestParam("shippingAddress") Long shippingAddressId, @RequestParam("couponCodeId") Long couponCodeId,
			@RequestParam("address") String address1, Model model, HttpServletRequest request, String... books) {

		Address address = new Address(street, postalCode, city, country);
		RepositoriesAccess.addressRepository.save(address);

		addNeedObjects(model, couponCodeId, billingAddressId, shippingAddressId, books);

		if (address1.equals("shipping"))
			model.addAttribute("shippingAddress", address);
		else
			model.addAttribute("billingAddress", address);

		return "administratorSite/ordersManager/create";
	}

	@RequestMapping(value = "/createCouponCode", method = RequestMethod.POST)
	public String createCouponCode(@RequestParam("codeDiscount") String codeDiscount, @RequestParam("code") String code,
			@RequestParam("billingAddress") Long billingAddressId,
			@RequestParam("shippingAddress") Long shippingAddressId, @RequestParam("couponCodeId") Long couponCodeId,
			Model model, HttpServletRequest request, @RequestParam(name = "books", required = false) String... books) {

		addNeedObjects(model, couponCodeId, billingAddressId, shippingAddressId, books);
		CouponCode couponCodeFound = RepositoriesAccess.couponCodesRepository.findByCode(code);

		if (couponCodeFound != null) {
			model.addAttribute("msgError", "couponCode already exist");
			return "administratorSite/ordersManager/create";
		}
		CouponCode couponCode = new CouponCode(Double.parseDouble(codeDiscount), code);

		RepositoriesAccess.couponCodesRepository.save(couponCode);
		model.addAttribute("couponCode", couponCode);

		return "administratorSite/ordersManager/create";
	}

	@RequestMapping(value = "/createBook", method = RequestMethod.POST)
	public String createBook(@RequestParam("billingAddress") Long billingAddressId,
			@RequestParam("shippingAddress") Long shippingAddressId, @RequestParam("couponCodeId") Long couponCodeId,
			Model model, HttpServletRequest request) {
		Iterable<Book> books = RepositoriesAccess.booksRepository.findAll();
		LinkedList<Book> chosenBooks = new LinkedList<Book>();
		chosenBooks.clear();

		for (Book x : books)
			if (request.getParameter(x.getName()) != null)
				chosenBooks.add(x);

		String[] books1 = new String[chosenBooks.size()];
		int j = 0;
		for (int i = 0; i < books1.length; i++)
			books1[j++] = chosenBooks.get(i).getName();

		addNeedObjects(model, couponCodeId, billingAddressId, shippingAddressId, books1);

		return "administratorSite/ordersManager/create";
	}

	public void addNeedObjects(Model model, Long couponCodeId, Long billingAddressId, Long shippingAddressId,
			String[] bookNames) {
		if ((couponCodeId != null)) {
			CouponCode couponCode = RepositoriesAccess.couponCodesRepository.findById(couponCodeId);
			model.addAttribute("couponCode", couponCode);
		}
		if ((billingAddressId != null)) {
			Address billingAddress = RepositoriesAccess.addressRepository.findById(billingAddressId);
			model.addAttribute("billingAddress", billingAddress);
		}
		if ((shippingAddressId != null)) {
			Address shippingAddress = RepositoriesAccess.addressRepository.findById(shippingAddressId);
			model.addAttribute("shippingAddress", shippingAddress);
		}

		EnumPayments[] kindOfPayment = EnumPayments.values();
		String[] paymentName = new String[kindOfPayment.length];
		int i = 0;
		for (EnumPayments x : kindOfPayment)
			paymentName[i++] = x.name();

		model.addAttribute("payments", paymentName);
		model.addAttribute("books", bookNames);

		Iterable<Book> books = RepositoriesAccess.booksRepository.findAll();
		model.addAttribute("allBooks", books);
	}
}