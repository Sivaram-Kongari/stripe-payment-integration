package com.hulkhiretech.payments.service.interfaces;

import com.hulkhiretech.payments.dto.stripe.StripeEventDTO;

public interface StripeWebhookService {

	public void processEvent(StripeEventDTO eventDTO);
}
