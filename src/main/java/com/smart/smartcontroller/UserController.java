package com.smart.smartcontroller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactReositery;
import com.smart.dao.Userrepositery;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helpar.Messages;

@Controller
@RequestMapping("/user")
public class UserController {
@Autowired
	private Userrepositery userrepositery;

	@Autowired
	private ContactReositery contactReositery;
	
    @ModelAttribute
    public void addcommondate(Model model,Principal principal) {
	
	String usernameString=principal.getName();
    User user=	userrepositery.getUserByUserName(usernameString);
	
	System.out.println("User Nma"+ usernameString);
	System.out.println("User"+user);
	model.addAttribute("user",user);
	
}
	
	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal) {
		
		return "normal/user_dashboard";
		
		
	}
	
	
	
// handler for adding the contact 
	@GetMapping("add-contact")
	public String openAdContact(Model model) {
		
		model.addAttribute("tittle","Add Contact");
		model.addAttribute("conatct",new Contact() );
		
		return "normal/add_conatct_form";
		
	}
	
	
 // Processing the add contact
	@PostMapping("/process_contact")
	public String  processcontact(@ModelAttribute Contact contact , 
			@RequestParam("imageUrl") MultipartFile file,
			HttpSession session,
			Principal principal) {
	try {
		String name=principal.getName();
		User user=this.userrepositery.getUserByUserName(name);
		contact.setUser(user);
		//Image Uploaded start from here 
		
		if(file.isEmpty()) {
		//throw messes Img not set
			System.out.println("File is Empty");
			contact.setImage("contact.png");
		}
		else {
			
			contact.setImage(file.getOriginalFilename());
			File saveFile=new ClassPathResource("static/img").getFile();
			Path path=	Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);		 
		}	
		user.getContacts().add(contact);
		this.userrepositery.save(user);
		System.out.println("Data Added");
		//messages for data added
		session.setAttribute("message", new Messages("Contact Added Succesfully ....", "alert-success"));
		
		
		
		System.out.println("data"+contact);
	}
	catch (Exception e) {
		System.out.println("Error"+e.getMessage());
         e.printStackTrace();
         
         // error for data not added
 		session.setAttribute("message", new Messages("Something went Wrong Try Again ....", "alert-danger"));

		// TODO: handle exception
	}
		return "normal/add_conatct_form";
		
	}
	
	
	// show contact
	@GetMapping("/show-contact/{page}")
	public String viewcontact(@PathVariable("page") Integer page,  Model m,Principal principal) {
		
	String userName=principal.getName();
	User user=this.userrepositery.getUserByUserName(userName);
	Pageable pageable=PageRequest.of(page, 5);
	
	
    Page<Contact> contacts=	this.contactReositery.findContactsByUser(user.getId(),pageable);
		m.addAttribute("contacts",contacts);
		m.addAttribute("currentPage",page);
		m.addAttribute("totalPages",contacts.getTotalPages());
		
		
		return "normal/show_contact";
		
		
	}
	
//show detail for specific controller
	@RequestMapping("/{cId}/show-contact")
	public String showContactDetail(@PathVariable("cId") Integer cId , Model model) {
		
		System.out.println("Cid"+cId);
		
		Optional<Contact> conOptional=this.contactReositery.findById(cId);
		Contact contact=conOptional.get();
		model.addAttribute("contact",contact);
		
		return "normal/contact_detail";
		
	}
	
	
}
