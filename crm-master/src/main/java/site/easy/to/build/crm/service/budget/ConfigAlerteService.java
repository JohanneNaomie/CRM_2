package site.easy.to.build.crm.service.budget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.ConfigAlerte;
import site.easy.to.build.crm.repository.ConfigAlerteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigAlerteService {


    @Autowired
    private ConfigAlerteRepository configAlerteRepository;

    // Method to get all ConfigAlerte entries
    public List<ConfigAlerte> getAllConfigAlertes() {
        return configAlerteRepository.findAll();
    }

    // Method to save a ConfigAlerte
    public ConfigAlerte saveConfigAlerte(ConfigAlerte configAlerte) {
        return configAlerteRepository.save(configAlerte);
    }


    // Method to get ConfigAlerte by its ID
    public Optional<ConfigAlerte> getConfigAlerteById(Integer id) {
        return configAlerteRepository.findById(id);
    }

    // Method to get the last inserted ConfigAlerte based on the date
    public Optional<ConfigAlerte> getLastConfigAlerte() {
        return configAlerteRepository.findTopByOrderByDateDesc();
    }
}
