package edu.pwr.iotmobile.entities

import edu.pwr.iotmobile.dto.DashboardDTO
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(columnNames = ["project_id", "name"])]
)
class Dashboard (
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var project: Project,
    var name: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null
) {
    constructor() : this(Project(), "")

    fun toDTO() : DashboardDTO {
        return DashboardDTO(
            id,  name, project.id!!
        )
    }


}