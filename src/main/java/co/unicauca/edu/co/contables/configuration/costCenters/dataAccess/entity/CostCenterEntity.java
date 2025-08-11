package co.unicauca.edu.co.contables.configuration.costCenters.dataAccess.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.TenantId;

import java.util.List;

@Entity
@Table(name = "CostCenter", uniqueConstraints = @UniqueConstraint(columnNames = { "code", "idEnterprise" }), indexes = {
                @Index(name = "idx_cost_center_id_enterprise", columnList = "idEnterprise"),
                @Index(name = "idx_cost_center_code", columnList = "code")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostCenterEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String code;

        @Column(nullable = false)
        private String name;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent_id", referencedColumnName = "id")
        private CostCenterEntity parent;

        @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private List<CostCenterEntity> children;

        // Id de empresa obligatorio
        @Column(nullable = false)
        private String idEnterprise;

        @TenantId
        private String tenantId;
}
