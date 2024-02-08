/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package erp.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import erp.domain.Activity;
import erp.domain.Customer;
import erp.domain.Message;
import erp.domain.User;
import erp.services.ActivityService;
import erp.services.CustomerService;
import erp.services.MessagingService;
import erp.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author brandon
 */
@Controller
public class MessagingController {

    @Autowired
    private final MessagingService messagingService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final CustomerService customerService;

    @Autowired
    private final ActivityService activityService;

    public MessagingController(MessagingService messagingService, UserService userService, CustomerService customerService, ActivityService activityService) {
        this.messagingService = new MessagingService();
        this.userService = userService;
        this.activityService = activityService;
        this.customerService = customerService;
    }

    @GetMapping("/home/communications")
    public String showMessageList(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) ((Map<String, Object>) auth.getDetails()).get("id");
        List<Message> messages = messagingService.findAllUserMessages(userId);
        model.addAttribute("messages", messages);
        return "messageList";
    }

    @GetMapping("/home/communications/message-form")
    public String createMessage(Model model) {
        Message message = new Message();
        List<User> users = userService.getAllUsers();
        List<Customer> customers = customerService.getAllCustomers();
        List<Activity> activities = activityService.getAllActivities();
        List<User> userRecipent = message.getUserRecipients();
        model.addAttribute("userRecipients", userRecipent);
        model.addAttribute("activities", activities);
        model.addAttribute("customers", customers);
        model.addAttribute("message", message);
        model.addAttribute("users", users);
        return "messageForm";
    }

    @PostMapping("/home/communications/send-message")
    public String sendMessage(@ModelAttribute Message message, @RequestParam String userRecipients, @RequestParam String customerRecipients, @RequestParam String activityRecipients, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) ((Map<String, Object>) auth.getDetails()).get("id");

        messagingService.sendMessage(userId, userRecipients, customerRecipients, activityRecipients, message.getSubject(), message.getContent());

        return "redirect:/home/communications";
    }

    @GetMapping("/home/communications/message-overview/{id}")
    public String messageOverview(@PathVariable("id") long id, Model model) {
        Message message = messagingService.getMessageById(id);
        model.addAttribute("message", message);
        return "messageOverview";
    }

    @PostMapping("/home/communications/delete-message/{id}")
    public String deleteMessage(@PathVariable("id") long id) {
        messagingService.deleteMessage(id);
        return "redirect:/home/communications";
    }

    @GetMapping("/filtered-messages-by-subject")
    public String filterMessagesBySubject(@RequestParam("subject") String subject, Model model) {
        List<Message> filteredMessages = messagingService.findMessageBySubject(subject);
        model.addAttribute("messages", filteredMessages);
        return "messageList";
    }

}
