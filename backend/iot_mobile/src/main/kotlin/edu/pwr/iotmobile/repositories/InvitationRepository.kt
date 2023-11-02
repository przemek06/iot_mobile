package edu.pwr.iotmobile.repositories

import edu.pwr.iotmobile.entities.Invitation
import edu.pwr.iotmobile.enums.EInvitationStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InvitationRepository : JpaRepository<Invitation, Int> {
    fun findAllByUserId(userId: Int) : List<Invitation>
    fun findAllByProjectIdAndStatus(projectId: Int, status: EInvitationStatus) : List<Invitation>

    fun findAllByUserIdAndStatus(userId: Int, status: EInvitationStatus) : List<Invitation>
    fun existsByUserIdAndProjectIdAndStatus(userId: Int, projectId: Int, status: EInvitationStatus) : Boolean
}