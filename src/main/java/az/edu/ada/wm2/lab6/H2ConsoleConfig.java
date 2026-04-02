package az.edu.ada.wm2.lab6;

import jakarta.servlet.Servlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H2ConsoleConfig {
	@Bean
	public ServletRegistrationBean<Servlet> h2ConsoleServlet() {
		try {
			Class<?> webServletClass = Class.forName("org.h2.server.web.JakartaWebServlet");
			Object instance = webServletClass.getDeclaredConstructor().newInstance();

			ServletRegistrationBean<Servlet> registration = new ServletRegistrationBean<>(
					(Servlet) instance,
					"/h2-console/*"
			);

			registration.addInitParameter("webAllowOthers", "false");
			return registration;
		} catch (Exception e) {
			throw new IllegalStateException("Failed to register H2 console servlet", e);
		}
	}
}

