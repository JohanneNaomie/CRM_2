package site.easy.to.build.crm.service.lead;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.repository.LeadRepository;

import java.math.BigDecimal;
import java.util.List;

public interface LeadService { //its an interface

    String updateAmountById(Integer leadId, BigDecimal newAmount);
    
    List<Lead> getLeadsByStatus(String status);//added

    public Lead findByLeadId(int id);

    public List<Lead> findAll();

    public List<Lead> findAssignedLeads(int userId);

    public List<Lead> findCreatedLeads(int userId);

    public Lead findByMeetingId(String meetingId);

    public Lead save(Lead lead);

    public void delete(Lead lead);

    public List<Lead> getRecentLeads(int mangerId, int limit);
    public List<Lead> getCustomerLeads(int customerId);

    long countByEmployeeId(int employeeId);

    long countByManagerId(int managerId);
    long countByCustomerId(int customerId);

    List<Lead> getRecentLeadsByEmployee(int employeeId, int limit);
    List<Lead> getRecentCustomerLeads(int customerId, int limit);
    public void deleteAllByCustomer(Customer customer);
}
