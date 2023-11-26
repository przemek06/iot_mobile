package edu.pwr.iotmobile.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.UuidGenerator

@Entity
class DiscordIntermediate {
    @Id
    @UuidGenerator
    var id: String? = null
    var guildId: String? = null
}