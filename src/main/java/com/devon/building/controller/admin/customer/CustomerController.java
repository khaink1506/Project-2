package com.devon.building.controller.admin.customer;


import com.devon.building.constant.SystemConstant;
import com.devon.building.entity.User;
import com.devon.building.enums.Status;
import com.devon.building.enums.Transaction;
import com.devon.building.model.request.CustomerRequest;
import com.devon.building.model.request.CustomerSearchRequest;
import com.devon.building.model.response.CustomerSearchResponse;
import com.devon.building.service.CustomerService;
import com.devon.building.service.TransactionService;
import com.devon.building.service.UserService;
import com.devon.building.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final UserService userService;
    private static final String STATUS = "status";
    private static final String TRANSACTION = "transaction";
    private static final String TRANSACTIONCSKH = "transactionCSKH";
    private static final String TRANSACTIONDDX = "transactionDDX";
    private final TransactionService transactionService;

    @GetMapping("/list")
    public ModelAndView getCustomerList( @ModelAttribute CustomerSearchRequest customerSearchRequest){
        if(SecurityUtils.getAuthorities().contains(SystemConstant.STAFF_ROLE)){
            User user = userService.getUserByUsername(SecurityUtils.getCurrentUsername());
            customerSearchRequest.setStaffId(user.getId());
        }
        ModelAndView modelAndView = new ModelAndView("admin/customer/customerList");
        modelAndView.addObject("staffs", userService.loadStaff());
        modelAndView.addObject(STATUS, Status.getStatus());
        modelAndView.addObject("customerSearchRequest", customerSearchRequest);
        Page<CustomerSearchResponse> customerPage = customerService.findCustomer(customerSearchRequest);
        modelAndView.addObject("customerPage", customerPage);
        return modelAndView;
    }

    @GetMapping("/edit")
    public ModelAndView getCustomerEdit(@ModelAttribute("customerEdit") CustomerRequest request, Model model){
        ModelAndView modelAndView = new ModelAndView("admin/customer/customerEdit");
        modelAndView.addObject("customer", request);
        modelAndView.addObject(STATUS, Status.getStatus());
        return modelAndView;
    }

    @GetMapping("/{id}/update")
    public ModelAndView getUpdateBuilding(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("admin/customer/customerEdit");
        if(SecurityUtils.getAuthorities().contains(SystemConstant.STAFF_ROLE)){
            User staff = userService.getUserByUsername(SecurityUtils.getCurrentUsername());
            if(staff.getCustomers().stream().noneMatch(customer -> customer.getId().equals(id))){
                return new ModelAndView("404");
            }
        }
        CustomerRequest customerRequest = customerService.findById(id);
        modelAndView.addObject("customer", customerRequest);
        modelAndView.addObject(STATUS, Status.getStatus());
        modelAndView.addObject(TRANSACTION, Transaction.getTransaction());
        modelAndView.addObject(TRANSACTIONCSKH, transactionService.getTransactions(id, "CSKH"));
        modelAndView.addObject(TRANSACTIONDDX, transactionService.getTransactions(id, "DDX"));
        return modelAndView;
    }
}
