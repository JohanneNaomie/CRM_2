package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.ConfigAlerte;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigAlerteRepository extends JpaRepository<ConfigAlerte, Integer> {

    // Method to save a ConfigAlerte
    ConfigAlerte save(ConfigAlerte configAlerte);

    // Retrieve all ConfigAlerte entities
    List<ConfigAlerte> findAll();

    // Retrieve a ConfigAlerte by its ID
    Optional<ConfigAlerte> findById(Integer id);

    // Retrieve the last inserted ConfigAlerte by the most recent date
    Optional<ConfigAlerte> findTopByOrderByDateDesc();


}
