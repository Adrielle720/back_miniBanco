package com.corretora;

import com.corretora.model.Corretora;
import com.corretora.repository.CorretoraRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CorretoraRepository corretoraRepository;

    public DataInitializer(CorretoraRepository corretoraRepository) {
        this.corretoraRepository = corretoraRepository;
    }

    @Override
    public void run(String... args) {
        // Garante que sempre existe uma corretora com ID=1
        if (corretoraRepository.count() == 0) {
            Corretora corretora = new Corretora("HiveX Corretora", "00000000000001");
            corretoraRepository.save(corretora);
            System.out.println("✅ Corretora HiveX criada automaticamente com ID=1");
        }
    }
}
