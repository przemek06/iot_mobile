package edu.pwr.iotmobile.repositories

import edu.pwr.iotmobile.entities.DiscordIntermediate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DiscordIntermediateRepository : JpaRepository<DiscordIntermediate, String> {
}