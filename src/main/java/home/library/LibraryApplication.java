package home.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties
public class LibraryApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(LibraryApplication.class, args);
    }

}
