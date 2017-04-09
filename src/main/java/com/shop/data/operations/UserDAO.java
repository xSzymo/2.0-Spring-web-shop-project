package com.shop.data.operations;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.shop.data.tables.Address;
import com.shop.data.tables.UserRole;
import com.shop.data.tables.Users;
import com.shop.others.RepositoriesAccess;
import com.shop.services.CustomUserDetails;

public class UserDAO {
	public static Authentication login(String login, String password) {
		Users admin = RepositoriesAccess.usersRepository.findByLogin(login);
		Iterable<UserRole> found = RepositoriesAccess.userRolesRepository.findAll();
		List<String> userRoles = new ArrayList<>();

		for (UserRole x : found)
			for (Iterator<Users> iterator = x.getUser().iterator(); iterator.hasNext();) {
				Users a = iterator.next();
				if (a.getId() == admin.getId()) {
					userRoles.add(x.getRole());
				}
			}

		if (userRoles.isEmpty()) {
			System.out.println("Anonymous user");
			return null;
		}

		SecurityContextHolder.createEmptyContext();
		CustomUserDetails customUserDetails = new CustomUserDetails(admin, userRoles);
		UserDetails userDetails = customUserDetails;
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return authentication;
	}

	public static boolean isUser(String login, String password) {
		Iterable<Users> u = RepositoriesAccess.usersRepository.findAll();
		for (Users x : u)
			if (x.getLogin().equals(login) && x.getPassword().equals(password))
				return true;
		return false;
	}

	public static void register(String login, String password, String eMail, String name, String surname, String street,
			String country, String city, String postalCode, String date) {
		Users user = new Users();
		user.setLogin(login);
		user.setPassword(password);
		user.seteMail(eMail);
		user.setName(name);
		user.setSurname(surname);
		if (date.equals(""))
			user.setAge(0);
		else
			user.setAge(UserDAO.convertDateIntoYears(date));

		Address address = new Address(street, postalCode, city, country);
		RepositoriesAccess.addressRepository.save(address);

		user.setAddress(address);
		RepositoriesAccess.usersRepository.save(user);

		UserRole role = RepositoriesAccess.userRolesRepository.findRoleByRole("ROLE_USER");
		role.getUser().add(user);
		RepositoriesAccess.userRolesRepository.save(role);
	}

	// inexact algorithm
	@SuppressWarnings("deprecation")
	public static int convertDateIntoYears(String date) {
		String year = date.substring(0, 4);
		String month = date.substring(5, 7);
		String day = date.substring(8, 10);

		ZonedDateTime a = ZonedDateTime.now();
		Date newDate = new Date(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
		Date newDate1 = new Date(a.getYear(), a.getMonthValue(), a.getDayOfMonth());

		long time = newDate1.getTime() - newDate.getTime();
		int years = (int) ((time / (1000 * 60 * 60 * 24)) / 31 / 12);
		return years;
	}
}
