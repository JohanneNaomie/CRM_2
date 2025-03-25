package site.easy.to.build.crm.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import site.easy.to.build.crm.service.budget.*;

import org.springframework.validation.ObjectError;
import jakarta.persistence.EntityManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.entity.settings.TicketEmailSettings;
import site.easy.to.build.crm.google.service.acess.GoogleAccessService;
import site.easy.to.build.crm.google.service.gmail.GoogleGmailApiService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.settings.TicketEmailSettingsService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/configAlerte")
public class ConfigAlerteController {

    public final ConfigAlerteService configAlerteService;

    @Autowired
    public ConfigAlerteController(ConfigAlerteService configAlerteService) {
        this.configAlerteService=configAlerteService;
    }

     // Endpoint to get the remaining budget by customer ID
     @GetMapping("/data")
     @ResponseBody
     public ResponseEntity<Map<String, Object>> getPourcentageConfigAlerte() {
         try {
            
            BigDecimal pourcentage=BigDecimal.ZERO;
            Optional<ConfigAlerte> configTest=configAlerteService.getLastConfigAlerte();
            ConfigAlerte config=null;
            // Check if the Optional contains a value or not
            if (configTest.isPresent()) {
                config=configTest.get();
                pourcentage = config.getPourcentage();
                // You can use statusBudget here
            } 
             
             // Return the budget as a JSON response
             Map<String, Object> response = new HashMap<>();
             response.put("poucentage", pourcentage);
             return ResponseEntity.ok(response);
         } catch (Exception e) {
            System.out.println("error poucentage config :" + e.getMessage());
             Map<String, Object> errorResponse = new HashMap<>();
             errorResponse.put("error", "Unable to retrieve poucentage config .");
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
         }
     }

}
