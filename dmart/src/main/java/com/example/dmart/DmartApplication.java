package com.example.dmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;

@SpringBootApplication
public class DmartApplication {

    public static void main(String[] args) {
        SpringApplication.run(DmartApplication.class, args);
    }

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
