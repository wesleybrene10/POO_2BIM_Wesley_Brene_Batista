package br.edu.avaliacao.sacm;
import br.edu.avaliacao.sacm.console.SistemaConsole;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication public class SacmApplication implements CommandLineRunner {
 private final SistemaConsole console;
 public SacmApplication(SistemaConsole console){this.console=console;}
 public static void main(String[] args){SpringApplication.run(SacmApplication.class,args);}
 @Override public void run(String...args){console.exibirMenu();}
}
