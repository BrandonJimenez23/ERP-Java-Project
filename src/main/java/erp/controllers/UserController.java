/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package erp.controllers;

import erp.domain.User;
import erp.services.UserService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
 * @author brandon
 */
@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(User user) {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, @RequestParam("photo") MultipartFile photo) {
        try {
            String photoPath;
            if (photo != null && !photo.isEmpty()) {
                // Si el usuario ha seleccionado una foto, la procesamos y guardamos
                photoPath = userService.savePhoto(photo, user);
            } else {
                // Si el usuario no ha seleccionado una foto, usamos una imagen predeterminada
                photoPath = "usuario2.png";
            }

            // Establecemos la ruta de la foto en el usuario
            user.setPhotoPath(photoPath);

            // Guardamos el usuario
            userService.saveOrUpdateUser(user);
        } catch (IOException e) {
            // Manejamos la excepción
            System.out.println(e.getMessage());
        }

        return "redirect:/login";
    }

    @GetMapping("/home/users")
    public String showAllPersons(Model model) {
        List<User> users = userService.getAllUsers();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) ((Map<String, Object>) authentication.getDetails()).get("id");
        User user = userService.findById(userId);
        users.remove(user);
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/home/users/user-form")
    public String showUserForm(Model model) {
        User user = new User();
        user.setPhotoPath("usuario2.png");
        model.addAttribute("user", user);
        return "userForm";
    }

    @Transactional
    @PostMapping("/home/users/update")
    public String updateUser(@ModelAttribute User user, @RequestParam("photo") MultipartFile photo) {
        String photoPath;
        User oldUser = null;

        // Comprueba si el usuario ya existe
        if (user.getId() != null) {
            oldUser = userService.findById(user.getId());
        }

        try {
            // Si no se proporciona una nueva foto, usa la foto actual del usuario
            photoPath = userService.savePhoto(photo, oldUser != null ? oldUser : user);
            user.setPhotoPath(photoPath);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        // Guardamos el usuario
        userService.saveOrUpdateUser(user);

        return "redirect:/home/users";
    }

    @Transactional
    @PostMapping("/home/users/updateCurrentUser")
    public String updateCurrentUser(@ModelAttribute User user, @RequestParam("photo") MultipartFile photo) {
        String photoPath;
        User oldUser = userService.findById(user.getId());
        try {
            // Si no se proporciona una nueva foto, usa la foto actual del usuario
            photoPath = userService.savePhoto(photo, oldUser);

            user.setPhotoPath(photoPath);
        } catch (IOException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Guarda el usuario
        userService.saveOrUpdateUser(user);

        // Actualiza los detalles de autenticación
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                user.getEmail(), auth.getCredentials(), auth.getAuthorities());
        Map<String, Object> details = new HashMap<>();
        details.put("id", Long.valueOf(user.getId()));
        details.put("name", user.getName());
        details.put("photoPath", user.getPhotoPath());
        newAuth.setDetails(details);
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "redirect:/home/users";
    }

    @GetMapping("/home/users/update/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) ((Map<String, Object>) auth.getDetails()).get("id");
        User existingUser = userService.findById(userId);
        User user = userService.findById(id);
        user.setPassword(null);
        model.addAttribute("user", user);
        if (existingUser != null && existingUser.equals(user)) {
            return "editProfile";
        } else {
            return "userForm";
        }
    }

    @GetMapping("/filtered-users-by-name")
    public String filterUsersByName(@RequestParam("name") String name, Model model) {
        List<User> filteredUsers = userService.findUsersByName(name);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        filteredUsers.remove(user);
        model.addAttribute("users", filteredUsers);
        return "users";
    }

    @Transactional
    @PostMapping("/home/users/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/home/users";
    }

    @GetMapping("/home/users/{id}")
    public String showUserProfile(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "userOverview";
    }

    @GetMapping("/header")
    public String getHeader(Model model) {

        // Luego, devuelves la vista que contiene el HTML del encabezado
        // Asegúrate de que esta vista solo contenga el HTML del encabezado y no de toda la página
        return "header";
    }

}
