package com.physio.springmvc.controller;

import com.physio.springmvc.dto.TreatmentPlanForm;
import com.physio.springmvc.model.SessionEntity;
import com.physio.springmvc.model.TreatmentPlanEntity;
import com.physio.springmvc.service.TreatmentPlanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

/**
 * Unit 3.2.1 - 3.2.4: Spring 6 MVC Controller.
 * Demonstrates:
 * - @Controller, @RequestMapping, @GetMapping, @PostMapping
 * - Model & View data transfer
 * - JSR-380 Bean Validation via @Valid and BindingResult
 * - Post-Redirect-Get (PRG) pattern with Flash Attributes
 */
@Controller
@RequestMapping("/plans")
public class TreatmentPlanController {

    private final TreatmentPlanService planService;

    @Autowired
    public TreatmentPlanController(TreatmentPlanService planService) {
        this.planService = planService;
    }

    /**
     * Unit 3.2.3: Handler Method - List all Treatment Plans via JPQL.
     */
    @GetMapping
    public String listPlans(Model model) {
        List<TreatmentPlanEntity> plans = planService.getAllPlans();
        model.addAttribute("plans", plans);
        return "plan-list";
    }

    /**
     * Unit 3.2.4: Show Plan Creation Form with pre-populated dropdowns.
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("planForm")) {
            TreatmentPlanForm form = new TreatmentPlanForm();
            form.setTargetSessions(10);
            model.addAttribute("planForm", form);
        }
        populateDropdowns(model);
        return "plan-create";
    }

    /**
     * Unit 3.2.4: Process Form Submission with JSR-380 @Valid and BindingResult.
     */
    @PostMapping
    public String processCreateForm(
            @Valid @ModelAttribute("planForm") TreatmentPlanForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Validate date consistency if both dates are supplied
        if (form.getStartDate() != null && form.getEstimatedEndDate() != null) {
            if (form.getEstimatedEndDate().before(form.getStartDate())) {
                bindingResult.rejectValue("estimatedEndDate", "date.invalid", "Estimated completion date cannot be before the start date.");
            }
        }

        // Unit 3.2.4: JSR-380 Validation Check
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Please correct the highlighted validation errors below.");
            populateDropdowns(model);
            return "plan-create";
        }

        try {
            TreatmentPlanEntity created = planService.createPlan(form);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Treatment Plan #" + created.getId() + " for patient " +
                    created.getPatient().getUser().getFullName() + " created successfully!");
            return "redirect:/plans";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Failed to create plan: " + ex.getMessage());
            populateDropdowns(model);
            return "plan-create";
        }
    }

    /**
     * Unit 3.2.3: View single Treatment Plan details and sessions.
     */
    @GetMapping("/{id}")
    public String viewPlanDetails(@PathVariable("id") Long id, Model model) {
        TreatmentPlanEntity plan = planService.getPlanById(id);
        List<SessionEntity> sessions = planService.getSessionsForPlan(id);

        model.addAttribute("plan", plan);
        model.addAttribute("sessions", sessions);
        return "plan-details";
    }

    /**
     * Unit 3.4: Clinic Recovery & Adherence Analytics Dashboard.
     */
    @GetMapping("/report")
    public String viewRecoveryReport(Model model) {
        Map<String, Object> stats = planService.getClinicRecoveryStats();
        List<TreatmentPlanEntity> plans = planService.getAllPlans();

        model.addAttribute("stats", stats);
        model.addAttribute("plans", plans);
        return "recovery-report";
    }

    private void populateDropdowns(Model model) {
        model.addAttribute("patients", planService.getAllPatients());
        model.addAttribute("therapists", planService.getAllTherapists());
    }
}
