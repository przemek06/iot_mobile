package edu.pwr.iotmobile.repositories

import edu.pwr.iotmobile.entities.ActionDestination
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ActionDestinationRepository : JpaRepository <ActionDestination, Int> {

}