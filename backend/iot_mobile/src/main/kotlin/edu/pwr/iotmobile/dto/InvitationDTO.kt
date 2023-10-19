package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.Invitation
import edu.pwr.iotmobile.enums.EInvitationStatus
import jakarta.validation.constraints.NotNull

data class InvitationDTO(
    val id: Int? = null,
    @field:NotNull
    val project: ProjectDTO,
    @field:NotNull
    val user: UserInfoDTO,
    val status: EInvitationStatus? = null,
) {
    fun toEntity(status: EInvitationStatus = EInvitationStatus.PENDING) : Invitation {
        return Invitation(project.toEntity(), user.toEntity(), status, id = id)
    }
}
