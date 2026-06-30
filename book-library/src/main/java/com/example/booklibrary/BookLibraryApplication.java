package com.example.booklibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;

@SpringBootApplication
public class BookLibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookLibraryApplication.class, args);
    }

    /**
     * Once Spring Boot has fully started (server is up, ready to accept requests),
     * automatically open the dashboard in the system's default browser.
     */
    @Component
    static class BrowserLauncher {

        @EventListener(ApplicationReadyEvent.class)
        public void openBrowser() {
            String url = "http://localhost:8080";
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(url));
                } else {
                    System.out.println("Open this URL in your browser: " + url);
                }
            } catch (Exception e) {
                System.out.println("Could not auto-open browser. Open this URL manually: " + url);
            }
        }
    }
}
