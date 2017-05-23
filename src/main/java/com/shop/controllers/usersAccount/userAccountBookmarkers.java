package com.shop.controllers.usersAccount;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shop.data.tables.User;
import com.shop.others.RepositoriesAccess;

@Controller
@RequestMapping("/account")
public class userAccountBookmarkers {

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String start(Model model) {
		String roleName = SecurityContextHolder.getContext().getAuthentication().getName();
		if(roleName.equals("admin"))
			model.addAttribute("isAdmin", true);
		else
			model.addAttribute("isAdmin", false);
		
		return "userAccount/userAccount";
	}

	@RequestMapping(value = "orders", method = RequestMethod.GET)
	public String ordesBookmarker(Model model) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		model.addAttribute("orders", user.getOrders());
		return "userAccount/options/orders";
	}

	@RequestMapping(value = "changeData", method = RequestMethod.GET)
	public String changeDataBookmarker(Model model) {
		User user1 = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = RepositoriesAccess.usersRepository.findByLogin(user1.getLogin());

		model.addAttribute("user", user);
		return "userAccount/options/changeData";
	}

	@RequestMapping(value = "changeEmail", method = RequestMethod.GET)
	public String changeEmailBookmarker(Model model) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		model.addAttribute("user", user);
		return "userAccount/options/changeEmail";
	}

	@RequestMapping(value = "changePasswd", method = RequestMethod.GET)
	public String changePasswordBookmarker(Model model) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		model.addAttribute("user", user);
		return "userAccount/options/changePassword";
	}
	
	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public String deleteAccountSite() {		
		return "userAccount/options/deletAccount";
	}
}