/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package erp.controllers;

import erp.domain.Customer;
import erp.services.CustomerService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Marc
 */
@Controller
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/home/customers")
    public String showAllCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customers";
    }

    @GetMapping("/home/customers/{id}")
    public String showCustomerProfile(@PathVariable("id") Long id, Model model) {
        Customer customer = customerService.findById(id);
        model.addAttribute("customer", customer);
        return "customerOverview";
    }

    @GetMapping("/home/customers/show-participant-list")
    public String showParticipants(Model model) {
        // List<User> customers = userService.getAllUsers();
        //model.addAttribute("customers", customers);
        return "participantList";
    }

    @PostMapping("/home/customers/update")
    public String updateCustomer(@ModelAttribute Customer customer, @RequestParam("photo") MultipartFile photo) {
        try {
            String photoPath;
            if (photo != null && !photo.isEmpty()) {
                // Si el usuario ha seleccionado una foto, la procesamos y guardamos
                photoPath = customerService.savePhoto(photo);
            } else {
                // Si el usuario no ha seleccionado una foto, usamos una imagen predeterminada
                photoPath = "/images/usuario2.png";
            }

            // Establecemos la ruta de la foto en el usuario
            customer.setPhotoPath(photoPath);

            // Guardamos el usuario
            customerService.saveOrUpdateCustomer(customer);
        } catch (IOException e) {
            // Manejamos la excepción
            System.out.println(e.getMessage());
        }

        return "redirect:/home/customers";
    }

    @GetMapping("/home/customers/update/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Customer customer = customerService.findById(id);
        model.addAttribute("customer", customer);
        return "customerForm";
    }

    @PostMapping("/home/customers/delete/{id}")
    public String deleteCustomer(@PathVariable("id") long id) {
        customerService.deleteCustomer(id);
        return "redirect:/home/customers";
    }
}