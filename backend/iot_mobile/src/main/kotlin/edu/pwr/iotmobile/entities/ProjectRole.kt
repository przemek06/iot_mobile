package edu.pwr.iotmobile.entities

import edu.pwr.iotmobile.dto.ProjectRoleDTO
import edu.pwr.iotmobile.enums.EProjectRole
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(columnNames = ["project_id", "user_id"])]
)
class ProjectRole (
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var project: Project,
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var user: User,
    @Column(nullable = false)
    var role: EProjectRole,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null
) {
    constructor() : this(Project(), User(), EProjectRole.VIEWER)

    fun toDTO() : ProjectRoleDTO {
        return ProjectRoleDTO(id, project.id!!, user.toUserInfoDTO(), role)
    }

}