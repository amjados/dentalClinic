package com.dentalclinic.dentalclinicapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing configurable reference codes and lookup values
 */
@Entity
@Table(name = "cg_ref_code")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CgRefCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code_id", nullable = false, length = 50)
    private String codeId;

    @Column(name = "code_name", nullable = false, length = 100)
    private String codeName;

    @Column(name = "code_display_value", nullable = false, length = 200)
    private String codeDisplayValue;

    @Column(name = "code_lng", nullable = false, length = 10)
    @Builder.Default
    private String codeLng = "en";

    @Column(name = "code_desc", length = 500)
    private String codeDesc;

    @Column(name = "code_value", length = 100)
    private String codeValue;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
