package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.Invitation
import edu.pwr.iotmobile.entities.Project
import edu.pwr.iotmobile.entities.User
import edu.pwr.iotmobile.enums.EInvitationStatus
import jakarta.validation.constraints.NotNull

data class InvitationDTO(
    val id: Int? = null,
    @field:NotNull
    val projectId: Int,
    @field:NotNull
    val userId: Int,
    val status: EInvitationStatus? = null,
) {
    fun toEntity(status: EInvitationStatus = EInvitationStatus.PENDING) : Invitation {
        val project = Project()
        project.id = projectId
        val user = User()
        user.id = userId
        return Invitation(project, user, status, id = id)
    }
}
