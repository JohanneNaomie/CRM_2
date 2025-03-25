package site.easy.to.build.crm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "config_alerte")
public class ConfigAlerte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_depense", updatable = false, nullable = false)
    private Integer idDepense;

    @Column(name = "date", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime date = LocalDateTime.now();

    @Column(name = "pourcentage", nullable = false, precision = 5, scale = 2)
    @NotNull(message = "Pourcentage is required")
    private BigDecimal pourcentage;

    public ConfigAlerte() {
    }

    public ConfigAlerte(BigDecimal pourcentage) {
        this.pourcentage = pourcentage;
        this.date = LocalDateTime.now(); // Ensure the date is set upon creation
    }


    public Integer getIdDepense() {
        return idDepense;
    }

    public void setIdDepense(Integer idDepense) {
        this.idDepense = idDepense;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(BigDecimal pourcentage) {
        this.pourcentage = pourcentage;
    }
}
