package io.say.better.storage.mysql.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity(name = "EDUCATOR")
class Educator(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "educator_id")
    val educatorId: Long = 0,
    @ManyToOne(targetEntity = Member::class)
    @JoinColumn(name = "member_id")
    var memberId: Member,
    @Column(name = "name", nullable = false, length = 100)
    val name: String? = null,
    @Column(name = "birth_date")
    val birthDate: String = "",
) : BaseTimeEntity() {
    companion object {
        fun createEducator(
            memberId: Member,
            name: String? = null,
            birthDate: String = "",
        ): Educator {
            return Educator(
                memberId = memberId,
                name = name,
                birthDate = birthDate,
            )
        }
    }
}
