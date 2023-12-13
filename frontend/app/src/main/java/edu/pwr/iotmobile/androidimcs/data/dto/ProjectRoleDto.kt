package edu.pwr.iotmobile.androidimcs.data.dto

import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.helpers.extensions.asEnum

data class ProjectRoleDto(
    val id: Int? = null,
    val projectId: Int,
    val user: UserInfoDto,
    val role: String? = null,
) {
    companion object {
        fun ProjectRoleDto.toUserProjectRole() = this.role?.asEnum<UserProjectRole>()
    }
}
