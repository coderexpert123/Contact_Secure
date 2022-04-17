package com.smart.smartcontroller;

import javax.servlet.http.HttpSession;
import javax.sound.midi.Soundbank;
import javax.validation.Valid;

import com.smart.helpar.Messages;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.smart.dao.Userrepositery;
import com.smart.entities.User;

 
@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@Autowired
	private Userrepositery userrepo;
	
	@RequestMapping("/")
	public String home(Model model) {
		 model.addAttribute("title", "Home: This is main Page");
	 return "home";
		 
	 }
	
	@RequestMapping("/about")
	public String about(Model model) {
		 model.addAttribute("title", "About: This is about Page");
	 return "about";
		 
	 }
	
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		 model.addAttribute("title", "Register: This is Register Page");
		 model.addAttribute("user",new User());	
	 return "signup";
		 
	 }
	//handler for registering
	@RequestMapping(value="/do_register" , method = RequestMethod.POST)
	public String registeruser(@Valid @ModelAttribute("user")
	User user,BindingResult result1,@RequestParam(value = "agreement",defaultValue = "false")
	boolean agreement ,Model model,HttpSession session) {
	
		
		try {
			if(!agreement) {
				
				System.out.println("Not Accept the aggrement...");
				throw new Exception("Not Agree Terms conditions");
			}
			
			if(result1.hasErrors()) {
				System.out.println("Error"+result1.toString());
				model.addAttribute("User",user);
				
				return "signup";
				
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
			System.out.println("Aggremnt"+agreement);
			System.out.println("USer"+user);
			
			User result=this.userrepo.save(user);
			
			model.addAttribute("user", new  User());
			session.setAttribute("message", new Messages("Registration sucessful ....", "alert-success"));
			return "signup";

			
	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("User",user);
			
			session.setAttribute("message", new Messages("Something Went Wrong...."+e.getMessage(), "alert-danger"));
			return "signup";
		}
		
}
	    @GetMapping("/sigin")
		public String customlogin(Model model) {
				model.addAttribute("Sigin","Login Images....");
		
			return "login";
			
		}
}