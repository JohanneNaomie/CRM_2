
package site.easy.to.build.crm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.service.budget.DepenseService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import site.easy.to.build.crm.service.budget.ConfigAlerteService;
import org.springframework.http.HttpStatus;

@Controller
@RequestMapping("/api")
public class ApiController {

    private final DepenseService depenseService;
    private final TicketService ticketService;
    private final LeadService leadService;
    private final CustomerService customerService;
    private final ConfigAlerteService configAlerteService;

    @Autowired
    public ApiController(DepenseService depenseService, CustomerService customerService, TicketService ticketService, LeadService leadService,ConfigAlerteService configAlerteService) {
        this.depenseService = depenseService;
        this.customerService = customerService;
        this.ticketService = ticketService;
        this.leadService = leadService;
        this.configAlerteService = configAlerteService;
    }

    @GetMapping("/customer/top5")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getTop5Customers() {
        List<Depense> depenses = depenseService.findAll();

        Map<Customer, BigDecimal> customerSpending = depenses.stream()
            .collect(Collectors.groupingBy(
                Depense::getCustomer,
                Collectors.reducing(BigDecimal.ZERO, Depense::getMontant, BigDecimal::add)
            ));

        List<Map<String, Object>> top5Customers = customerSpending.entrySet().stream()
            .sorted(Map.Entry.<Customer, BigDecimal>comparingByValue().reversed())
            .limit(5)
            .map(entry -> {
                Customer customer = entry.getKey();
                Map<String, Object> data = new HashMap<>();
                data.put("customerId", customer.getCustomerId());
                data.put("customerName", customer.getName());
                data.put("totalDepense", entry.getValue());
                return data;
            })
            .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("top5Customers", top5Customers);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ticketByStatus")
    public ResponseEntity<Map<String, Map<String, Object>>> ticketByStatus() {
        List<Ticket> tickets = ticketService.findAll();
        Map<String, Map<String, Object>> result = new HashMap<>();

        for (Ticket ticket : tickets) {
            String status = ticket.getStatus();
            BigDecimal amount = ticket.getAmount();
            result.putIfAbsent(status, new HashMap<>());
            Map<String, Object> statusData = result.get(status);
            statusData.putIfAbsent("totalAmount", BigDecimal.ZERO);
            statusData.putIfAbsent("count", 0);
            statusData.put("totalAmount", ((BigDecimal) statusData.get("totalAmount")).add(amount));
            statusData.put("count", (int) statusData.get("count") + 1);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/leadByStatus")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> leadByStatus() {
        List<Lead> leads = leadService.findAll();
        Map<String, Long> leadCounts = leads.stream()
            .collect(Collectors.groupingBy(Lead::getStatus, Collectors.counting()));
        Map<String, BigDecimal> leadAmounts = leads.stream()
            .collect(Collectors.groupingBy(Lead::getStatus, 
                    Collectors.reducing(BigDecimal.ZERO, Lead::getAmount, BigDecimal::add)));

        Map<String, Object> response = new HashMap<>();
        for (String status : leadCounts.keySet()) {
            Map<String, Object> statusData = new HashMap<>();
            statusData.put("count", leadCounts.get(status));
            statusData.put("totalAmount", leadAmounts.get(status));
            response.put(status, statusData);
        }
        return ResponseEntity.ok(response);
    }
    

    @GetMapping("/totalAll")
    @ResponseBody
    public ResponseEntity<Map<String, BigDecimal>> totalAll() {
        List<Lead> leads = leadService.findAll();
        List<Ticket> tickets = ticketService.findAll();

        BigDecimal totalLead = BigDecimal.ZERO;
        for (Lead lead : leads) {
            totalLead = totalLead.add(lead.getAmount()); // Add each lead's amount
        }

        BigDecimal totalTicket = BigDecimal.ZERO;
        for (Ticket ticket : tickets) {
            totalTicket = totalTicket.add(ticket.getAmount()); // Add each ticket's amount
        }

        BigDecimal total = totalLead.add(totalTicket); // Compute total of both

        // Creating response map
        Map<String, BigDecimal> response = new HashMap<>();
        response.put("totalLead", totalLead);
        response.put("totalTicket", totalTicket);
        response.put("total", total);

        return ResponseEntity.ok(response);
    }

    // getAllTicketByStatus  (String status)
    // getAllLeadsByStatus (String status)
    // getAllDepenseByCustomer (int customerId)

    @GetMapping("/getAllLeadByStatus/{status}")
    @ResponseBody
    public ResponseEntity<Map<String, List<Lead>>> getAllLeadByStatus(@PathVariable String status) {
        List<Lead> leads = leadService.getLeadsByStatus(status);
        
        Map<String, List<Lead>> response = new HashMap<>();
        response.put("leads", leads);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllTicketByStatus/{status}")
    @ResponseBody
    public ResponseEntity<Map<String, List<Ticket>>> getAllTicketByStatus(@PathVariable String status) {
        List<Ticket> tickets = ticketService.getTicketsByStatus(status);
        
        Map<String, List<Ticket>> response = new HashMap<>();
        response.put("tickets", tickets);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/getDepenseByCustomer/{customerId}")
    @ResponseBody
    public ResponseEntity<Map<String, List<Depense>>> getDepenseByCustomer(@PathVariable int customerId) {
        List<Depense> depenses = depenseService.findDepenseByCustomerId(customerId);
        
        Map<String, List<Depense>> response = new HashMap<>();
        response.put("depenses", depenses);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/saveConfigAlerte/{pourcentage}")
    @ResponseBody
    public ResponseEntity<String> saveConfigAlerte(@PathVariable BigDecimal pourcentage) {
        // Validate that pourcentage is between 1 and 100
        if (pourcentage.compareTo(BigDecimal.ONE) < 0 || pourcentage.compareTo(BigDecimal.valueOf(100)) > 0) {
            return ResponseEntity.badRequest().body("Pourcentage must be between 1 and 100.");
        }

        ConfigAlerte conf = new ConfigAlerte(pourcentage);
        String message = "";

        try {
            configAlerteService.saveConfigAlerte(conf);
            message = "Save config alerte successful";
        } catch (Exception e) {
            message = "Save config alerte failed, error: " + e.getMessage();
        }

        return ResponseEntity.ok(message);
    }

    @GetMapping("/deleteTicket/{idTicket}")
    @ResponseBody
    public ResponseEntity<String> deleteTicket(@PathVariable int idTicket) {
        Ticket ticket=ticketService.findByTicketId(idTicket);

        String message = "";

        try {
            ticketService.delete(ticket);
            message = "delete ticket successful";
        } catch (Exception e) {
            message = "delete ticket failed, error: " + e.getMessage();
        }
        return ResponseEntity.ok(message);
    }
    @GetMapping("/deleteLead/{idLead}")
    @ResponseBody
    public ResponseEntity<String> deleteLead(@PathVariable int idLead) {
        Lead lead=leadService.findByLeadId(idLead);

        String message = "";

        try {
            leadService.delete(lead);
            message = "delete lead successful";
        } catch (Exception e) {
            message = "delete lead failed, error: " + e.getMessage();
        }
        return ResponseEntity.ok(message);
    }

    //any am ticket/lead modif dia trigger modif depense
    @GetMapping("/deleteDepense/{id}/{type_depense_transaction}")
    @ResponseBody
    public ResponseEntity<String> deleteDepense(@PathVariable int id,@PathVariable int type_depense_transaction) {

        if(type_depense_transaction==1){//lead
            Lead lead=leadService.findByLeadId(id);

            String message = "";

            try {
                leadService.delete(lead);
                message = "delete lead successful";
            } catch (Exception e) {
                message = "delete lead failed, error: " + e.getMessage();
            }
            return ResponseEntity.ok(message);
        }

        Ticket ticket=ticketService.findByTicketId(id);

        String message = "";

        try {
            ticketService.delete(ticket);
            message = "delete ticket successful";
        } catch (Exception e) {
            message = "delete ticket failed, error: " + e.getMessage();
        }
        return ResponseEntity.ok(message);

    }

    @GetMapping("/updateLead/{idLead}/{montant}")
    @ResponseBody
    public ResponseEntity<String> updateLead(@PathVariable Integer idLead,@PathVariable  BigDecimal montant) {
        String message = "";

        // Check if idTicket is not null
        if (idLead == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lead ID cannot be null!");
        }

        try {
            message = leadService.updateAmountById(idLead, montant);
        } catch (Exception e) {
            message = "update lead failed, error: " + e.getMessage();
        }
        return ResponseEntity.ok(message);
    }
    

    @GetMapping("/updateTicket/{idTicket}/{montant}")
    @ResponseBody
    public ResponseEntity<String> updateTicket(@PathVariable Integer idTicket, @PathVariable BigDecimal montant) {
        String message = "";

        // Check if idTicket is not null
        if (idTicket == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ticket ID cannot be null!");
        }

        try {
            message = ticketService.updateAmountById(idTicket, montant);
        } catch (Exception e) {
            message = "Update ticket failed, error: " + e.getMessage();
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/updateDepense/{id}/{type_depense_transaction}/{montant}")
    @ResponseBody
    public ResponseEntity<String> updateDepense(@PathVariable Integer id, @PathVariable Integer type_depense_transaction, @PathVariable BigDecimal montant) {
        String message = "";

        // Check if any of the parameters are null or invalid
        if (id == null || type_depense_transaction == null || montant == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("All parameters (id, type_depense_transaction, montant) are required and cannot be null.");
        }

        if (type_depense_transaction == 1) { // Lead
            // Proceed with lead update
            try {
                message = leadService.updateAmountById(id, montant);
            } catch (Exception e) {
                message = "Update lead failed, error: " + e.getMessage();
            }
            return ResponseEntity.ok(message);
        }

        // If not Lead (ticket update)
        try {
            message = ticketService.updateAmountById(id, montant);
        } catch (Exception e) {
            message = "Update ticket failed, error: " + e.getMessage();
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/getLastConfigAlerte")
    @ResponseBody
    public ResponseEntity<Map<String, BigDecimal>> getLastConfigAlerte() {

        Map<String, BigDecimal> response = new HashMap<>();
        
        BigDecimal pourcentage=BigDecimal.ZERO;
        BigDecimal negativeOne = new BigDecimal("-1");
        BigDecimal one = new BigDecimal("1");

        Optional<ConfigAlerte> configTest=configAlerteService.getLastConfigAlerte();
        ConfigAlerte config=null;
        // Check if the Optional contains a value or not
        if (configTest.isPresent()) {
            config=configTest.get();
            pourcentage = config.getPourcentage();
            response.put("result", one);
            // You can use statusBudget here
        }else{
            response.put("result", negativeOne);
        }
        response.put("pourcentage", pourcentage);

        return ResponseEntity.ok(response);

    }











}
