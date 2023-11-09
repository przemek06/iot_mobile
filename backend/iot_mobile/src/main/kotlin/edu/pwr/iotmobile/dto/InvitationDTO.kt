package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.Invitation
import edu.pwr.iotmobile.entities.Project
import edu.pwr.iotmobile.entities.User
import edu.pwr.iotmobile.enums.EInvitationStatus
import jakarta.validation.constraints.NotNull

data class InvitationDTO(
    val id: Int? = null,
    @field:NotNull
    val project: ProjectDTO,
    @field:NotNull
    val userId: Int,
    val status: EInvitationStatus? = null,
) {
    fun toEntity(status: EInvitationStatus = EInvitationStatus.PENDING) : Invitation {
        val user = User()
        user.id = userId
        return Invitation(project.toEntity(), user, status, id = id)
    }
}
