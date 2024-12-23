package com.MyProject.SimpleProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.MyProject.SimpleProject.Registration.Registration;
import com.MyProject.SimpleProject.Registration.loginpage;
import com.MyProject.SimpleProject.Repository.MyRepository;
import com.MyProject.SimpleProject.Repository.loginRepository;

import jakarta.servlet.http.HttpSession;



@Controller
public class MyController {
	
	@Autowired
	MyRepository myRepository;
	
	@Autowired
	loginRepository loginRpo;
	
	
	@GetMapping("/")
	public String load() {
		return "home";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	
	@GetMapping("/dashboard")
	public String dashboard() {
		return "dashboard";
	}
	

	
//login page 
	
	/*@PostMapping("/login") 
	public String login(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
		loginpage login = loginRpo.findByUsername(username);
		
		if(login != null && login.getPassword().equals(password)) {
			 
	        session.setAttribute("loggedInUser", username);
	        
	        return "redirect:/dashboard";
	    } 
		else {
			model.addAttribute("success","login failed");
			return "login";
		}
		
	}*/
	
	//Optimized login code:
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
	    // Retrieve user by username
	    loginpage login = loginRpo.findByUsername(username);

	    // Check if user exists and password matches
	    if (login != null && password.equals(login.getPassword())) {
	        // Set session attribute for logged-in user
	        session.setAttribute("loggedInUser", username);
	        return "redirect:/dashboard";
	    } else {
	        // Add error message and return to login page
	        model.addAttribute("error", "Invalid username or password");
	        return "login";
	    }
	}


	
	
	//logout
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    session.invalidate();// Clear session data
	    return "redirect:/login"; // Redirect to the login page
	}

	
	
/*	//register

	@GetMapping("/register")
	public String addStudent(ModelMap map, HttpSession session) {
		// Check if the user is logged in
	    String loggedInUser = (String) session.getAttribute("loggedInUser");
	    if (loggedInUser == null) {
	        // Redirect to login if no session exists
	        map.put("error", "You must log in first!");
	        return "login";
	    }
	    
	   
		map.put("add", "add");
		return "dashboard";
	}
*/
	
	
	//Optimized login code:
	
	@GetMapping("/register")
	public String addStudent(ModelMap model, HttpSession session) {
	    // Check if the user is logged in
	    String loggedInUser = (String) session.getAttribute("loggedInUser");
	    
	    // If no user is logged in, redirect to login page with an error message
	    if (loggedInUser == null) {
	        model.addAttribute("error", "You must log in first!");
	        return "login";
	    }
	    
	    // Proceed to dashboard 
	    model.addAttribute("add", "add");
	    return "dashboard";
	}

	
	
	
	
	
	@PostMapping("/register")
	public String addStudent(Registration registration, RedirectAttributes redirectAttributes, @RequestParam String email) {
		
	    // Check if email is already registered
	    Registration existingUser = myRepository.findByEmail(email);
	    if (existingUser != null) {
	    	redirectAttributes.addFlashAttribute("error", "Email is already registered.");     
	        return "redirect:/register"; // Show registration page with error
	    }

	    // Save the new user
	    myRepository.save(registration);
	    redirectAttributes.addFlashAttribute("success", "Successfully Registered");
	    return "redirect:/dashboard"; // Redirect to login after registration
	}

	
	
	
	
	
	
	//fetch the values
	
	@GetMapping("/fetch")
	public String fetchStudent(ModelMap map, HttpSession session) {
		// Check if the user is logged in
	    String loggedInUser = (String) session.getAttribute("loggedInUser");
	    if (loggedInUser == null) {
	        // Redirect to login if session is invalid
	        map.put("error", "You must log in first!");
	        return "login";
	    } 
		map.put("records",myRepository.findAll());
		System.out.println("Session loggedInUser: " + session.getAttribute("loggedInUser"));

		return "dashboard";
	}
	
	
	

	

 /*   @GetMapping("/fetch")
    public ResponseEntity<?> fetchStudent(HttpSession session) {
        // Check if the user is logged in
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            // Return error response as JSON
            Map<String, String> error = new HashMap<>();
            error.put("error", "You must log in first!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        // Fetch student records
        List<Registration> studentRecords = myRepository.findAll();

        // Return the student records as JSON
        return ResponseEntity.ok(studentRecords);
    }*/


	
	
	
/*	//delete the values based on id
	
	@GetMapping("/delete")
	public String delete(@RequestParam int id,ModelMap map, HttpSession session) {
		// Check if the user is logged in
	    String loggedInUser = (String) session.getAttribute("loggedInUser");
	    if (loggedInUser == null) {
	        // Redirect to login if session is invalid
	        map.put("error", "You must log in first!");
	        return "login";
	    }
		myRepository.deleteById(id);
		map.put("success","Record Deleted Success");
		return "dashboard";
	}
*/	
	


	
	@GetMapping("/delete")
	public String delete(@RequestParam int id, ModelMap model, HttpSession session) {
	    // Check if the user is logged in
	    String loggedInUser = (String) session.getAttribute("loggedInUser");

	    if (loggedInUser == null) {
	        // Redirect to login if session is invalid
	        model.addAttribute("error", "You must log in first!");
	        return "login";
	    }

	    // Delete the record by id
	    myRepository.deleteById(id);
	    model.addAttribute("success", "Record deleted successfully.");

	    return "dashboard";
	}

	
	
/*	//edit the values
	
	@GetMapping("/edit")
	public String edit(@RequestParam int id,ModelMap map, HttpSession session) {
		// Check if the user is logged in
	    String loggedInUser = (String) session.getAttribute("loggedInUser");
	    if (loggedInUser == null) {
	        // Redirect to login if session is invalid
	        map.put("error", "You must log in first!");
	        return "login";
	    }

		Registration registration=myRepository.findById(id).orElseThrow();
		map.put("emp", registration);
		map.put("edit", "edit");
		return "dashboard";
	}
	
	
	
	//update the value
	
	@PostMapping("/update")
	public String update(Registration registration,ModelMap map, HttpSession session) {
		// Check if the user is logged in
	    String loggedInUser = (String) session.getAttribute("loggedInUser");
	    if (loggedInUser == null) {
	        // Redirect to login if session is invalid
	        map.put("error", "You must log in first!");
	        return "login";
	    }
		myRepository.save(registration);
		map.put("success", "Record Updated Success");
		return "dashboard";
	}
	

}
*/
	
	//Optimized Edit the values
	@GetMapping("/edit")
	public String edit(@RequestParam int id, ModelMap model, HttpSession session) {
	    // Check if the user is logged in
	    String loggedInUser = (String) session.getAttribute("loggedInUser");
	    if (loggedInUser == null) {
	        // Redirect to login if session is invalid
	        model.addAttribute("error", "You must log in first!");
	        return "login";
	    }

	    // Fetch the registration by ID, or handle the case where it's not found
	    Registration registration = myRepository.findById(id).orElse(null);
	    if (registration == null) {
	        model.addAttribute("error", "Record not found.");
	        return "dashboard"; // Redirect to dashboard if record is not found
	    }

	    // Add the record to the model
	    model.addAttribute("emp", registration);
	    model.addAttribute("edit", "edit");
	    return "dashboard";
	}

	//Optimized Update the value
	@PostMapping("/update")
	public String update(Registration registration, ModelMap model, HttpSession session) {
	    // Check if the user is logged in
	    String loggedInUser = (String) session.getAttribute("loggedInUser");
	    if (loggedInUser == null) {
	        // Redirect to login if session is invalid
	        model.addAttribute("error", "You must log in first!");
	        return "login";
	    }

	    // Save the updated registration object
	    myRepository.save(registration);
	    model.addAttribute("success", "Record updated successfully.");
	    return "dashboard";
	}

}

