package co.unicauca.edu.co.contables.configuration.classesOfDocuments.dataAccess.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.TenantId;

@Entity
@Table(
    name = "classes_of_documents",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "id_enterprise"})
    },
    indexes = {
        @Index(name = "idx_doc_class_id_enterprise", columnList = "id_enterprise"),
        @Index(name = "idx_doc_class_name", columnList = "name")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "id_enterprise", nullable = false)
    private String idEnterprise;

    @TenantId
    @Column(name = "tenant_id")
    private String tenantId;
}


