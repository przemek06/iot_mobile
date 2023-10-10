package edu.pwr.iotmobile.entities

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.Instant
import java.util.*

@Entity
class ResetPasswordToken (
    @Column(nullable = false, unique = true)
    var code: String,
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var user: User,
    @Column(nullable = false)
    var expiryDate: Date,
    @Column(nullable = false)
    var active: Boolean,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null
) {
    constructor() : this("", User(), Date.from(Instant.now()), false)
}