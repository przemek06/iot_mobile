package edu.pwr.iotmobile.entities

import edu.pwr.iotmobile.dto.InvitationDTO
import edu.pwr.iotmobile.enums.EInvitationStatus
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
class Invitation (
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var project: Project,
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var user: User,
    @Column(nullable = false)
    var status: EInvitationStatus,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null
) {
    constructor() : this(Project(), User(), EInvitationStatus.PENDING)

    fun toDTO() : InvitationDTO {
        return InvitationDTO(id, project.toDTO(), user.id!!, status)
    }
}