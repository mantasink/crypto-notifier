package cn;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Mantas Sinkevicius on 2018-01-18.
 */
public class MailSenderService {
	private static final Logger logger = LogManager.getLogger(MailSenderService.class);
	private static LocalDateTime lastSent = LocalDateTime.MIN;

	public static void send(List<Coin> coins) throws IOException {
		if (lastSent.plusMinutes(61).isAfter(LocalDateTime.now())) {
			// if email was sent less than 1h ago
			logger.info("Email notification was sent less than 1h ago.");
			return;
		}

		Email from = new Email(PropertyManager.EMAIL_FROM);

		Personalization personalization = new Personalization();
		personalization.addTo(from);
		for (String cc : PropertyManager.EMAILS_BCC) {
			personalization.addBcc(new Email(cc));
		}

		List<String> coinStringValues = coins.stream().map(Coin::toString).collect(Collectors.toList());
		Content content = new Content("text/plain", String.join("\n\n", coinStringValues));
		Mail mail = new Mail(from, "Crypto notification", from, content);
		mail.addPersonalization(personalization);

		SendGrid sg = new SendGrid(PropertyManager.API_KEY);
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			lastSent = LocalDateTime.now();
			logger.info(response.getStatusCode());
			logger.info(response.getHeaders());
		} catch (IOException ex) {
			logger.fatal(ex);
		}
	}

}
