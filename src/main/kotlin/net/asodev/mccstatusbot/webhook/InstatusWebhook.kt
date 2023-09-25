package net.asodev.mccstatusbot.webhook

import net.asodev.mccstatusbot.twitter.TweetRequest
import net.asodev.mccstatusbot.twitter.Twitter
import net.asodev.mccstatusbot.webhook.schema.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import java.time.format.DateTimeFormatter

@RestController
class InstatusWebhook(val twitter: Twitter) {
    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    // Status to be shown as "<Component> experiencing <Status>"
    private val EXPERIENCING = listOf(STATUS_MAJOR_OUTAGE, STATUS_PARTIAL_OUTAGE, STATUS_DEGRADED_PERFORMANCE)
    // Status to be shown as "<Component> is <Status>"
    private val IS = listOf(STATUS_UNDER_MAINTENANCE, STATUS_OPERATIONAL)

    @Value("\${spring.webhook.secret}")
    lateinit var webhookSecret: String

    @PostMapping("/webhook")
    fun webhook(@RequestBody data: InstatusUpdate, @RequestParam secret: String?) {
        if (secret != webhookSecret) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid secret.")
        }
        val incident = data.incident ?: return

        val content = buildString {
            val component = incident.affected_components.firstOrNull()
            if (component != null) {
                this.appendLine( getComponentStatusMessage(component) )
                this.appendLine()
            }
            this.appendLine("${replaceImpactName(incident.impact)}- ${incident.name}")

            val updates = incident.incident_updates.sortedBy { Instant.from(DateTimeFormatter.ISO_INSTANT.parse(it.updated_at)).epochSecond }
            val description = updates.lastOrNull()
            if (description != null) {
                this.appendLine(description.markdown)
            }
        }

        val tweet = TweetRequest(
            text = content
        )
        twitter.sendTweet(tweet)
        LOGGER.info("Sent tweet for incident: ${incident.id}")
    }

    fun getComponentStatusMessage(component: AffectedComponent): String {
        val name = replaceComponentName(component.name)
        val statusName = replaceComponentStatus(component.status)

        return if (EXPERIENCING.contains(component.status)) {
             "$name experiencing $statusName"
        } else if (IS.contains(component.status)) {
            "$name is $statusName"
        } else ""
    }

    fun replaceComponentName(name: String): String {
        return when(name) {
            "MCC Island Minecraft Server" -> "MCC Island"
            else -> name
        }
    }

    fun replaceImpactName(name: String): String {
        return name + when(name) {
            IMPACT_INVESTIGATING -> " \uD83D\uDD0E "
            IMPACT_IDENTIFIED -> "\uD83D\uDCA1"
            IMPACT_MONITORING -> " \uD83D\uDCCB "
            IMPACT_RESOLVED -> " ✅ "
            else -> " "
        }
    }

    fun replaceComponentStatus(status: String): String {
        return status + when(status) {
            STATUS_MAJOR_OUTAGE -> " \uD83D\uDFE5"
            STATUS_PARTIAL_OUTAGE -> " \uD83D\uDFE7"
            STATUS_DEGRADED_PERFORMANCE -> " ⚠\uFE0F"
            STATUS_UNDER_MAINTENANCE -> " ⚒\uFE0F"
            STATUS_OPERATIONAL -> " \uD83D\uDFE9"
            else -> " "
        }
    }

}