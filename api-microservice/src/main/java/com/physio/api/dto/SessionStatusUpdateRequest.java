package com.physio.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class SessionStatusUpdateRequest {

    @NotBlank(message = "Status cannot be blank.")
    @Pattern(regexp = "^(SCHEDULED|ATTENDED|CANCELLED|NO_SHOW)$", message = "Status must be SCHEDULED, ATTENDED, CANCELLED, or NO_SHOW.")
    private String status;

    private String cancellationReason;

    public SessionStatusUpdateRequest() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }
}
