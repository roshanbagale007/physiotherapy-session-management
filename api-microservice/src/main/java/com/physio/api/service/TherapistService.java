package com.physio.api.service;

import com.physio.api.dto.TherapistSummaryResponse;
import com.physio.api.entity.Session;
import com.physio.api.entity.Therapist;
import com.physio.api.exception.ResourceNotFoundException;
import com.physio.api.repository.SessionRepository;
import com.physio.api.repository.TherapistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TherapistService {

    private final TherapistRepository therapistRepository;
    private final SessionRepository sessionRepository;

    @Autowired
    public TherapistService(TherapistRepository therapistRepository, SessionRepository sessionRepository) {
        this.therapistRepository = therapistRepository;
        this.sessionRepository = sessionRepository;
    }

    public List<TherapistSummaryResponse> getAllTherapists() {
        return therapistRepository.findAllWithUsers().stream()
                .map(t -> mapToResponse(t, null))
                .collect(Collectors.toList());
    }

    public TherapistSummaryResponse getTherapistAvailability(Long therapistId, LocalDate date) {
        Therapist t = therapistRepository.findByIdWithUser(therapistId)
                .orElseThrow(() -> new ResourceNotFoundException("Therapist not found with ID: " + therapistId));

        LocalDate queryDate = date != null ? date : LocalDate.now();

        // Calculate booked slots
        List<Session> booked = sessionRepository.findByTherapistIdAndSessionDate(t.getId(), queryDate);
        Set<LocalTime> bookedStartTimes = booked.stream()
                .filter(s -> !"CANCELLED".equalsIgnoreCase(s.getStatus()))
                .map(Session::getStartTime)
                .collect(Collectors.toSet());

        // Working hours: 09:00 to 17:00 in 1-hour intervals
        List<String> available = new ArrayList<>();
        LocalTime slot = LocalTime.of(9, 0);
        while (slot.isBefore(LocalTime.of(17, 0))) {
            if (!bookedStartTimes.contains(slot)) {
                available.add(slot.toString());
            }
            slot = slot.plusMinutes(t.getSlotDurationMinutes() > 0 ? t.getSlotDurationMinutes() : 60);
        }

        return mapToResponse(t, available);
    }

    private TherapistSummaryResponse mapToResponse(Therapist t, List<String> availableSlots) {
        TherapistSummaryResponse res = new TherapistSummaryResponse();
        res.setId(t.getId());
        res.setUserId(t.getUser().getId());
        res.setFullName(t.getUser().getFullName());
        res.setEmail(t.getUser().getEmail());
        res.setPhone(t.getUser().getPhone());
        res.setSpecialization(t.getSpecialization());
        res.setLicenseNumber(t.getLicenseNumber());
        res.setExperienceYears(t.getExperienceYears());
        res.setConsultationFee(t.getConsultationFee());
        res.setAvailableDays(t.getAvailableDays());
        res.setSlotDurationMinutes(t.getSlotDurationMinutes());
        res.setAvailableTimeSlots(availableSlots != null ? availableSlots : List.of());
        return res;
    }
}
