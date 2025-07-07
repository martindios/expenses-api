package com.dios.expensesapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity // Entidad de JPA, anota que se va a mapear a una tabla de BD
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Genera un builder para la clase para la creación de instancias con builder
@Schema(description = "Entity that represents a user's expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expenseDate;

    @ManyToOne(fetch = FetchType.EAGER) // Muchos gastos pueden pertenecer a la misma categoria
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public UUID getUserId() {
        return user != null ? user.getId() : null;
    }

}
